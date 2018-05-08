package com.example.a777.card_campus.activity;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import okhttp3.*;

import android.os.Handler;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.bean.User;
import com.example.a777.card_campus.constant.Constant;
import com.example.a777.card_campus.util.CurrentUserUtil;
import com.example.a777.card_campus.util.EducationalSysLoginParse;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {
    private String loginUrl = "http://jwc.jxnu.edu.cn/Default_Login.aspx?preurl=";//教务在线登录url
    private String searchUserUrl = "http://47.106.148.107:8080/Card-Campus-Server/getUserBySno";
    private String insertUserUrl = "http://47.106.148.107:8080/Card-Campus-Server/insertNewUser";

    private EditText et_userName,et_passWord;
    private Button login;
    private static String __VIEWSTATE;//教务在线html动态post参数之一
    private static String __EVENTVALIDATION;;//教务在线html动态post参数之一
    private static String Student_Name;
    private OkHttpClient.Builder builder;// OkHttpClient内部类Builder对象
    private OkHttpClient okHttpClient;// OkHttpClient对象
    private Request request;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.LOGIN_SUCCESS:
                    searchUserInMySQL(et_userName.getText().toString());
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
                case Constant.EXIST_USER:
                    //登录成功且用户在数据库中已存在
                    //就跳转到MainActivity并且把当前用户的姓名和头像传递过去显示在侧滑栏顶部
                    User user = (User)msg.obj;
                    CurrentUserUtil.user = user;
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("student_name",user.getUser_nickname());
                    intent.putExtra("student_avatar",user.getUser_avatar());
                    startActivity(intent);
                    finish();
                    break;
                case Constant.ADD_NEW_USER:
                    //用户不存在，那就插入数据库，然后把名字和默认头像传给MainActivity啦
                    addNewUserToMySQL(et_userName.getText().toString());

                    User current_user = new User();
                    current_user.setUser_nickname(Student_Name);
                    current_user.setUser_sno(et_userName.getText().toString());
                    current_user.setUser_avatar("http://p81fp7gd5.bkt.clouddn.com/useravatar.png");
                    current_user.setUser_qq("");
                    current_user.setUser_sex("");
                    current_user.setUser_tel("");
                    CurrentUserUtil.user = current_user;

                    Intent intent1=new Intent(LoginActivity.this,MainActivity.class);
                    intent1.putExtra("student_name",current_user.getUser_nickname());
                    intent1.putExtra("student_avatar",current_user.getUser_avatar());
                    startActivity(intent1);
                    finish();
                    break;
            }



        }


    };

    private void addNewUserToMySQL(String s) {
        //开子线程访问服务器啦
        //实例化OkHttpClient
        OkHttpClient client = new OkHttpClient();
        //创建表单请求体
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("user_sno",s);
        formBody.add("user_nickname",Student_Name);

        //创建Request对象
        Request request = new Request.Builder()
                .url(insertUserUrl)
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
                    //从服务器取到Json键值对
                    String temp = response.body().string();
                    try{
                        JSONObject jsonObject=new JSONObject(temp);
                        String result = jsonObject.get("addSuccess").toString();
                        Log.d("插入成功了吗",result);
                    }catch (JSONException a){

                    }
                }else{

                }
            }
        });
    }

    public void searchUserInMySQL(String s) {
        //开子线程访问服务器啦
        //实例化OkHttpClient
        OkHttpClient client = new OkHttpClient();
        //创建表单请求体
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("user_sno",s);

        //创建Request对象
        Request request = new Request.Builder()
                .url(searchUserUrl)
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
                    //从服务器取到Json键值对
                    String temp = response.body().string();

                    if(temp.equals("{}")){
                        //数据库不存在当前登录用户！要插入啦！！
                        Log.d("用户测试","查无此人！！！");
                        //通过handler传递数据到主线程
                        Message msg = new Message();
                        msg.what = Constant.ADD_NEW_USER;
                        mHandler.sendMessage(msg);
                    }else{
                        //Log.d("用户测试","烦死啦！！");

                        //数据库存在当前登录用户，那就把拿到该用户，把头像名字设置到侧滑栏上
                        try{
                            /**
                             * 拿到了User的json数据
                             * 利用Gson解析Json键值对
                             */
                            JSONObject jsonObject=new JSONObject(temp);
                            String userresult = jsonObject.get("user").toString();

                            Gson gson = new Gson();
                            User user = gson.fromJson(userresult, User.class);
                            //通过handler传递数据到主线程
                            Message msg = new Message();
                            msg.what = Constant.EXIST_USER;
                            msg.obj = user;
                            mHandler.sendMessage(msg);

                        }catch (JSONException a){

                        }
                    }


                }else{

                }
            }
        });
    }


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
                public void onResponse(Call call, final Response response) throws IOException {
                    final String resp = response.body().string();
                    final Document parse = Jsoup.parse(resp);
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

                            /**
                             * 获取当前登录用户的真实姓名
                             */
                            EducationalSysLoginParse myParse = new EducationalSysLoginParse(resp);
                            final List endList = myParse.getEndList();
                            Student_Name = endList.get(0).toString();
                            Log.d("姓名",Student_Name);

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
                                //成功的话就把用户姓名传过去
                                msg.obj = Student_Name;
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