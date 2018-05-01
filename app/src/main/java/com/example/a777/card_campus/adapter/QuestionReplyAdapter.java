package com.example.a777.card_campus.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.bean.User;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 777 on 2018/5/1.
 */

public class QuestionReplyAdapter extends BaseAdapter {
    Context context;
    List<HashMap<String, Object>> replyList;

    public QuestionReplyAdapter(Context applicationContext, List<HashMap<String, Object>> currentPostReplys) {
        context = applicationContext;
        replyList = currentPostReplys;
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
        LinearLayout ll = (LinearLayout)view.inflate(context, R.layout.questionreply_item,null);

        CircleImageView avatar = (CircleImageView)ll.findViewById(R.id.questionreply_avatar);
        TextView tv_username = (TextView)ll.findViewById(R.id.questionreply_username);
        TextView tv_replycontent = (TextView)ll.findViewById(R.id.questionreply_content);
        TextView tv_replytime = (TextView)ll.findViewById(R.id.questionreply_time);

        User user = (User)replyList.get(i).get("user");
        String username = user.getUser_nickname();

        Glide.with(context).load(user.getUser_avatar()).into(avatar);
        tv_username.setText(username);
        tv_replycontent.setText(replyList.get(i).get("breply_content").toString());
        tv_replytime.setText(replyList.get(i).get("breply_time").toString());

        return ll;
    }
}
