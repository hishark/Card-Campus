package com.example.a777.card_campus.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.bean.User;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mac on 2018/4/27.
 */

public class DaiwalkAdapter extends BaseAdapter {

    Context context;
    List<HashMap<String, Object>> list;

    public DaiwalkAdapter(Context applicationContext, List<HashMap<String, Object>> daiwalkResult) {
        this.context = applicationContext;
        this.list = daiwalkResult;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LinearLayout ll=(LinearLayout)View.inflate(context, R.layout.daiwalk_item, null);
        CircleImageView avatar = (CircleImageView)ll.findViewById(R.id.daiwalk_avatar);
        TextView tv_username = (TextView)ll.findViewById(R.id.daiwalk_username);
        TextView tv_daiwalk_content = (TextView)ll.findViewById(R.id.daiwalk_title);
        ImageView im_solve=(ImageView)ll.findViewById(R.id.daiwalk_issolved);

        int state;
        state=Integer.parseInt(list.get(i).get("is_solved").toString());
        if(state==1) {
            im_solve.setImageResource(R.drawable.jiejue);
        }else{
            im_solve.setImageResource(R.drawable.question);
        }

        User user = (User)list.get(i).get("user");
        String username = user.getUser_nickname();
        tv_username.setText(username);

        String content = list.get(i).get("dpost_title").toString();
        tv_daiwalk_content.setText(content);
        Glide.with(context).load(user.getUser_avatar()).into(avatar);
        return ll;
    }
}
