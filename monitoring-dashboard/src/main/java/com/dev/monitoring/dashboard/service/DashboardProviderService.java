package com.dev.monitoring.dashboard.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DashboardProviderService {

    private final ObjectMapper objectMapper;
    Logger log = LoggerFactory.getLogger(DashboardProviderService.class);

    public DashboardProviderService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<Map<String, Object>> loadDashboards() {
        List<Map<String, Object>> dashboards = new ArrayList<>();
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver()
                    .getResources("classpath:dashboards/*.json");

            for (Resource resource : resources) {
                try {
                    String content = new String(resource.getInputStream().readAllBytes(),
                            StandardCharsets.UTF_8);
                    Map<String, Object> dashboard = objectMapper.readValue(content, Map.class);
                    dashboards.add(dashboard);
                    log.info("Loaded dashboard: {}", resource.getFilename());
                } catch (IOException e) {
                    log.error("Failed to load dashboard: {}", resource.getFilename(), e);
                }
            }
        } catch (IOException e) {
            log.error("Failed to load dashboards", e);
        }
        return dashboards;
    }
}
