package com.example.a777.card_campus.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.bean.User;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DaibuyDetailActivity extends AppCompatActivity {

    private CircleImageView user_avatar;
    private TextView daibuydetail_username,daibuydetail_time,daibuydetail_title,daikedetail_content;
    private Button daibuydetail_qq,daibuydetail_tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daibuy_detail);
        getSupportActionBar().hide();

        Button daibuydetail_back=(Button)this.findViewById(R.id.daibuydetail_back);
        daibuydetail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initView();

        Intent intent = getIntent();

        HashMap<String, Object> daibuy_item = new HashMap<String,Object>();

        // 通过daike得到得到对象
        // getSerializableExtra得到序列化数据
        daibuy_item = (HashMap<String, Object>)intent.getSerializableExtra("daibuy");

        final User user = (User)daibuy_item.get("user");
        String username = user.getUser_nickname();

        String timestamp = daibuy_item.get("dpost_time").toString();
        String post_time = timestamp.substring(0,timestamp.length()-2);

        daibuydetail_username.setText(username);
        Glide.with(getApplicationContext()).load(user.getUser_avatar()).into(user_avatar);
        daibuydetail_time.setText(post_time);
        daibuydetail_title.setText(daibuy_item.get("dpost_title").toString());
        daikedetail_content.setText(daibuy_item.get("dpost_content").toString());

        final String qq=user.getUser_qq().toString();
        if(qq!=null||qq.equals("")){
            if(isQQClientAvailable(this)){
                daibuydetail_qq.setOnClickListener(new View.OnClickListener() {
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
            daibuydetail_tel.setOnClickListener(new View.OnClickListener(){
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
        daibuydetail_username=(TextView)this.findViewById(R.id.dbdetail_username);
        daibuydetail_time=(TextView)this.findViewById(R.id.dbdetail_time);
        daibuydetail_title=(TextView)this.findViewById(R.id.daibuydetail_title);
        daikedetail_content=(TextView)this.findViewById(R.id.daibuydetail_content);
        daibuydetail_qq=(Button)this.findViewById(R.id.dbdetail_qq);
        daibuydetail_tel=(Button)this.findViewById(R.id.dbdetail_tel);
        user_avatar=(CircleImageView)this.findViewById(R.id.dbdetail_avatar);
    }
}
