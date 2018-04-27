package com.example.a777.card_campus.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a777.card_campus.R;
import com.example.a777.card_campus.bean.User;

import java.util.HashMap;
import java.util.List;

/**
 * Created by 777 on 2018/4/27.
 */

public class DaikeAdapter extends BaseAdapter {
    Context context;
    List<HashMap<String, Object>> list;

    public DaikeAdapter(Context applicationContext, List<HashMap<String, Object>> daikeResult) {
        this.context = applicationContext;
        this.list = daikeResult;
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
        LinearLayout ll=(LinearLayout)View.inflate(context, R.layout.daike_item, null);
        ImageView iv = (ImageView)ll.findViewById(R.id.daike_avatar);
        TextView tv_username = (TextView)ll.findViewById(R.id.daike_username);
        TextView tv_daike_content = (TextView)ll.findViewById(R.id.daike_content);

        User user = (User)list.get(i).get("user");
        String username = user.getUser_nickname();
        tv_username.setText(username);

        String content = list.get(i).get("dpost_content").toString();
        tv_daike_content.setText(content);

        return ll;
    }
}