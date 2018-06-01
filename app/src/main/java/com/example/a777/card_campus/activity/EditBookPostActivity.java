package com.example.a777.card_campus.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.a777.card_campus.R;
import com.example.a777.card_campus.bean.User;
import com.example.a777.card_campus.fragment.MyPostFragment;
import com.example.a777.card_campus.util.CurrentUserUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditBookPostActivity extends AppCompatActivity {

    private static String editbookURL="http://47.106.148.107:8080/Card-Campus-Server/editBookPost";

    private EditText title,content;
    private Button back,edit;
    private RadioGroup rg_state;
    private RadioButton sold,sale;

    private String post_title,post_id,post_content,post_state,state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book_post);
        getSupportActionBar().hide();
        //得到当前用户
        User user = CurrentUserUtil.getCurrentUser();

        //控件初始化
        initView();

        post_title=getIntent().getStringExtra("title");
        post_content=getIntent().getStringExtra("content");
        post_id=getIntent().getStringExtra("id");
        post_state=getIntent().getStringExtra("state");

        title.setText(post_title);
        content.setText(post_content);
        if(post_state.equals("1")){
            sold.setChecked(true);
            sale.setChecked(false);
        }else{
            sold.setChecked(false);
            sale.setChecked(true);
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String changed_title=title.getText().toString();
                String changed_content=content.getText().toString();
                for(int i=0;i<rg_state.getChildCount();i++){
                    RadioButton radioButton=(RadioButton)rg_state.getChildAt(i);
                    if(radioButton.isChecked()){
                        state=radioButton.getText().toString();
                    }
                }

                if(state.equals("已卖出")){
                    state="1";
                }else {
                    state="0";
                }
                Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();

                updataBookPost(changed_title,changed_content,state);

                Intent intent = new Intent(EditBookPostActivity.this, MyPostFragment.class);
                finish();
            }
        });

    }

    private void updataBookPost(String changed_title, String changed_content,String issolve) {
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();

        //创建表单请求体
        /**
         * key值与服务器端controller中request.getParameter中的key一致
         */

        RequestBody formBody = new FormBody.Builder()
                .add("book_id",post_id)
                .add("user_sno",CurrentUserUtil.getCurrentUser().getUser_sno())
                .add("book_title",changed_title)
                .add("book_describe",changed_content)
                .add("book_time",String.valueOf(System.currentTimeMillis()))
                .add("is_sold",issolve)
                .build();

        //创建一个请求对象
        Request request = new Request.Builder()
                .url(editbookURL)
                .post(formBody)
                .build();
        /**
         * Get的异步请求，不需要跟同步请求一样开启子线程
         * 但是回调方法还是在子线程中执行的
         * 所以要用到Handler传数据回主线程更新UI
         */
        okHttpClient.newCall(request).enqueue(new Callback() {
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
        title=(EditText)this.findViewById(R.id.et_editBookPostTitle);
        content=(EditText)this.findViewById(R.id.et_editBookPostContent);
        back=(Button)this.findViewById(R.id.editBookPost_back);
        edit=(Button)this.findViewById(R.id.bt_editbookpost);
        rg_state=(RadioGroup)this.findViewById(R.id.rg_bookstate);
        sold=(RadioButton)this.findViewById(R.id.rb_sold);
        sale=(RadioButton)this.findViewById(R.id.rb_sale);
    }
}
