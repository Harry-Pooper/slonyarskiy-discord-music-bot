FROM openjdk:21-jdk-slim
WORKDIR /app

COPY /target/slonyarskiy-bot*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]