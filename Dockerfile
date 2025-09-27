FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
RUN ls /app/target
COPY /target/slonyarskiy-bot*.jar app.jar
RUN ls /app

FROM openjdk:21-jdk-slim
WORKDIR /app

COPY --from=build /app/app.jar app.jar

RUN ls ./
RUN ls /app

ENTRYPOINT ["java", "-jar", "app.jar"]