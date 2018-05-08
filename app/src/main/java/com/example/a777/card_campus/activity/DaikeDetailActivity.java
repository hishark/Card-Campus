package com.example.a777.card_campus.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.bean.DaiPost;
import com.example.a777.card_campus.bean.User;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DaikeDetailActivity extends AppCompatActivity {

    private CircleImageView user_avatar;
    private TextView dkdetail_username,dkdetail_time,daikedetail_title,daikedetail_content;
    private Button dkdetal_qq,dkdetal_tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daike_detail);
        getSupportActionBar().hide();

        Button daike_back=(Button)this.findViewById(R.id.daikedetail_back);
        daike_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initView();

        Intent intent = getIntent();

        HashMap<String, Object> daike_item = new HashMap<String,Object>();

        // 通过daike得到得到对象
        // getSerializableExtra得到序列化数据
        daike_item = (HashMap<String, Object>)intent.getSerializableExtra("daike");

        final User user = (User)daike_item.get("user");
        String username = user.getUser_nickname();

        String timestamp = daike_item.get("dpost_time").toString();
        String post_time = timestamp.substring(0,timestamp.length()-2);

        dkdetail_username.setText(username);
        Glide.with(getApplicationContext()).load(user.getUser_avatar()).into(user_avatar);
        dkdetail_time.setText(post_time);
        daikedetail_title.setText(daike_item.get("dpost_title").toString());
        daikedetail_content.setText(daike_item.get("dpost_content").toString());

        final String qq=user.getUser_qq().toString();
        if(qq!=null||qq.equals("")){
            if(isQQClientAvailable(this)){
                dkdetal_qq.setOnClickListener(new View.OnClickListener() {
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
            dkdetal_tel.setOnClickListener(new View.OnClickListener(){
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
        dkdetail_username=(TextView)this.findViewById(R.id.dkdetail_username);
        dkdetail_time=(TextView)this.findViewById(R.id.dkdetail_time);
        daikedetail_title=(TextView)this.findViewById(R.id.daikedetail_title);
        daikedetail_content=(TextView)this.findViewById(R.id.daikedetail_content);
        dkdetal_qq=(Button)this.findViewById(R.id.dkdetail_qq);
        dkdetal_tel=(Button)this.findViewById(R.id.dkdetail_tel);
        user_avatar=(CircleImageView)this.findViewById(R.id.dkdetail_avatar);
    }
}
