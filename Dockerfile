FROM openjdk:11-slim AS builder
COPY app/build.gradle app/gradlew /app/
COPY app/gradle /app/gradle
WORKDIR /app
RUN ["./gradlew", "build", "-x", "test"]

FROM openjdk:11-jre-slim
COPY /app/build/libs/demo-0.0.1-SNAPSHOT.jar /app/easy-pod.jar
CMD ["java", "-jar", "/app/easy-pod.jar"]
