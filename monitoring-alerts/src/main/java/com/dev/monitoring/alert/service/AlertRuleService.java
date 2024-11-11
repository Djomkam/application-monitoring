package com.dev.monitoring.alert.service;

import com.dev.monitoring.alert.model.AlertRule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertRuleService {
    private final ObjectMapper objectMapper;
    Logger log = LoggerFactory.getLogger(AlertRuleService.class);

    public AlertRuleService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<AlertRule> getAlertRules() {
        try {
            return objectMapper.readValue(
                    getClass().getResourceAsStream("/alert-rules/rules.json"),
                    new TypeReference<>() {
                    }
            );
        } catch (Exception e) {
            log.error("Failed to load alert rules", e);
            return List.of();
        }
    }
}

