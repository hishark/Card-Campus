package com.example.a777.card_campus.Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a777.card_campus.R;

import java.util.ArrayList;

/**
 * Created by 777 on 2018/4/6.
 */

public class BookAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> bookName;
    public BookAdapter(Context context,ArrayList<String> bookName){
        this.context = context;
        this.bookName = bookName;
    }

    @Override
    public int getCount() {
        return bookName.size();
    }

    @Override
    public Object getItem(int i) {
        return bookName.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LinearLayout ll=(LinearLayout)View.inflate(context, R.layout.book_item, null);
        TextView tv=(TextView)ll.findViewById(R.id.textview_bookname);
        tv.setText(String.valueOf(bookName.get(i)));
        ll.setGravity(Gravity.CENTER_HORIZONTAL);
        return ll;
    }
}
