FROM openjdk:11-jre-slim

VOLUME /tmp

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]