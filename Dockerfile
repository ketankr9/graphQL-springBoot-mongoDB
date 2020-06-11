FROM openjdk:8-jdk-alpine

ARG JAR_FILE=target/springboot-graphql-1.0-SNAPSHOT.jar
ARG SPRING_CONFIG=src/main/resources/application.prod.properties

COPY ${JAR_FILE} ./springboot-graphql.jar
COPY ${SPRING_CONFIG} ./production.properties

ENTRYPOINT ["java", "-jar", "/springboot-graphql.jar", "--spring.config.location=/production.properties"]