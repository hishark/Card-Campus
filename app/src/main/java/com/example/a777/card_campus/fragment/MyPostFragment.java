package com.example.a777.card_campus.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.activity.EditDaiPostActivity;
import com.example.a777.card_campus.adapter.DaiPostAdapter;
import com.example.a777.card_campus.adapter.FragmentAdapter;
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

public class MyPostFragment extends Fragment {


    private  View view;
    //买了个服务器 ip为47.106.148.107 模拟器是10.0.2.2 真机192.168.137.1
    private String PostListURL="http://47.106.148.107:8080/Card-Campus-Server/getallDaiPostList";
    private List<HashMap<String, Object>> daipostResult;
    private ListView lv_daiposts;
    private CircleImageView avatar;
    private TextView username;
    private SwipeRefreshLayout swipeRefreshLayout;//SwipeRefreshLayout下拉刷新控件
    private String TAG="MyPostFragment";

    /**
     * TabLayout初尝试
     */
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FragmentAdapter adapter;
    private List<Fragment> mFragments;
    private List<String> mTitles;
    private String[] titles = new String[]{"代代代","二手书"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_post, container, false);
        //控件初始化
        initView();

        //将当前用户的头像和昵称展示在顶端
        Glide.with(getActivity().getApplicationContext()).load(CurrentUserUtil.getCurrentUser().getUser_avatar()).into(avatar);
        username.setText(CurrentUserUtil.getCurrentUser().getUser_nickname());




        /**
         * TabLayout标签页
         */

        mTitles = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            mTitles.add(titles[i]);
        }
        mFragments = new ArrayList<>();
        mFragments.add(new MyDaiPostFragment());
        mFragments.add(new MyBookFragment());
        adapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(adapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来
        mTabLayout.getTabAt(0).setIcon(R.drawable.instead2);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ershoushu);


        return view;
    }

    private void initView() {
        avatar=(CircleImageView)view.findViewById(R.id.myPost_Avatar) ;
        username=(TextView)view.findViewById(R.id.myPost_Username);
        lv_daiposts=(ListView)view.findViewById(R.id.lv_daipostList);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.refresh_currentUserAllDaiPost);
        /**
         * TabLayout
         */
        mTabLayout =(TabLayout)view.findViewById(R.id.mypost_tab_layout);
        mViewPager = (ViewPager)view.findViewById(R.id.mypost_view_pager);
    }



}
