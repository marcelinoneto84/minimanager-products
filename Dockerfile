FROM openjdk:11-jre-slim

# Instalar curl para healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

VOLUME /tmp

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]