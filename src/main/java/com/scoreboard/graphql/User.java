package com.scoreboard.graphql;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//@Document annotation for overriding the collection name, else default to className('User').
@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String name;
    private Integer score;

    public User(){}
    public User(String name){
        this.name = name;
    }
    public User(String name, Integer score){
        this.name = name;
        this.score = score;
    }
//    public User(String name, Integer score, String id){
//        this.name = name;
//        this.score = score;
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Integer getScore() {
        return score;
    }

    public void setName(String name){ this.name = name; }

    public void setId(String id) { this.id = id; }

    public void setScore(Integer score){ this.score = score; }

    @Override
    public String toString() {
        return "User: "+id + ":" + name + ":" + (score);
    }
}