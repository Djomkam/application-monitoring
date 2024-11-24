package com.dev.monitoring.alert.model;

import lombok.Data;

import java.util.Map;

@Data
public class Alert {
    private String status;
    private Map<String, String> labels;
    private Map<String, String> annotations;
    private String startsAt;
    private String endsAt;
    private String generatorURL;
    private String fingerprint;
}