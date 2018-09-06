package com.autodo;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.ListView;
import android.widget.TextView;

import com.autodo.adapter.BaseViewHolder;
import com.autodo.adapter.SuperAdapter;
import com.autodo.capture.CaptureMainActivity;
import com.autodo.capture.CaptureScreenService;
import com.autodo.qrcode.ErweimaUtils;
import com.autodo.tools.LogUtils;
import com.autodo.utils.SystemUtils;
import com.autodo.utils.bufferknife.MyBindView;
import com.autodo.utils.bufferknife.MyBufferKnifeUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatActivity mContext;


    ListView listView;
    SuperAdapter adapter;

    public final static List<String> listStrings = new ArrayList<>();

    @MyBindView(R.id.btnOpen)
    TextView tvOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        MyBufferKnifeUtils.inject(mContext);

        listView = findViewById(R.id.listView);

        startCapture();
        checkAccessiblityCPServiceEnable();

        tvOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccessibility();
            }
        });

        findViewById(R.id.startCapture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCapture();
            }
        });

        /**
         * 停止服务
         */
        findViewById(R.id.stopCapture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(mContext, CaptureScreenService.class));
            }
        });
        //选择二维码进行识别
        findViewById(R.id.choosePicture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooserImpl();
            }
        });

        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
            }
        });

        listView.setAdapter(adapter = new SuperAdapter<String>(mContext, listStrings, R.layout.fragment1) {
            @Override
            protected void onBind(BaseViewHolder holder, String item, int position) {
                holder.setText(R.id.textView, item);
            }
        });
    }

    /***
     * 检查辅助功能已打开
     */
    private void checkAccessiblityCPServiceEnable() {
        LogUtils.d(AccessibilityCPService.class.getName());
        if (SystemUtils.isAccessiblilityServiceEnabled(mContext, AccessibilityCPService.class.getName())) {
            tvOpen.setText("辅助功能已打开");
        } else {
            tvOpen.setText("(->去打开)辅助功能");
        }
    }

    /***
     * 启动截屏服务
     */
    private void startCapture() {
        if (!SystemUtils.isServiceRunning(mContext, CaptureScreenService.class.getName())) {
            if (Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT) {
                Tools.showToast("正在启动截屏服务");
                startActivity(new Intent(mContext, CaptureMainActivity.class));
            } else {
                Tools.showToast("该手机版本太低不支持截屏" + Build.VERSION.SDK_INT);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (adapter.getAllData().size() > 500) {
            adapter.clear();
        }
        adapter.notifyDataSetChanged();
        checkAccessiblityCPServiceEnable();
    }

    @Override
    public void onClick(View v) {
    }


    private static final String TAG = "MainActivity";


    private void openAccessibility() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_CHOOSER_RESULT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    String[] proj = new String[]{MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(data.getData(), proj, null, null, null);
                    String imgPath = null;
                    if (cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        System.out.println(columnIndex);
                        //获取到用户选择的二维码图片的绝对路径
                        imgPath = cursor.getString(columnIndex);
                    }
                    cursor.close();

                    Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                    LogUtils.d(TAG, "图片大小是:width: " + bitmap.getWidth() + "height: " + bitmap.getHeight());
                    String ttt = ErweimaUtils.parseQRcodeBitmap(bitmap);
                    LogUtils.d(TAG, "二维码识别结果是: " + ttt);
                }
                break;
        }

    }


    private void openFileChooserImpl() {
        Intent intentA = new Intent(Intent.ACTION_PICK);
        intentA.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mContext.startActivityForResult(intentA, FILE_CHOOSER_RESULT_CODE);
    }

    private final int FILE_CHOOSER_RESULT_CODE = 1213;// 表单的结果回调
}
