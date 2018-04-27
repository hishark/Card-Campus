package com.example.a777.card_campus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.a777.card_campus.R;
import com.example.a777.card_campus.activity.DaiactivityActivity;
import com.example.a777.card_campus.activity.DaibuyActivity;
import com.example.a777.card_campus.activity.DaideliverActivity;
import com.example.a777.card_campus.activity.DaifoodActivity;
import com.example.a777.card_campus.activity.DaikeActivity;
import com.example.a777.card_campus.activity.DaiwalkActivity;

public class InsteadFragment extends Fragment {

    //定义成员变量
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_instead, container, false);

        /**
         * 代课的点击事件
         * 点进去就加载所有关于代课的帖子
         */
        CardView cardView_insteadCourse = (CardView)view.findViewById(R.id.CardView_InsteadCourse);
        cardView_insteadCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"hello！代课吗！",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DaikeActivity.class);
                startActivity(intent);
            }
        });


        /**
         * 代拿外卖的点击事件
         * 点进去就加载所有关于代拿外卖的帖子
         */
        CardView cardView_insteadFood = (CardView)view.findViewById(R.id.CardView_InsteadFood);
        cardView_insteadFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"hello！拿个外卖呗！",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DaifoodActivity.class);
                startActivity(intent);
            }
        });


        /**
         * 代活动的点击事件
         * 点进去就加载所有关于代活动的帖子
         */
        CardView cardView_insteadActivity = (CardView)view.findViewById(R.id.CardView_InsteadActivity);
        cardView_insteadActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"hello！活动我帮你去！",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DaiactivityActivity.class);
                startActivity(intent);
            }
        });


        /**
         * 代健步走的点击事件
         * 点进去就加载所有关于代健步走的帖子
         */
        CardView cardView_insteadWalk = (CardView)view.findViewById(R.id.CardView_InsteadWalk);
        cardView_insteadWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"hello！不想健步走对吧！",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),DaiwalkActivity.class);
                startActivity(intent);
            }
        });


        /**
         * 代购的点击事件
         * 点进去就加载所有关于代购的帖子
         */
        CardView cardView_insteadShop = (CardView)view.findViewById(R.id.CardView_InsteadShop);
        cardView_insteadShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"hello！代购不！",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),DaibuyActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 代拿快递的点击事件
         * 点进去就加载所有关于代拿快递的帖子
         */
        CardView cardView_insteadPackage = (CardView)view.findViewById(R.id.CardView_InsteadPackage);
        cardView_insteadPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"hello！快递又来了哦！",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DaideliverActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
