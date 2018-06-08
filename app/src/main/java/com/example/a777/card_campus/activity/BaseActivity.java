package com.example.a777.card_campus.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.a777.card_campus.util.CommonAction;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //把Activity添加到集合里面
        CommonAction.getInstance().addActivity(this);
    }
}
