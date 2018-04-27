package com.example.a777.card_campus.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by 777 on 2018/3/27.
 */

public class DialogUtil {
    public static AlertDialog createDialog(String message, Context context){
        AlertDialog.Builder bd = new AlertDialog.Builder(context);
        bd.setTitle("提示信息");
        bd.setMessage(message);
        bd.setPositiveButton("OK", null);
        return bd.create();
    }
}
