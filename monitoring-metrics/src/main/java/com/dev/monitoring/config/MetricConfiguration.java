package com.dev.monitoring.config;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.PushGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;

@Configuration
public class MetricConfiguration {

    @Value("${prometheus.pushgateway.url}")
    private String pushgatewayUrl;

    @Bean
    public CollectorRegistry collectorRegistry() {
        return CollectorRegistry.defaultRegistry;
    }

    @Bean
    public PushGateway pushGateway() throws MalformedURLException {
        return new PushGateway(new URL(pushgatewayUrl));
    }
}
