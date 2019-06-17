# See https://hub.docker.com/_/openjdk/
FROM openjdk:8-jre-slim

COPY target/backend-0.0.1-SNAPSHOT.jar /app/app.jar
WORKDIR /app
CMD ["java",\
        "-XX:+UnlockExperimentalVMOptions",\
        "-XX:+UseCGroupMemoryLimitForHeap",\
        "-jar", "app.jar"]
