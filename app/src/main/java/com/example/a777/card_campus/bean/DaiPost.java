package com.example.a777.card_campus.bean;

import java.sql.Timestamp;

/**
 * Created by 777 on 2018/4/27.
 */

public class DaiPost {
    int dpost_id;
    String dpost_content;
    String dpost_title;
    Timestamp dpost_time;
    String dpost_type;//用于判断帖子属于代代代的哪个模块
    int is_solved; //判断该活动是否已经解决  1表示已解决  0表示未解决
    User user;

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public int getDpost_id() {
        return dpost_id;
    }
    public void setDpost_id(int dpost_id) {
        this.dpost_id = dpost_id;
    }
    public String getDpost_content() {
        return dpost_content;
    }
    public void setDpost_content(String dpost_content) {
        this.dpost_content = dpost_content;
    }
    public String getDpost_title() {
        return dpost_title;
    }
    public void setDpost_title(String dpost_title) {
        this.dpost_title = dpost_title;
    }
    public Timestamp getDpost_time() {
        return dpost_time;
    }
    public void setDpost_time(Timestamp dpost_time) {
        this.dpost_time = dpost_time;
    }
    public String getDpost_type() {
        return dpost_type;
    }
    public void setDpost_type(String dpost_type) {
        this.dpost_type = dpost_type;
    }
    public int getIs_solved() {
        return is_solved;
    }
    public void setIs_solved(int is_solved) {
        this.is_solved = is_solved;
    }



}

