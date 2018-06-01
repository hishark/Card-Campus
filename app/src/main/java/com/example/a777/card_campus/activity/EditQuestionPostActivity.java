package com.example.a777.card_campus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a777.card_campus.R;
import com.example.a777.card_campus.bean.User;
import com.example.a777.card_campus.util.CurrentUserUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EditQuestionPostActivity extends AppCompatActivity {

    EditText et_title,et_content;
    Button bt_back,bt_edit;
    private static String editPostURL="http://47.106.148.107:8080/Card-Campus-Server/editQuestionPost";

    String post_content;
    String post_title;
    String post_id;


    int current_post_Num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question_post);
        getSupportActionBar().hide();
        //得到当前用户
        User user = CurrentUserUtil.getCurrentUser();

        //控件初始化
        initView();


        //得到选中帖子的标题和内容
        post_content = getIntent().getStringExtra("content");
        post_title = getIntent().getStringExtra("title");
        post_id = getIntent().getStringExtra("id");



        et_content.setText(post_content);
        et_title.setText(post_title);


        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String changed_title = et_title.getText().toString().trim();
                String changed_content = et_content.getText().toString().trim();
                updatePost(changed_title,changed_content);
                Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(EditQuestionPostActivity.this,CurrentUserBSTPostDetailActivity.class);


                finish();
            }
        });




    }

    private void updatePost(String changed_title, String changed_content) {
        //实例化OkHttpClient
        OkHttpClient client = new OkHttpClient();
        //创建表单请求体
        FormBody.Builder formBody = new FormBody.Builder();

        /**
         * 传递键值对参数
         * key一定要和LoginActivityAction里面的变量同名！！！一定要同名！！！
         */
        formBody.add("bpost_id",post_id);
        formBody.add("bpost_title",changed_title);
        formBody.add("bpost_content",changed_content);
        formBody.add("bpost_time",String.valueOf(System.currentTimeMillis()));
        formBody.add("user_sno",CurrentUserUtil.getCurrentUser().getUser_sno());

        //创建Request对象
        Request request = new Request.Builder()
                .url(editPostURL)
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
        bt_back = (Button)this.findViewById(R.id.editQuestionPost_back);
        bt_edit = (Button)this.findViewById(R.id.bt_editquestionpost);
        et_content = (EditText)this.findViewById(R.id.et_editQuestionPostContent);
        et_title = (EditText)this.findViewById(R.id.et_editQuestionPostTitle);
    }
}
