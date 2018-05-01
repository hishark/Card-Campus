package com.example.a777.card_campus.bean;

import java.io.Serializable;

/**
 * Created by 777 on 2018/4/27.
 */

public class User implements Serializable {
    String user_sno;
    String user_nickname;
    String user_avatar;
    String user_sex;
    String user_tel;
    String user_qq;


    public String getUser_sno() {
        return user_sno;
    }
    public void setUser_sno(String user_sno) {
        this.user_sno = user_sno;
    }
    public String getUser_nickname() {
        return user_nickname;
    }
    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }
    public String getUser_avatar() {
        return user_avatar;
    }
    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }
    public String getUser_sex() {
        return user_sex;
    }
    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }
    public String getUser_tel() {
        return user_tel;
    }
    public void setUser_tel(String user_tel) {
        this.user_tel = user_tel;
    }
    public String getUser_qq() {
        return user_qq;
    }
    public void setUser_qq(String user_qq) {
        this.user_qq = user_qq;
    }


}
