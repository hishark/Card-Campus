package com.example.a777.card_campus.fragment;

import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.activity.AddQuestionPostActivity;
import com.example.a777.card_campus.activity.DaikeActivity;
import com.example.a777.card_campus.activity.DaikeDetailActivity;
import com.example.a777.card_campus.activity.QuestionPostDetailActivity;
import com.example.a777.card_campus.adapter.DaikeAdapter;
import com.example.a777.card_campus.adapter.QuestionPostAdapter;
import com.example.a777.card_campus.bean.QuestionPost;
import com.example.a777.card_campus.bean.User;
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

public class EverythingFragment extends Fragment {
    //定义成员变量
    private View view;
    private ListView lv_question;
    //买了个服务器 ip为47.106.148.107
    private static String URL="http://47.106.148.107:8080/Card-Campus-Server/getQuestionPostList";
    private List<HashMap<String, Object>> QuestionPostResult;
    private static String reply_URL="http://47.106.148.107:8080/Card-Campus-Server/getQuestionPostReplyNum";
    private List<HashMap<String, Object>> QuestionReplyResult;
    private List<List<HashMap<String, Object>>> QuestionReplyResults;
    private int QuestionReplyNum[];
    private int count=0;
    private int count2=1;
    /*//Handler用来从子线程往主线程传输数据
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            QuestionPostResult = (List)msg.obj;
            Log.d("handler",QuestionPostResult.size()+"");

            QuestionReplyNum = new int[QuestionPostResult.size()+1];


            for(int i=0;i<QuestionPostResult.size();i++){
                getQuestionPostReplyNum(Integer.parseInt(QuestionPostResult.get(i).get("bpost_id").toString()));
            }




            super.handleMessage(msg);
        }
    };


    private Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {

            QuestionReplyResult = (List)msg.obj;

            *//**
             * QuestionReplyNum就成功按照帖子的id记下了每个帖子的回复数量
             *//*
            QuestionReplyNum[msg.what] = ((List)msg.obj).size();
            count++;

            if(count==QuestionReplyNum.length-1){
                //然后再加载到适配器里去

                //控件初始化
                initView();

                //适配器一定要写在这里，不然会出现空指针问题
                QuestionPostAdapter questionPostAdapter = new QuestionPostAdapter(getActivity().getApplicationContext(),QuestionPostResult,QuestionReplyNum);
                lv_question.setAdapter(questionPostAdapter);
            }


            Log.d("handler1:ReplyNum",msg.what+"===="+QuestionReplyNum[msg.what]+"");

            Log.d("handler1",QuestionReplyResult.size()+"");


            super.handleMessage(msg);
        }
    };*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_everything, container, false);


        /**
         * 从服务器取得所有的百事通问题数据
         * 并且取得每一条记录的回复数量
         * 给ListView配置适配器
         */
        getDataFromClient_setListView();



