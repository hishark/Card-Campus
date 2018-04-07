package com.example.a777.card_campus.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a777.card_campus.R;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

public class HomepageFragment extends Fragment {
    //定义成员变量
    private View view;
    //图片轮播框架
    private RollPagerView rollPagerView;
    //日历
    private CalendarView calendarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homepage, container, false);

        //图片轮播初始化
        rollPagerView = (RollPagerView)view.findViewById(R.id.rpv_homepage_image);
        rollPagerViewSet();

        /**
         * 日历初始化及点击事件设置
         * 这里要注意一下的是日历里的月份默认值会比真实月份小1
         * 所以自增一下
         */
        calendarView = (CalendarView)view.findViewById(R.id.homepage_calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                month++;
                Toast.makeText(getContext(),"今天是"+month+"月"+dayOfMonth+"日，哎呀还没放假哦",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    //图片轮播相关设置
    private void rollPagerViewSet() {
        rollPagerView.setPlayDelay(3000);//*播放间隔
        rollPagerView.setAnimationDurtion(500);//透明度
        rollPagerView.setAdapter(new rollViewpagerAdapter());//配置适配器
    }


    //自定义适配器封装图片加载到rollviewpager里
    private class rollViewpagerAdapter extends StaticPagerAdapter {

        //轮播图片源
        private int[] res={
                R.drawable.rvp_1,R.drawable.rvp_2,R.drawable.rvp_3
        };

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView imageView=new ImageView(container.getContext());
            /**
             * Glide图片加载框架，一句就ok啦
             */
            Glide.with(getContext()).load(res[position]).into(imageView);

            /**
             * ImageView的Scaletype决定了图片在View上显示时的样子，如进行何种比例的缩放，及显示图片的整体还是部分，等等。
             * CENTER_CROP——按比例扩大图片的size居中显示
             */
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            return imageView;
        }

        @Override
        public int getCount() {
            return res.length;
        }
    }

}
