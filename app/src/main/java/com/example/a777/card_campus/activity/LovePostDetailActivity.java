package com.example.a777.card_campus.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.adapter.LoveReplyAdapter;
import com.example.a777.card_campus.bean.LovePost;
import com.example.a777.card_campus.bean.LoveReply;
import com.example.a777.card_campus.bean.QuestionPost;
import com.example.a777.card_campus.bean.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LovePostDetailActivity extends AppCompatActivity {

    //真机地址192.168.137.1
    private String URL="http://10.0.2.2:8080/Card-Campus-Server/getLovePostList";
    private Button loveone_back;
    private TextView title,content,name,time,replynum,send;
    private EditText et_comment;
    private LinearLayout llCommentTrue,llCommentFalse,click_comment;
    private ListView lv_allReply;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_post_detail);
        getSupportActionBar().hide();


        initView();

        loveone_back=(Button)this.findViewById(R.id.lovepostdetail_back);
        loveone_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        HashMap<String,Object> loveone_item = new HashMap<String,Object>();
        HashMap<String,Object> reply_item = new HashMap<String,Object>();
        //List<HashMap<String, Object>>loveone_item = new ArrayList<HashMap<String, Object>>();
        //List<HashMap<String, Object>> reply_item = new ArrayList<HashMap<String, Object>>();
        int count;
        count=intent.getIntExtra("num",0);
        loveone_item=(HashMap<String,Object>)intent.getSerializableExtra("lovepost");



        for(int i=0;i<loveone_item.size();i++) {
            String username = loveone_item.get("love_username").toString();
            String lovepost_title = loveone_item.get("love_title").toString();
            String lovepost_content = loveone_item.get("love_content").toString();
            String timestamp = loveone_item.get("love_time").toString();
            String lovepost_time = timestamp.substring(0, timestamp.length() - 2);

            title.setText(lovepost_title);
            content.setText(lovepost_content);
            name.setText(username);
            time.setText(lovepost_time);
            replynum.setText(String.valueOf(count));
        }
        final String replyContent=et_comment.getText().toString();

        click_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(llCommentTrue.isShown()){
                    hideComentView();
                }else {
                    showCommentView();
                }
            }
        });
        llCommentFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCommentView();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(replyContent.equals("")){
                    Toast.makeText(getApplicationContext(),"总得留下点什么吧，少年！",Toast.LENGTH_SHORT).show();
                }else{

                }
            }
        });


        /**
         * 告白评论加载
         */
        List<HashMap<String, Object>> current_lovereplys = new ArrayList<>();
        current_lovereplys =  (List<HashMap<String,Object>>)getIntent().getSerializableExtra("current_loveReplys");
        Log.d("看一下传过来没有哦",current_lovereplys.toString());
        //此时就得到了所有的回复
        //Log.d("看一下传过来的回复内容",current_lovereplys.get(0).get("lreply_content").toString());
        LoveReplyAdapter loveReplyAdapter = new LoveReplyAdapter(getApplicationContext(),current_lovereplys);
        lv_allReply.setAdapter(loveReplyAdapter);




    }

    /**
     * 实现按下back键不退出app,而是返回上一步操作
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if (llCommentTrue.isShown()) {
                hideComentView();
                return true;
            }else{
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    private void showCommentView() {
        llCommentTrue.setVisibility(View.VISIBLE);
        llCommentFalse.setVisibility(View.GONE);
    }
    public void hideComentView() {
        llCommentTrue.setVisibility(View.GONE);
        llCommentFalse.setVisibility(View.VISIBLE);
    }

    private void initView() {
        loveone_back=(Button)this.findViewById(R.id.lovepostdetail_back);
        title=(TextView)this.findViewById(R.id.tv_loveone_title);
        content=(TextView)this.findViewById(R.id.tv_loveone_content);
        name=(TextView)this.findViewById(R.id.tv_loveone_name);
        time=(TextView)this.findViewById(R.id.tv_loveone_time);
        replynum=(TextView)this.findViewById(R.id.tv_loveone_replyNum);
        click_comment=(LinearLayout)this.findViewById(R.id.click_comment);
        et_comment=(EditText)this.findViewById(R.id.edt_loveone_comment);
        llCommentTrue=(LinearLayout)this.findViewById(R.id.ll_loveone_comment_true);
        llCommentFalse=(LinearLayout)this.findViewById(R.id.ll_loveone_comment_false);
        send=(TextView)this.findViewById(R.id.btn_loveone_send);
        lv_allReply = (ListView)this.findViewById(R.id.lv_loveone_reply);
    }
}
