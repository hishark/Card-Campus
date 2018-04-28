package com.example.a777.card_campus.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.*;

import com.example.a777.card_campus.R;
import com.example.a777.card_campus.bean.DaiPost;

import de.hdodenhof.circleimageview.CircleImageView;

public class DaikeDetailActivity extends AppCompatActivity {

    private CircleImageView head;
    private TextView dkdetail_username,dkdetail_time,daikedetail_title,daikedetail_content;
    private Button dkdetal_qq,dkdetal_tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daike_detail);
        getSupportActionBar().hide();

        Button daike_back=(Button)this.findViewById(R.id.daikedetail_back);
        daike_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initView();

        Intent intent = getIntent();
        // 通过daike得到得到对象
        // getSerializableExtra得到序列化数据
        DaiPost dk = (DaiPost) intent.getSerializableExtra("daike");
        dkdetail_username.setText(dk.getUser().getUser_nickname());
        dkdetail_time.setText("00:00:00");
        daikedetail_title.setText(dk.getDpost_title());
        daikedetail_content.setText(dk.getDpost_content());
    }

    private void initView() {
        dkdetail_username=(TextView)this.findViewById(R.id.dkdetail_username);
        dkdetail_time=(TextView)this.findViewById(R.id.dkdetail_username);
        daikedetail_title=(TextView)this.findViewById(R.id.daikedetail_title);
        daikedetail_content=(TextView)this.findViewById(R.id.daikedetail_content);
        dkdetal_qq=(Button)this.findViewById(R.id.dkdetail_qq);
        dkdetal_tel=(Button)this.findViewById(R.id.dkdetail_tel);
    }
}
