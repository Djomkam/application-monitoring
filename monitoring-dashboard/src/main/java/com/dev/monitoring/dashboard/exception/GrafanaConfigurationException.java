package com.dev.monitoring.dashboard.exception;

public class GrafanaConfigurationException extends RuntimeException {
    public GrafanaConfigurationException(String message) {
        super(message);
    }

    public GrafanaConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
