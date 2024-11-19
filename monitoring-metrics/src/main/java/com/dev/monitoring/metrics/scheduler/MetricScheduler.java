package com.dev.monitoring.metrics.scheduler;

import com.dev.monitoring.metrics.service.MetricService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
public class MetricScheduler implements ApplicationListener<ApplicationReadyEvent> {

    private final MetricService metricService;
    private final Random random = new Random();

    public MetricScheduler(MetricService metricService) {
        this.metricService = metricService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Application is ready - Starting initial metrics simulation");
        simulateMetrics();
    }

    @Scheduled(fixedRate = 30000)
    public void simulateMetrics() {
        log.info("Simulating metrics...");
        try {
            metricService.updateActiveUsers();
            metricService.incrementRequests();
            metricService.recordProcessingTime(random.nextInt(100));
            metricService.recordDataSize(random.nextInt(1000));
        } catch (Exception e) {
            log.error("Error while simulating metrics", e);
        }
    }
}
