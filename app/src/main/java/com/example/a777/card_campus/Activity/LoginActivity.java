package com.example.a777.card_campus.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


import okhttp3.*;

import android.os.Handler;
import android.widget.Toast;

import com.example.a777.card_campus.R;
import com.example.a777.card_campus.constant.Constant;

public class LoginActivity extends AppCompatActivity {
    private String loginUrl = "http://jwc.jxnu.edu.cn/Default_Login.aspx?preurl=";//教务在线登录url

    private EditText et_userName,et_passWord;
    Button login;
    private static String __VIEWSTATE;//教务在线html动态post参数之一
    private static String __EVENTVALIDATION;;//教务在线html动态post参数之一
    private OkHttpClient.Builder builder;// OkHttpClient内部类Builder对象
    private OkHttpClient okHttpClient;// OkHttpClient对象
    private Request request;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.LOGIN_SUCCESS:
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                //网络不顺畅
                case Constant.LOGIN_FAILURE_NETWORK_ERROR:
                    Toast.makeText(getApplicationContext(), "请检查网络~", Toast.LENGTH_SHORT).show();
                    break;
                //登陆出错（没有输入学号）
                case Constant.LOGIN_FAILURE_SNO_NULL:
                    Toast.makeText(getApplicationContext(),"请您输入学号",Toast.LENGTH_SHORT).show();
                    break;
                //登陆出错（学号不存在）
                case Constant.LOGIN_FAILURE_SNO_INCORRECT:
                    Toast.makeText(getApplicationContext(),"学号不存在",Toast.LENGTH_SHORT).show();
                    break;
                //登陆出错（没有输入密码）
                case Constant.LOGIN_FAILURE_PASSWORD_NULL:
                    Toast.makeText(getApplicationContext(),"请您输入密码",Toast.LENGTH_SHORT).show();
                    break;
                //登陆出错（您的密码不正确）
                case Constant.LOGIN_FAILURE_PASSWORD_INCORRECT:
                    Toast.makeText(getApplicationContext(),"您的密码不正确",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏标题栏
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_login);

        et_userName = (EditText) findViewById(R.id.username);
        et_passWord = (EditText) findViewById(R.id.password);
        login=(Button)findViewById(R.id.bt_login);


        login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                LoginJXNU(loginUrl,getApplication());
            }
        });

    }

    /**
     * 检测当前网络状态
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean checkNetwork(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }



    public void LoginJXNU(final String loginUrl,Context context) {

        builder = new OkHttpClient.Builder();
        okHttpClient = builder.build();

        if(checkNetwork(context)){
            request = new Request.Builder().url(loginUrl).build();
            //Log.e("LoginJXNU",request.toString());
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String resp = response.body().string();
                    Document parse = Jsoup.parse(resp);
                    //如果返回密码错误则停止获得参数并需要重新输入学号密码
                    Element content = parse.getElementById("__VIEWSTATE");
                    __VIEWSTATE = content.attr("value");
                    Element content2 = parse.getElementById("__EVENTVALIDATION");
                    __EVENTVALIDATION = content2.attr("value");
                    String userName = et_userName.getText().toString();
                    String passWord = et_passWord.getText().toString();
                    //登录教务在线的所有参数post
                    FormBody formBody = new FormBody.Builder()
                            .add("__EVENTTARGET", "")
                            .add("__EVENTARGUMENT", "")
                            .add("__LASTFOCUS", "")
                            .add("__VIEWSTATE", get__VIEWSTATE())
                            .add("__EVENTVALIDATION", get__EVENTVALIDATION())
                            .add("rblUserType", "User")
                            .add("ddlCollege", "180     ")
                            .add("StuNum", userName)
                            .add("TeaNum", "")
                            .add("Password", passWord)
                            .add("Login", "登陆")
                            .build();
                    request = new Request.Builder().post(formBody).url(loginUrl).build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            String resp = response.body().string();
                            Document parse = Jsoup.parse(resp);
                            Elements getIsPwd = parse.select("script");

                            String s = getIsPwd.html();
                            Log.e("LoginActivity", getIsPwd.html());
                            Message msg = mHandler.obtainMessage();
                            //String.matches() 这个方法主要是返回是否匹配指定的字符串，如果匹配则为true,否则为false;
                            Boolean isStuNumEmpty = s.matches(".*(请您输入学号).*");//*表达式不出现或出现任意次，相当于 {0,}，比如："\^*b"可以匹配 "b","^^^b"..
                            Boolean isStuNum = s.matches(".*(学号不存在).*");
                            Boolean isPasswordEmpty = s.matches(".*(请您输入密码).*");
                            Boolean isPassword = s.matches(".*(您的密码不正确).*");

                            if (isStuNumEmpty) {
                                msg.what = Constant.LOGIN_FAILURE_SNO_NULL;//传递msg  请您输入学号
                                mHandler.sendMessage(msg);
                                return;
                            } else if (isStuNum) {
                                msg.what = Constant.LOGIN_FAILURE_SNO_INCORRECT;//传递msg  学号不存在
                                mHandler.sendMessage(msg);
                                return;
                            } else if (isPasswordEmpty) {
                                msg.what = Constant.LOGIN_FAILURE_PASSWORD_NULL;//传递msg  请您输入密码
                                mHandler.sendMessage(msg);
                                return;
                            } else if (isPassword) {
                                msg.what = Constant.LOGIN_FAILURE_PASSWORD_INCORRECT;//传递msg  您的密码不正确
                                mHandler.sendMessage(msg);
                                return;
                            } else {
                                msg.what = Constant.LOGIN_SUCCESS;//传递msg  成功
                                mHandler.sendMessage(msg);
                                return;

                            }
                        }
                    });
                }
            });
        }else{
            Message msg = mHandler.obtainMessage();
            msg.what = Constant.LOGIN_FAILURE_NETWORK_ERROR;
            mHandler.sendMessage(msg);
            return;
        }
    }

    public String get__VIEWSTATE() {
        return __VIEWSTATE;
    }

    public String get__EVENTVALIDATION() {
        return __EVENTVALIDATION;
    }



}