package com.example.a777.card_campus.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.adapter.QuestionPostAdapter;
import com.example.a777.card_campus.adapter.QuestionPostEditDeleteAdapter;
import com.example.a777.card_campus.bean.User;
import com.example.a777.card_campus.util.CurrentUserUtil;
import com.example.a777.card_campus.util.ToastUtil;
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

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CurrentUserBSTPostDetailActivity extends AppCompatActivity {
    private static String getBSTPostURL="http://47.106.148.107:8080/Card-Campus-Server/getQuestionPostList";
    private static final int GET_BSTPOST_SUCCESS = 1;

    //控件们
    private CircleImageView avatar;
    private TextView username;
    private ListView lv_userAllQuestion;
    private TextView TopQuestionNum;
    private Button back;
    //下拉刷新
    private SwipeRefreshLayout swipeRefreshLayout;//SwipeRefreshLayout下拉刷新控件
    private boolean isGetPost = false;
    private int current_post_Num;
    private List<HashMap<String,Object>> Current_BSTPosts;
    private List<HashMap<String,Object>> BSTPosts;
    private HashMap<String, Object> BSTPostReplyNum;
    QuestionPostEditDeleteAdapter questionPostEditDeleteAdapter;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what){
                case GET_BSTPOST_SUCCESS:
                    BSTPosts = (List<HashMap<String, Object>>)msg.obj;
                    Current_BSTPosts = new ArrayList<>();
                    for(int i=0;i<BSTPosts.size();i++){
                        User user = (User)(BSTPosts.get(i).get("user"));
                        String sno = user.getUser_sno();
                        if(sno==CurrentUserUtil.getCurrentUser().getUser_sno()||sno.equals(CurrentUserUtil.getCurrentUser().getUser_sno())){
                            Current_BSTPosts.add(BSTPosts.get(i));
                        }
                    }
                    questionPostEditDeleteAdapter = new QuestionPostEditDeleteAdapter(getApplicationContext(),Current_BSTPosts,BSTPostReplyNum);
                    lv_userAllQuestion.setAdapter(questionPostEditDeleteAdapter);
                    questionPostEditDeleteAdapter.setOnItemEditClickListener(new QuestionPostEditDeleteAdapter.onItemEditListener() {
                        @Override
                        public void onEditClick(int i) {
                            Intent intent = new Intent(CurrentUserBSTPostDetailActivity.this,EditQuestionPostActivity.class);
                            intent.putExtra("title",Current_BSTPosts.get(i).get("bpost_title").toString());
                            intent.putExtra("content",Current_BSTPosts.get(i).get("bpost_content").toString());
                            intent.putExtra("id",Current_BSTPosts.get(i).get("bpost_id").toString());
                            //startActivity(intent);
                            startActivityForResult(intent,1);
                        }
                    });
                    //isGetPost = true;
                    //ToastUtil.createToast(getContext(),BSTPosts.toString());
                    break;
                default:
                    break;
            }




            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_user_bstpost_detail);
        getSupportActionBar().hide();
        initView();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        current_post_Num = intent.getIntExtra("QuestionPostNum",0);
        TopQuestionNum.setText(current_post_Num+"");
        //将当前用户的头像和昵称展示在顶端
        Glide.with(getApplicationContext()).load(CurrentUserUtil.getCurrentUser().getUser_avatar()).into(avatar);
        username.setText(CurrentUserUtil.getCurrentUser().getUser_nickname());

        Current_BSTPosts =  (List<HashMap<String,Object>>)intent.getSerializableExtra("QuestionPost");
        BSTPostReplyNum = (HashMap<String,Object>)intent.getSerializableExtra("QuestionPostReplyNum");
        questionPostEditDeleteAdapter = new QuestionPostEditDeleteAdapter(getApplicationContext(),Current_BSTPosts,BSTPostReplyNum);
        lv_userAllQuestion.setAdapter(questionPostEditDeleteAdapter);


        questionPostEditDeleteAdapter.setOnItemEditClickListener(new QuestionPostEditDeleteAdapter.onItemEditListener() {
            @Override
            public void onEditClick(int i) {
                Intent intent = new Intent(CurrentUserBSTPostDetailActivity.this,EditQuestionPostActivity.class);
                intent.putExtra("title",Current_BSTPosts.get(i).get("bpost_title").toString());
                intent.putExtra("content",Current_BSTPosts.get(i).get("bpost_content").toString());
                intent.putExtra("id",Current_BSTPosts.get(i).get("bpost_id").toString());
                //startActivity(intent);
                startActivityForResult(intent,1);
            }
        });



    }
    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                getBSTPostList();
                ToastUtil.createToast(getApplicationContext(),"?>>>");
                break;
            default:
                break;
        }
    }

    private void initView() {
        avatar = (CircleImageView)this.findViewById(R.id.CurrentUserAllQuestion_Avatar);
        username = (TextView)this.findViewById(R.id.CurrentUserAllQuestion_Username);
        lv_userAllQuestion = (ListView)this.findViewById(R.id.CurrentUserAllQuestion_List);
        TopQuestionNum = (TextView)this.findViewById(R.id.CurrentUserQuestionNum);
        back = (Button)this.findViewById(R.id.currentUserALLQuestion_back);
        swipeRefreshLayout = (SwipeRefreshLayout)this.findViewById(R.id.refresh_currentUserAllPost);
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

}
