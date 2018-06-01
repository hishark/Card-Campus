package com.example.a777.card_campus.activity;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.bean.User;
import com.example.a777.card_campus.util.CurrentUserUtil;
import com.example.a777.card_campus.util.RandomIDUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddQuestionPostActivity extends AppCompatActivity {

    EditText et_title,et_content;
    Button bt_back,bt_post;
    private static String addPostURL="http://47.106.148.107:8080/Card-Campus-Server/addQuestionPost";

    String post_content;
    String post_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question_post);
        getSupportActionBar().hide();
        //得到当前用户
        User user = CurrentUserUtil.getCurrentUser();

        //控件初始化
        initView();



        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        bt_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post_content = et_content.getText().toString();
                post_title = et_title.getText().toString();
                Log.d("帖子内容",post_content);
                Log.d("帖子标题",post_title);
                addPostToServer();
                Toast.makeText(getApplicationContext(),"发布成功",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void addPostToServer() {
        //实例化OkHttpClient
        OkHttpClient client = new OkHttpClient();
        //创建表单请求体
        FormBody.Builder formBody = new FormBody.Builder();

        /**
         * 传递键值对参数
         * key一定要和LoginActivityAction里面的变量同名！！！一定要同名！！！
         */
        formBody.add("bpost_id", RandomIDUtil.getID());
        formBody.add("bpost_title",post_title);
        formBody.add("bpost_content",post_content);
        formBody.add("bpost_time",String.valueOf(System.currentTimeMillis()));
        formBody.add("user_sno",CurrentUserUtil.getCurrentUser().getUser_sno());

        //创建Request对象
        Request request = new Request.Builder()
                .url(addPostURL)
                .post(formBody.build())
                .build();

        /**
         * Get的异步请求，不需要跟同步请求一样开启子线程
         * 但是回调方法还是在子线程中执行的
         * 所以要用到Handler传数据回主线程更新UI
         */
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            //回调的方法执行在子线程
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){

                }else{

                }
            }
        });
    }

    private void initView() {
        bt_back = (Button)this.findViewById(R.id.addQuestionPost_back);
        bt_post = (Button)this.findViewById(R.id.bt_addquestionpost);
        et_content = (EditText)this.findViewById(R.id.et_addQuestionPostContent);
        et_title = (EditText)this.findViewById(R.id.et_addQuestionPostTitle);
    }
}
