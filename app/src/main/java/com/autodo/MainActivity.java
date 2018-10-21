package com.autodo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.autodo.adapter.BaseViewHolder;
import com.autodo.adapter.SuperAdapter;
import com.autodo.alipaycapture.DataSaveUtils;
import com.autodo.capture.CaptureMainActivity;
import com.autodo.capture.CaptureScreenService;
import com.autodo.jiakaocapture.QuestionItem;
import com.autodo.lottery.OrderItem;
import com.autodo.lottery.LotteryJSONBean;
import com.autodo.lottery.MyOrders;
import com.autodo.qrcode.ErweimaUtils;
import com.autodo.socket.SocketMessageListener;
import com.autodo.tools.AppOpsUtils;
import com.autodo.tools.LogUtils;
import com.autodo.utils.SuSystemUtils;
import com.autodo.utils.SystemUtils;
import com.autodo.utils.Tools;
import com.autodo.utils.bufferknife.MyBindView;
import com.autodo.utils.bufferknife.MyBufferKnifeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static boolean isOnBackground = false;


    AppCompatActivity mContext;


    ListView listView;
    SuperAdapter adapter;

    public final List<String> listStrings = new ArrayList<>();

    @MyBindView(R.id.btnOpen)
    TextView tvOpen;

    @MyBindView(R.id.startCaptureService)
    TextView tvCaptureService;

    @MyBindView(R.id.stopCapture)
    TextView tvStopCapture;


    @MyBindView(R.id.startCapture)
    TextView tvStartCapture;

    @MyBindView(R.id.tvSocketStatus)
    TextView tvSocketStatus;

    MySocket mySocket = MySocket.getInstance();




    @Override
    protected void onDestroy() {
        super.onDestroy();
        mySocket.onDestory();
        isOnBackground = true;
    }


    private SocketMessageListener listener = new SocketMessageListener() {
        @Override
        public void onServerMessage(String event, String data) throws Exception {
            addLogUI(String.format("event" + event + "data" + data));

            switch (event) {
                case MySocket.LISTEN_KEY_HANDLEREPLY://已经处理完成了
                    JSONObject jsonObject = JSON.parseObject(data);
                    int statusCode = jsonObject.getIntValue("code");
                    String message = jsonObject.getString("message");
                    String pid = jsonObject.getJSONObject("data").getString("pid");
                    // TODO: 2018/9/11
                    break;
                case MySocket.LISTEN_KEY_DATA:
                    LotteryJSONBean jsonBean = JSON.parseObject(data, LotteryJSONBean.class);
                    OrderItem orderItem = new OrderItem(jsonBean);
                    MyOrders.addOrderItem(orderItem);
                    break;
            }
        }

        @Override
        public void onLocalMessageConnect() throws Exception {
            super.onLocalMessageConnect();
            tvSocketStatus.setText("socket连接成功!");
            Tools.showToast("socket连接成功!");
        }

        @Override
        public void onLocalMessageTimeOut() throws Exception {
            super.onLocalMessageTimeOut();
            tvSocketStatus.setText("socketTimeOut!");
        }

        @Override
        public void onLocalMessageError() throws Exception {
            super.onLocalMessageError();
            tvSocketStatus.setText("socketError!");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        mContext = this;
        MyBufferKnifeUtils.inject(mContext);
        listView = findViewById(R.id.listView);

        findViewById(R.id.btnOpen).setOnClickListener(this);

        findViewById(R.id.startCaptureService).setOnClickListener(this);

        findViewById(R.id.startCapture).setOnClickListener(this);
        findViewById(R.id.stopCapture).setOnClickListener(this);
        //选择二维码进行识别
        findViewById(R.id.choosePicture).setOnClickListener(this);

        findViewById(R.id.clear).setOnClickListener(this);

        findViewById(R.id.superUser).setOnClickListener(this);

        listView.setAdapter(adapter = new SuperAdapter<String>(mContext, listStrings, R.layout.fragment1) {
            @Override
            protected void onBind(BaseViewHolder holder, String item, int position) {
                holder.setText(R.id.textView, item);
            }
        });
        mySocket.addOnGetSocketDataListener(listener);
        mySocket.startSocket();
        startCaptureService();
        test();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOpen:
                openAccessibility();
                break;
            case R.id.startCaptureService:
                startCaptureService();
                break;
            case R.id.startCapture:
                CaptureScreenService.doCapture(mContext);
                break;
            case R.id.stopCapture:
                stopService(new Intent(mContext, CaptureScreenService.class));
                //延时检查
                listView.postDelayed(() -> checkCapturServiceEnable(), 100);
                break;
            case R.id.choosePicture:
                openFileChooserImpl();
                break;
            case R.id.clear:
                adapter.clear();
                break;
            case R.id.superUser:
                SuSystemUtils.execShellCmd("input keyevent 3");//home
                break;


        }
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

    private void checkCapturServiceEnable() {
        if (SystemUtils.isServiceRunning(mContext, CaptureScreenService.class)) {
            tvCaptureService.setText("截图服务已打开");
            tvStopCapture.setVisibility(View.VISIBLE);
            tvStartCapture.setVisibility(View.VISIBLE);
        } else {
            tvCaptureService.setText("(->去打开)截图服务");
            tvStopCapture.setVisibility(View.GONE);
            tvStartCapture.setVisibility(View.GONE);
        }
    }


    /***
     * 启动截屏服务
     */
    private void startCaptureService() {
        if (!AppOpsUtils.checkFloatPermission(mContext)) {
            Tools.showToast("没有悬浮窗权限,请在设置中打开悬浮窗权限");
            return;
        }
        if (!SystemUtils.isServiceRunning(mContext, CaptureScreenService.class)) {
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
        checkAccessiblityCPServiceEnable();
        checkCapturServiceEnable();
        isOnBackground = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnBackground = true;
    }

    /**
     * 增加一条log
     *
     * @param data
     */
    private void addLogUI(String data) {
        if (listStrings.size() > 100) {
            listStrings.clear();
        }
        listStrings.add(0, data);
        adapter.notifyDataSetChanged();
    }


    private static final String TAG = "MainActivity";


    private void openAccessibility() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
                    String ttt;
                    LogUtils.d(TAG, ttt = "图片大小是:width: " + bitmap.getWidth() + "height: " + bitmap.getHeight());
                    addLogUI(ttt);

                    String ttt2 = ErweimaUtils.parseQRcodeBitmap(bitmap);
                    LogUtils.d(TAG, ttt = "二维码识别结果是: " + ttt2);
                    addLogUI(ttt);
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

    private void test() {
//        System.out.println(ttt);

//        String string = null;
//        ContentResolver contentResolver = getContentResolver();
//        Cursor cursor = contentResolver.query(Uri.parse("content://browser/bookmarks"), new String[]{"url"}, null, null, null);
//        while (cursor != null && cursor.moveToNext()) {
//            string = cursor.getString(cursor.getColumnIndex("url"));
//            Log.d("debug", string == null ? "null" : string);
//        }
    }

}
