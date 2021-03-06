package com.example.a777.card_campus.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.adapter.DaifoodAdapter;
import com.example.a777.card_campus.bean.User;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DaifoodActivity extends AppCompatActivity {

    //控件
    ListView lv_daifoods;
    FloatingActionButton sendDaifoodPost;
    private SwipeRefreshLayout swipeRefreshLayout;//SwipeRefreshLayout下拉刷新控件
    //模拟器用10.0.2.2，真机用无线局域网适配器ip——192.168.137.1
    //买了个服务器 ip为47.106.148.107
    //private static String URL="http://10.0.2.2:8080/Card-Campus-Server/getDaifoodList";
    private static String DFLISTURL="http://47.106.148.107:8080/Card-Campus-Server/getDaifoodList";
    private List<HashMap<String, Object>> daifoodResult;
    //Handler用来从子线程往主线程传输数据
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            daifoodResult = (List)msg.obj;

            //控件初始化
            initView();
            sendDaifoodPost.attachToListView(lv_daifoods);

            initswipe();
            //发帖一般最新的在最上面，用这句话就可以让帖子倒序显示
            Collections.reverse(daifoodResult);
            //适配器一定要写在这里，不然会出现空指针问题
            DaifoodAdapter daifoodadapter = new DaifoodAdapter(getApplicationContext(),daifoodResult);
            lv_daifoods.setAdapter(daifoodadapter);
            lv_daifoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(DaifoodActivity.this,DaifoodDetailActivity.class);
                    intent.putExtra("daifood", daifoodResult.get(i));
                    startActivity(intent);
                }
            });
            sendDaifoodPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DaifoodActivity.this,SendDaifoodpostActivity.class);
                    startActivity(intent);
                }
            });
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daifood);
        getSupportActionBar().hide();

        Button daifood_back=(Button)this.findViewById(R.id.daifood_back);
        daifood_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        /**
         * 从服务器取得代拿外卖数据
         */
        getDaifoodList();

    }

    private void initswipe() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO 刷新的时候获取数据
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDaifoodList(); //刷新
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });
    }


    /**
     * 初始化控件
     */
    private void initView() {
        lv_daifoods = (ListView)this.findViewById(R.id.lv_daifoods);
        sendDaifoodPost = (FloatingActionButton)this.findViewById(R.id.daifood_send);
        swipeRefreshLayout = (SwipeRefreshLayout)this.findViewById(R.id.refreshlayout_daifood);
    }

    private void getDaifoodList() {
        //实例化OkHttpClient
        OkHttpClient client = new OkHttpClient();
        //创建表单请求体
        FormBody.Builder formBody = new FormBody.Builder();

        /**
         * 传递键值对参数
         * key一定要和LoginActivityAction里面的变量同名！！！一定要同名！！！
         */
        /*formBody.add("username",userName);
        formBody.add("password",passWord);*/

        //创建Request对象
        Request request = new Request.Builder()
                .url(DFLISTURL)
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

                    List<HashMap<String, Object>> daifoods=null;
                    HashMap<String, Object> daifood=null;

                    //从服务器取到Json键值对{“key”:“value”}
                    String temp = response.body().string();
                    try{
                        JSONArray jsonArray=new JSONArray(temp);
                        daifoods=new ArrayList<HashMap<String, Object>>();

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                            daifood=new HashMap<String, Object>();

                            daifood.put("dpost_id", jsonObject.get("dpost_id"));
                            daifood.put("dpost_content",jsonObject.get("dpost_content"));
                            daifood.put("dpost_title", jsonObject.get("dpost_title"));
                            daifood.put("dpost_time", jsonObject.get("dpost_time"));
                            daifood.put("dpost_type", jsonObject.get("dpost_type"));
                            daifood.put("is_solved", jsonObject.get("is_solved"));

                            /**
                             * 拿到了User的json数据
                             * 利用Gson解析Json键值对
                             */
                            String userresult = jsonObject.get("user").toString();
                            Gson gson = new Gson();
                            User user = gson.fromJson(userresult, User.class);

                            daifood.put("user",user);

                            //timeresult是从数据库插到的dpost_time字段，类型为timestamp
                            //从数据库读出来长这个样子：存到timeresult里面
                            //{"date":21,"day":6,"hours":23,"minutes":18,"month":3,"nanos":0,"seconds":0,"time":1524323880000,"timezoneOffset":-480,"year":118}
                            String timeresult = jsonObject.get("dpost_time").toString();

                            //利用fastJson——JSON取出timeresult里面的time字段，也就是13位的时间戳
                            long time = JSON.parseObject(timeresult).getLong("time");
                            Timestamp trueTime = new Timestamp(time);
                            //把时间put进daike
                            daifood.put("dpost_time",trueTime);
                            //把时间put进daike
                            daifoods.add(daifood);
                        }


                    }catch (JSONException a){

                    }

                    //通过handler传递数据到主线程
                    Message msg = new Message();
                    msg.obj = daifoods;
                    handler.sendMessage(msg);

                }else{

                }
            }
        });
    }
}
