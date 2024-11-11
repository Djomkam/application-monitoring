package com.dev.monitoring.service;

import com.dev.monitoring.component.MetricComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricServiceTest {

    private MetricService metricService;

    @Mock
    private MetricComponent commonMetrics;

    @BeforeEach
    void setUp() {
        metricService = new MetricService(commonMetrics);
    }

    @Test
    void incrementRequests_ShouldCallCommonMetrics() {
        metricService.incrementRequests();
        verify(commonMetrics, times(1)).incrementRequests();
    }

    @Test
    void incrementErrors_ShouldCallCommonMetrics() {
        metricService.incrementErrors();
        verify(commonMetrics, times(1)).incrementErrors();
    }

    @Test
    void setActiveConnections_ShouldCallCommonMetrics() {
        int connections = 42;
        metricService.setActiveConnections(connections);
        verify(commonMetrics, times(1)).setActiveConnections(connections);
    }

    @Test
    void setMemoryUsage_ShouldCallCommonMetrics() {
        metricService.setMemoryUsage();
        verify(commonMetrics, times(1)).setMemoryUsage(anyLong());
    }

    @Test
    void recordRequestDuration_ShouldCallCommonMetrics() {
        double duration = 1.5;
        metricService.recordRequestDuration(duration);
        verify(commonMetrics, times(1)).recordRequestDuration(duration);
    }

    @Test
    void recordRequestSize_ShouldCallCommonMetrics() {
        long size = 1024L;
        metricService.recordRequestSize(size);
        verify(commonMetrics, times(1)).recordRequestSize(size);
    }

    @Test
    void pushMetrics_Success() {
        doNothing().when(commonMetrics).pushMetrics();
        metricService.pushMetrics();
        verify(commonMetrics, times(1)).pushMetrics();
    }

    // Test multiple metric operations
    @Test
    void multipleMetricOperations() {
        metricService.incrementRequests();
        metricService.setActiveConnections(10);
        metricService.recordRequestDuration(0.5);

        verify(commonMetrics, times(1)).incrementRequests();
        verify(commonMetrics, times(1)).setActiveConnections(10);
        verify(commonMetrics, times(1)).recordRequestDuration(0.5);
    }

    // Utility method to help with testing
    private void verifyNoMoreMetricInteractions() {
        verifyNoMoreInteractions(commonMetrics);
    }
}
