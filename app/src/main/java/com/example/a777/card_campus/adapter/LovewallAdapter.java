package com.example.a777.card_campus.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a777.card_campus.activity.LovePostDetailActivity;
import com.moxun.tagcloudlib.view.TagsAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class LovewallAdapter extends TagsAdapter {

    Context context;
    //ArrayList<String> lovetitle;
    List<HashMap<String, Object>> lovepostResult,lovereplyResult;
    ArrayList<Integer>  replyNum;
    ArrayList<String> postId;

    private String color[] = {"#7dfaed", "#ffb8d5", "#ffda80", "#88adf9", "#ff00ff", "#e4007f", "#00CD66", "#A020F0", "#90EE90"};
    //public LovewallAdapter(Context context, ArrayList<String> title){
    public LovewallAdapter(Context context,List<HashMap<String, Object>> lovepostResult,List<HashMap<String,Object>> lovereplyResult){
        this.context = context;

        this.lovepostResult = lovepostResult;
        this.lovereplyResult=lovereplyResult;
        //this.replyNum=replyNum;
        //this.postId=postId;
    }

    @Override
    public int getCount() {
        //return lovetitle.size();
        return lovepostResult.size();
    }

    /**
     * 每一个标签的样式
     */
    @Override
    public View getView(final Context context, final int position, ViewGroup parent) {
        TextView tv = new TextView(context);
        //设置控件的长宽
        //ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(100, 100);
        //tv.setLayoutParams(lp);

        // 1.设置随机的字体大小(随机大小)
        Random random = new Random();
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                random.nextInt(8) + 18);// 14-29
        tv.setTextColor(Color.parseColor(color[position % 8]));

        //tv.setText(lovetitle.get(position));
        final String lovetitle=lovepostResult.get(position).get("love_title").toString();
        tv.setText(lovepostResult.get(position).get("love_title").toString());
        tv.setGravity(Gravity.CENTER);
        return tv;
    }



    @Override
    public Object getItem(int position) {
        return lovepostResult.get(position);
    }

    /**
     * 针对每个Tag返回一个权重值，该值与ThemeColor和Tag初始大小有关；
     * 一个简单的权重值生成方式是对一个数N取余或使用随机数
     */
    @Override
    public int getPopularity(int position) {
        return position % 5;
    }

    /**
     * Tag主题色发生变化时会回调该方法
     */
    @Override
    public void onThemeColorChanged(View view, int themeColor) {

    }
}
