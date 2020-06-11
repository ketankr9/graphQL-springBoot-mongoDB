package com.scoreboard.graphql;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class UserQuery implements GraphQLQueryResolver {

    private final UserRepository userRepository;

    @Autowired
    public UserQuery(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    // The function name should be exactly same as that in the graphqlSchema defined, "getAllUsers"
    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    // "getTopNUsers" defined in graphqlSchema with optional parameter count.
    public List<User> getTopNUsers(final Integer count){

        // userRepository.functions() already implemented by MongoRepository,
        // make sure to define findByScoreIsGreaterThanOrderByScoreDesc in the UserRepository.java interface
        Stream<User> str = userRepository.findByScoreIsGreaterThanOrderByScoreDesc(0);

        //if no argument provided, return all users (score > 0) sorted in descending order by user.score
        if(count == null)
            return str.collect(Collectors.toList());
        return str.limit(count).collect(Collectors.toList());
    }

    // "getUserById" defined in graphqlSchema with mandatory parameter "id"
    // so, this function will never get executed with id==null.
    public User getUserById(final String id){
        Optional<User> opt = userRepository.findById(id);
        return opt.orElse(null);
    }
}
