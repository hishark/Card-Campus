package com.example.a777.card_campus.util;

import java.util.UUID;

/**
 * Created by mac on 2018/5/16.
 */

public class RandomIDUtil {
    public static String getID(){
        UUID uuid=UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr=str.replace("-", "");
        return uuidStr;
    }
}
