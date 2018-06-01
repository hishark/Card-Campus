package com.example.a777.card_campus.activity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a777.card_campus.fragment.BaiShiTongFragment;
import com.example.a777.card_campus.fragment.BookFragment;
import com.example.a777.card_campus.fragment.EverythingFragment;
import com.example.a777.card_campus.fragment.HomepageFragment;
import com.example.a777.card_campus.fragment.InsteadFragment;
import com.example.a777.card_campus.fragment.LovewallFragment;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.fragment.MyPostFragment;
import com.example.a777.card_campus.qiniuyun.Auth;
import com.example.a777.card_campus.util.CurrentUserUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;


import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //47.106.148.107
    private static String AVATARURL="http://47.106.148.107:8080/Card-Campus-Server/updateUserAvatar";
    private static String NAMEURL="http://47.106.148.107:8080/Card-Campus-Server/updateUserName";
    private CircleImageView ci_avatar;
    private String userName;
    private TextView tv_username;
    //碎片初始化
    HomepageFragment homepageFragment = new HomepageFragment();
    InsteadFragment insteadFragment = new InsteadFragment();
    EverythingFragment everythingFragment = new EverythingFragment();
    LovewallFragment lovewallFragment = new LovewallFragment();
    BookFragment bookFragment = new BookFragment();
    MyPostFragment myPostFragment = new MyPostFragment();
    private static String AccessKey = "HBckSDRko17AS-s_Ufbb29bYfFMKMV7opdRnx-2C";//此处填七牛云的AccessKey
    private static String SecretKey = "grNgIr009LWhQyfGvOGua8CPWFmlqfhySioKTrdk";//此处填七牛云的SecretKey
    private Uri imageUri;
    private Uri localUri = null;
    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_PICTURE = 1;
    private static final int RESULT_CROP = 2;
    private static final String TAG = "MainActivity";

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

        /**
         * 教务在线崩了，先人为设置一个用户
         */
        /*User user = new User();
        user.setUser_sno("201526702054");
        user.setUser_nickname("张小琪");
        user.setUser_avatar("http://p81fp7gd5.bkt.clouddn.com/dd72f306ly1fqv27v5dkmj207n07ndi4.jpg");
        CurrentUserUtil.user = user;*/

        /**
         * 将侧滑栏中的HeaderView的名字设置成当前登录用户的昵称，并显示头像
         */
        View headerView = navigationView.getHeaderView(0);

        //获取从LoginActivity传来的姓名
        userName = getIntent().getExtras().getString("student_name");
        String avatar = getIntent().getExtras().getString("student_avatar");
        tv_username = (TextView)headerView.findViewById(R.id.nav_header_userName);
        ci_avatar = (CircleImageView)headerView.findViewById(R.id.nav_header_avatar);
        tv_username.setText(userName);
        Glide.with(getApplicationContext()).load(avatar).into(ci_avatar);

        tv_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog ad=creteDialog();
                ad.show();
            }
        });



        ci_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"这里可以换头像啦",Toast.LENGTH_SHORT).show();
                String title = "选择获取图片方式";
                String[] items = new String[]{"拍照", "相册"};

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(title)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                switch (which) {
                                    case 0:
                                        //选择拍照
                                        pickImageFromCamera();
                                        break;
                                    case 1:
                                        //选择相册
                                        pickImageFromPicture();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }).show();
            }
        });

    }

    private AlertDialog creteDialog() {
        final AlertDialog alertDialog;
        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
        ad.setTitle("设置用户名");
        //加载自定义的login.xml到程序中
        final LinearLayout ll=(LinearLayout)getLayoutInflater()
                .inflate(R.layout.dialog_set_username, null);


        //dialog_set_username.xml加载到Dialog中
        ad.setView(ll);
        ad.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                EditText edit=(EditText)ll.findViewById(R.id.edit_username);
                userName=edit.getText().toString();
                tv_username.setText(userName);
                addUserNameToServer(userName);
            }
        });
        ad.setNegativeButton("取消",null);

        alertDialog=ad.create();
        return alertDialog;
    }

    private void addUserNameToServer(String userName) {
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();

        //创建表单请求体
        /**
         * key值与服务器端controller中request.getParameter中的key一致
         */

        RequestBody formBody = new FormBody.Builder()
                .add("user_sno", CurrentUserUtil.getCurrentUser().getUser_sno())
                .add("user_nickname",userName)
                .build();
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(NAMEURL)
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


    /**
     * 激活系统图库，选择一张图片
     */
    private void pickImageFromPicture() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");//图片
        startActivityForResult(galleryIntent, REQUEST_PICTURE);
    }


    //拍照
    private void pickImageFromCamera() {

        Intent intent = new Intent();
        File file = getOutputMediaFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {  //针对Android7.0，需要通过FileProvider封装过的路径，提供给外部调用
            imageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);//通过FileProvider创建一个content类型的Uri，进行封装
        } else { //7.0以下，如果直接拿到相机返回的intent值，拿到的则是拍照的原图大小，很容易发生OOM，所以我们同样将返回的地址，保存到指定路径，返回到Activity时，去指定路径获取，压缩图片

            imageUri = Uri.fromFile(file);

        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, REQUEST_CAMERA);//启动拍照
    }


    /**
     * 建立保存头像的路径及名称
     */
    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        File mediaFile;
        String mImageName = "avatar.png";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    if (null != imageUri) {
                        localUri = imageUri;
                        performCrop(localUri);
                    }
                    break;
                case REQUEST_PICTURE:
                    localUri = data.getData();
                    performCrop(localUri);
                    break;
                case RESULT_CROP:
                    Bundle extras = data.getExtras();
                    Bitmap selectedBitmap = extras.getParcelable("data");
                    //判断返回值extras是否为空，为空则说明用户截图没有保存就返回了，此时应该用上一张图，
                    //否则就用用户保存的图
                    if (extras == null) {
                        // ci_avatar.setImageBitmap(mBitmap);
                        // storeImage(mBitmap);
                    } else {
                        ci_avatar.setImageBitmap(selectedBitmap);
                        //Glide.with(getApplicationContext()).load(selectedBitmap).into(ci_avatar);
                        storeImage(selectedBitmap);
                        uploadImgToQiNiu();
                    }
                    break;
            }
        }
    }


    private void uploadImgToQiNiu() {
        UploadManager uploadManager = new UploadManager();
        // 设置图片名字
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String key = "icon_" + sdf.format(new Date());
        String picPath = getOutputMediaFile().toString();
        Log.d("picPath: " , picPath);

        uploadManager.put(picPath, key, Auth.create(AccessKey, SecretKey).uploadToken("cardcampus"), new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject res) {
                // info.error中包含了错误信息，可打印调试
                // 上传成功后将key值上传到自己的服务器
                if (info.isOK()) {
                    Log.i(TAG, "token===" + Auth.create(AccessKey, SecretKey).uploadToken("photo"));
                    String headpicPath = "http://p81fp7gd5.bkt.clouddn.com/" + key;
                    Log.d("completePath: " ,headpicPath);
                    addAvatarToServer(headpicPath);

                }

            }
        }, null);
    }

    private void addAvatarToServer(String picPath) {
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();

        //创建表单请求体
        /**
         * key值与服务器端controller中request.getParameter中的key一致
         */

        RequestBody formBody = new FormBody.Builder()
                .add("user_sno", CurrentUserUtil.getCurrentUser().getUser_sno())
                .add("user_avatar",picPath)
                .build();
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(AVATARURL)
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


    //裁剪图片
    private void performCrop(Uri uri) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                grantUriPermission("com.android.camera", uri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("return-data", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFile().toString());
            startActivityForResult(intent, RESULT_CROP);
        } catch (ActivityNotFoundException anfe) {
            String errorMessage = "你的设备不支持裁剪行为！";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     *保存图像
     */

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions: ");
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
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
            homepageFragment = new HomepageFragment();
            replaceFragment(homepageFragment);
        } else if (id == R.id.nav_DaiDaiDai) {
            //getWindow().setTitle("代代代");
            insteadFragment = new InsteadFragment();
            replaceFragment(insteadFragment);
        } else if (id == R.id.nav_BaiShiTong) {
            //everythingFragment = new EverythingFragment();
            //replaceFragment(everythingFragment);
            BaiShiTongFragment baiShiTongFragment = new BaiShiTongFragment();
            replaceFragment(baiShiTongFragment);
        } else if (id == R.id.nav_BiaoBaiQiang) {
            lovewallFragment = new LovewallFragment();
            replaceFragment(lovewallFragment);
        } else if (id == R.id.nav_ErShouShu) {
            bookFragment = new BookFragment();
            replaceFragment(bookFragment);
        } else if(id == R.id.nav_MyPost){
            myPostFragment = new MyPostFragment();
            replaceFragment(myPostFragment);
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
