package com.example.a777.card_campus.adapter;

import android.content.Context;
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
 * Created by mac on 2018/5/11.
 */

public class DaiPostAdapter extends BaseAdapter {

    Context context;
    List<HashMap<String, Object>> list;

    public DaiPostAdapter(Context applicationContext, List<HashMap<String, Object>> daipostResult) {
        this.context = applicationContext;
        this.list = daipostResult;
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
        LinearLayout ll=(LinearLayout)View.inflate(context, R.layout.daipost_item, null);
        //CircleImageView avatar = (CircleImageView)ll.findViewById(R.id.daipost_avatar);
        //TextView tv_username = (TextView)ll.findViewById(R.id.daipost_username);
        TextView tv_posttime = (TextView)ll.findViewById(R.id.daipost_time);
        TextView tv_title = (TextView)ll.findViewById(R.id.daipost_title);
        TextView tv_content = (TextView)ll.findViewById(R.id.daipost_content);
        ImageView im_solve=(ImageView)ll.findViewById(R.id.daipost_issolved);
        Button bt_edit=(Button)ll.findViewById(R.id.daipost_edit);

        int state;
        state=Integer.parseInt(list.get(i).get("is_solved").toString());
        if(state==1) {
            im_solve.setImageResource(R.drawable.jiejue);
        }else{
            im_solve.setImageResource(R.drawable.question);
        }

        User user = (User)list.get(i).get("user");
        String username = user.getUser_nickname();

        //tv_username.setText(username);
        String time=list.get(i).get("dpost_time").toString();
        tv_posttime.setText(time);

        String title = list.get(i).get("dpost_title").toString();
        tv_title.setText(title);

        String content = list.get(i).get("dpost_content").toString();
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

    private onItemEditListener mOnItemEditListener;

    public void setOnItemDeleteClickListener(onItemEditListener mOnItemEditListener) {
        this.mOnItemEditListener = mOnItemEditListener;
    }

}
