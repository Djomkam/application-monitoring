package com.dev.monitoring.alert.service;

import com.dev.monitoring.alert.model.Alert;
import com.dev.monitoring.alert.model.AlertPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlertProcessingServiceTest {

    private AlertProcessingService alertProcessingService;

    @BeforeEach
    void setUp() {
        alertProcessingService = new AlertProcessingService();
    }

    @Test
    void processAlert_ShouldHandleValidPayload() {
        // Given
        AlertPayload payload = new AlertPayload();
        Alert alert = new Alert();
        alert.setStatus("firing");
        alert.setLabels(Map.of("severity", "critical"));
        alert.setAnnotations(Map.of("description", "Test alert"));
        payload.setAlerts(List.of(alert));

        // When
        alertProcessingService.processAlert(payload);

        // Then
        // Verify logging - would require a logging test framework like LogCaptor
        // For now, we just verify no exceptions are thrown
    }

    @Test
    void processAlert_ShouldHandleNullPayload() {
        // Given
        AlertPayload payload = null;

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> {
            alertProcessingService.processAlert(payload);
        });
    }
}