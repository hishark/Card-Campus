package com.example.a777.card_campus.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.moxun.tagcloudlib.view.TagsAdapter;

import java.util.ArrayList;

/**
 * Created by 777 on 2018/4/7.
 */

public class LovewallAdapter extends TagsAdapter {

    Context context;
    ArrayList<String> lovetitle;

    public LovewallAdapter(Context context, ArrayList<String> title){
        this.context = context;
        this.lovetitle = title;
    }

    @Override
    public int getCount() {
        return lovetitle.size();
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
        tv.setText(lovetitle.get(position));
        tv.setGravity(Gravity.CENTER);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,lovetitle.get(position),Toast.LENGTH_SHORT).show();
            }
        });

        return tv;
    }

    @Override
    public Object getItem(int position) {
        return lovetitle.get(position);
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
