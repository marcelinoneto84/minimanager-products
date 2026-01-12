# Stage 1: Build
FROM gradle:7.6-jdk11 AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:11-jre-alpine

# Instalar curl para healthcheck
RUN apk add --no-cache curl

WORKDIR /app

# Copiar o JAR do stage de build
COPY --from=build /app/build/libs/*.jar app.jar

# Criar diretório de logs
RUN mkdir -p /app/logs

VOLUME /tmp
VOLUME /app/logs

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]