package com.example.a777.card_campus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.a777.card_campus.fragment.BookFragment;
import com.example.a777.card_campus.fragment.EverythingFragment;
import com.example.a777.card_campus.fragment.HomepageFragment;
import com.example.a777.card_campus.fragment.InsteadFragment;
import com.example.a777.card_campus.fragment.LovewallFragment;
import com.example.a777.card_campus.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //碎片初始化
    HomepageFragment homepageFragment = new HomepageFragment();
    InsteadFragment insteadFragment = new InsteadFragment();
    EverythingFragment everythingFragment = new EverythingFragment();
    LovewallFragment lovewallFragment = new LovewallFragment();
    BookFragment bookFragment = new BookFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置默认显示的碎片为主页
        setFirstFragment();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * 这个是悬浮球的点击事件，先留着说不定以后可以用啦
         */
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        /**
         * 这几个是NavigationView自动生成的，不管他
         */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        /**
         * 设置侧滑栏的点击监听
         */
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * NavigationView自动生成的
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * NavigationView自动生成的
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 界面右上角三个点点的点击事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 侧滑栏的点击事件
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_homepage){
            replaceFragment(homepageFragment);
        } else if (id == R.id.nav_DaiDaiDai) {
            //getWindow().setTitle("代代代");
            replaceFragment(insteadFragment);

        } else if (id == R.id.nav_BaiShiTong) {
            replaceFragment(everythingFragment);
        } else if (id == R.id.nav_BiaoBaiQiang) {
            replaceFragment(lovewallFragment);
        } else if (id == R.id.nav_ErShouShu) {
            replaceFragment(bookFragment);
        } else if (id == R.id.nav_Setting) {
            //登录界面放到这测试一下
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_About) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     *  动态添加碎片
     */
    public void replaceFragment(Fragment fragment) {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.Right_Content, fragment);
        transaction.commit();
    }

    /**
     * 设置默认显示的碎片
     */
    private void setFirstFragment() {
        replaceFragment(homepageFragment);
    }


    /**
     * Android按返回键退出程序但不销毁
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
