package com.dev.monitoring;

import com.dev.monitoring.service.MetricService;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import io.prometheus.client.exporter.PushGateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MetricApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext);
    }

    @Test
    void metricServiceBeanExists() {
        assertNotNull(applicationContext.getBean(MetricService.class));
    }

    @Test
    void prometheusMeterRegistryBeanExists() {
        assertNotNull(applicationContext.getBean(PrometheusMeterRegistry.class));
    }

    @Test
    void pushGatewayBeanExists() {
        assertNotNull(applicationContext.getBean(PushGateway.class));
    }
}

