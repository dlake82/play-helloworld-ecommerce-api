#!/bin/bash

URL="https://d1i4a15mxbxib1.cloudfront.net/api/plugins/confluentinc/kafka-connect-jdbc/versions/10.7.6/confluentinc-kafka-connect-jdbc-10.7.6.zip"
FILE_NAME="confluentinc-kafka-connect-jdbc-10.7.6.zip"
TARGET_DIR="./kafka/jars"

curl -L $URL -o $FILE_NAME
mkdir -p $TARGET_DIR

unzip $FILE_NAME
cp -r confluentinc-kafka-connect-jdbc-10.7.6/lib/* $TARGET_DIR

rm $FILE_NAME
rm -rf confluentinc-kafka-connect-jdbc-10.7.6
