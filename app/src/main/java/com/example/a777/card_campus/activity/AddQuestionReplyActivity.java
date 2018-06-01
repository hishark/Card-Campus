package com.example.a777.card_campus.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
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
import com.example.a777.card_campus.bean.QuestionPost;
import com.example.a777.card_campus.bean.User;
import com.example.a777.card_campus.util.CurrentUserUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddQuestionReplyActivity extends AppCompatActivity {

    private static String addQuestionReplyURL="http://47.106.148.107:8080/Card-Campus-Server/addQuestionReply";
    private static String getQuestionReplysURL="http://47.106.148.107:8080/Card-Campus-Server/getQuestionReplyList";

    public static void a(){

    }
    private int reply_postid;
    private int currentReplyNum;

    EditText et_content;
    Button back,add;
    private List<HashMap<String, Object>> replyResult;
    String replyContent;
    public static final String action = "jason.broadcast.action";

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            replyResult = (List)msg.obj;
            Log.d("发广播的这里",replyResult.toString());

            Intent intent = new Intent(action);
            intent.putExtra("NewReplyResults", (Serializable)replyResult);
            sendBroadcast(intent);

            super.handleMessage(msg);
        }
    };

    public Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what==1){
                getQuestionReplyList();
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question_reply);
        getSupportActionBar().hide();

        initView();

        reply_postid=getIntent().getIntExtra("reply_postId",0);
        currentReplyNum = getIntent().getIntExtra("currentReplyNum",0);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replyContent = et_content.getText().toString().trim();
                addPostToServer();
                Toast.makeText(getApplicationContext(),"发表成功",Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initView() {
        et_content = (EditText)this.findViewById(R.id.et_addQuestionReplyContent);
        back = (Button)this.findViewById(R.id.addQuestionReply_back);
        add = (Button)this.findViewById(R.id.bt_addquestionreply);
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
        formBody.add("breply_id",String.valueOf(currentReplyNum+1));
        formBody.add("breply_content",replyContent);
        formBody.add("breply_time",String.valueOf(System.currentTimeMillis()));
        formBody.add("user_sno", CurrentUserUtil.getCurrentUser().getUser_sno());
        formBody.add("bpost_id",String.valueOf(reply_postid));

        //创建Request对象
        Request request = new Request.Builder()
                .url(addQuestionReplyURL)
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

                    //通过handler传递数据到主线程
                    Message msg = new Message();

                    msg.what = 1;
                    handler1.sendMessage(msg);


                }else{

                }
            }
        });
    }

    private void getQuestionReplyList() {
        //实例化OkHttpClient
        OkHttpClient client = new OkHttpClient();
        //创建表单请求体
        FormBody.Builder formBody = new FormBody.Builder();

        /**
         * 传递键值对参数
         * key值与服务器端controller中request.getParameter中的key一致
         */
        /*formBody.add("username",userName);
        formBody.add("password",passWord);*/

        //创建Request对象
        Request request = new Request.Builder()
                .url(getQuestionReplysURL)
                .build();
        //.post(formBody.build())

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

                    List<HashMap<String, Object>> replys=null;
                    HashMap<String, Object> reply=null;

                    //从服务器取到Json键值对{“key”:“value”}
                    String temp = response.body().string();
                    Log.d("看temp完整数据",temp);

                    try{
                        JSONArray jsonArray=new JSONArray(temp);
                        replys=new ArrayList<HashMap<String, Object>>();

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                            reply=new HashMap<String, Object>();

                            reply.put("breply_id", jsonObject.get("breply_id"));
                            reply.put("breply_content",jsonObject.get("breply_content"));

                            /**
                             * 拿到了User的json数据
                             * 利用Gson解析Json键值对
                             */
                            String userresult = jsonObject.get("user").toString();
                            Gson gson = new Gson();
                            User user = gson.fromJson(userresult, User.class);
                            reply.put("user",user);

                            /**
                             * 拿到了QuestionPost的json数据
                             * 哇天坑
                             * QuestionPost里面还有一个TimeStamp和user要单独拎出来
                             * 没法用Gson了直接全部json一个个解析吧
                             */
                            String postresult = jsonObject.get("questionPost").toString();

                            JSONObject jsonObject1 = new JSONObject(postresult);
                            String posttime = jsonObject1.get("bpost_time").toString();
                            long posttime1 = JSON.parseObject(posttime).getLong("time");
                            Timestamp truePostTime = new Timestamp(posttime1);

                            String bpost_id = jsonObject1.get("bpost_id").toString();

                            String postcontent = jsonObject1.get("bpost_content").toString();
                            String posttitle = jsonObject1.get("bpost_title").toString();

                            String post_userresult = jsonObject1.get("user").toString();
                            Gson gson2 = new Gson();
                            User postuser = gson2.fromJson(post_userresult, User.class);


                            QuestionPost post1 = new QuestionPost();
                            post1.setBpost_time(truePostTime);
                            post1.setBpost_id(bpost_id);
                            post1.setBpost_content(postcontent);
                            post1.setBpost_title(posttitle);
                            post1.setUser(postuser);

                            reply.put("questionPost",post1);


                            //timeresult是从数据库插到的dpost_time字段，类型为timestamp
                            //从数据库读出来长这个样子：存到timeresult里面
                            //{"date":21,"day":6,"hours":23,"minutes":18,"month":3,"nanos":0,"seconds":0,"time":1524323880000,"timezoneOffset":-480,"year":118}
                            String timeresult = jsonObject.get("breply_time").toString();

                            //利用fastJson——JSON取出timeresult里面的time字段，也就是13位的时间戳
                            long time = JSON.parseObject(timeresult).getLong("time");
                            Timestamp trueTime = new Timestamp(time);

                            //把时间put进daike
                            reply.put("breply_time",trueTime);

                            //将13位时间戳转换为年月日时分秒！
                            /*Long time1 = 1524323880000L;
                            SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date= new Date(time1);
                            String d = format.format(date);*/

                            replys.add(reply);
                        }


                    }catch (JSONException a){

                    }

                    //通过handler传递数据到主线程
                    Message msg = new Message();
                    Log.d("什么情况",replys.size()+"");
                    msg.obj = replys;
                    handler.sendMessage(msg);

                }else{

                }
            }
        });
    }
}
