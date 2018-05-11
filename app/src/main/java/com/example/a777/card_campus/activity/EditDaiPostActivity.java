package com.example.a777.card_campus.activity;

import android.content.Intent;
import android.support.annotation.IdRes;
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

public class EditDaiPostActivity extends AppCompatActivity {

    private static String editpostURL="http://47.106.148.107:8080/Card-Campus-Server/editDaiPost";

    private EditText title,content;
    private Button back,edit;
    private RadioGroup rg_state;
    private RadioButton solve,nosolve;

    private String post_title,post_id,post_content,post_state,state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dai_post);
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
            solve.setChecked(true);
            nosolve.setChecked(false);
        }else{
            solve.setChecked(false);
            nosolve.setChecked(true);
        }


        /*for(int i=0;i<rg_state.getChildCount();i++){
            RadioButton radioButton=(RadioButton)rg_state.getChildAt(i);
            if(radioButton.isChecked()){
                state=radioButton.getText().toString();
            }

        }*/

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

                if(state.equals("已解决")){
                    state="1";
                }else {
                    state="0";
                }
                Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();

                updataDaiPost(changed_title,changed_content,state);

                Intent intent = new Intent(EditDaiPostActivity.this, MyPostFragment.class);
                finish();
            }
        });

    }

    private void updataDaiPost(String changed_title, String changed_content,String issolve) {
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();

        //创建表单请求体
        /**
         * key值与服务器端controller中request.getParameter中的key一致
         */

        RequestBody formBody = new FormBody.Builder()
                .add("dpost_id",post_id)
                .add("user_sno",CurrentUserUtil.getCurrentUser().getUser_sno())
                .add("dpost_title",changed_title)
                .add("dpost_content",changed_content)
                .add("dpost_time",String.valueOf(System.currentTimeMillis()))
                .add("is_solved",issolve)
                .build();

        //创建一个请求对象
        Request request = new Request.Builder()
                .url(editpostURL)
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
        title=(EditText)this.findViewById(R.id.et_editDaiPostTitle);
        content=(EditText)this.findViewById(R.id.et_editDaiPostContent);
        back=(Button)this.findViewById(R.id.editDaiPost_back);
        edit=(Button)this.findViewById(R.id.bt_editdaipost);
        rg_state=(RadioGroup)this.findViewById(R.id.rg_daistate);
        solve=(RadioButton)this.findViewById(R.id.rb_solved);
        nosolve=(RadioButton)this.findViewById(R.id.rb_nosolve);
    }
}
