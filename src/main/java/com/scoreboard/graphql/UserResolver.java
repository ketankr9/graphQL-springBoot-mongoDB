package com.scoreboard.graphql;

import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserResolver implements GraphQLResolver<User> {

    private final UserRepository userRepository ;

    @Autowired
    public UserResolver(UserRepository userRepository) {
        this.userRepository = userRepository ;
    }

}
