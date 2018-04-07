package com.example.a777.card_campus.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a777.card_campus.Adapter.LovewallAdapter;
import com.example.a777.card_campus.R;
import com.moxun.tagcloudlib.view.TagCloudView;
import com.moxun.tagcloudlib.view.TagsAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LovewallFragment extends Fragment {
    //定义成员变量
    private View view;
    //标签云
    private TagCloudView tagCloudView;
    //先弄一票假的表白标题
    ArrayList<String> loveTitle;
    //表白标签云的适配器
    LovewallAdapter lovewallAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lovewall, container, false);

        //标签云的初始化和适配器的设置
        tagCloudView = (TagCloudView)view.findViewById(R.id.tcv_lovewall);
        loveTitle = new ArrayList<String>();
        addTags(loveTitle);
        lovewallAdapter = new LovewallAdapter(getContext(),loveTitle);
        tagCloudView.setAdapter(lovewallAdapter);

        /**
         * 悬浮球的点击事件，点一下加一条表白记录，先做了一个假的
         */
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab_loveAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loveTitle.add("我来表个白就走");
                lovewallAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    /**
     * 给标签云添加数据
     * @param loveTitle——存放数据的arraylist
     */
    private void addTags(ArrayList<String> loveTitle) {
        loveTitle.add("小琪小琪我爱你");
        loveTitle.add("就像老鼠爱大米");
        loveTitle.add("捷豹捷豹跑得快");
        loveTitle.add("肖小月是猪");
        loveTitle.add("郑见见智障");
        loveTitle.add("嘻嘻嘻嘻");
        loveTitle.add("小洁嫁我");
        loveTitle.add("啊啊啊啊啊啊啊啊啊");
        loveTitle.add("表白一个");
        loveTitle.add("哼唧");
        loveTitle.add("好玩好玩");
        loveTitle.add("今天食堂看到一个小哥");
        loveTitle.add("穿白裙子的小姐姐我喜欢你");
    }

}
