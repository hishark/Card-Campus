package com.example.a777.card_campus.util;



/**
 * Created by MummyDing on 16-1-25.
 * GitHub: https://github.com/MummyDing
 * Blog: http://blog.csdn.net/mummyding
 */
public class TextUtil {
    public static boolean isNull(String str){
        if(str == null || str.replaceAll("\\s*", "").equals("")){
            return true;
        }
        return false;
    }



}