        /**
         * 悬浮球的点击事件
         */
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab_questionpost_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"test",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity().getApplicationContext(),AddQuestionPostActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }



    private void getDataFromClient_setListView() {
        final Handler handler1 = new Handler() {
            public void handleMessage(Message msg) {

                /**
                 * QuestionReplyResult接收来自子线程从服务器查询到的帖子回复数据
                 */
                QuestionReplyResult = (List)msg.obj;

                /**
                 * QuestionReplyResults就成功按照帖子的id记下了每个帖子的回复（所有属性都有）
                 */
                QuestionReplyResults.set(msg.what,QuestionReplyResult);

                /**
                 * QuestionReplyNum就成功按照帖子的id记下了每个帖子的回复数量
                 */
                QuestionReplyNum[msg.what] = ((List)msg.obj).size();
                Log.d("啊啊啊啊怎么了", QuestionReplyNum[msg.what]+"");
                count++;

                //count是计数器，当所有的帖子都记录过回复数量之后，就可以设置适配器了
                if(count==QuestionReplyNum.length-1){
                    //然后再加载到适配器里去

                    //控件初始化
                    initView();

                    //适配器一定要写在这里，不然会出现空指针问题
                    QuestionPostAdapter questionPostAdapter = new QuestionPostAdapter(getActivity().getApplicationContext(),QuestionPostResult,QuestionReplyNum);
                    lv_question.setAdapter(questionPostAdapter);


                    lv_question.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //Toast.makeText(getContext(),i+"",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity().getApplicationContext(),QuestionPostDetailActivity.class);
                            intent.putExtra("QuestionPost",(Serializable)QuestionPostResult);
                            intent.putExtra("QuestionReplys",(Serializable)QuestionReplyResults);
                            intent.putExtra("currentItem",i);
                            startActivity(intent);
                        }
                    });
                }

                super.handleMessage(msg);
            }
        };

        //Handler用来从子线程往主线程传输数据
         final Handler handler = new Handler() {
            public void handleMessage(Message msg) {

                QuestionPostResult = (List)msg.obj;


                Log.d("handler",QuestionPostResult.size()+"");

                QuestionReplyNum = new int[QuestionPostResult.size()+1];

                QuestionReplyResults = new ArrayList<List<HashMap<String, Object>>>();
                List<HashMap<String, Object>> a = new ArrayList<HashMap<String, Object>>();
                HashMap<String, Object> test = new HashMap<String, Object>();
                a.add(test);
                for(int i=0;i<QuestionPostResult.size()+1;i++){
                    QuestionReplyResults.add(a);
                }

                for(int i=0;i<QuestionPostResult.size();i++){
                    getQuestionPostReplyNum(Integer.parseInt(QuestionPostResult.get(i).get("bpost_id").toString()),handler1);
                }




                super.handleMessage(msg);
            }
        };


        /**
         * 从服务器取得所有的百事通问题数据
         */
        getQuestionPostList(handler);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        lv_question = (ListView)view.findViewById(R.id.lv_question);
    }

    private void getQuestionPostList(final Handler handler) {
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

                    List<HashMap<String, Object>> questionPosts=null;
                    HashMap<String, Object> questionPost=null;

                    //从服务器取到Json键值对{“key”:“value”}
                    String temp = response.body().string();
                    try{
                        JSONArray jsonArray=new JSONArray(temp);
                        questionPosts=new ArrayList<HashMap<String, Object>>();

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                            questionPost=new HashMap<String, Object>();

                            questionPost.put("bpost_id", jsonObject.get("bpost_id"));
                            questionPost.put("bpost_content", jsonObject.get("bpost_content"));
                            questionPost.put("bpost_title", jsonObject.get("bpost_title"));

                            /**
                             * 拿到了User的json数据
                             * 利用Gson解析Json键值对
                             */
                            String userresult = jsonObject.get("user").toString();
                            Gson gson = new Gson();
                            User user = gson.fromJson(userresult, User.class);
                            questionPost.put("user",user);

                            //timeresult是从数据库插到的dpost_time字段，类型为timestamp
                            //从数据库读出来长这个样子：存到timeresult里面
                            //{"date":21,"day":6,"hours":23,"minutes":18,"month":3,"nanos":0,"seconds":0,"time":1524323880000,"timezoneOffset":-480,"year":118}
                            String timeresult = jsonObject.get("bpost_time").toString();

                            //利用fastJson——JSON取出timeresult里面的time字段，也就是13位的时间戳
                            long time = JSON.parseObject(timeresult).getLong("time");
                            Timestamp trueTime = new Timestamp(time);

                            //把时间put进questionPost
                            questionPost.put("bpost_time",trueTime);

                            questionPosts.add(questionPost);
                        }


                    }catch (JSONException a){

                    }

                    //通过handler传递数据到主线程
                    Message msg = new Message();
                    msg.obj = questionPosts;

                    handler.sendMessage(msg);

                }else{

                }
            }
        });
    }

    private void getQuestionPostReplyNum(final int bpost_id,final Handler handler1) {
        //实例化OkHttpClient
        OkHttpClient client = new OkHttpClient();
        //创建表单请求体
        FormBody.Builder formBody = new FormBody.Builder();

        /**
         * 传递键值对参数
         * key一定要和LoginActivityAction里面的变量同名！！！一定要同名！！！
         */
        formBody.add("bpost_id",String.valueOf(bpost_id));

        //创建Request对象
        Request request = new Request.Builder()
                .url(reply_URL)
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

                    List<HashMap<String, Object>> questionReplys=null;
                    HashMap<String, Object> questionReply=null;

                    //从服务器取到Json键值对{“key”:“value”}
                    String temp = response.body().string();
                    try{
                        JSONArray jsonArray=new JSONArray(temp);
                        questionReplys=new ArrayList<HashMap<String, Object>>();

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                            questionReply=new HashMap<String, Object>();

                            questionReply.put("breply_id", jsonObject.get("breply_id"));
                            questionReply.put("breply_content",jsonObject.get("breply_content"));
                            /**
                             * 拿到了User的json数据
                             * 利用Gson解析Json键值对
                             */
                            String userresult = jsonObject.get("user").toString();
                            Gson gson1 = new Gson();
                            User user = gson1.fromJson(userresult, User.class);
                            questionReply.put("user",user);

                            String questionPostresult = jsonObject.get("questionPost").toString();
                            Gson gson2 = new Gson();
                            QuestionPost questionPost = gson2.fromJson(questionPostresult, QuestionPost.class);
                            questionReply.put("questionPost",questionPost);

                            //timeresult是从数据库插到的dpost_time字段，类型为timestamp
                            //从数据库读出来长这个样子：存到timeresult里面
                            //{"date":21,"day":6,"hours":23,"minutes":18,"month":3,"nanos":0,"seconds":0,"time":1524323880000,"timezoneOffset":-480,"year":118}
                            String timeresult = jsonObject.get("breply_time").toString();

                            //利用fastJson——JSON取出timeresult里面的time字段，也就是13位的时间戳
                            long time = JSON.parseObject(timeresult).getLong("time");
                            Timestamp trueTime = new Timestamp(time);

                            //把时间put进questionReply
                            questionReply.put("breply_time",trueTime);

                            questionReplys.add(questionReply);
                        }


                    }catch (JSONException a){

                    }

                    //通过handler传递数据到主线程
                    Message msg = new Message();
                    msg.obj = questionReplys;
                    msg.what = bpost_id;
                    //QuestionReplyNum.add(questionReplys.size());
                    Log.d("访问服务器的子线程里得到的数据：","Post的id："+bpost_id+"该Post的回复数"+questionReplys.size()+"");
                    handler1.sendMessage(msg);

                }else{

                }
            }
        });
    }

    @Override
    public void onResume() {
        getDataFromClient_setListView();
        super.onResume();
    }

}
