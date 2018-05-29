package com.example.a777.card_campus.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by mac on 2018/5/28.
 */

public class ToastUtil {
    public static void createToast(Context context, String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }
}
