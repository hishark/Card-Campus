package com.example.a777.card_campus.activity;

import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.bumptech.glide.Glide;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.adapter.DaikeAdapter;
import com.example.a777.card_campus.adapter.QuestionPostAdapter;
import com.example.a777.card_campus.adapter.QuestionPostEditDeleteAdapter;
import com.example.a777.card_campus.bean.QuestionPost;
import com.example.a777.card_campus.bean.User;
import com.example.a777.card_campus.fragment.EverythingFragment;
import com.example.a777.card_campus.util.CurrentUserUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CurrentUserPostDetailActivity extends AppCompatActivity {

    boolean flag = true;

    //控件们
    private CircleImageView avatar;
    private TextView username;
    private ListView allQuestion;
    private TextView TopQuestionNum;
    private Button back;
    //下拉刷新
    private SwipeRefreshLayout swipeRefreshLayout;//SwipeRefreshLayout下拉刷新控件


    private int[] QuestionReplyNum;
    private List<HashMap<String, Object>> QuestionPostResult;
    private List<List<HashMap<String, Object>>> QuestionReplyResults;

    List<HashMap<String, Object>> currentUserPost;
    List<List<HashMap<String, Object>>> currentPostReplys;
    ArrayList<Integer> currentUserPostId;


    private static String URL="http://47.106.148.107:8080/Card-Campus-Server/getQuestionPostList";
    private List<HashMap<String, Object>> BQuestionPostResult;
    private static String reply_URL="http://47.106.148.107:8080/Card-Campus-Server/getQuestionPostReplyNum";
    private List<HashMap<String, Object>> BQuestionReplyResult;
    private List<List<HashMap<String, Object>>> BQuestionReplyResults;
    private int BQuestionReplyNum[];
    private int Bcount=0;


    final Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            /**
             * QuestionReplyResult接收来自子线程从服务器查询到的帖子回复数据
             */
            BQuestionReplyResult = (List)msg.obj;

            /**
             * QuestionReplyResults就成功按照帖子的id记下了每个帖子的回复（所有属性都有）
             */
            BQuestionReplyResults.set(msg.what,BQuestionReplyResult);

            /**
             * QuestionReplyNum就成功按照帖子的id记下了每个帖子的回复数量
             */
            BQuestionReplyNum[msg.what] = ((List)msg.obj).size();
            Log.d("啊啊啊啊怎么了", BQuestionReplyNum[msg.what]+"");
            Bcount++;

            //count是计数器，当所有的帖子都记录过回复数量之后，就可以设置适配器了
            if(Bcount==BQuestionReplyNum.length-1){
                //然后再加载到适配器里去

                //发帖一般最新的在最上面，用这句话就可以让帖子倒序显示
                //final List<HashMap<String, Object>> temp1 = QuestionPostResult;
                Collections.reverse(BQuestionPostResult);
                //final List<List<HashMap<String, Object>>> temp2 = QuestionReplyResults;
                Collections.reverse(BQuestionReplyResults);


            }

            TEST();

            super.handleMessage(msg);
        }
    };

    //Handler用来从子线程往主线程传输数据
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            BQuestionPostResult = (List)msg.obj;
            Log.d("handler",BQuestionPostResult.size()+"");

            BQuestionReplyNum = new int[BQuestionPostResult.size()+1];
            BQuestionReplyResults = new ArrayList<List<HashMap<String, Object>>>();
            List<HashMap<String, Object>> a = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> test = new HashMap<String, Object>();
            a.add(test);
            for(int i=0;i<BQuestionPostResult.size()+1;i++){
                BQuestionReplyResults.add(a);
            }

            for(int i=0;i<BQuestionPostResult.size();i++){
                getQuestionPostReplyNum(Integer.parseInt(BQuestionPostResult.get(i).get("bpost_id").toString()),handler1);
            }


            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_user_post_detail);
        getSupportActionBar().hide();

        //控件初始化
        initView();

        //将当前用户的头像和昵称展示在顶端
        Glide.with(getApplicationContext()).load(CurrentUserUtil.getCurrentUser().getUser_avatar()).into(avatar);
        username.setText(CurrentUserUtil.getCurrentUser().getUser_nickname());

        QuestionReplyNum = getIntent().getIntArrayExtra("ReplyNum");
        QuestionPostResult = (List<HashMap<String,Object>>)getIntent().getSerializableExtra("QuestionPost");
        QuestionReplyResults =  (List<List<HashMap<String,Object>>>)getIntent().getSerializableExtra("QuestionReplys");

        currentUserPost = new ArrayList<HashMap<String, Object>>();
        currentPostReplys = new ArrayList<>();
        currentUserPostId = new ArrayList<>();

        for(int i=0;i<QuestionPostResult.size();i++){
            User user= (User)QuestionPostResult.get(i).get("user");
            if(user.getUser_nickname()==CurrentUserUtil.getCurrentUser().getUser_nickname()||user.getUser_nickname().equals(CurrentUserUtil.getCurrentUser().getUser_nickname())){
                currentUserPost.add(QuestionPostResult.get(i));
                currentUserPostId.add(Integer.parseInt(QuestionPostResult.get(i).get("bpost_id").toString()));
            }
        }

        Log.d("疯狂测试中currentUserPostId",currentUserPostId.toString());
        Log.d("疯狂测试中currentUserPost",currentUserPost.toString());


        int currentUserReplyNum[] = new int[currentUserPost.size()+1];
        for(int i=1;i<currentUserReplyNum.length;i++){
            currentUserReplyNum[i] = QuestionReplyNum[currentUserPostId.get(i-1)];
        }
        TopQuestionNum.setText(" "+(currentUserReplyNum.length-1)+" ");

        //设置适配器
        final QuestionPostEditDeleteAdapter questionPostEditDeleteAdapter = new QuestionPostEditDeleteAdapter(getApplicationContext(),currentUserPost,currentUserReplyNum);
        allQuestion.setAdapter(questionPostEditDeleteAdapter);


        /**
         * TODO:吐血申请放弃删除操作，流泪
         */
        /*questionPostEditDeleteAdapter.setOnItemDeleteClickListener(new QuestionPostEditDeleteAdapter.onItemDeleteListener() {
            @Override
            public void onDeleteClick(int i) {
                currentUserPost.remove(i);
                //deleteSelectedPost(i+1+9);
                //Log.d("选中的id",(i+1)+"");
                questionPostEditDeleteAdapter.notifyDataSetChanged();
            }
        });*/

        questionPostEditDeleteAdapter.setOnItemEditClickListener(new QuestionPostEditDeleteAdapter.onItemEditListener() {
            @Override
            public void onEditClick(int i) {
                Intent intent = new Intent(CurrentUserPostDetailActivity.this,EditQuestionPostActivity.class);
                intent.putExtra("title",currentUserPost.get(i).get("bpost_title").toString());
                intent.putExtra("content",currentUserPost.get(i).get("bpost_content").toString());
                intent.putExtra("id",currentUserPost.get(i).get("bpost_id").toString());

                //Log.d("疯狂测试中clicktitle",currentUserPost.get(i).get("bpost_title").toString());
                //Log.d("疯狂测试中clickcontent",currentUserPost.get(i).get("bpost_content").toString());
                startActivity(intent);
            }
        });

        /*List<HashMap<String, Object>> a = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> test = new HashMap<String, Object>();
        a.add(test);
        currentPostReplys.add(a);*/
        for(int k : currentUserPostId){
            currentPostReplys.add(QuestionReplyResults.get(QuestionReplyResults.size()-k-1));
        }

        Log.d("疯狂测试中currentPostReplys",currentPostReplys.toString());

        allQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getContext(),i+"",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),QuestionPostDetailActivity.class);
                intent.putExtra("QuestionPost",(Serializable)currentUserPost);
                intent.putExtra("QuestionReplys",(Serializable)currentPostReplys);
                intent.putExtra("currentItem",i);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO 刷新的时候获取数据
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         * 从服务器取得所有的百事通问题数据
                         */
                        Bcount=0;
                        getQuestionPostList(handler);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });



    }

    private void TEST() {

        currentUserPost = new ArrayList<HashMap<String, Object>>();
        currentPostReplys = new ArrayList<>();
        currentUserPostId = new ArrayList<>();

        for(int i=0;i<BQuestionPostResult.size();i++){
            User user= (User)BQuestionPostResult.get(i).get("user");
            if(user.getUser_nickname()==CurrentUserUtil.getCurrentUser().getUser_nickname()||user.getUser_nickname().equals(CurrentUserUtil.getCurrentUser().getUser_nickname())){
                currentUserPost.add(BQuestionPostResult.get(i));
                currentUserPostId.add(Integer.parseInt(BQuestionPostResult.get(i).get("bpost_id").toString()));
            }
        }

        Log.d("疯狂测试中currentUserPostId",currentUserPostId.toString());
        Log.d("疯狂测试中currentUserPost",currentUserPost.toString());


        int currentUserReplyNum[] = new int[currentUserPost.size()+1];
        for(int i=1;i<currentUserReplyNum.length;i++){
            currentUserReplyNum[i] = BQuestionReplyNum[currentUserPostId.get(i-1)];
        }
        TopQuestionNum.setText(" "+(currentUserReplyNum.length-1)+" ");

        //设置适配器
        final QuestionPostEditDeleteAdapter questionPostEditDeleteAdapter = new QuestionPostEditDeleteAdapter(getApplicationContext(),currentUserPost,currentUserReplyNum);
        allQuestion.setAdapter(questionPostEditDeleteAdapter);

        questionPostEditDeleteAdapter.setOnItemEditClickListener(new QuestionPostEditDeleteAdapter.onItemEditListener() {
            @Override
            public void onEditClick(int i) {
                Intent intent = new Intent(CurrentUserPostDetailActivity.this,EditQuestionPostActivity.class);
                intent.putExtra("title",currentUserPost.get(i).get("bpost_title").toString());
                intent.putExtra("content",currentUserPost.get(i).get("bpost_content").toString());
                intent.putExtra("id",currentUserPost.get(i).get("bpost_id").toString());

                //Log.d("疯狂测试中clicktitle",currentUserPost.get(i).get("bpost_title").toString());
                //Log.d("疯狂测试中clickcontent",currentUserPost.get(i).get("bpost_content").toString());
                startActivity(intent);
            }
        });

        for(int k : currentUserPostId){
            currentPostReplys.add(QuestionReplyResults.get(QuestionReplyResults.size()-k-1));
        }

        Log.d("疯狂测试中currentPostReplys",currentPostReplys.toString());

        allQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getContext(),i+"",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),QuestionPostDetailActivity.class);
                intent.putExtra("QuestionPost",(Serializable)currentUserPost);
                intent.putExtra("QuestionReplys",(Serializable)currentPostReplys);
                intent.putExtra("currentItem",i);
                startActivity(intent);
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
        avatar = (CircleImageView)this.findViewById(R.id.CurrentUserAllQuestion_Avatar);
        username = (TextView)this.findViewById(R.id.CurrentUserAllQuestion_Username);
        allQuestion = (ListView)this.findViewById(R.id.CurrentUserAllQuestion_List);
        TopQuestionNum = (TextView)this.findViewById(R.id.CurrentUserQuestionNum);
        back = (Button)this.findViewById(R.id.currentUserALLQuestion_back);
        swipeRefreshLayout = (SwipeRefreshLayout)this.findViewById(R.id.refresh_currentUserAllPost);
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
                    //如果要倒转，这里就等于size-id-1
                    //QuestionPostResult.size()+1-bpost_id
                    msg.what = bpost_id;
                    //QuestionReplyNum.add(questionReplys.size());
                    Log.d("访问服务器的子线程里得到的数据：","Post的id："+bpost_id+"该Post的回复数"+questionReplys.size()+"");
                    handler1.sendMessage(msg);

                }else{

                }
            }
        });
    }

}


