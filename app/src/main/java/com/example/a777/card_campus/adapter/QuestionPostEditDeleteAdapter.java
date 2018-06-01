package com.example.a777.card_campus.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.bean.User;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 777 on 2018/4/28.
 */

public class QuestionPostEditDeleteAdapter extends BaseAdapter {
    Context context;
    List<HashMap<String, Object>> list;
    HashMap<String, Object> BSTPostReplyNum;

    public QuestionPostEditDeleteAdapter(Context applicationContext, List<HashMap<String, Object>> questionPostResult, HashMap<String, Object> questionReplyNum) {
        this.context = applicationContext;
        this.list = questionPostResult;
        this.BSTPostReplyNum = questionReplyNum;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final LinearLayout ll=(LinearLayout)View.inflate(context, R.layout.questionposteditdelete_item, null);

        Button edit = (Button)ll.findViewById(R.id.questionpost_edit);
        //Button delete = (Button)ll.findViewById(R.id.questionpost_delete);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemEditListener.onEditClick(i);
            }
        });

        String timestamp = list.get(i).get("bpost_time").toString();
        String post_time = timestamp.substring(0,timestamp.length()-2);
        TextView time = (TextView)ll.findViewById(R.id.questionpost_posttime);
        time.setText(post_time);


        TextView tv_questionReplyNum = (TextView)ll.findViewById(R.id.questionpost_replyNum);

        String post_id = list.get(i).get("bpost_id").toString();
        int replyNum = (int)BSTPostReplyNum.get(post_id);

        tv_questionReplyNum.setText(replyNum+"");
        tv_questionReplyNum.setTextColor(Color.parseColor("#757575"));

        //User user = (User)list.get(i).get("user");
        //String username = user.getUser_nickname();

        //TextView tv_username = (TextView)ll.findViewById(R.id.questionpost_username);
        //tv_username.setText(username);
        //tv_username.setTextColor(Color.parseColor("#757575"));

        TextView tv_questionpost_title = (TextView)ll.findViewById(R.id.questionpost_title);
        String question_post_title = list.get(i).get("bpost_title").toString();
        tv_questionpost_title.setText(question_post_title);
        tv_questionpost_title.setTextColor(Color.parseColor("#757575"));


        TextView tv_questionpost_content = (TextView)ll.findViewById(R.id.currentt_questionpost_content);
        String question_post_content = list.get(i).get("bpost_content").toString();
        tv_questionpost_content.setText(question_post_content);
        tv_questionpost_content.setTextColor(Color.parseColor("#a49b9b"));

        //CircleImageView avatar = (CircleImageView)ll.findViewById(R.id.questionpost_avatar);
        //Glide.with(context).load(user.getUser_avatar()).into(avatar);

        return ll;
    }

    /**
     * 删除按钮的监听接口
     */
    public interface onItemDeleteListener {
        void onDeleteClick(int i);
    }

    private onItemDeleteListener mOnItemDeleteListener;

    public void setOnItemDeleteClickListener(onItemDeleteListener mOnItemDeleteListener) {
        this.mOnItemDeleteListener = mOnItemDeleteListener;
    }


    /**
     * 编辑按钮的监听接口
     */
    public interface onItemEditListener {
        void onEditClick(int i);
    }

    private onItemEditListener mOnItemEditListener;

    public void setOnItemEditClickListener(onItemEditListener mOnItemEditListener) {
        this.mOnItemEditListener = mOnItemEditListener;
    }

}
