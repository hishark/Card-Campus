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

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendDaifoodpostActivity extends AppCompatActivity {

    //private static String URL="http://10.0.2.2:8080/Card-Campus-Server/addDaifood";
    //private static String linkWayURL="http://10.0.2.2:8080/Card-Campus-Server/insertLinkWay";
    private static String URL="http://192.168.137.1:8080/Card-Campus-Server/addDaifood";
    private static String linkWayURL="http://192.168.137.1:8080/Card-Campus-Server/insertLinkWay";
    private EditText senddf_title,senddf_content,senddf_qq,senddf_tel;
    private TextView tv_type;
    private Button daifood_back,daifood_send;
    private String dfpost_content,dfpost_title,dfpost_qq,dfpost_tel,type;
    private int current_post_Num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_daifoodpost);
        getSupportActionBar().hide();

        initView();

        final User user = CurrentUserUtil.getCurrentUser();


        //得到当前帖子数量，然后给即将添加的帖子设置id
        current_post_Num = getIntent().getIntExtra("daiPostNum2",0);
        Log.d("current_post_num", String.valueOf(current_post_Num));

        daifood_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        daifood_send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                dfpost_title=senddf_title.getText().toString();
                dfpost_content=senddf_content.getText().toString();
                dfpost_qq=senddf_qq.getText().toString();
                dfpost_tel=senddf_tel.getText().toString();
                type=tv_type.getText().toString();


                if(dfpost_qq.equals("")&&dfpost_tel.equals("")){
                    Toast.makeText(getApplicationContext(),"请输入联系方式",Toast.LENGTH_LONG).show();
                }else{
                    /**
                     * 用于查找用户表中是否已经填写联系方式
                     */
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody formBody = new FormBody.Builder()
                            .add("user_sno",CurrentUserUtil.getCurrentUser().getUser_sno())
                            .add("user_qq",dfpost_qq)
                            .add("user_tel",dfpost_tel)
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

                    addDFPostToServer();
                    //Toast.makeText(getApplicationContext(),"发布成功",Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
    }


    private void addDFPostToServer() {
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();

        //创建表单请求体
        /**
         * key值与服务器端controller中request.getParameter中的key一致
         */

        RequestBody formBody = new FormBody.Builder()
                .add("dpost_id",String.valueOf(current_post_Num+1))
                .add("user_sno",CurrentUserUtil.getCurrentUser().getUser_sno())
                .add("dpost_title",dfpost_title)
                .add("dpost_content",dfpost_content)
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

    private void initView (){
        senddf_title=(EditText)this.findViewById(R.id.senddaifood_title);
        senddf_content=(EditText)this.findViewById(R.id.senddaifood_content);
        senddf_qq=(EditText)this.findViewById(R.id.senddaifood_qq);
        senddf_tel=(EditText)this.findViewById(R.id.senddaifood_phone);
        tv_type=(TextView)this.findViewById(R.id.senddaifoodtype);
        daifood_back=(Button)this.findViewById(R.id.senddaifood_back);
        daifood_send=(Button)this.findViewById(R.id.bt_senddaifood);
    }
}
