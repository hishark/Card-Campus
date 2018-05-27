package com.example.a777.card_campus.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.a777.card_campus.R;
import com.example.a777.card_campus.adapter.LovewallAdapter;
import com.example.a777.card_campus.bean.User;
import com.example.a777.card_campus.util.CurrentUserUtil;
import com.example.a777.card_campus.util.RandomIDUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendLoveActivity extends AppCompatActivity {

    private static String URL="http://192.168.137.1:8080/Card-Campus-Server/addLovepost";
    private Button back,send;
    private EditText et_title,et_content;
    private RadioGroup rg_state;
    private RadioButton showname,noshowname;
    private String state,title,content,name,id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_love);
        getSupportActionBar().hide();

        initView();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title=et_title.getText().toString();
                content=et_content.getText().toString();
                for(int i=0;i<rg_state.getChildCount();i++){
                    RadioButton radioButton=(RadioButton)rg_state.getChildAt(i);
                    if(radioButton.isChecked()){
                        state=radioButton.getText().toString();
                    }
                }

                User user = CurrentUserUtil.getCurrentUser();
                /**
                 * 这段一定要写在send的点击事件里面，不然报空指针错误
                 */
                if(state.equals("不匿名")){
                    name=user.getUser_nickname();
                    state="0";
                }else{
                    name="某某童鞋";
                    state="1";
                }

                id= RandomIDUtil.getID();
                Log.d("aaaaaaaaa",id);

                if(title.equals("")&&content.equals("")){
                    Toast.makeText(getApplicationContext(),"童靴，请填完整信息哦~",Toast.LENGTH_SHORT).show();
                }else{
                    addLovePostToServer();
                    Toast.makeText(getApplicationContext(),"发布成功",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    public String getLoveID(){
        UUID uuid=UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr=str.replace("-", "");
        return uuidStr;
    }


    private void addLovePostToServer() {
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();

        //创建表单请求体
        /**
         * key值与服务器端controller中request.getParameter中的key一致
         */

        RequestBody formBody = new FormBody.Builder()
                .add("love_id",id)
                .add("love_username",name)
                .add("love_title",title)
                .add("love_content",content)
                .add("love_time",String.valueOf(System.currentTimeMillis()))
                .add("is_anonymous",state)
                .build();

        //创建一个请求对象
        Request request = new Request.Builder()
                .url(URL)
                .post(formBody)
                .build();

        /**
         * Get的异步请求，不需要跟同步请求一样开启子线程
         * 但是回调方法还是在子线程中执行的
         * 所以要用到Handler传数据回主线程更新UI
         */
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            //回调的方法执行在子线程
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){

                }else{

                }
            }
        });
    }

    private void initView() {
        back=(Button)this.findViewById(R.id.sendloveactivity_back);
        send=(Button)this.findViewById(R.id.bt_sendlove);
        et_title=(EditText)this.findViewById(R.id.sendlove_title);
        et_content=(EditText)this.findViewById(R.id.sendlove_content);
        rg_state=(RadioGroup)this.findViewById(R.id.rg_namestate);
        showname=(RadioButton)this.findViewById(R.id.rb_showname);
        noshowname=(RadioButton)this.findViewById(R.id.rb_noshowname);
    }
}
