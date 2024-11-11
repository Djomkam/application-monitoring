package com.dev.monitoring.service;

import com.dev.monitoring.component.MetricComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MetricService {

    private static final Logger logger = LoggerFactory.getLogger(MetricService.class);

    private final MetricComponent metricComponent;

    public MetricService(MetricComponent metricComponent) {
        this.metricComponent = metricComponent;
    }

    @Scheduled(fixedRateString = "${metrics.push.interval:60000}")
    public void pushMetrics() {
        metricComponent.pushMetrics();
        logger.info("Metrics pushed successfully to Prometheus Pushgateway");
    }

    // Methods to update common metrics
    public void incrementRequests() {
        metricComponent.incrementRequests();
    }

    public void incrementErrors() {
        metricComponent.incrementErrors();
    }

    public void setActiveConnections(int connections) {
        metricComponent.setActiveConnections(connections);
    }

    public void setMemoryUsage() {
        long memoryUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        metricComponent.setMemoryUsage(memoryUsage);
    }

    public void recordRequestDuration(double durationSeconds) {
        metricComponent.recordRequestDuration(durationSeconds);
    }

    public void recordRequestSize(long sizeBytes) {
        metricComponent.recordRequestSize(sizeBytes);
    }
}
