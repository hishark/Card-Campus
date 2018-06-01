package com.example.a777.card_campus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.example.a777.card_campus.activity.AddBookPostActivity;
import com.example.a777.card_campus.activity.BookDetailActivity;
import com.example.a777.card_campus.adapter.BookAdapter;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.bean.User;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

public class BookFragment extends Fragment {
    //定义成员变量
    private View view;
    private GridView gridView_Book;
    private FloatingActionButton fab_bookAdd;


    //买了个服务器 ip为47.106.148.107 模拟器是10.0.2.2
    private static String URL="http://47.106.148.107:8080/Card-Campus-Server/getBookList";

    private List<HashMap<String, Object>> BookPostResult;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            BookPostResult = (List)msg.obj;

            BookAdapter bookAdapter = new BookAdapter(getActivity().getApplicationContext(),BookPostResult);
            gridView_Book.setAdapter(bookAdapter);

            gridView_Book.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity().getApplicationContext(),BookDetailActivity.class);
                    intent.putExtra("BookPost",BookPostResult.get(i));
                    startActivity(intent);
                    //ToastUtil.createToast(getContext(),BookPostResult.get(i).get("book_title").toString());
                }
            });


            super.handleMessage(msg);
        }
    };

    @Override
    public void onResume() {
        getBookPostList();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_book, container, false);

        gridView_Book = (GridView)view.findViewById(R.id.GridView_Book);
        fab_bookAdd = (FloatingActionButton)view.findViewById(R.id.fab_bookAdd);
        fab_bookAdd.attachToListView(gridView_Book);



        /**
         * 悬浮球的点击事件，点一下加一本
         */
        fab_bookAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //bookAdapter.notifyDataSetChanged();
                Intent intent = new Intent(getActivity().getApplicationContext(),AddBookPostActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    private void getBookPostList() {
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

                    List<HashMap<String, Object>> bookPosts=null;
                    HashMap<String, Object> bookPost=null;

                    //从服务器取到Json键值对{“key”:“value”}
                    String temp = response.body().string();
                    try{
                        JSONArray jsonArray=new JSONArray(temp);
                        bookPosts=new ArrayList<HashMap<String, Object>>();

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                            bookPost=new HashMap<String, Object>();

                            bookPost.put("book_id", jsonObject.get("book_id"));
                            bookPost.put("book_img", jsonObject.get("book_img"));
                            bookPost.put("book_describe", jsonObject.get("book_describe"));
                            bookPost.put("book_title", jsonObject.get("book_title"));
                            bookPost.put("is_sold", jsonObject.get("is_sold"));

                            /**
                             * 拿到了User的json数据
                             * 利用Gson解析Json键值对
                             */
                            String userresult = jsonObject.get("user").toString();
                            Gson gson = new Gson();
                            User user = gson.fromJson(userresult, User.class);
                            bookPost.put("user",user);

                            //timeresult是从数据库插到的dpost_time字段，类型为timestamp
                            //从数据库读出来长这个样子：存到timeresult里面
                            //{"date":21,"day":6,"hours":23,"minutes":18,"month":3,"nanos":0,"seconds":0,"time":1524323880000,"timezoneOffset":-480,"year":118}
                            String timeresult = jsonObject.get("book_time").toString();

                            //利用fastJson——JSON取出timeresult里面的time字段，也就是13位的时间戳
                            long time = JSON.parseObject(timeresult).getLong("time");
                            Timestamp trueTime = new Timestamp(time);

                            //把时间put进questionPost
                            bookPost.put("book_time",trueTime);

                            bookPosts.add(bookPost);
                        }


                    }catch (JSONException a){

                    }

                    //通过handler传递数据到主线程
                    Message msg = new Message();
                    msg.obj = bookPosts;

                    handler.sendMessage(msg);

                }else{

                }
            }
        });
    }
}
