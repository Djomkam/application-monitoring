package com.dev.monitoring.component;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import io.prometheus.client.exporter.PushGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MetricComponent {
    private static final Logger logger = LoggerFactory.getLogger(MetricComponent.class);

    private final CollectorRegistry registry;
    private final PushGateway pushGateway;
    @Value("${spring.application.name}")
    private String job;
    @Value("${spring.application.instance}")
    private String instance;

    // Counters
    private final Counter requestsTotal;
    private final Counter errorsTotal;

    // Gauges
    private final Gauge activeConnections;
    private final Gauge memoryUsage;

    // Histogram
    private final Histogram requestDuration;

    // Summary
    private final Summary requestSize;

    public MetricComponent(PushGateway pushGateway, CollectorRegistry registry) {
        this.registry = registry;
        this.pushGateway = pushGateway;

        // Initialize Counters
        this.requestsTotal = Counter.build()
                .name("requests_total")
                .help("Total number of requests")
                .register(registry);

        this.errorsTotal = Counter.build()
                .name("errors_total")
                .help("Total number of errors")
                .register(registry);

        // Initialize Gauges
        this.activeConnections = Gauge.build()
                .name("active_connections")
                .help("Number of active connections")
                .register(registry);

        this.memoryUsage = Gauge.build()
                .name("memory_usage_bytes")
                .help("Current memory usage in bytes")
                .register(registry);

        // Initialize Histogram
        this.requestDuration = Histogram.build()
                .name("request_duration_seconds")
                .help("Request duration in seconds")
                .buckets(0.1, 0.5, 1, 2, 5)
                .register(registry);

        // Initialize Summary
        this.requestSize = Summary.build()
                .name("request_size_bytes")
                .help("Request size in bytes")
                .quantile(0.5, 0.05)
                .quantile(0.9, 0.01)
                .register(registry);
    }

    // Counter methods
    public void incrementRequests() {
        requestsTotal.inc();
    }

    public void incrementErrors() {
        errorsTotal.inc();
    }

    // Gauge methods
    public void setActiveConnections(int connections) {
        activeConnections.set(connections);
    }

    public void setMemoryUsage(long bytes) {
        memoryUsage.set(bytes);
    }

    // Histogram method
    public void recordRequestDuration(double durationSeconds) {
        requestDuration.observe(durationSeconds);
    }

    // Summary method
    public void recordRequestSize(long sizeBytes) {
        requestSize.observe(sizeBytes);
    }

    // Push metrics to Pushgateway
    public void pushMetrics() {
        try {
            pushGateway.pushAdd(registry, job);
            logger.info("Metrics pushed successfully to Pushgateway");
        } catch (Exception e) {
            logger.error("Error pushing metrics to Pushgateway", e);
        }
    }
}

