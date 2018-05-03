package com.example.a777.card_campus.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.adapter.QuestionReplyAdapter;
import com.example.a777.card_campus.bean.User;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class QuestionPostDetailActivity extends AppCompatActivity {
    TextView questionpostdetail_username,questionpostdetail_time,questionpostdetail_title,questionpostdetail_content;

    CircleImageView user_avatar;
    ListView lv_reply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_post_detail);
        getSupportActionBar().hide();

        /**
         * 返回按钮
         */
        Button questionpost_back=(Button)this.findViewById(R.id.questionpost_detail_back);
        questionpost_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /**
         * 控件初始化
         */
        initView();


        /**
         * 获取到所有的帖子，以及帖子对应的所有回复
         */
        Intent intent = getIntent();
        List<HashMap<String,Object>> QuestionPost =  (List<HashMap<String,Object>>)intent.getSerializableExtra("QuestionPost");
        List<List<HashMap<String,Object>>> QuestionReplys =  (List<List<HashMap<String,Object>>>)intent.getSerializableExtra("QuestionReplys");
        int current_selected_item = intent.getIntExtra("currentItem",0);

        HashMap<String, Object> current_questionpost;
        current_questionpost = QuestionPost.get(current_selected_item);

        User user = (User)current_questionpost.get("user");
        String username = user.getUser_nickname();

        String timestamp = current_questionpost.get("bpost_time").toString();
        String post_time = timestamp.substring(0,timestamp.length()-2);

        questionpostdetail_time.setText(post_time);
        questionpostdetail_username.setText(username);
        questionpostdetail_content.setText(current_questionpost.get("bpost_content").toString());
        Glide.with(getApplicationContext()).load(user.getUser_avatar()).into(user_avatar);

        /*for(int i=1;i<=QuestionPost.size();i++){
            Log.d("看一下回复",QuestionReplys.get(i).get(0).get("breply_content").toString());
        }*/
        List<HashMap<String,Object>> currentPostReplys = QuestionReplys.get(current_selected_item+1);

        QuestionReplyAdapter questionReplyAdapter = new QuestionReplyAdapter(getApplicationContext(),currentPostReplys);
        lv_reply.setAdapter(questionReplyAdapter);
    }

    private void initView() {
        questionpostdetail_username=(TextView)this.findViewById(R.id.questionpost_username);
        questionpostdetail_time=(TextView)this.findViewById(R.id.questionpost_time);
        questionpostdetail_title=(TextView)this.findViewById(R.id.questionpost_title);
        questionpostdetail_content=(TextView)this.findViewById(R.id.questionpostdetail_content);



        lv_reply = (ListView)this.findViewById(R.id.lv_questionpost_reply);

        user_avatar = (CircleImageView)this.findViewById(R.id.questionpost_avatar);
    }
}
