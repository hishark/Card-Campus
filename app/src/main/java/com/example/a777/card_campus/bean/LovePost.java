package com.example.a777.card_campus.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by 777 on 2018/4/27.
 */

public class LovePost implements Serializable {
    String love_id;
    String love_username;
    int is_anonymous;
    String love_content;
    String love_title;
    Timestamp love_time;

    public String getLove_id() {
        return love_id;
    }

    public void setLove_id(String love_id) {
        this.love_id = love_id;
    }

    public String getLove_username() {
        return love_username;
    }
    public void setLove_username(String love_username) {
        this.love_username = love_username;
    }
    public int getIs_anonymous() {
        return is_anonymous;
    }
    public void setIs_anonymous(int is_anonymous) {
        this.is_anonymous = is_anonymous;
    }
    public String getLove_content() {
        return love_content;
    }
    public void setLove_content(String love_content) {
        this.love_content = love_content;
    }
    public String getLove_title() {
        return love_title;
    }
    public void setLove_title(String love_title) {
        this.love_title = love_title;
    }
    public Timestamp getLove_time() {
        return love_time;
    }
    public void setLove_time(Timestamp love_time) {
        this.love_time = love_time;
    }


}
