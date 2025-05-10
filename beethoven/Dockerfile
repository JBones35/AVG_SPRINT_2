FROM eclipse-temurin:21-jdk-alpine

LABEL maintainer="Jonas Kirschner <kirschner.jonas@gmx.de>"
LABEL description="Spring Boot Maven Server: kirschner.flaig.beethoven"
LABEL org.opencontainers.image.title="kirschner.flaig.beethoven-server"

ARG JAR_FILE=target/*.jar

WORKDIR /app

COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]