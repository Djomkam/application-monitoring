package com.dev.monitoring.metrics.service;

import io.micrometer.core.instrument.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class MetricService {

    private final MeterRegistry meterRegistry;
    private final Counter requestCounter;
    private final AtomicInteger activeUsers;
    private final Random random = new Random();

    public MetricService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Counter example
        this.requestCounter = Counter.builder("app.requests.total")
                .description("Total number of requests")
                .tag("type", "service")
                .register(meterRegistry);

        // Gauge example
        this.activeUsers = new AtomicInteger(0);
        Gauge.builder("app.users.active", activeUsers, AtomicInteger::get)
                .description("Number of active users")
                .register(meterRegistry);

        // Timer example
        Timer.builder("app.processing.time")
                .description("Processing time")
                .register(meterRegistry);

        // Distribution summary example
        DistributionSummary.builder("app.data.size")
                .description("Data size distribution")
                .baseUnit("bytes")
                .register(meterRegistry);
    }

    public void incrementRequests() {
        requestCounter.increment();
        log.debug("Incremented request counter: {}", requestCounter.count());
    }

    public void updateActiveUsers() {
        int newValue = random.nextInt(100);
        activeUsers.set(newValue);
        log.debug("Updated active users count: {}", newValue);
    }

    public void setActiveUsers(int count) {
        activeUsers.set(count);
        log.debug("Set active users count to: {}", count);
    }

    public void recordProcessingTime(long timeMs) {
        Timer.builder("app.processing.time")
                .description("Processing time")
                .register(meterRegistry)
                .record(() -> {
                    try {
                        Thread.sleep(timeMs);
                        log.debug("Recorded processing time: {}ms", timeMs);
                    } catch (InterruptedException e) {
                        log.error("Error during processing time recording", e);
                        Thread.currentThread().interrupt();
                    }
                });
    }

    public void recordDataSize(long bytes) {
        DistributionSummary.builder("app.data.size")
                .description("Data size distribution")
                .baseUnit("bytes")
                .register(meterRegistry)
                .record(bytes);

        log.debug("Recorded data size: {} bytes", bytes);
    }
}
