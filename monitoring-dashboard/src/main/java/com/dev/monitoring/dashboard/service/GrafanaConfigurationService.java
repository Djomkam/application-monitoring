package com.dev.monitoring.dashboard.service;

import com.dev.monitoring.alert.model.AlertRule;
import com.dev.monitoring.dashboard.config.GrafanaConfig;
import com.dev.monitoring.dashboard.exception.GrafanaConfigurationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GrafanaConfigurationService {
    Logger log = LoggerFactory.getLogger(GrafanaConfigurationService.class);
    private final RestTemplate restTemplate;
    private final GrafanaConfig grafanaConfig;
    private final DashboardProviderService dashboardProviderService;
    private final ObjectMapper objectMapper;

    public GrafanaConfigurationService(RestTemplate restTemplate,
                                       GrafanaConfig grafanaConfig,
                                       ObjectMapper objectMapper,
                                       DashboardProviderService dashboardProviderService) {
        this.restTemplate = restTemplate;
        this.grafanaConfig = grafanaConfig;
        this.dashboardProviderService = dashboardProviderService;
        this.objectMapper = objectMapper;
    }

    public void initializeGrafanaConfig() {
        try {
            //createOrUpdateDataSource();
            //createDashboardFolder();
            //provisionDashboards();
            createDashboard(createDefaultDashboard("Application Metrics", List.of(
                    "requests_total",
                    "errors_total",
                    "active_connections_total",
                    "memory_usage_bytes",
                    "request_duration_seconds",
                    "request_size_bytes"
            )));
            log.info("Grafana configuration completed successfully");
        } catch (Exception e) {
            log.error("Failed to initialize Grafana configuration", e);
            throw new GrafanaConfigurationException("Grafana configuration failed", e);
        }
    }

    @Retryable(maxAttempts = 3)
    public void createOrUpdateDataSource() {
        String url = grafanaConfig.getUrl() + "/api/datasources";
        Map<String, Object> dataSource = createDataSourcePayload();

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(dataSource, createHeaders());

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Data source created/updated successfully");
            } else {
                log.error("Failed to create/update data source: {}", response.getBody());
                throw new GrafanaConfigurationException("Failed to create/update data source");
            }
        } catch (Exception e) {
            log.error("Error creating/updating data source", e);
            throw new GrafanaConfigurationException("Error creating/updating data source", e);
        }
    }

    @Retryable(maxAttempts = 3)
    public void createDashboardFolder() {
        String url = grafanaConfig.getUrl() + "/api/folders";
        Map<String, Object> folder = Map.of(
                "title", grafanaConfig.getDashboard().getFolderName()
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(folder, createHeaders());

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Dashboard folder created successfully");
            }
        } catch (Exception e) {
            log.warn("Folder might already exist: {}", e.getMessage());
        }
    }

    @Retryable(maxAttempts = 3)
    public void createDashboard(Map<String, Object> dashboardJson) {
        String url = grafanaConfig.getUrl() + "/api/dashboards/db";

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(dashboardJson, createHeaders());

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Dashboard created successfully");
            }
        } catch (Exception e) {
            log.warn("Dashboard might already exist: {}", e.getMessage());
        }
    }

    @Retryable(maxAttempts = 3)
    public void provisionDashboards() {
        String url = grafanaConfig.getUrl() + "/api/dashboards/db";
        List<Map<String, Object>> dashboards = dashboardProviderService.loadDashboards();

        for (Map<String, Object> dashboard : dashboards) {
            try {
                Map<String, Object> payload = createDashboardPayload(dashboard);
                HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, createHeaders());

                ResponseEntity<String> response = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        request,
                        String.class
                );

                if (response.getStatusCode().is2xxSuccessful()) {
                    log.info("Dashboard provisioned successfully: {}", dashboard.get("title"));
                }
            } catch (Exception e) {
                log.error("Failed to provision dashboard: {}", dashboard.get("title"), e);
            }
        }
    }

    @Retryable(maxAttempts = 3)
    public void setupAlertRules() {
        String url = grafanaConfig.getUrl() + "/api/ruler/grafana/api/v1/rules";
        List<AlertRule> alertRules = getAlertRules();

        for (AlertRule alertRule : alertRules) {
            try {
                HttpEntity<AlertRule> request = new HttpEntity<>(alertRule, createHeaders());

                ResponseEntity<String> response = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        request,
                        String.class
                );

                if (response.getStatusCode().is2xxSuccessful()) {
                    log.info("Alert rule created successfully: {}", alertRule.getName());
                }
            } catch (Exception e) {
                log.error("Failed to create alert rule: {}", alertRule.getName(), e);
            }
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(grafanaConfig.getUsername(), grafanaConfig.getPassword());
        return headers;
    }

    private Map<String, Object> createDataSourcePayload() {
        Map<String, Object> dataSource = new HashMap<>();
        dataSource.put("name", "Prometheus");
        dataSource.put("type", grafanaConfig.getDataSource().getType());
        dataSource.put("url", grafanaConfig.getDataSource().getUrl());
        dataSource.put("access", "proxy");
        dataSource.put("isDefault", true);

        if (grafanaConfig.getDataSource().isBasicAuth()) {
            dataSource.put("basicAuth", true);
        }

        return dataSource;
    }

    public Map<String, Object> createDefaultDashboard(String applicationName, List<String> metrics) {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("title", applicationName + " Metrics Dashboard");
        dashboard.put("timezone", grafanaConfig.getDashboard().getTimezone());

        List<Map<String, Object>> panels = new ArrayList<>();
        int panelId = 1;

        // Get alert rules
        List<AlertRule> alertRules = getAlertRules();
        Map<String, AlertRule> metricToAlertRule = alertRules.stream()
                .collect(Collectors.toMap(AlertRule::getMetric, rule -> rule, (r1, r2) -> r1));

        // Create panels for each metric with alerts if applicable
        for (String metric : metrics) {
            Map<String, Object> panel = createPanel(metric, panelId++, metricToAlertRule.get(metric));
            panels.add(panel);
        }

        dashboard.put("panels", panels);

        return createDashboardPayload(dashboard);
    }

    private Map<String, Object> createPanel(String metric, int panelId, AlertRule alertRule) {
        Map<String, Object> panel = new HashMap<>();
        panel.put("id", panelId);
        panel.put("title", metric);
        panel.put("type", "graph");

        // Basic panel settings
        Map<String, Object> gridPos = new HashMap<>();
        gridPos.put("h", 8);
        gridPos.put("w", 12);
        gridPos.put("x", (panelId - 1) % 2 * 12);
        panel.put("gridPos", gridPos);

        // Add prometheus query
        Map<String, Object> target = new HashMap<>();
        target.put("expr", metric);
        target.put("refId", "A");
        panel.put("targets", Collections.singletonList(target));

        // Add alert if rule exists
        if (alertRule != null) {
            Map<String, Object> alert = createAlert(alertRule);
            panel.put("alert", alert);
        }

        return panel;
    }

    private Map<String, Object> createAlert(AlertRule rule) {
        Map<String, Object> alert = new HashMap<>();
        alert.put("name", rule.getName());
        alert.put("message", rule.getMessage());

        // Alert conditions
        List<Map<String, Object>> conditions = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        condition.put("type", "query");
        condition.put("query", createAlertQuery(rule));
        condition.put("reducer", createReducer());
        condition.put("evaluator", createEvaluator(rule));
        conditions.add(condition);
        alert.put("conditions", conditions);

        // Alert settings
        alert.put("frequency", "1m");
        alert.put("handler", 1);
        alert.put("severity", rule.getSeverity());
        alert.put("executionErrorState", "alerting");
        alert.put("noDataState", "no_data");
        alert.put("alertRuleTags", Map.of("severity", rule.getSeverity()));

        return alert;
    }

    private Map<String, Object> createAlertQuery(AlertRule rule) {
        return Map.of(
                "params", List.of(rule.getMetric(), "5m", "now"),
                "datasourceId", 1,
                "model", Map.of(
                        "expr", rule.getMetric(),
                        "intervalMs", 1000,
                        "maxDataPoints", 43200,
                        "refId", "A"
                )
        );
    }

    private Map<String, Object> createReducer() {
        return Map.of(
                "type", "avg",
                "params", List.of()
        );
    }

    private Map<String, Object> createEvaluator(AlertRule rule) {
        return Map.of(
                "type", rule.getCondition(),
                "params", List.of(Double.parseDouble(rule.getThreshold()))
        );
    }

    private Map<String, Object> createDashboardPayload(Map<String, Object> dashboard) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("dashboard", dashboard);
        payload.put("folderId", 0);
        payload.put("overwrite", grafanaConfig.getDashboard().isOverwrite());

        return payload;
    }

    private List<AlertRule> getAlertRules() {
        try {
            return objectMapper.readValue(
                    getClass().getResourceAsStream("/alert-rules/rules.json"),
                    new TypeReference<>() {
                    }
            );
        } catch (Exception e) {
            log.error("Failed to load alert rules", e);
            return List.of();
        }
    }
}
