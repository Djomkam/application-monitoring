package com.dev.monitoring.alert.controller;

import com.dev.monitoring.alert.model.AlertPayload;
import com.dev.monitoring.alert.service.AlertProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/alert")
public class AlertController {

    private final AlertProcessingService alertProcessingService;

    @PostMapping
    public ResponseEntity<String> receiveWebhook(@RequestBody AlertPayload payload) {
        alertProcessingService.processAlert(payload);
        return ResponseEntity.ok("Webhook received successfully");
    }
}
