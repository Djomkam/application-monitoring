package com.dev.monitoring.dashboard.config;

import com.dev.monitoring.dashboard.service.GrafanaConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class GrafanaInitializer {
    Logger log = LoggerFactory.getLogger(GrafanaInitializer.class);
    private final GrafanaConfigurationService grafanaConfigurationService;

    public GrafanaInitializer(GrafanaConfigurationService grafanaConfigurationService) {
        this.grafanaConfigurationService = grafanaConfigurationService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initialize() {
        log.info("Initializing Grafana configuration...");
        grafanaConfigurationService.initializeGrafanaConfig();
    }
}

