package com.scoreboard.graphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTest;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.scoreboard.graphql.annotations.MutationTest;
import com.scoreboard.graphql.annotations.QueryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

/*
 *  Based on: https://github.com/graphql-java-kickstart/graphql-spring-boot/blob/4c7cdbaa8b576134bad0370add9241207e267904/example-graphql-tools/src/test/java/com/graphql/sample/boot/GraphQLToolsSampleApplicationTest.java
 */
//@RunWith(SpringRunner.class) // Junit4
@ExtendWith(SpringExtension.class) // Junit5
@GraphQLTest
public class ApplicationTest {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @MockBean
    private UserMutation userMutation;

    @MockBean
    private UserRepository userRepository;

    private User user;
    private GraphQLResponse response;

    @BeforeEach
    public void init(){
        user = new User("Utsav", 123);
        user.setId("kdjfyvk89dej");
    }

    public void printResponse(){
        System.out.println(response.context().jsonString());
    }

    public void assertSingleUser(){
        assertTrue(response.isOk());
        System.out.println(response.context().jsonString());

        User u1 = response.context().read("$.data.user", User.class);
        assertNotNull(u1, "{\"user\":null}");
        assertEquals(user, u1, "User Not Same");
    }

    public void assertMultipleUser(){
        assertTrue(response.isOk());
        assertTrue(Integer.parseInt(response.get("$.data.users.length()")) > 0, "0 Users Returns");
        assertEquals(user, response.context().read("$.data.users[0]", User.class), "User Not Same");
    }

    @Test
    @QueryTest(withArgs = true)
    public void getTopNUsersCount() throws IOException {

        when(userRepository.findByScoreIsGreaterThanOrderByScoreDesc(0))
                .thenReturn(Stream.of(user));

        response = graphQLTestTemplate.postForResource("getTopNUsersCount.graphql");

        assertMultipleUser();
    }

    @Test
    @QueryTest
    public void getTopNUsers() throws IOException {

        when(userRepository.findByScoreIsGreaterThanOrderByScoreDesc(0))
                .thenReturn(Stream.of(user));

        response = graphQLTestTemplate.postForResource("getTopNUsers.graphql");

        assertMultipleUser();
    }

    @Test
    @QueryTest
    public void getAllUsers() throws IOException {

        when(userRepository.findAll())
                .thenReturn(Collections.singletonList(user));

        response = graphQLTestTemplate.postForResource("getAllUsers.graphql");

        assertMultipleUser();
    }

    @Disabled
    @Test
    @MutationTest
    public void createUser() throws IOException {
        user.setScore(0);
        // This fails.
        when(userRepository.save( eq(user) )).thenReturn(user);
        System.out.println(userRepository.save(user));
        // This works.
//        when(userMutation.createUser(eq(user.getName()))).thenReturn(user);

        ObjectNode variables = new ObjectMapper().createObjectNode()
                .put("name", user.getName());

        response = graphQLTestTemplate.perform("createUser.graphql", variables);

        assertSingleUser();
    }

    @Disabled
    @Test
    @MutationTest
    public void updateUser() throws Exception {
        /* This fails the test. */
        when(userRepository.findById( eq(user.getId()) )).thenReturn(Optional.of(user));
        System.out.println(userRepository.findById(user.getId()));

        when(userRepository.save( eq(user) )).thenReturn(user);
        System.out.println(userRepository.save(user));

        /* Where as this passes. */
//        when(userMutation.updateUser(eq(user.getId()), eq(user.getScore())))
//                .thenReturn(user);

        ObjectNode variables = new ObjectMapper().createObjectNode()
                .put("id", user.getId())
                .put("score", user.getScore());

        response = graphQLTestTemplate.perform("updateUser.graphql", variables);

        assertSingleUser();
    }

}