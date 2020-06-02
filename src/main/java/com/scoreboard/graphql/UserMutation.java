package com.scoreboard.graphql;

import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class UserMutation implements GraphQLMutationResolver {

    private UserRepository userRepository;

    @Autowired
    public UserMutation(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(final String name) {
        User user = new User(name, 0);
        return userRepository.save(user);
    }

    public User updateUser(final String id, final Integer score) throws Exception {
        Optional<User> optionalUser = userRepository.findById(id);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setScore(score);
            return userRepository.save(user);
        }

        throw new Exception("User not found with id = "+id);
    }

}
