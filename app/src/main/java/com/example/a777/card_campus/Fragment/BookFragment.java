package com.example.a777.card_campus.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.a777.card_campus.Adapter.BookAdapter;
import com.example.a777.card_campus.R;

import java.util.ArrayList;

public class BookFragment extends Fragment {
    //定义成员变量
    private View view;
    private GridView gridView_Book;
    ArrayList<String> bookName = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_book, container, false);

        //给GridView准备数据
        bookName.add("张小琪的一生");
        bookName.add("捷豹的一生");
        bookName.add("月月的一生");
        bookName.add("郑见见的一生");
        bookName.add("老人与海");

        //给GridView设置适配器
        gridView_Book = (GridView)view.findViewById(R.id.GridView_Book);
        final BookAdapter bookAdapter = new BookAdapter(getActivity().getApplicationContext(),bookName);
        gridView_Book.setAdapter(bookAdapter);

        /**
         * 悬浮球的点击事件，点一下加一本，先做了个假的
         */
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab_bookAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookName.add("加了一本嘻嘻嘻");
                bookAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}
