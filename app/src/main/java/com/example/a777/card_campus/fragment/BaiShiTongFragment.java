package com.example.a777.card_campus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.activity.AddQuestionPostActivity;
import com.example.a777.card_campus.activity.BSTPostDetailActivity;
import com.example.a777.card_campus.activity.CurrentUserBSTPostDetail2Activity;
import com.example.a777.card_campus.activity.CurrentUserBSTPostDetailActivity;
import com.example.a777.card_campus.activity.CurrentUserPostDetailActivity;
import com.example.a777.card_campus.activity.QuestionPostDetailActivity;
import com.example.a777.card_campus.adapter.QuestionPostAdapter;
import com.example.a777.card_campus.bean.User;
import com.example.a777.card_campus.util.CurrentUserUtil;
import com.example.a777.card_campus.util.JsonUtil;
import com.example.a777.card_campus.util.ToastUtil;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BaiShiTongFragment extends Fragment {
    private static final int GET_BSTPOST_SUCCESS = 1;
    private static final int GET_BSTPOST_REPLY_NUM_SUCCESS = 2;
    private static final int GET_USER_POST_NUM_SUCCESS = 3;
    private static final int GET_BSTPOST_REPLY_NUM_SUCCESS_REFRESH = 4;

    private static String getBSTPostURL="http://47.106.148.107:8080/Card-Campus-Server/getQuestionPostList";
    private static String getBSTPostReplyURL="http://47.106.148.107:8080/Card-Campus-Server/getBSTPostReplyNum";
    private static String getUserPostNumURL="http://47.106.148.107:8080/Card-Campus-Server/userBSTPostNum";

    private View view;
    private FloatingActionButton bt_sendBSTPost;
    private HashMap<String, Object> BSTPostReplyNum;
    private List<HashMap<String, Object>> BSTPosts;
    private List<HashMap<String, Object>> CurrentUserBSTPosts;
    private boolean isGetPost = false;
    private boolean isGetReplyNum = false;
    private boolean isGetNum = false;
    private boolean isRefresh = false;
    private QuestionPostAdapter questionPostAdapter;
    private ListView lv_BSTPosts;
    private LinearLayout question_top;
    private int current_post_Num;
    private TextView CurrentUserquestionNumber;


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what){
                case GET_BSTPOST_REPLY_NUM_SUCCESS:
                    BSTPostReplyNum = (HashMap<String, Object>)msg.obj;
                    isGetReplyNum = true;
                    //ToastUtil.createToast(getContext(),BSTPostReplyNum.get("9").toString());
                    break;
                case GET_BSTPOST_SUCCESS:
                    BSTPosts = (List<HashMap<String, Object>>)msg.obj;
                    isGetPost = true;
                    //ToastUtil.createToast(getContext(),BSTPosts.toString());
                    break;
                case GET_USER_POST_NUM_SUCCESS:
                    current_post_Num = (Integer)msg.obj;
                    isGetNum = true;
                    CurrentUserquestionNumber.setText(current_post_Num+"");
                    break;
                case GET_BSTPOST_REPLY_NUM_SUCCESS_REFRESH:
                    isRefresh = true;
                    BSTPostReplyNum.clear();
                    BSTPostReplyNum.putAll((HashMap<String, Object>)msg.obj);
                    questionPostAdapter.notifyDataSetChanged();
                    break;

            }

            if(isGetReplyNum&&isGetPost&&isGetNum&&!isRefresh){
                //ToastUtil.createToast(getContext(),"yes!");
                questionPostAdapter = new QuestionPostAdapter(getContext(),BSTPosts,BSTPostReplyNum);
                lv_BSTPosts.setAdapter(questionPostAdapter);

                Log.d("sssssize",BSTPosts.size()+"");
                CurrentUserBSTPosts = new ArrayList<>();
                for(int i=0;i<BSTPosts.size();i++){
                    User user = (User)(BSTPosts.get(i).get("user"));
                    String sno = user.getUser_sno();
                    if(sno==CurrentUserUtil.getCurrentUser().getUser_sno()||sno.equals(CurrentUserUtil.getCurrentUser().getUser_sno())){
                        CurrentUserBSTPosts.add(BSTPosts.get(i));
                    }
                }


                question_top.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(getActivity().getApplicationContext(),"点这进个人界面",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity().getApplicationContext(),CurrentUserBSTPostDetail2Activity.class);
                        /*intent.putExtra("QuestionPostNum",current_post_Num);
                        intent.putExtra("QuestionPost",(Serializable)CurrentUserBSTPosts);
                        intent.putExtra("QuestionPostReplyNum",(Serializable)BSTPostReplyNum);*/
                        //不需要传数据过去，直接去那边查就好了
                        startActivity(intent);
                    }
                });

                lv_BSTPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getActivity().getApplicationContext(),BSTPostDetailActivity.class);

                        intent.putExtra("ClickQuestionPost",(Serializable)BSTPosts.get(i));
                        intent.putExtra("ReplyNum",(Serializable)BSTPostReplyNum);
                        //startActivity(intent);
                        startActivityForResult(intent,1);
                    }
                });
            }



            super.handleMessage(msg);
        }
    };

    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                getBSTPostReplyNumRefresh();
                break;
            case 2:
                isGetReplyNum = false;
                isGetPost = false;
                getBSTPostReplyNum();
                getBSTPostList();
                break;
            default:
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bai_shi_tong, container, false);
        /**
         * 初始化变量
         */
        lv_BSTPosts = (ListView)view.findViewById(R.id.lv_question);
        bt_sendBSTPost = (FloatingActionButton)view.findViewById(R.id.fab_questionpost_add);
        bt_sendBSTPost.attachToListView(lv_BSTPosts);

        question_top = (LinearLayout)view.findViewById(R.id.question_top);
        CurrentUserquestionNumber = (TextView)view.findViewById(R.id.tv_questionNumber);

        bt_sendBSTPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(),"test",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity().getApplicationContext(),AddQuestionPostActivity.class);
                //startActivity(intent);
                startActivityForResult(intent,2);
            }
        });




        getBSTPostReplyNum();
        getBSTPostList();
        getUserPostNum();


        return view;
    }

    private void getBSTPostList() {
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
                .url(getBSTPostURL)
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
                    msg.what = GET_BSTPOST_SUCCESS;
                    handler.sendMessage(msg);

                }else{

                }
            }
        });
    }

    private void getBSTPostReplyNum() {
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
        final Request request = new Request.Builder()
                .url(getBSTPostReplyURL)
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
                    HashMap<String, Object> BSTPostReplyNum = new HashMap<String, Object>();
                    //从服务器取到Json键值对{“key”:“value”}
                    String temp = response.body().string();

                    try{
                        JSONObject jsonObject=new JSONObject(temp);
                        String jsonResult = jsonObject.get("ReplyNumMap").toString();

                        BSTPostReplyNum = JsonUtil.convertJsonStrToMap(jsonResult);

                    }catch (JSONException a){

                    }

                    //通过handler传递数据到主线程
                    Message msg = new Message();
                    msg.what = GET_BSTPOST_REPLY_NUM_SUCCESS;
                    msg.obj = BSTPostReplyNum;
                    handler.sendMessage(msg);

                }else{

                }
            }
        });
    }

    private void getBSTPostReplyNumRefresh() {
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
        final Request request = new Request.Builder()
                .url(getBSTPostReplyURL)
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
                    HashMap<String, Object> BSTPostReplyNum = new HashMap<String, Object>();
                    //从服务器取到Json键值对{“key”:“value”}
                    String temp = response.body().string();

                    try{
                        JSONObject jsonObject=new JSONObject(temp);
                        String jsonResult = jsonObject.get("ReplyNumMap").toString();

                        BSTPostReplyNum = JsonUtil.convertJsonStrToMap(jsonResult);

                    }catch (JSONException a){

                    }

                    //通过handler传递数据到主线程
                    Message msg = new Message();
                    msg.what = GET_BSTPOST_REPLY_NUM_SUCCESS_REFRESH;
                    msg.obj = BSTPostReplyNum;
                    handler.sendMessage(msg);

                }else{

                }
            }
        });
    }

    private void getUserPostNum() {
        OkHttpClient okHttpClient = new OkHttpClient();

        FormBody.Builder formBody = new FormBody.Builder();

        /**
         * 传递键值对参数
         * key一定要和LoginActivityAction里面的变量同名！！！一定要同名！！！
         */
        formBody.add("getUserPostNum_user_sno", CurrentUserUtil.getCurrentUser().getUser_sno());


        //创建一个请求对象
        Request request = new Request.Builder()
                .url(getUserPostNumURL)
                .post(formBody.build())
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
                    //从服务器取到Json键值对
                    String temp = response.body().string();
                    int num;
                    try {
                        JSONObject jsonObject=new JSONObject(temp);
                        num = Integer.parseInt(jsonObject.get("currentUserPostNum").toString());
                        //通过handler传递数据到主线程
                        Message msg = new Message();
                        msg.obj = num;
                        msg.what = GET_USER_POST_NUM_SUCCESS;
                        handler.sendMessage(msg);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{

                }
            }
        });
    }

}
