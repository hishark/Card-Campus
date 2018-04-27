package com.example.a777.card_campus.bean;

import java.sql.Timestamp;

/**
 * Created by 777 on 2018/4/27.
 */

public class QuestionReply {
    int breply_id;
    QuestionPost questionPost;
    User user;
    String breply_content;
    Timestamp breply_time;
    public int getBreply_id() {
        return breply_id;
    }
    public void setBreply_id(int breply_id) {
        this.breply_id = breply_id;
    }
    public QuestionPost getQuestionPost() {
        return questionPost;
    }
    public void setQuestionPost(QuestionPost questionPost) {
        this.questionPost = questionPost;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getBreply_content() {
        return breply_content;
    }
    public void setBreply_content(String breply_content) {
        this.breply_content = breply_content;
    }
    public Timestamp getBreply_time() {
        return breply_time;
    }
    public void setBreply_time(Timestamp breply_time) {
        this.breply_time = breply_time;
    }

}
