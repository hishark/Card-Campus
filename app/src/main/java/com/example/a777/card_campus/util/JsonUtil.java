package com.example.a777.card_campus.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.HashMap;

/**
 * Created by mac on 2018/5/29.
 */

public class JsonUtil {

    /**
     * 将json转化成HashMap
     * @param jsonStr
     * @return
     */
    public static HashMap<String, Object> convertJsonStrToMap(String jsonStr){

        HashMap<String, Object> hashmap = JSON.parseObject(jsonStr,new TypeReference<HashMap<String, Object>>(){} );
        return hashmap;
    }
}
