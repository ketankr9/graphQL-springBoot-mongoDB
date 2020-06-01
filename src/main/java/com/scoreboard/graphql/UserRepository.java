package com.scoreboard.graphql;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

//Below annotation not needed if the repository is in the same package(or subpackage) of @SpringBootApplication
@EnableMongoRepositories
public interface UserRepository extends MongoRepository<User, String> {
    Stream<User> findByScoreIsGreaterThanOrderByScoreDesc(Integer n);
    Optional<User> findById(String id);
}