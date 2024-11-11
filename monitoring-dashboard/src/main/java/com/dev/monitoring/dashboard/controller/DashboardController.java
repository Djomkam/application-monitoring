package com.dev.monitoring.dashboard.controller;

import com.dev.monitoring.dashboard.service.DashboardProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/dashboards")
public class DashboardController {

    private final DashboardProviderService dashboardProviderService;

    public DashboardController(DashboardProviderService dashboardProviderService) {
        this.dashboardProviderService = dashboardProviderService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getDashboards() {
        List<Map<String, Object>> dashboards = dashboardProviderService.loadDashboards();
        return ResponseEntity.ok(dashboards);
    }
}
