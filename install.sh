#!/bin/bash

# Install Kafka Connect JDBC
./kafka/connect-download.sh

# Start the services
docker-compose down && docker-compose up -d

# Install Kafka Source Connector
./kafka/connect-source-install.sh
