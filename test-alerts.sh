#!/bin/bash

BASE_URL="http://localhost:8081/api/test"

echo "Testing alerts..."

echo "1. Triggering high request count..."
curl -X POST "${BASE_URL}/trigger-high-requests?count=50"
echo -e "\n"

echo "2. Triggering high active users..."
curl -X POST "${BASE_URL}/trigger-high-users?userCount=85"
echo -e "\n"

echo "3. Triggering high processing time..."
curl -X POST "${BASE_URL}/trigger-high-processing-time?processingTime=300"
echo -e "\n"

echo "Waiting for alerts to fire (60 seconds)..."
sleep 60

echo "Check alerts in:"
echo "- Prometheus UI: http://localhost:9090/alerts"
echo "- AlertManager UI: http://localhost:9093"
echo "- monitoring alerts logs: docker-compose logs -f monitoring-alerts"