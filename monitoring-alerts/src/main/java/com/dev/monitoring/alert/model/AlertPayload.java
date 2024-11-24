package com.dev.monitoring.alert.model;

import lombok.Data;

import java.util.List;

@Data
public class AlertPayload {
    private String version;
    private String groupKey;
    private String status;
    private String receiver;
    private List<Alert> alerts;
    private String externalURL;
}
