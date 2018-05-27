package com.example.a777.card_campus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.example.a777.card_campus.activity.LovePostDetailActivity;
import com.example.a777.card_campus.activity.SendLoveActivity;
import com.example.a777.card_campus.adapter.LovewallAdapter;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.bean.LovePost;;
import com.example.a777.card_campus.bean.MyEntity;
import com.example.a777.card_campus.bean.User;
import com.google.gson.Gson;
import com.moxun.tagcloudlib.view.TagCloudView;

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

public class LovewallFragment extends Fragment {


    private String URL="http://192.168.137.1:8080/Card-Campus-Server/getLovePostList";
    private String REPLYURL="http://192.168.137.1:8080/Card-Campus-Server/getLoveReplyNum";
    //定义成员变量
    private View view;
    //标签云
    private TagCloudView tagCloudView;
    //表白标题
    ArrayList<String> loveTitle;

    private List<HashMap<String, Object>> lovepostResult;
    List<HashMap<String, Object>> loveposts=null;
    HashMap<String, Object> lovepost=null;
    private List<List<HashMap<String, Object>>> LoveReplyResults;
    //private List<HashMap<String,Object>> lovereplyResult;
    MyEntity lovereplyResult;

    //表白标签云的适配器
    LovewallAdapter lovewallAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lovewall, container, false);

        //标签云的初始化
        tagCloudView = (TagCloudView)view.findViewById(R.id.tcv_lovewall);

        //不能写在这里，不然第一次进来数据加载了2次
        //getLoveTitle();


        /**
         * 悬浮球的点击事件，发布一条表白记录
         */
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab_loveAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplication(),SendLoveActivity.class);
                startActivity(intent);

            }
        });

        return view;
    }
    private Handler handler1=new Handler(){
        public void handleMessage(Message msg){
            lovereplyResult = (MyEntity) msg.obj;
            lovewallAdapter = new LovewallAdapter(getActivity().getApplication(),lovepostResult,lovereplyResult.getReplys());
            Log.d("结果集的大小",String.valueOf(lovereplyResult.getReplys().size()));
            Log.d("wwwa",lovereplyResult.getPostId());
            Log.d("wwwb",lovereplyResult.getReplys().toString());
            Log.d("wwwc",String.valueOf(lovereplyResult.getReplyNum()));


            /*ArrayList<List<HashMap<String, Object>>> matchs=new ArrayList<List<HashMap<String,Object>>>();
            final HashMap<String, Object> match_item=new HashMap<String, Object>();
            match_item.put("id",lovereplyResult.getPostId());
            match_item.put("post",lovepostResult);
            match_item.put("number",lovereplyResult.getReplyNum());
            match_item.put("reply",lovereplyResult.getReplys());

            Log.d("sssss",match_item.toString());*/


            tagCloudView.setAdapter(lovewallAdapter);
            tagCloudView.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
                @Override
                public void onItemClick(ViewGroup parent, View view, int i) {
                    Intent intent = new Intent(getActivity().getApplication(),LovePostDetailActivity.class);
                    //HashMap<String, Object> current_post=(HashMap<String, Object>)match_item.get("post");
                    //HashMap<String, Object> current_reply=(HashMap<String, Object>)match_item.get("reply");
                   /* List<HashMap<String, Object>> current_post=(List<HashMap<String, Object>>)match_item.get("post");
                    List<HashMap<String, Object>> current_reply=(List<HashMap<String, Object>>)match_item.get("reply");
                    int current_num=(int) match_item.get("number");
                    String current_id=(String)match_item.get("id");

                    if (lovepostResult.get(i).get("love_id").equals(current_id)){
                        intent.putExtra("lovepost", lovepostResult.get(i));
                        intent.putExtra("replys",(Serializable)current_reply);
                        intent.putExtra("num",current_num);
                    }else{

                    }*/

                    String current_id=lovepostResult.get(i).get("love_id").toString();
                    String check_id=lovereplyResult.getPostId();
                    Log.d("kkkk",current_id);
                    Log.d("kkkk",check_id);
                    Log.d("cnm",String.valueOf(i));
                    intent.putExtra("lovepost", lovepostResult.get(i));
                    for(int j=0;j<lovepostResult.size();j++) {
                        if (current_id.equals(check_id)) {

                            intent.putExtra("num", lovereplyResult.getReplyNum());

                        } else {
                            if(!(lovereplyResult.getReplys().listIterator().next().equals(""))) {
                                Log.d("ttt", lovereplyResult.getReplys().listIterator().next().toString());

                                Log.d("ttt",check_id);
                            }
                        }
                    }
                    startActivity(intent);
                }
            });



            //Collections.reverse(lovereplyResult.getReplys());
            //getReplyData(lovereplyResult.getPostId(),lovereplyResult.getReplys(),lovereplyResult.getReplyNum());
            /*Log.d("wwwa",lovereplyResult.getPostId());
            Log.d("wwwb",lovereplyResult.getReplys().toString());
            Log.d("wwwc",String.valueOf(lovereplyResult.getReplyNum()));*/
            super.handleMessage(msg);
        }
    };

    private void getReplyData(String postId,final List<HashMap<String, Object>> replys, final int replyNum) {

        Log.d("wwwa",postId);
        Log.d("wwwb",lovereplyResult.getReplys().toString());
        Log.d("wwwc",String.valueOf(lovereplyResult.getReplyNum()));


        lovewallAdapter = new LovewallAdapter(getActivity().getApplication(),lovepostResult,replys);

        tagCloudView.setAdapter(lovewallAdapter);
        tagCloudView.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, int i) {
                Intent intent = new Intent(getActivity().getApplication(),LovePostDetailActivity.class);
                intent.putExtra("lovepost", replys.get(i));
                //intent.putExtra("lovereply",lovereplyResult.getReplys().get(i));
                intent.putExtra("num",replyNum);
                Log.d("cnm",String.valueOf(i));
                startActivity(intent);
            }
        });

    }






    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            lovepostResult = (List)msg.obj;

            loveTitle = new ArrayList<String>();
            //发帖一般最新的在最上面，用这句话就可以让帖子倒序显示
            Collections.reverse(lovepostResult);

            Log.d("size",lovepostResult.size()+"");
            for (int i=0;i<lovepostResult.size();i++){
                loveTitle.add(lovepostResult.get(i).get("love_title").toString());
                //Log.d("title",loveTitle.get(i));
                //Log.d("reply",lovepostResult.get(i).get("love_id").toString());
                //getLoveReplyNum(lovepostResult.get(i).get("love_id").toString(),handler1);

                getLoveReplyNum(lovepostResult.get(i).get("love_id").toString());
                //Log.d("ssssss",lovepostResult.get(i).get("love_id").toString());
            }

            //lovewallAdapter = new LovewallAdapter(getActivity().getApplication(),loveTitle);
            /*lovewallAdapter = new LovewallAdapter(getActivity().getApplication(),lovepostResult,lovereplyResult);
            tagCloudView.setAdapter(lovewallAdapter);*/


            super.handleMessage(msg);
        }
    };

    private void getLoveReplyNum(final String love_id){
        //实例化OkHttpClient
        OkHttpClient client = new OkHttpClient();
        //创建表单请求体
        FormBody.Builder formBody = new FormBody.Builder();

        /**
         * 传递键值对参数
         * key一定要和LoginActivityAction里面的变量同名！！！一定要同名！！！
         */
        formBody.add("love_id",love_id);

        //创建Request对象
        Request request = new Request.Builder()
                .url(REPLYURL)
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

                    List<HashMap<String, Object>> loveReplys=null;
                    HashMap<String, Object> loveReply=null;

                    //从服务器取到Json键值对{“key”:“value”}
                    String temp = response.body().string();

                    /*if(temp.equals("")||temp==null){
                        Log.d("temp空","null!");
                    }else{
                        Log.d("getLoveReplyNum看一哈temp",temp);
                    }*/

                    try{

                        JSONArray jsonArray=new JSONArray(temp);
                        loveReplys=new ArrayList<HashMap<String, Object>>();

                        //Log.d("回复数jsonArray size",jsonArray.length()+"");

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                            loveReply=new HashMap<String, Object>();

                            loveReply.put("lreply_id", jsonObject.get("lreply_id"));
                            loveReply.put("lreply_content",jsonObject.get("lreply_content"));
                            /***
                             * 拿到了User的json数据
                             * 利用Gson解析Json键值对
                             */
                            String userresult = jsonObject.get("user").toString();
                            Gson gson1 = new Gson();
                            User user = gson1.fromJson(userresult, User.class);
                            loveReply.put("user",user);

                            /**
                             * 待转换的JSON字符串中某些结点没能和我们定义的Java Bean 匹配上，于是就会出错。
                             * 还有一个TimeStamp和user要单独拎出来
                             */
                            /*String lovepostresult = jsonObject.get("lovepost").toString();
                            JSONObject jsonObject1 = new JSONObject(lovepostresult);
                            String postid = jsonObject1.get("love_id").toString();*/
                            String lovepostresult = jsonObject.get("lovepost").toString();
                            Gson gson2 = new Gson();
                            LovePost lovePost = gson2.fromJson(lovepostresult, LovePost.class);
                            loveReply.put("lovepost",lovePost);

                            //timeresult是从数据库插到的dpost_time字段，类型为timestamp
                            //从数据库读出来长这个样子：存到timeresult里面
                            //{"date":21,"day":6,"hours":23,"minutes":18,"month":3,"nanos":0,"seconds":0,"time":1524323880000,"timezoneOffset":-480,"year":118}
                            String timeresult = jsonObject.get("lreply_time").toString();

                            //利用fastJson——JSON取出timeresult里面的time字段，也就是13位的时间戳
                            long time = JSON.parseObject(timeresult).getLong("time");
                            Timestamp trueTime = new Timestamp(time);

                            //把时间put进questionReply
                            loveReply.put("lreply_time",trueTime);

                            loveReplys.add(loveReply);
                        }


                    }catch (JSONException a){

                    }

                    //通过handler传递数据到主线程
                    Message msg = new Message();
                    msg.obj=new MyEntity(love_id,loveReplys,loveReplys.size());
                    //Log.d("访问服务器的子线程里得到的数据：","Post的id："+love_id+"该Post的回复数"+loveReplys.size()+"");
                    handler1.sendMessage(msg);

                }else{

                }
            }
        });
    }

    /**
     * 从服务器取得表白标题数据
     */
    private void getLoveTitle() {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder().build();

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

                    //从服务器取到Json键值对{“key”:“value”}
                    String temp = response.body().string();
                    if(temp.equals("")||temp==null){
                        Log.d("嗷嗷","null!");
                    }else{
                        Log.d("嗷嗷嗷",temp);
                    }
                    try{
                        JSONArray jsonArray=new JSONArray(temp);
                        loveposts=new ArrayList<HashMap<String, Object>>();

                        Log.d("jsonArray size",jsonArray.length()+"");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                            lovepost=new HashMap<String, Object>();

                            lovepost.put("love_id", jsonObject.get("love_id"));
                            lovepost.put("love_username",jsonObject.get("love_username"));
                            lovepost.put("love_content", jsonObject.get("love_content"));
                            lovepost.put("love_title", jsonObject.get("love_title"));
                            lovepost.put("is_anonymous", jsonObject.get("is_anonymous"));



                            //timeresult是从数据库插到的dpost_time字段，类型为timestamp
                            //从数据库读出来长这个样子：存到timeresult里面
                            //{"date":21,"day":6,"hours":23,"minutes":18,"month":3,"nanos":0,"seconds":0,"time":1524323880000,"timezoneOffset":-480,"year":118}
                            String timeresult = jsonObject.get("love_time").toString();

                            //利用fastJson——JSON取出timeresult里面的time字段，也就是13位的时间戳
                            long time = JSON.parseObject(timeresult).getLong("time");
                            Timestamp trueTime = new Timestamp(time);

                            //把时间put进lovepost
                            lovepost.put("love_time",trueTime);

                            //将13位时间戳转换为年月日时分秒！
                            /*Long time1 = 1524323880000L;
                            SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date= new Date(time1);
                            String d = format.format(date);*/

                            loveposts.add(lovepost);
                            Log.d("内部loveposts size",loveposts.size()+"");
                        }

                    }catch (JSONException a){

                    }

                    //通过handler传递数据到主线程
                    Message msg = new Message();
                    msg.obj = loveposts;
                    handler.sendMessage(msg);

                }else{

                }
            }
        });
    }


    /**
     * 发布完标签云立即更新
     */
    @Override
    public void onResume() {
        super.onResume();
        getLoveTitle();
    }
}
