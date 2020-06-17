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

    // "id" is managed by spring->data but "setId" function needed while testing.
    public void setId(String id) { this.id = id; }

    public void setScore(Integer score){ this.score = score; }

    public User deepCopy(){
        User newUser = new User(this.getName(), this.getScore());
        newUser.setId(this.getId());
        return newUser;
    }

    // if all the values are same, then we will say that the users are same,
    // unlike object location comparision.
    @Override
    public boolean equals(Object obj) {
        return ((User) obj).getId().equals(this.getId())
                && ((User) obj).getName().equals(this.getName())
                && ((User) obj).getScore().equals(this.getScore());
    }

    @Override
    public String toString() {
        return "User: (id:"+id + ", name:" + name + ", score:" + score + ")";
    }

}