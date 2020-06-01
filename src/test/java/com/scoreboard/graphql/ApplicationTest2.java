package com.scoreboard.graphql;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTest;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static graphql.Assert.assertNotNull;
import static graphql.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@GraphQLTest
public class ApplicationTest2 {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Test
    public void getAllUsers() throws IOException {
        GraphQLResponse response = graphQLTestTemplate.postForResource("getAllUsers.graphql");
        assertNotNull(response);
        assertTrue(response.isOk());
    }

}
