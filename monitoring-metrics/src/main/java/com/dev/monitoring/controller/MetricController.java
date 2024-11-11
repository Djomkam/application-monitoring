package com.dev.monitoring.controller;

import com.dev.monitoring.service.MetricService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController

@RequestMapping("/metrics")
public class MetricController {
    Logger log = LoggerFactory.getLogger(MetricController.class);
    private final MetricService metricService;
    private final Random random = new Random();

    public MetricController(MetricService metricService) {
        this.metricService = metricService;
    }

    // Common Metrics Endpoints
    @GetMapping("/requests")
    public ResponseEntity<String> incrementRequests() {
        try {
            long startTime = System.nanoTime();

            // Simulate some work
            Thread.sleep(random.nextInt(100));

            metricService.incrementRequests();

            // Record request duration
            double duration = (System.nanoTime() - startTime) / 1e9;
            metricService.recordRequestDuration(duration);

            return ResponseEntity.ok("Request counted successfully");
        } catch (Exception e) {
            metricService.incrementErrors();
            log.error("Error processing request", e);
            return ResponseEntity.internalServerError().body("Error processing request");
        }
    }

    @GetMapping("/errors")
    public ResponseEntity<String> simulateError() {
        metricService.incrementErrors();
        return ResponseEntity.internalServerError().body("Error simulated");
    }

    @PostMapping("/connections")
    public ResponseEntity<String> updateConnections(@RequestParam int connections) {
        metricService.setActiveConnections(connections);
        return ResponseEntity.ok("Active connections updated");
    }

    @GetMapping("/memory")
    public ResponseEntity<String> memoryUsage() {
        metricService.setMemoryUsage();
        return ResponseEntity.ok("Memory usage metrics updated");
    }

    @PostMapping("/request-size")
    public ResponseEntity<String> recordRequestSize(@RequestBody String payload) {
        metricService.recordRequestSize(payload.length());
        return ResponseEntity.ok("Request size recorded");
    }

    // Endpoint to manually trigger metrics push
    @PostMapping("/push")
    public ResponseEntity<String> pushMetrics() {
        try {
            metricService.pushMetrics();
            return ResponseEntity.ok("Metrics pushed successfully");
        } catch (Exception e) {
            log.error("Error pushing metrics", e);
            return ResponseEntity.internalServerError().body("Error pushing metrics");
        }
    }

    // Batch metrics endpoint
    @PostMapping("/batch")
    public ResponseEntity<String> recordBatchMetrics(@RequestBody String batchData) {
        try {
            long startTime = System.nanoTime();

            // Record batch processing metrics
            metricService.incrementRequests();
            metricService.recordRequestSize(batchData.length());

            // Simulate batch processing
            Thread.sleep(random.nextInt(500));

            // Record duration
            double duration = (System.nanoTime() - startTime) / 1e9;
            metricService.recordRequestDuration(duration);

            return ResponseEntity.ok("Batch metrics recorded");
        } catch (Exception e) {
            metricService.incrementErrors();
            log.error("Error processing batch metrics", e);
            return ResponseEntity.internalServerError().body("Error processing batch metrics");
        }
    }
}

