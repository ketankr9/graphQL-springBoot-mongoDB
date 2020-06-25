# FROM openjdk:8-jdk-alpine

# ARG JAR_FILE=target/springboot-graphql-1.0-SNAPSHOT.jar
# ARG SPRING_CONFIG=src/main/resources/application.prod.properties

# COPY ${JAR_FILE} ./springboot-graphql.jar
# COPY ${SPRING_CONFIG} ./production.properties

# ENTRYPOINT ["java", "-jar", "/springboot-graphql.jar", "--spring.config.location=/production.properties"]

FROM maven:3.6.3-openjdk-11 AS build
RUN mkdir -p /usr/src/app/src 
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml package

FROM gcr.io/distroless/java
COPY --from=build /usr/src/app/target/*.jar /usr/app/graphqlServer.jar
ARG SPRING_CONFIG=src/main/resources/application.prod.properties
COPY ${SPRING_CONFIG} /usr/app/application.prod.properties
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/app/graphqlServer.jar", "--spring.config.location=/usr/app/application.prod.properties"]
