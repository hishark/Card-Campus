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
    int ReplyNum[];

    public QuestionPostEditDeleteAdapter(Context applicationContext, List<HashMap<String, Object>> questionPostResult, int[] questionReplyNum) {
        this.context = applicationContext;
        this.list = questionPostResult;
        this.ReplyNum = questionReplyNum;
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



        TextView tv_questionReplyNum = (TextView)ll.findViewById(R.id.questionpost_replyNum);
        tv_questionReplyNum.setText(ReplyNum[i+1]+"");
        tv_questionReplyNum.setTextColor(Color.parseColor("#757575"));

        User user = (User)list.get(i).get("user");
        String username = user.getUser_nickname();

        TextView tv_username = (TextView)ll.findViewById(R.id.questionpost_username);
        tv_username.setText(username);
        tv_username.setTextColor(Color.parseColor("#757575"));

        TextView tv_questionpost_title = (TextView)ll.findViewById(R.id.questionpost_title);
        String question_post_title = list.get(i).get("bpost_title").toString();
        tv_questionpost_title.setText(question_post_title);
        tv_questionpost_title.setTextColor(Color.parseColor("#757575"));

        CircleImageView avatar = (CircleImageView)ll.findViewById(R.id.questionpost_avatar);
        Glide.with(context).load(user.getUser_avatar()).into(avatar);

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
