package com.example.a777.card_campus.util;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by mac on 2018/6/1.
 */

public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
