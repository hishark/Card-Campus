package com.example.a777.card_campus.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a777.card_campus.R;
import com.example.a777.card_campus.bean.User;

import java.util.HashMap;
import java.util.List;

/**
 * Created by mac on 2018/5/29.
 */

public class CurrentUserBookAdapter extends BaseAdapter {
    List<HashMap<String, Object>> Post;
    Context context;

    public CurrentUserBookAdapter(Context applicationContext, List<HashMap<String, Object>> currentUserPost) {
        this.Post = currentUserPost;
        this.context = applicationContext;
    }

    @Override
    public int getCount() {
        return Post.size();
    }

    @Override
    public Object getItem(int i) {
        return Post.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LinearLayout ll=(LinearLayout)View.inflate(context, R.layout.bookpost_item, null);
        //CircleImageView avatar = (CircleImageView)ll.findViewById(R.id.daipost_avatar);
        //TextView tv_username = (TextView)ll.findViewById(R.id.daipost_username);
        TextView tv_posttime = (TextView)ll.findViewById(R.id.mybookpost_time);
        TextView tv_title = (TextView)ll.findViewById(R.id.mybookpost_title);
        TextView tv_content = (TextView)ll.findViewById(R.id.mybookpost_content);
        ImageView im_solve=(ImageView)ll.findViewById(R.id.mybookpost_issold);
        Button bt_edit=(Button)ll.findViewById(R.id.mybookpost_edit);

        int state;
        state=Integer.parseInt(Post.get(i).get("is_sold").toString());
        if(state==1) {
            im_solve.setImageResource(R.drawable.sold3);
        }else{
            im_solve.setImageResource(R.drawable.sale3);
        }

        User user = (User)Post.get(i).get("user");
        String username = user.getUser_nickname();

        //tv_username.setText(username);
        String time=Post.get(i).get("book_time").toString();
        String post_time = time.substring(0,time.length()-2);
        tv_posttime.setText(post_time);

        String title = Post.get(i).get("book_title").toString();
        tv_title.setText(title);

        String content = Post.get(i).get("book_describe").toString();
        tv_content.setText(content);

        //Glide.with(context).load(user.getUser_avatar()).into(avatar);


        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemEditListener.onEditClick(i);

            }
        });

        return ll;
    }
    /**
     * 编辑按钮的监听接口
     * https://blog.csdn.net/weixin_41828085/article/details/79571728
     */
    public interface onItemEditListener {
        void onEditClick(int i);
    }

    private CurrentUserBookAdapter.onItemEditListener mOnItemEditListener;

    public void setOnItemEditClickListener(CurrentUserBookAdapter.onItemEditListener mOnItemEditListener) {
        this.mOnItemEditListener = mOnItemEditListener;
    }
}
