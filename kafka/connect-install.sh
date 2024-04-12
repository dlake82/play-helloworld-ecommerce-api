# Wait for Kafka Connect to start
echo "Waiting for Kafka Connect to start..."
while [ $(curl -s -o /dev/null -w %{http_code} http://localhost:8083/connectors) -eq 000 ]
do
  echo -e $(date) " Kafka Connect listener HTTP state: " $(curl -s -o /dev/null -w %{http_code} http://localhost:8083/connectors) " (waiting for 200)"
  sleep 5
done

# Delete the connectors
curl -X DELETE http://localhost:8083/connectors/my-source-connect

echo '
{
  "name" : "my-source-connect",
  "config" : {
    "connector.class" : "io.confluent.connect.jdbc.JdbcSourceConnector",
    "connection.url":"jdbc:mysql://mariadb:3306/mydb",
    "connection.user":"test",
    "connection.password":"test1234",
    "mode": "incrementing",
    "incrementing.column.name" : "id",
    "table.whitelist":"users",
    "topic.prefix" : "my_topic_",
    "tasks.max" : "1"
  }
}
' | curl -X POST -d @- http://localhost:8083/connectors --header "content-Type:application/json"

# Install the connectors
curl -X DELETE http://localhost:8083/connectors/my-sink-connect

echo '
{
  "name":"my-sink-connect",
      "config": {
      "connector.class":"io.confluent.connect.jdbc.JdbcSinkConnector",
      "connection.url":"jdbc:mysql://mariadb:3306/mydb",
      "connection.user":"test",
      "connection.password":"test1234",
      "auto.create":"true",
      "auto.evolve":"true",
      "delete.enabled":"false",
      "tasks.max":"1",
      "topics":"my_topic_users"
  }
}
'| curl -X POST -d @- http://localhost:8083/connectors --header "content-Type:application/json"
