package com.scoreboard.graphql;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class UserQuery implements GraphQLQueryResolver {

    private UserRepository userRepository;

    @Autowired
    public UserQuery(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    public List<User> getTopNUsers(final Integer count){
        Stream<User> str = userRepository.findByScoreIsGreaterThanOrderByScoreDesc(0);
        if(count == null)
            return str.collect(Collectors.toList());
        return str.limit(count).collect(Collectors.toList());
    }
}
