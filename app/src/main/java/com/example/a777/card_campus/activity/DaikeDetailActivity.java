package com.example.a777.card_campus.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.bean.DaiPost;
import com.example.a777.card_campus.bean.User;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class DaikeDetailActivity extends AppCompatActivity {

    private CircleImageView user_avatar;
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

        HashMap<String, Object> daike_item = new HashMap<String,Object>();

        // 通过daike得到得到对象
        // getSerializableExtra得到序列化数据
        daike_item = (HashMap<String, Object>)intent.getSerializableExtra("daike");

        User user = (User)daike_item.get("user");
        String username = user.getUser_nickname();

        String timestamp = daike_item.get("dpost_time").toString();
        String post_time = timestamp.substring(0,timestamp.length()-2);

        dkdetail_username.setText(username);
        Glide.with(getApplicationContext()).load(user.getUser_avatar()).into(user_avatar);
        dkdetail_time.setText(post_time);
        daikedetail_title.setText(daike_item.get("dpost_title").toString());
        daikedetail_content.setText(daike_item.get("dpost_content").toString());
    }

    private void initView() {
        dkdetail_username=(TextView)this.findViewById(R.id.dkdetail_username);
        dkdetail_time=(TextView)this.findViewById(R.id.dkdetail_time);
        daikedetail_title=(TextView)this.findViewById(R.id.daikedetail_title);
        daikedetail_content=(TextView)this.findViewById(R.id.daikedetail_content);
        dkdetal_qq=(Button)this.findViewById(R.id.dkdetail_qq);
        dkdetal_tel=(Button)this.findViewById(R.id.dkdetail_tel);
        user_avatar=(CircleImageView)this.findViewById(R.id.dkdetail_avatar);
    }
}
