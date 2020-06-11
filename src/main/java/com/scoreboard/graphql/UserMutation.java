package com.scoreboard.graphql;

import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserMutation implements GraphQLMutationResolver {

    private UserRepository userRepository;

    @Autowired
    public UserMutation(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    // function name should be exactly same as that defined in the graphql Schema -> "createUser"
    public User createUser(final String name) {
        // every new user gets score=0
        User user = new User(name, 0);
        return userRepository.save(user);
    }

    // both "id" and "score" are mandatory parameter as defined in the graphql Schema.
    public User updateUser(final String id, final Integer score) throws Exception {
        // findById is already implemented by mongodb repository
        Optional<User> optionalUser = userRepository.findById(id);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setScore(user.getScore()+score);
            return userRepository.save(user);
        }

        return null;
    }

}
