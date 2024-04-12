#!/bin/bash

# Kafka Connect JDBC plugin
CONNECT_URL="https://d1i4a15mxbxib1.cloudfront.net/api/plugins/confluentinc/kafka-connect-jdbc/versions/10.7.6/confluentinc-kafka-connect-jdbc-10.7.6.zip"
JDBC_FILE_NAMEFILE_NAME="confluentinc-kafka-connect-jdbc-10.7.6.zip"
TARGET_DIR="./kafka/libs"

# MariaDB Java client
MARIA_JAVA_CLIENT_FILE_NAME=mariadb-java-client-3.3.3.jar
MARIA_JAVA_CLIENT_URL="https://repo1.maven.org/maven2/org/mariadb/jdbc/mariadb-java-client/3.3.3/$MARIA_JAVA_CLIENT_FILE_NAME"

# rm kafka libs
rm -rf $TARGET_DIR

# Download and extract the Kafka Connect JDBC plugin
mkdir -p $TARGET_DIR
curl -L $CONNECT_URL -o $JDBC_FILE_NAMEFILE_NAME

unzip $JDBC_FILE_NAMEFILE_NAME
cp -r confluentinc-kafka-connect-jdbc-10.7.6/lib/* $TARGET_DIR

rm $JDBC_FILE_NAMEFILE_NAME
rm -rf confluentinc-kafka-connect-jdbc-10.7.6

# Download the MariaDB Java client
echo Download the MariaDB Java client

rm -rf $MARIA_JAVA_CLIENT_FILE_NAME

curl -L "$MARIA_JAVA_CLIENT_URL" -o $TARGET_DIR/$MARIA_JAVA_CLIENT_FILE_NAME
mv $MARIA_JAVA_CLIENT_FILE_NAME $TARGET_DIR
