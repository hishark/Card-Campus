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
import com.example.a777.card_campus.util.CheckQQUtil;
import com.example.a777.card_campus.util.CurrentUserUtil;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DaideliverDetailActivity extends AppCompatActivity {

    private CircleImageView user_avatar;
    private TextView daideliverdetail_username,daideliverdetail_time,daideliverdetail_title,daideliverdetail_content;
    private Button daideliverdetail_qq,daideliverdetail_tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daideliver_detail);
        getSupportActionBar().hide();

        Button daideliverdetail_back=(Button)this.findViewById(R.id.daideliverdetail_back);
        daideliverdetail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initView();

        Intent intent = getIntent();

        HashMap<String, Object> daideliver_item = new HashMap<String,Object>();

        // 通过daike得到得到对象
        // getSerializableExtra得到序列化数据
        daideliver_item = (HashMap<String, Object>)intent.getSerializableExtra("daideliver");

        final User user = (User)daideliver_item.get("user");
        String username = user.getUser_nickname();

        String timestamp = daideliver_item.get("dpost_time").toString();
        String post_time = timestamp.substring(0,timestamp.length()-2);

        daideliverdetail_username.setText(username);
        Glide.with(getApplicationContext()).load(user.getUser_avatar()).into(user_avatar);
        daideliverdetail_time.setText(post_time);
        daideliverdetail_title.setText(daideliver_item.get("dpost_title").toString());
        daideliverdetail_content.setText(daideliver_item.get("dpost_content").toString());

        final String qq=user.getUser_qq().toString();
        if(qq!=null||qq.equals("")){
            if(isQQClientAvailable(this)){
                daideliverdetail_qq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url="mqqwpa://im/chat?chat_type=wpa&uin="+qq;
                        System.out.print("QQ打开了没"+url);
                        if(CheckQQUtil.qqCheck(qq)) {
                            if(qq.equals(CurrentUserUtil.getCurrentUser().getUser_qq())){
                                Toast.makeText(getApplicationContext(),"自己不能跟自己聊天哦~",Toast.LENGTH_SHORT).show();
                            }else {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"该用户留下了错误的QQ号，请选择其他联系方式",Toast.LENGTH_SHORT).show();
                        }
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
            daideliverdetail_tel.setOnClickListener(new View.OnClickListener(){
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
        daideliverdetail_username=(TextView)this.findViewById(R.id.dddetail_username);
        daideliverdetail_time=(TextView)this.findViewById(R.id.dddetail_time);
        daideliverdetail_title=(TextView)this.findViewById(R.id.daideliverdetail_title);
        daideliverdetail_content=(TextView)this.findViewById(R.id.daideliverdetail_content);
        daideliverdetail_qq=(Button)this.findViewById(R.id.dddetail_qq);
        daideliverdetail_tel=(Button)this.findViewById(R.id.dddetail_tel);
        user_avatar=(CircleImageView)this.findViewById(R.id.dddetail_avatar);
    }
}
