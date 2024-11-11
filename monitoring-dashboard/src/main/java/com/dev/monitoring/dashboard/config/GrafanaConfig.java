package com.dev.monitoring.dashboard.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "grafana")
public class GrafanaConfig {
    private String url;
    private String username;
    private String password;
    private String orgId;
    private DataSource dataSource = new DataSource();
    private Dashboard dashboard = new Dashboard();
    private Alert alert = new Alert();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }

    public static class DataSource {
        private String type = "prometheus";
        private String url;
        private boolean basicAuth;
        private String basicAuthUser;
        private String basicAuthPassword;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isBasicAuth() {
            return basicAuth;
        }

        public void setBasicAuth(boolean basicAuth) {
            this.basicAuth = basicAuth;
        }

        public String getBasicAuthUser() {
            return basicAuthUser;
        }

        public void setBasicAuthUser(String basicAuthUser) {
            this.basicAuthUser = basicAuthUser;
        }

        public String getBasicAuthPassword() {
            return basicAuthPassword;
        }

        public void setBasicAuthPassword(String basicAuthPassword) {
            this.basicAuthPassword = basicAuthPassword;
        }
    }

    public static class Dashboard {
        private String folderName = "Application Dashboards";
        private boolean overwrite = true;
        private String timezone = "browser";

        public String getFolderName() {
            return folderName;
        }

        public void setFolderName(String folderName) {
            this.folderName = folderName;
        }

        public boolean isOverwrite() {
            return overwrite;
        }

        public void setOverwrite(boolean overwrite) {
            this.overwrite = overwrite;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }
    }

    public static class Alert {
        private boolean enabled = true;
        private String contactPointName = "default";
        private String evaluationInterval = "1m";

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getContactPointName() {
            return contactPointName;
        }

        public void setContactPointName(String contactPointName) {
            this.contactPointName = contactPointName;
        }

        public String getEvaluationInterval() {
            return evaluationInterval;
        }

        public void setEvaluationInterval(String evaluationInterval) {
            this.evaluationInterval = evaluationInterval;
        }
    }
}
