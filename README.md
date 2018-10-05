# MailService
An Asynchronous Mail Service developed using Spring Boot + Kafka + Swagger + Java Mail

Start zookeper and kafka

start zookeeper-server-start.bat ....\config\zookeeper.properties && TIMEOUT 10 && start kafka-server-start.bat ....\config\server.properties && exit

Add webtrekk topic to Kafka

kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic webtrekk

Swagger API doc: http://localhost:8080/v2/api-docs

Swagger UI: http://localhost:8080/swagger-ui.html

Download Kafka https://www.apache.org/dyn/closer.cgi?path=/kafka/2.0.0/kafka_2.11-2.0.0.tgz
