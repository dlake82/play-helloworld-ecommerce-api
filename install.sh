#!/bin/bash

# Install Kafka Connect JDBC
./kafka/connect-install.sh

# Start the services
docker-compose up --build -d --force-recreate

sleep 10
