FROM openjdk:8-jdk-alpine

COPY target/auth-service-0.0.1-SNAPSHOT.jar /app/auth-service.jar

CMD ["java", "-jar", "/app/auth-service.jar"]