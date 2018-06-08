package com.example.a777.card_campus.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 *  Activity管理类
 *
 * Created by mac on 2018/6/8.
 */

public class CommonAction {
    private List<Activity> AllActivitites = new ArrayList<Activity>();
    private static CommonAction instance;

    public CommonAction() {

    }

    public synchronized static CommonAction getInstance() {
        if (null == instance) {
            instance = new CommonAction();
        }
        return instance;
    }

    //在Activity基类的onCreate()方法中执行
    public void addActivity(Activity activity) {
        AllActivitites.add(activity);
    }

    //注销是销毁所有的Activity
    public void OutSign() {
        for (Activity activity : AllActivitites) {
            if (activity != null) {
                activity.finish();
            }
        }
    }
}
