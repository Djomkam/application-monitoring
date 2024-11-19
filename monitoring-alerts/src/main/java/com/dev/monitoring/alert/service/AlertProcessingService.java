package com.dev.monitoring.alert.service;

import com.dev.monitoring.alert.model.AlertPayload;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlertProcessingService {

    public void processAlert(AlertPayload payload) {
        log.info("Received alert payload: {}", payload);
        // Add your alert processing logic here
        if(ObjectUtils.isEmpty(payload) || ObjectUtils.isEmpty(payload.getAlerts())){
            throw new IllegalArgumentException("Payload cannot be null");
        }
        payload.getAlerts().forEach(alert -> {
            log.info("Processing alert: {}", alert);
            // Add specific alert handling logic
        });
    }
}

