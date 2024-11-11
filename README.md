# Monitoring System

This project is a comprehensive monitoring system consisting of three main components: metrics collection, alerts generation, and a dashboard for visualization. The system is built using Spring Boot and follows a modular architecture.

## Project Structure

The project is divided into the following modules:

1. monitoring-metrics # Metrics collection and exposure
2. monitoring-alerts 
3. monitoring-dashboard # Dashboard and Visualization

## Prerequisites

- Java 17+
- Maven 3.x
- Docker & Docker Compose
- Git

## Quick Start

1. Build the project:
```bash
mvn clean install
```
2. Start all Services
```
docker-compose up -d
```
3. Access the Services
- Grafana: http://localhost:3000 (admin/admin)

- Prometheus: http://localhost:9090

- Pushgateway: http://localhost:9091

- Monitoring Dashboard: http://localhost:8082

- Monitoring Metrics: http://localhost:8081


## Modules

### 1. Monitoring Metrics

The metrics collection module responsible for gathering and exposing application metrics.

#### Key Features:
- Metric collection using Spring Boot Actuator

- Integration with Prometheus for metric storage and querying

- Support for custom metrics

- Pushgateway integration for batch jobs and short-lived processes

#### Key Dependencies:
- Spring Boot Actuator
- Micrometer (Prometheus registry)
- Prometheus Metrics Core
- Prometheus Pushgateway Client
- Spring Boot Web

#### Building:
```
cd monitoring-metrics
mvn clean install
```
#### Running:
```
mvn spring-boot:run
```

### 2. monitoring-dashboard

Handles dashboard configuration and visualization.

#### Key Features:

- Integration with Grafana

- Custom dashboard configurations

- Prometheus data source management

- Alert management
#### Configuration:
```
grafana:
  url: ${GRAFANA_URL:http://localhost:3000}
  username: ${GRAFANA_USERNAME:admin}
  password: ${GRAFANA_PASSWORD:admin}
  org-id: ${GRAFANA_ORG_ID:1}
  data-source:
    type: prometheus
    url: ${PROMETHEUS_URL:http://localhost:9090}
  dashboard:
    folder-name: Application Dashboards
    overwrite: true
    timezone: browser
  alert:
    enabled: true
    contact-point-name: default
    evaluation-interval: 1m
```

#### Key Dependencies:
- Spring Boot Web
- Spring Boot WebFlux
- Jackson Databind
- Spring Retry
- monitoring-metrics (internal dependency)
- monitoring-alerts (internal dependency)


### 3. monitoring-alerts

This module handles the generation and management of alerts based on the collected metrics.

#### Key Dependencies:
- Spring Boot Actuator
- Spring Boot Mail
- Spring Boot Web

## Docker Configuration
The project uses Docker Compose for containerization with the following services:
```
services:
  prometheus:
    image: prom/prometheus:v2.54.0
    ports:
      - "9090:9090"

  pushgateway:
    image: prom/pushgateway:v1.7.0
    ports:
      - "9091:9091"

  grafana:
    image: grafana/grafana:9.5.2
    ports:
      - "3000:3000"

  monitoring-metrics:
    build: ./monitoring-metrics
    ports:
      - "8081:8081"

  monitoring-dashboard:
    build: ./monitoring-dashboard
    ports:
      - "8082:8082"
```

### Running with Docker
Start all services:
```
docker compose up -d
```
Stop services:
```
docker compose down
```
## Building the Project

To build the entire project, run the following command in the root directory:

```
mvn clean install
```

This will compile the code, run tests, and package the application for all modules.

## Running the Application

To run a specific module, navigate to its directory and use the Spring Boot Maven plugin:

```
mvn spring-boot:run
```

## Testing

The project uses JUnit 5 for unit testing. To run tests for all modules:

```
mvn test
```

## Configuration
### Environment Variables
Key environment variables used in the project:
- GRAFANA_URL: Grafana server URL

- GRAFANA_USERNAME: Grafana admin username

- GRAFANA_PASSWORD: Grafana admin password

- PROMETHEUS_URL: Prometheus server URL

- SPRING_APPLICATION_NAME: Application name for metrics

## Troubleshooting 
Common issues and solutions:
1. Docker Container Issues

- Check container logs: docker-compose logs <service-name>

- Verify network connectivity between containers

- Ensure all required ports are available

2. Metrics Not Showing

- Verify Prometheus configuration

- Check application endpoints are accessible

- Validate Pushgateway connectivity if used
## Contributing

Please refer to the CONTRIBUTING.md file for guidelines on how to contribute to this project.

## License

This project is licensed under the [MIT License](LICENSE).