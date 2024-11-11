package com.dev.monitoring.alert.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "alerts")
public class AlertConfig {
    private String rulesPath;
    private String alertManagerUrl;

    public String getRulesPath() {
        return rulesPath;
    }

    public void setRulesPath(String rulesPath) {
        this.rulesPath = rulesPath;
    }

    public String getAlertManagerUrl() {
        return alertManagerUrl;
    }

    public void setAlertManagerUrl(String alertManagerUrl) {
        this.alertManagerUrl = alertManagerUrl;
    }

}
