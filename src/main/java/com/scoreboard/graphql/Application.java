package com.scoreboard.graphql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/*
 * Based on:
 * Spring-graphQL:
 * https://dzone.com/articles/a-beginners-guide-to-graphql-with-spring-boot
 * Spring-graphQL-mongoDB:
 * https://bezkoder.com/spring-boot-graphql-mongodb-example-graphql-java/
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
