package com.example.a777.card_campus.bean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by mac on 2018/5/25.
 */

public class MyEntity{
    String postId;
    List<HashMap<String, Object>> replys;
    int  replyNum;

    public MyEntity(String postId, List<HashMap<String, Object>> replys, int replyNum) {
        this.postId = postId;
        this.replys = replys;
        this.replyNum = replyNum;

    }


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public List<HashMap<String, Object>> getReplys() {
        return replys;
    }

    public void setReplys(List<HashMap<String, Object>> replys) {
        this.replys = replys;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }
}
