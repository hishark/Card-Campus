package com.example.a777.card_campus.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.a777.card_campus.R;
import com.example.a777.card_campus.bean.User;
import com.example.a777.card_campus.util.CurrentUserUtil;

public class AddQuestionPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question_post);

        User user = CurrentUserUtil.getCurrentUser();
    }
}
