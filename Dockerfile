FROM openjdk:8-jdk-alpine

COPY target/api-gateway-0.0.1-SNAPSHOT.jar /app/api-gateway.jar

CMD ["java", "-jar", "/app/api-gateway.jar"]