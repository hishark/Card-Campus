package com.example.a777.card_campus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.activity.EditBookPostActivity;
import com.example.a777.card_campus.activity.EditDaiPostActivity;
import com.example.a777.card_campus.adapter.CurrentUserBookAdapter;
import com.example.a777.card_campus.adapter.DaiPostAdapter;
import com.example.a777.card_campus.bean.User;
import com.example.a777.card_campus.util.CurrentUserUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

public class MyBookFragment extends Fragment {


    private View view;
    //买了个服务器 ip为47.106.148.107 模拟器是10.0.2.2 真机192.168.137.1
    private String BookListURL = "http://47.106.148.107:8080/Card-Campus-Server/getBookList";
    private List<HashMap<String, Object>> bookpostResult;
    private ListView lv_bookposts;
    private SwipeRefreshLayout swipeRefreshLayout;//SwipeRefreshLayout下拉刷新控件


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            bookpostResult = (List) msg.obj;

            //该list一定要在这里初始化，否则数据会重复出现
            final List<HashMap<String, Object>> currentUserPost = new ArrayList<HashMap<String, Object>>();
            for (int i = 0; i < bookpostResult.size(); i++) {
                User user = (User) bookpostResult.get(i).get("user");

                if (user.getUser_nickname() == CurrentUserUtil.getCurrentUser().getUser_nickname() || user.getUser_nickname().equals(CurrentUserUtil.getCurrentUser().getUser_nickname())) {
                    currentUserPost.add(bookpostResult.get(i));
                    Log.d("？？？？？？？？？",bookpostResult.get(i).get("is_sold").toString());
                }

            }
            initswipe();

            //适配器一定要写在这里，不然会出现空指针问题
            CurrentUserBookAdapter bookpostadapter;
            bookpostadapter = new CurrentUserBookAdapter(getActivity().getApplicationContext(), currentUserPost);
            lv_bookposts.setAdapter(bookpostadapter);

            //ListView item 中的编辑按钮的点击事件

            bookpostadapter.setOnItemEditClickListener(new CurrentUserBookAdapter.onItemEditListener() {
                @Override
                public void onEditClick(int i) {
                    Intent intent = new Intent(getActivity().getApplication(), EditBookPostActivity.class);
                    intent.putExtra("title", currentUserPost.get(i).get("book_title").toString());
                    intent.putExtra("content", currentUserPost.get(i).get("book_describe").toString());
                    intent.putExtra("id", currentUserPost.get(i).get("book_id").toString());
                    intent.putExtra("state", currentUserPost.get(i).get("is_sold").toString());
                    startActivity(intent);
                }
            });

            super.handleMessage(msg);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_book, container, false);
        //控件初始化
        initView();

        //getMyBookListFromServer();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getMyBookListFromServer();
    }

    private void initView() {

        lv_bookposts = (ListView) view.findViewById(R.id.lv_bookpostList);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_currentUserAllBookPost);

    }

    private void initswipe() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO 刷新的时候获取数据
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getMyBookListFromServer(); //刷新
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });
    }

    private void getMyBookListFromServer() {
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
                .url(BookListURL)
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
                if (response.isSuccessful()) {

                    List<HashMap<String, Object>> bookposts = null;
                    HashMap<String, Object> bookpost = null;

                    //从服务器取到Json键值对{“key”:“value”}
                    String temp = response.body().string();
                    //Log.d("看一下temp",temp);
                    try {
                        JSONArray jsonArray = new JSONArray(temp);
                        bookposts = new ArrayList<HashMap<String, Object>>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            bookpost = new HashMap<String, Object>();

                            bookpost.put("book_id", jsonObject.get("book_id"));
                            bookpost.put("book_img", jsonObject.get("book_img"));
                            bookpost.put("book_describe", jsonObject.get("book_describe"));
                            bookpost.put("book_title", jsonObject.get("book_title"));
                            bookpost.put("is_sold",jsonObject.get("is_sold"));

                            /**
                             * 拿到了User的json数据
                             * 利用Gson解析Json键值对
                             */
                            String userresult = jsonObject.get("user").toString();
                            Gson gson = new Gson();
                            User user = gson.fromJson(userresult, User.class);


                            bookpost.put("user", user);
                            //timeresult是从数据库插到的dpost_time字段，类型为timestamp
                            //从数据库读出来长这个样子：存到timeresult里面
                            //{"date":21,"day":6,"hours":23,"minutes":18,"month":3,"nanos":0,"seconds":0,"time":1524323880000,"timezoneOffset":-480,"year":118}
                            String timeresult = jsonObject.get("book_time").toString();

                            //利用fastJson——JSON取出timeresult里面的time字段，也就是13位的时间戳
                            long time = JSON.parseObject(timeresult).getLong("time");
                            Timestamp trueTime = new Timestamp(time);

                            //把时间put进daike
                            bookpost.put("book_time", trueTime);
                            bookposts.add(bookpost);
                        }


                    } catch (JSONException a) {

                    }


                    //通过handler传递数据到主线程
                    Message msg = new Message();
                    msg.obj = bookposts;
                    handler.sendMessage(msg);

                } else {

                }
            }
        });
    }
}
