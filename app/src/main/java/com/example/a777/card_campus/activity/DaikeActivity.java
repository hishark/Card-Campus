package com.example.a777.card_campus.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.a777.card_campus.R;
import com.example.a777.card_campus.adapter.DaikeAdapter;
import com.example.a777.card_campus.bean.User;
import com.example.a777.card_campus.util.DialogUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DaikeActivity extends AppCompatActivity {

    //控件
    ListView lv_daikes;

    //模拟器用10.0.2.2，真机用无线局域网适配器ip——192.168.137.1
    private static String URL="http://10.0.2.2:8080/Card-Campus-Server/getDaikeList";
    //private static String URL="http://192.168.137.91:8080/Card-Campus-Server/getDaikeList";
    private List<HashMap<String, Object>> daikeResult;
    //Handler用来从子线程往主线程传输数据
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            daikeResult = (List)msg.obj;

            //控件初始化
            initView();
            //适配器一定要写在这里，不然会出现空指针问题
            DaikeAdapter daikeadapter = new DaikeAdapter(getApplicationContext(),daikeResult);
            lv_daikes.setAdapter(daikeadapter);

            lv_daikes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(DaikeActivity.this,DaikeDetailActivity.class);
                    intent.putExtra("daike", daikeResult.get(i));
                    startActivity(intent);
                }
            });
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_daike);
        getSupportActionBar().hide();

        Button daike_back=(Button)this.findViewById(R.id.daike_back);
        daike_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FloatingActionButton sendDaikePost = (FloatingActionButton)this.findViewById(R.id.daike_send);
        sendDaikePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DaikeActivity.this,SendDaikepostActivity.class);
                startActivity(intent);
            }
        });


        /**
         * 从服务器取得代课数据
         */
        getDaikeList();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        lv_daikes = (ListView)this.findViewById(R.id.lv_daikes);
    }
    List<HashMap<String, Object>> daikes=null;
    HashMap<String, Object> daike=null;
    private void getDaikeList() {
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
                .url(URL)
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

                    /*List<HashMap<String, Object>> daikes=null;
                    HashMap<String, Object> daike=null;*/

                    //从服务器取到Json键值对{“key”:“value”}
                    String temp = response.body().string();
                    try{
                        JSONArray jsonArray=new JSONArray(temp);
                        daikes=new ArrayList<HashMap<String, Object>>();

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                            daike=new HashMap<String, Object>();

                            daike.put("dpost_id", jsonObject.get("dpost_id"));
                            daike.put("dpost_content",jsonObject.get("dpost_content"));
                            daike.put("dpost_title", jsonObject.get("dpost_title"));
                            //daike.put("dpost_time", jsonObject.get("dpost_time"));
                            daike.put("dpost_type", jsonObject.get("dpost_type"));
                            daike.put("is_solved", jsonObject.get("is_solved"));

                            /**
                             * 拿到了User的json数据
                             * 利用Gson解析Json键值对
                             */
                            String userresult = jsonObject.get("user").toString();
                            Gson gson = new Gson();
                            User user = gson.fromJson(userresult, User.class);

                            daike.put("user",user);

                            daikes.add(daike);
                        }


                    }catch (JSONException a){

                    }

                    //通过handler传递数据到主线程
                    Message msg = new Message();
                    msg.obj = daikes;
                    handler.sendMessage(msg);

                }else{

                }
            }
        });
    }

}
