package com.example.a777.card_campus.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.bean.QuestionPost;
import com.example.a777.card_campus.bean.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 777 on 2018/4/28.
 */

public class QuestionPostAdapter extends BaseAdapter {
    Context context;
    List<HashMap<String, Object>> list;
    HashMap<String, Object> BSTPostReplyNum;
    //int ReplyNum[];

    public QuestionPostAdapter(Context applicationContext, List<HashMap<String, Object>> questionPostResult, HashMap<String, Object> num) {
        this.context = applicationContext;
        this.list = questionPostResult;
        this.BSTPostReplyNum = num;
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
        final LinearLayout ll=(LinearLayout)View.inflate(context, R.layout.questionpost_item, null);


        TextView tv_questionReplyNum = (TextView)ll.findViewById(R.id.questionpost_replyNum);
        Log.d("BSTREPLY好像为空",BSTPostReplyNum.toString()+"!");

        String post_id = list.get(i).get("bpost_id").toString();
        int replyNum = (int)BSTPostReplyNum.get(post_id);
        Log.d("回复还是拿到了吧",replyNum+"");


        tv_questionReplyNum.setText(replyNum+"");
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



}
