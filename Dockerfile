# Multi-stage: compile/test in the Maven image, run on a small Java runtime image.
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn --batch-mode --no-transfer-progress clean verify

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/target/cicd-java-demo-1.0.0-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
USER 10001
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
