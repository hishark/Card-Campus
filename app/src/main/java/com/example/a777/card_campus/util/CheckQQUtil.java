package com.example.a777.card_campus.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * Created by mac on 2018/5/29.
 */

public class CheckQQUtil {
    /**
     * 判断qq是否正确
     */
    public static boolean qqCheck(String qq){
        boolean flag=true;
        if(qq.length()>=6&&qq.length()<=11){
            if(!qq.startsWith("0")){
                char [] ch=qq.toCharArray();
                for(int i=0;i<ch.length;i++){
                    if(!Character.isDigit(ch[i])){

                        flag=false;
                        break;
                    }
                }
            }else{
                flag=false;
            }
        }else{
            flag=false;
        }
        return flag;
    }


}
