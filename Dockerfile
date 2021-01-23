FROM gradle:6.7.1-jdk11 AS builder
COPY app/ /app/
WORKDIR /app
RUN ["gradle", "build", "-x", "test"]

FROM openjdk:11-jre-slim
COPY --from=builder /app/build/libs/demo-0.0.1-SNAPSHOT.jar /app/easy-pod.jar
WORKDIR /app
CMD ["java", "-jar", "/app/easy-pod.jar"]
