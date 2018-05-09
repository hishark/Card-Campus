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

public class DaiactivityDetailActivity extends AppCompatActivity {

    private CircleImageView user_avatar;
    private TextView dadetail_username,dadetail_time,daiactivitydetail_title,daiactivitydetail_content;
    private Button dadetal_qq,dadetal_tel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daidetail);
        getSupportActionBar().hide();
        Button daiactivity_back=(Button)this.findViewById(R.id.daiactivitydetail_back);
        daiactivity_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initView();

        Intent intent = getIntent();

        HashMap<String, Object> daiactivity_item = new HashMap<String,Object>();

        // 通过daike得到得到对象
        // getSerializableExtra得到序列化数据
        daiactivity_item = (HashMap<String, Object>)intent.getSerializableExtra("daiactivity");

        final User user = (User)daiactivity_item.get("user");
        String username = user.getUser_nickname();

        String timestamp = daiactivity_item.get("dpost_time").toString();
        String post_time = timestamp.substring(0,timestamp.length()-2);

        dadetail_username.setText(username);
        Glide.with(getApplicationContext()).load(user.getUser_avatar()).into(user_avatar);
        dadetail_time.setText(post_time);
        daiactivitydetail_title.setText(daiactivity_item.get("dpost_title").toString());
        daiactivitydetail_content.setText(daiactivity_item.get("dpost_content").toString());

        final String qq=user.getUser_qq().toString();
        if(qq!=null||qq.equals("")){
            if(isQQClientAvailable(this)){
                dadetal_qq.setOnClickListener(new View.OnClickListener() {
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
            dadetal_tel.setOnClickListener(new View.OnClickListener(){
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
        dadetail_username=(TextView)this.findViewById(R.id.dactdetail_username);
        dadetail_time=(TextView)this.findViewById(R.id.dactdetail_time);
        daiactivitydetail_title=(TextView)this.findViewById(R.id.daiactivitydetail_title);
        daiactivitydetail_content=(TextView)this.findViewById(R.id.daiactivitydetail_content);
        dadetal_qq=(Button)this.findViewById(R.id.dactdetail_qq);
        dadetal_tel=(Button)this.findViewById(R.id.dactdetail_tel);
        user_avatar=(CircleImageView)this.findViewById(R.id.dactdetail_avatar);
    }
}