package com.dev.monitoring.metrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MonitoringMetricApplication {
    public static void main(String[] args) {
        SpringApplication.run(MonitoringMetricApplication.class, args);
    }
}
