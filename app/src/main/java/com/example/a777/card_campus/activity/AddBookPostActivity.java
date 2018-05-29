package com.example.a777.card_campus.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a777.card_campus.R;
import com.example.a777.card_campus.qiniuUtils.Auth;
import com.example.a777.card_campus.util.AnimatorUtil;
import com.example.a777.card_campus.util.CurrentUserUtil;
import com.example.a777.card_campus.util.RandomIDUtil;
import com.example.a777.card_campus.util.ToastUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class AddBookPostActivity extends AppCompatActivity{
    private static String AccessKey = "HBckSDRko17AS-s_Ufbb29bYfFMKMV7opdRnx-2C";//此处填你自己的AccessKey
    private static String SecretKey = "grNgIr009LWhQyfGvOGua8CPWFmlqfhySioKTrdk";//此处填你自己的SecretKey
    private static String linkWayURL="http://10.0.2.2:8080/Card-Campus-Server/insertLinkWay";
    private static final String TAG = "MainActivity";
    private static String addBookPostURL="http://10.0.2.2:8080/Card-Campus-Server/addBookPost";
    private ImageButton imgbt_addBookImg_gallery;
    private ImageButton imgbt_addBookImg_camera;
    private ImageButton imgbt_BookImg;
    private Uri imageUri;
    private static final int REQUEST_CAPTURE = 2;
    private static final int REQUEST_PICTURE = 5;
    private static final int RESULT_CROP = 7;
    private static final int GALLERY_ACTIVITY_CODE = 9;
    private Button sendBookPost;
    private Button back;
    //private String BookImageUrl;
    private Button fromCarame;
    private Button fromGarllary;
    private Button upload;

    private boolean isSet = false;

    private EditText et_booktitle,et_bookcontent,et_userqq,et_usertel;

    private Uri localUri = null;
    String BookImageUrl;
    String preBookImageUrl="test";
    String content;
    String title;
    String qq;
    String tel;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            BookImageUrl = (String)msg.obj;

            content = et_bookcontent.getText().toString().trim();
            title = et_booktitle.getText().toString().trim();
            qq = et_userqq.getText().toString().trim();
            tel = et_usertel.getText().toString().trim();
            //ToastUtil.createToast(getApplicationContext(),BookImageUrl+content+title+qq+tel);

            if(qq.equals("")&&tel.equals("")||BookImageUrl.equals("")){
                Toast.makeText(getApplicationContext(),"请输入联系方式",Toast.LENGTH_LONG).show();
            }else{
                /**
                 * 用于查找用户表中是否已经填写联系方式
                 */
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("user_sno",CurrentUserUtil.getCurrentUser().getUser_sno())
                        .add("user_qq",qq)
                        .add("user_tel",tel)
                        .build();

                //创建一个请求对象
                Request request = new Request.Builder()
                        .url(linkWayURL)
                        .post(formBody)
                        .build();
                /**
                 * Get的异步请求，不需要跟同步请求一样开启子线程
                 * 但是回调方法还是在子线程中执行的
                 * 所以要用到Handler传数据回主线程更新UI
                 */
                okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
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


                if(isSet==false){
                    ToastUtil.createToast(getApplicationContext(),"记得添加二手书实物照片哦~");
                }else{
                    addBookPostToServer();
                    Toast.makeText(getApplicationContext(),"发布成功",Toast.LENGTH_SHORT).show();
                    finish();
                }


            }




            super.handleMessage(msg);
        }
    };


    // 重用uploadManager。一般地，只需要创建一个uploadManager对象
    // UploadManager uploadManager = new UploadManager(config);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book_post);
        getSupportActionBar().hide();
        imgbt_BookImg = (ImageButton)findViewById(R.id.imgbt_BookImg);
        imgbt_addBookImg_gallery = (ImageButton)findViewById(R.id.imgbt_addBookImg_gallery);
        imgbt_addBookImg_camera = (ImageButton)findViewById(R.id.imgbt_addBookImg_camera);
        sendBookPost = (Button)findViewById(R.id.bt_sendbookpost);
        et_booktitle = (EditText)findViewById(R.id.edit_booktitle);
        et_bookcontent = (EditText)findViewById(R.id.edit_bookcontent);
        et_userqq = (EditText)findViewById(R.id.bookpost_edit_qq);
        et_usertel = (EditText)findViewById(R.id.bookpost_edit_phone);
        back = (Button)findViewById(R.id.addBook_back);
        //avatar = (ImageView) findViewById(R.id.avatar);
        //fromCarame = (Button) findViewById(R.id.carame);
        //fromGarllary = (Button) findViewById(R.id.select_img);
        //upload = (Button) findViewById(R.id.upload_img);


        imgbt_addBookImg_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery_Intent = new Intent(Intent.ACTION_PICK, null);
                gallery_Intent.setType("image/*");
                startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
            }
        });

        imgbt_addBookImg_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });


        sendBookPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImg2QiNiu();
                //ToastUtil.createToast(getApplicationContext(),BookImageUrl);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        methodRequiresTwoPermission();
    }

    @AfterPermissionGranted(1)//添加注解，是为了首次执行权限申请后，回调该方法
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            //已经申请过权限，直接调用相机
            // openCamera();
        } else {
            EasyPermissions.requestPermissions(this, "需要获取权限",
                    1, perms);
        }
    }

    private void openCamera() {  //调用相机拍照
        Intent intent = new Intent();
        File file = getOutputMediaFile(); //工具类稍后会给出
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {  //针对Android7.0，需要通过FileProvider封装过的路径，提供给外部调用
            imageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);//通过FileProvider创建一个content类型的Uri，进行封装
        } else { //7.0以下，如果直接拿到相机返回的intent值，拿到的则是拍照的原图大小，很容易发生OOM，所以我们同样将返回的地址，保存到指定路径，返回到Activity时，去指定路径获取，压缩图片

            imageUri = Uri.fromFile(file);

        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, REQUEST_CAPTURE);//启动拍照
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAPTURE:
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
                        // avatar.setImageBitmap(mBitmap);
                        // storeImage(mBitmap);
                    } else {
                        imgbt_BookImg.setImageBitmap(selectedBitmap);
                        isSet = true;
                        //avatar.setImageBitmap(selectedBitmap);
                        storeImage(selectedBitmap);
                    }
                    break;
                case GALLERY_ACTIVITY_CODE:

                    localUri = data.getData();
                    //  setBitmap(localUri);
                    performCrop(localUri);
                    break;
            }
        }
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

    //建立保存头像的路径及名称
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

    //保存图像
    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
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


    private void uploadImg2QiNiu() {
        UploadManager uploadManager = new UploadManager();
        // 设置图片名字
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String key = "icon_" + sdf.format(new Date());
        String picPath = getOutputMediaFile().toString();
        Log.i(TAG, "picPath: " + picPath);
        uploadManager.put(picPath, key, Auth.create(AccessKey, SecretKey).uploadToken("cardcampus"), new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject res) {
                // info.error中包含了错误信息，可打印调试
                // 上传成功后将key值上传到自己的服务器
                if (info.isOK()) {
                    Log.i(TAG, "token===" + Auth.create(AccessKey, SecretKey).uploadToken("photo"));
                    String headpicPath = "http://p81fp7gd5.bkt.clouddn.com/" + key;

                    //这里居然开了个子线程噢，那就通过handler传递数据到主线程
                    Message msg = new Message();
                    msg.obj = headpicPath ;
                    handler.sendMessage(msg);
                    Log.i(TAG, "complete: " + headpicPath);
                }

            }
        }, null);
    }

    private void addBookPostToServer() {
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();

        //创建表单请求体
        /**
         * key值与服务器端controller中request.getParameter中的key一致
         */

        RequestBody formBody = new FormBody.Builder()
                .add("book_id", RandomIDUtil.getID())
                .add("book_img",BookImageUrl)
                .add("user_sno", CurrentUserUtil.getCurrentUser().getUser_sno())
                .add("book_describe",content)
                .add("book_title",title)
                .add("book_time",String.valueOf(System.currentTimeMillis()))
                .add("is_sold",String.valueOf(0))
                .build();



        //创建一个请求对象
        Request request = new Request.Builder()
                .url(addBookPostURL)
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

}
