package com.example.a777.card_campus.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a777.card_campus.R;
import com.example.a777.card_campus.bean.User;
import com.example.a777.card_campus.util.CurrentUserUtil;
import com.example.a777.card_campus.util.RandomIDUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendDaideliverpostActivity extends AppCompatActivity {

    private static String URL="http://47.106.148.107:8080/Card-Campus-Server/addDaideliver";
    private static String linkWayURL="http://47.106.148.107:8080/Card-Campus-Server/insertLinkWay";
    private EditText senddeliver_title,senddeliver_content,senddeliver_qq,senddeliver_tel;
    private TextView tv_type;
    private Button daideliver_back,daideliver_send;
    private String deliverpost_content,deliverpost_title,deliverpost_qq,deliverpost_tel,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_daideliverpost);

        getSupportActionBar().hide();

        initView();

        final User user = CurrentUserUtil.getCurrentUser();



        daideliver_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        daideliver_send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                deliverpost_title=senddeliver_title.getText().toString();
                deliverpost_content=senddeliver_content.getText().toString();
                deliverpost_qq=senddeliver_qq.getText().toString();
                deliverpost_tel=senddeliver_tel.getText().toString();
                type=tv_type.getText().toString();


                if(deliverpost_qq.equals("")&&deliverpost_tel.equals("")){
                    Toast.makeText(getApplicationContext(),"请输入联系方式",Toast.LENGTH_LONG).show();
                }else{
                    /**
                     * 用于查找用户表中是否已经填写联系方式
                     */
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody formBody = new FormBody.Builder()
                            .add("user_sno",CurrentUserUtil.getCurrentUser().getUser_sno())
                            .add("user_qq",deliverpost_qq)
                            .add("user_tel",deliverpost_tel)
                            .build();

                    //创建一个请求对象
                    Request request = new Request.Builder()
                            .url(linkWayURL)
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

                    addDeliverPostToServer();
                    Toast.makeText(getApplicationContext(),"发布成功",Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
    }


    private void addDeliverPostToServer() {
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();

        //创建表单请求体
        /**
         * key值与服务器端controller中request.getParameter中的key一致
         */

        RequestBody formBody = new FormBody.Builder()
                .add("dpost_id", RandomIDUtil.getID())
                .add("user_sno",CurrentUserUtil.getCurrentUser().getUser_sno())
                .add("dpost_title",deliverpost_title)
                .add("dpost_content",deliverpost_content)
                .add("dpost_time",String.valueOf(System.currentTimeMillis()))
                .add("dpost_type",type)
                .add("is_solved",String.valueOf(0))
                .build();

        //创建一个请求对象
        Request request = new Request.Builder()
                .url(URL)
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

    private void initView(){
        senddeliver_title=(EditText)this.findViewById(R.id.senddaideliver_title);
        senddeliver_content=(EditText)this.findViewById(R.id.senddaideliver_content);
        senddeliver_qq=(EditText)this.findViewById(R.id.senddaideliver_qq);
        senddeliver_tel=(EditText)this.findViewById(R.id.senddaideliver_phone);
        tv_type=(TextView)this.findViewById(R.id.senddaidelivertype);
        daideliver_back=(Button)this.findViewById(R.id.senddaideliver_back);
        daideliver_send=(Button)this.findViewById(R.id.bt_senddaideliver);
    }
}
