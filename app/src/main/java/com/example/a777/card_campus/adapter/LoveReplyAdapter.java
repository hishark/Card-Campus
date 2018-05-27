package com.example.a777.card_campus.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.bean.User;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mac on 2018/5/22.
 */

public class LoveReplyAdapter extends BaseAdapter {
    private Context context;
    private List<HashMap<String, Object>> replyList;

    public LoveReplyAdapter(Context context, List<HashMap<String, Object>> replyList) {
        this.context = context;
        this.replyList = replyList;
    }

    @Override
    public int getCount() {
        return replyList.size();
    }

    @Override
    public Object getItem(int i) {
        return replyList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LinearLayout ll = (LinearLayout)view.inflate(context,R.layout.lovereply_item,null);
        CircleImageView avatar = (CircleImageView)ll.findViewById(R.id.lovereply_avatar);
        TextView tv_username = (TextView)ll.findViewById(R.id.lovereply_username);
        TextView tv_replycontent = (TextView)ll.findViewById(R.id.lovereply_content);
        TextView tv_replytime = (TextView)ll.findViewById(R.id.lovereply_time);

        //显示评论人的头像、用户名、回复时间、回复内容
        User user = (User)replyList.get(i).get("user");
        String username = user.getUser_nickname();
        Glide.with(context).load("http://p81fp7gd5.bkt.clouddn.com/useravatar.png").into(avatar);
        tv_username.setText("匿名用户");
        //tv_username.setTextColor(Color.parseColor("#E8E8E8"));

        String timestamp = replyList.get(i).get("lreply_time").toString();
        String post_time = timestamp.substring(0,timestamp.length()-2);
        tv_replytime.setText(post_time);
        Log.d("time",post_time);
        //tv_replytime.setTextColor(Color.parseColor("#E8E8E8"));

        tv_replycontent.setText(replyList.get(i).get("lreply_content").toString());
        //tv_replycontent.setTextColor(Color.parseColor("#E8E8E8"));

        return ll;
    }
}
