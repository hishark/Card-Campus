package com.example.a777.card_campus.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.adapter.LoveReplyAdapter;
import com.example.a777.card_campus.bean.LovePost;
import com.example.a777.card_campus.bean.LoveReply;
import com.example.a777.card_campus.bean.MyEntity;
import com.example.a777.card_campus.bean.QuestionPost;
import com.example.a777.card_campus.bean.User;
import com.example.a777.card_campus.util.CurrentUserUtil;
import com.example.a777.card_campus.util.RandomIDUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LovePostDetailActivity extends AppCompatActivity {

    //真机地址192.168.137.1
    private String URL="http://192.168.137.1:8080/Card-Campus-Server/getLoveReplyNum";
    private String ADDURL="http://192.168.137.1:8080/Card-Campus-Server/addLoveReply";
    private Button loveone_back;
    private TextView title,content,name,time,replynum,send;
    private String send_content,loveID;
    private EditText et_comment;
    private LinearLayout llCommentTrue,llCommentFalse,click_comment;
    private ListView lv_allReply;
    private SwipeRefreshLayout refresh_reply;
    //这个实体类存储了一个帖子的id、所有回复以及回复数量
    MyEntity lovereplyResult;

    //那就需要一个集合来存储所有的帖子对应的这个实体类
    Set<MyEntity> allReplys = new HashSet<>();

    private List<HashMap<String,Object>> replyResult;
    List<HashMap<String, Object>> current_lovereplys = new ArrayList<>();
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            lovereplyResult = (MyEntity) msg.obj;
            //把所有的回复结果存入allReplys
            allReplys.add(lovereplyResult);

            String check_id=lovereplyResult.getPostId();
            List<HashMap<String, Object>> current_replys = new ArrayList<HashMap<String, Object>>();
            Iterator<MyEntity> it = allReplys.iterator();
            while(it.hasNext()){
                MyEntity temp = it.next();
                if(temp.getPostId()==loveID){
                    current_replys = temp.getReplys();
                }
            }
            Collections.reverse(current_replys);

            LoveReplyAdapter loveReplyAdapter = new LoveReplyAdapter(getApplicationContext(),current_replys);
            lv_allReply.setAdapter(loveReplyAdapter);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_post_detail);
        getSupportActionBar().hide();


        initView();

        loveone_back=(Button)this.findViewById(R.id.lovepostdetail_back);
        loveone_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        HashMap<String,Object> loveone_item = new HashMap<String,Object>();
        HashMap<String,Object> reply_item = new HashMap<String,Object>();
        int count;
        count=intent.getIntExtra("num",0);
        loveone_item=(HashMap<String,Object>)intent.getSerializableExtra("lovepost");

        loveID=loveone_item.get("love_id").toString();
        Log.d("loveID",loveID);

        for(int i=0;i<loveone_item.size();i++) {
            String username = loveone_item.get("love_username").toString();
            String lovepost_title = loveone_item.get("love_title").toString();
            String lovepost_content = loveone_item.get("love_content").toString();
            String timestamp = loveone_item.get("love_time").toString();
            String lovepost_time = timestamp.substring(0, timestamp.length() - 2);

            title.setText(lovepost_title);
            content.setText(lovepost_content);
            name.setText(username);
            time.setText(lovepost_time);
            replynum.setText(String.valueOf(count));
        }
        /**
        * 告白评论加载
        */

        current_lovereplys =  (List<HashMap<String,Object>>)getIntent().getSerializableExtra("current_loveReplys");


        Log.d("看一下传过来没有哦",current_lovereplys.toString());
        //此时就得到了所有的回复
        //Log.d("看一下传过来的回复内容",current_lovereplys.get(0).get("lreply_content").toString());
        LoveReplyAdapter loveReplyAdapter = new LoveReplyAdapter(getApplicationContext(),current_lovereplys);
        lv_allReply.setAdapter(loveReplyAdapter);

        click_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(llCommentTrue.isShown()){
                    hideComentView();
                }else {
                    showCommentView();
                }
            }
        });
        llCommentFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCommentView();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                send_content=et_comment.getText().toString();
                if(send_content.equals("")){
                    Toast.makeText(getApplicationContext(),"总得留下点什么吧，少年！",Toast.LENGTH_SHORT).show();
                }else{
                    addReply();
                    et_comment.setText("");
                    hideComentView();
                    Toast.makeText(getApplicationContext(),"发布成功",Toast.LENGTH_SHORT).show();
                }
            }
        });



        //下拉刷新
        refresh_reply.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO 刷新的时候获取数据
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getReplyList(loveID); //刷新
                        refresh_reply.setRefreshing(false);
                    }
                }, 1000);
            }
        });


    }

    private void getReplyList(final String love_id) {
        //实例化OkHttpClient
        OkHttpClient client = new OkHttpClient();
        //创建表单请求体
        FormBody.Builder formBody = new FormBody.Builder();

        /**
         * 传递键值对参数
         * key值与服务器端controller中request.getParameter中的key一致
         */
        formBody.add("love_id",love_id);

        //创建Request对象
        Request request = new Request.Builder()
                .url(URL)
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

                            //把时间put进loveReply
                            loveReply.put("lreply_time",trueTime);

                            loveReplys.add(loveReply);


                        }


                    }catch (JSONException a){

                    }

                    //通过handler传递数据到主线程
                    Message msg = new Message();
                    msg.obj=new MyEntity(love_id,loveReplys,loveReplys.size());
                    //Log.d("访问服务器的子线程里得到的数据：","Post的id："+love_id+"该Post的回复数"+loveReplys.size()+"");
                    handler.sendMessage(msg);

                }else{

                }
            }
        });
    }

    private void addReply() {
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();

        //创建表单请求体
        /**
         * key值与服务器端controller中request.getParameter中的key一致
         */

        RequestBody formBody = new FormBody.Builder()
                .add("love_id",loveID)
                .add("user_sno", CurrentUserUtil.getCurrentUser().getUser_sno())
                .add("lreply_id", RandomIDUtil.getID())
                .add("lreply_time",String.valueOf(System.currentTimeMillis()))
                .add("lreply_content",send_content)
                .build();
        Log.d("timwresult",String.valueOf(System.currentTimeMillis()));
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(ADDURL)
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

    /**
     * 实现按下back键不退出app,而是返回上一步操作
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if (llCommentTrue.isShown()) {
                hideComentView();
                return true;
            }else{
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    private void showCommentView() {
        llCommentTrue.setVisibility(View.VISIBLE);
        llCommentFalse.setVisibility(View.GONE);
    }
    public void hideComentView() {
        llCommentTrue.setVisibility(View.GONE);
        llCommentFalse.setVisibility(View.VISIBLE);
    }

    private void initView() {
        loveone_back=(Button)this.findViewById(R.id.lovepostdetail_back);
        title=(TextView)this.findViewById(R.id.tv_loveone_title);
        content=(TextView)this.findViewById(R.id.tv_loveone_content);
        name=(TextView)this.findViewById(R.id.tv_loveone_name);
        time=(TextView)this.findViewById(R.id.tv_loveone_time);
        replynum=(TextView)this.findViewById(R.id.tv_loveone_replyNum);
        click_comment=(LinearLayout)this.findViewById(R.id.click_comment);
        et_comment=(EditText)this.findViewById(R.id.edt_loveone_comment);
        llCommentTrue=(LinearLayout)this.findViewById(R.id.ll_loveone_comment_true);
        llCommentFalse=(LinearLayout)this.findViewById(R.id.ll_loveone_comment_false);
        send=(TextView)this.findViewById(R.id.btn_loveone_send);
        lv_allReply = (ListView)this.findViewById(R.id.lv_loveone_reply);
        refresh_reply=(SwipeRefreshLayout)this.findViewById(R.id.refresh_reply);
    }
}
