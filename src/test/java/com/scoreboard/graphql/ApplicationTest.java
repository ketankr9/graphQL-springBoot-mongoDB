package com.scoreboard.graphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTest;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.scoreboard.graphql.annotations.MutationTest;
import com.scoreboard.graphql.annotations.QueryTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static graphql.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/*
 *  Based on: https://github.com/graphql-java-kickstart/graphql-spring-boot/blob/4c7cdbaa8b576134bad0370add9241207e267904/example-graphql-tools/src/test/java/com/graphql/sample/boot/GraphQLToolsSampleApplicationTest.java
 */
@RunWith(SpringRunner.class)
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

    @Before
    public void init(){
        user = new User("Some name", 123);
        user.setId("kdjfyvk89dej");
    }

    public void assertResponse(GraphQLResponse response) throws IOException {
        assertTrue(response.isOk());
        // {"data":{"users":[{"id":"12345","score":1000,"name":"utsav"}]}}
        assertEquals(user.getId(), response.get("$.data.users[0].id"));
        assertEquals(user.getName(), response.get("$.data.users[0].name"));
        assertEquals(String.valueOf(user.getScore()), response.get("$.data.users[0].score"));
    }

    @Test
    @QueryTest(withArgs = true)
    public void getTopNUsersCount() throws IOException {

        when(userRepository.findByScoreIsGreaterThanOrderByScoreDesc(0))
                .thenReturn(Stream.of(user));

        response = graphQLTestTemplate.postForResource("getTopNUsersCount.graphql");
        assertResponse(response);
    }

    @Test
    @QueryTest
    public void getTopNUsers() throws IOException {

        when(userRepository.findByScoreIsGreaterThanOrderByScoreDesc(0))
                .thenReturn(Stream.of(user));

        response = graphQLTestTemplate.postForResource("getTopNUsers.graphql");
        assertResponse(response);
    }

    @Test
    @QueryTest
    public void getAllUsers() throws IOException {

        when(userRepository.findAll())
                .thenReturn(Collections.singletonList(user));

        response = graphQLTestTemplate.postForResource("getAllUsers.graphql");
        assertResponse(response);
    }

    @Test
    @MutationTest
    public void createUser() throws IOException {
        user.setScore(0);
        when(userMutation.createUser(user.getName()))
                .thenReturn(user);

        ObjectNode variables = new ObjectMapper().createObjectNode()
                .put("name", user.getName());

        response = graphQLTestTemplate.perform("createUser.graphql", variables);

        assertEquals(user.getName(), response.get("$.data.user.name"));
        assertEquals(String.valueOf(user.getScore()), response.get("$.data.user.score"));
    }

    @Ignore
    @Test
    @MutationTest
    public void updateUser() throws Exception {

        // Why can't I mock the userRepository operations?
         when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
         when(userRepository.save(user)).thenReturn(user);
        // Below Mock works where as above fails.
//        when(userMutation.updateUser(user.getId(), user.getScore()))
//                .thenReturn(user);
//        when(userRepository.save(user)).the

        ObjectNode variables = new ObjectMapper().createObjectNode()
                .put("id", user.getId())
                .put("score", user.getScore());

        response = graphQLTestTemplate.perform("updateUser.graphql", variables);
        System.out.println(response.getRawResponse());
        assertEquals(user.getId(), response.get("$.data.user.id"));
        assertEquals(user.getName(), response.get("$.data.user.name"));
        assertEquals(String.valueOf(user.getScore()), response.get("$.data.user.score"));
    }

}