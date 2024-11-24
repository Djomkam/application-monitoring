package com.dev.monitoring.metrics.controller;

import com.dev.monitoring.metrics.service.MetricService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class AlertTestController {

    private final MetricService metricService;

    @PostMapping("/trigger-high-requests")
    public String triggerHighRequests(@RequestParam(defaultValue = "20") int count) {
        log.info("Triggering {} requests", count);
        for (int i = 0; i < count; i++) {
            metricService.incrementRequests();
        }
        return "Triggered " + count + " requests";
    }

    @PostMapping("/trigger-high-users")
    public String triggerHighUsers(@RequestParam(defaultValue = "90") int userCount) {
        log.info("Setting active users to {}", userCount);
        metricService.setActiveUsers(userCount);
        return "Set active users to " + userCount;
    }

    @PostMapping("/trigger-high-processing-time")
    public String triggerHighProcessingTime(@RequestParam(defaultValue = "200") long processingTime) {
        log.info("Triggering high processing time: {}ms", processingTime);
        metricService.recordProcessingTime(processingTime);
        return "Recorded high processing time: " + processingTime + "ms";
    }
}
