package com.example.a777.card_campus.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by 777 on 2018/4/27.
 */

public class QuestionPost implements Serializable {
    int bpost_id;
    User user;
    Timestamp bpost_time;
    String bpost_content;
    String bpost_title;
    public int getBpost_id() {
        return bpost_id;
    }
    public void setBpost_id(int bpost_id) {
        this.bpost_id = bpost_id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Timestamp getBpost_time() {
        return bpost_time;
    }
    public void setBpost_time(Timestamp bpost_time) {
        this.bpost_time = bpost_time;
    }
    public String getBpost_content() {
        return bpost_content;
    }
    public void setBpost_content(String bpost_content) {
        this.bpost_content = bpost_content;
    }
    public String getBpost_title() {
        return bpost_title;
    }
    public void setBpost_title(String bpost_title) {
        this.bpost_title = bpost_title;
    }

}

