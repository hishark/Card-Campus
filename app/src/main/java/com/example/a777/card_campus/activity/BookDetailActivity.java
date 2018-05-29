package com.example.a777.card_campus.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.bean.User;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView bookImage;
    private HashMap<String, Object> currentBookPost;
    private Button back,userqq,usertel;
    private TextView bookpost_content,bookpost_username,bookpost_time;
    private CircleImageView bookpost_useravatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        initView();

        setSupportActionBar(toolbar);


        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);



        currentBookPost = (HashMap<String,Object>)getIntent().getSerializableExtra("BookPost");
        collapsingToolbarLayout.setTitle(currentBookPost.get("book_title").toString());
        Glide.with(getApplicationContext()).load(currentBookPost.get("book_img")).into(bookImage);
        bookpost_content.setText(currentBookPost.get("book_describe").toString());
        final User user = (User)(currentBookPost.get("user"));
        Glide.with(getApplicationContext()).load(user.getUser_avatar()).into(bookpost_useravatar);
        bookpost_username.setText(user.getUser_nickname());
        String timestamp = currentBookPost.get("book_time").toString();
        String post_time = timestamp.substring(0,timestamp.length()-2);
        bookpost_time.setText(post_time);


        final String qq=user.getUser_qq().toString();
        if(qq!=null||qq.equals("")){
            if(isQQClientAvailable(this)){
                userqq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url="mqqwpa://im/chat?chat_type=wpa&uin="+qq;
                        System.out.print("QQ打开了没"+url);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    }
                });
            }else{
                Toast.makeText(getApplicationContext(), "亲，您还没安装QQ或者版本不匹配呢", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getApplicationContext(), "亲，它没留下QQ号哦", Toast.LENGTH_LONG).show();
        }


        String number=user.getUser_tel().toString();
        if(number!=null||number.equals("")){
            usertel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    String number=user.getUser_tel().toString();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "亲，ta没留下手机号哦", Toast.LENGTH_LONG).show();
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    /**
     * 判断qq是否可用
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
    private void initView() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        toolbar = (Toolbar) findViewById(R.id.bookdetail_toolbar);
        bookImage = (ImageView) findViewById(R.id.iv_bookImage);
        back = (Button)findViewById(R.id.currentBookPost_back);
        bookpost_content = (TextView)this.findViewById(R.id.bookpost_content);
        bookpost_time = (TextView)this.findViewById(R.id.bookpost_time);
        bookpost_useravatar = (CircleImageView)this.findViewById(R.id.bookpost_userAvatar);
        bookpost_username = (TextView)this.findViewById(R.id.bookpost_username);
        userqq = (Button)this.findViewById(R.id.bookpost_userqq);
        usertel = (Button)this.findViewById(R.id.bookpost_usertel);

    }
}
