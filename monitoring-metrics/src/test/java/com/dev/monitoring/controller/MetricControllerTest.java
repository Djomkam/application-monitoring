package com.dev.monitoring.controller;

import com.dev.monitoring.service.MetricService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MetricControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MetricService metricService;

    @InjectMocks
    private MetricController metricController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(metricController).build();
    }

    // Common Metrics Tests
    @Test
    void incrementRequests_ShouldIncrementAndRecordDuration() throws Exception {
        mockMvc.perform(get("/metrics/requests"))
                .andExpect(status().isOk())
                .andExpect(content().string("Request counted successfully"));

        verify(metricService).incrementRequests();
        verify(metricService).recordRequestDuration(anyDouble());
    }

    @Test
    void incrementRequests_WhenError_ShouldIncrementErrorCounter() throws Exception {
        doThrow(new RuntimeException("Test error")).when(metricService).incrementRequests();

        mockMvc.perform(get("/metrics/requests"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error processing request"));

        verify(metricService).incrementErrors();
    }

    @Test
    void simulateError_ShouldIncrementErrorCounter() throws Exception {
        mockMvc.perform(get("/metrics/errors"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error simulated"));

        verify(metricService).incrementErrors();
    }

    @Test
    void updateMemoryUsage_ShouldSetMemoryMetrics() throws Exception {
        mockMvc.perform(get("/metrics/memory"))
                .andExpect(status().isOk())
                .andExpect(content().string("Memory usage metrics updated"));

        verify(metricService).setMemoryUsage();
    }

    @Test
    void recordRequestSize_ShouldRecordSize() throws Exception {
        String payload = "test data";

        mockMvc.perform(post("/metrics/request-size")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Request size recorded"));

        verify(metricService).recordRequestSize(payload.length());
    }

    // Utility Endpoint Tests
    @Test
    void pushMetrics_ShouldPushSuccessfully() throws Exception {
        mockMvc.perform(post("/metrics/push")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Metrics pushed successfully"));

        verify(metricService).pushMetrics();
    }

    @Test
    void batchMetrics_ShouldRecordAllMetrics() throws Exception {
        String batchData = "test batch data";

        mockMvc.perform(post("/metrics/batch")
                        .content(batchData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Batch metrics recorded"));

        verify(metricService).incrementRequests();
        verify(metricService).recordRequestSize(batchData.length());
        verify(metricService).recordRequestDuration(anyDouble());
    }

    @Test
    void batchMetrics_WhenError_ShouldIncrementErrorCounter() throws Exception {
        String batchData = "test batch data";
        doThrow(new RuntimeException("Batch error")).when(metricService).recordRequestSize(anyLong());

        mockMvc.perform(post("/metrics/batch")
                        .content(batchData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error processing batch metrics"));

        verify(metricService).incrementErrors();
    }
}


