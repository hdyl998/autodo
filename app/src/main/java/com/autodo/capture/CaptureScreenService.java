package com.autodo.capture;


import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.autodo.App;
import com.autodo.R;
import com.autodo.utils.Tools;
import com.autodo.lottery.MyOrders;
import com.autodo.qrcode.ErweimaUtils;
import com.autodo.tools.LogUtils;
import com.autodo.utils.CalcTime;

import java.nio.ByteBuffer;

/***
 * 截图服务线程
 */
public class CaptureScreenService extends Service {

    private LinearLayout mFloatLayout = null;
    private WindowManager.LayoutParams wmParams = null;
    private WindowManager mWindowManager = null;
    private LayoutInflater inflater = null;
    private View mFloatView = null;
    private TextView mStopResume = null;

    private static final String TAG = "CaptureScreenService";
//
//    private SimpleDateFormat dateFormat = null;
//    private String strDate = null;
//    private String pathImage = null;
//    private String nameImage = null;

    private MediaProjection mMediaProjection = null;
    private VirtualDisplay mVirtualDisplay = null;

    public int mResultCode = 0;
    public Intent mResultData = null;
    public MediaProjectionManager mMediaProjectionManager1 = null;

    private WindowManager mWindowManager1 = null;
    private int windowWidth = 0;
    private int windowHeight = 0;
    private ImageReader mImageReader = null;
    private DisplayMetrics metrics = null;
    private int mScreenDensity = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtils.d(TAG, "onCreate");
        createFloatView();
        createVirtualEnvironment();

        PowerManager powerManager = (PowerManager) this.getSystemService(this.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        wakeLock.acquire();
    }

    PowerManager.WakeLock wakeLock = null;


    public static boolean isPause = false;


    private void createFloatView() {
        wmParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.x = 0;
        wmParams.y = 0;
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        inflater = LayoutInflater.from(getApplication());
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);
        mWindowManager.addView(mFloatLayout, wmParams);
        mFloatView = mFloatLayout.findViewById(R.id.button);
        mStopResume = mFloatLayout.findViewById(R.id.btnStopResume);
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        mFloatView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                wmParams.x = (int) event.getRawX() - mFloatView.getMeasuredWidth() / 2;
                wmParams.y = (int) event.getRawY() - mFloatView.getMeasuredHeight() / 2 - 25;
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                return false;
            }
        });


        mStopResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPause = !isPause;
                mStopResume.setText(isPause ? "继续" : "暂停");
            }
        });
        mStopResume.setText(isPause ? "继续" : "暂停");

        mFloatView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mFloatLayout.setVisibility(View.INVISIBLE);
                startVirtual();
                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    public void run() {
                        //capture the screen
                        startCapture();
                    }
                }, 1500);
            }
        });

        Log.i(TAG, "created the float sphere view");
    }

    private void createVirtualEnvironment() {
//        dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
//        strDate = dateFormat.format(new java.util.Date());
//        pathImage = Environment.getExternalStorageDirectory().getPath() + "/Pictures/";
//        nameImage = pathImage + strDate + ".png";
        mMediaProjectionManager1 = (MediaProjectionManager) getApplication().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        mWindowManager1 = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        windowWidth = mWindowManager1.getDefaultDisplay().getWidth();
        windowHeight = mWindowManager1.getDefaultDisplay().getHeight();
        metrics = new DisplayMetrics();
        mWindowManager1.getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
        mImageReader = ImageReader.newInstance(windowWidth, windowHeight, 0x1, 2); //ImageFormat.RGB_565
        Log.i(TAG, "prepared the virtual environment");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startVirtual() {
        if (mMediaProjection != null) {
            Log.i(TAG, "want to display virtual");
            virtualDisplay();
        } else {
            Log.i(TAG, "start screen capture intent");
            Log.i(TAG, "want to build mediaprojection and display virtual");
            setUpMediaProjection();
            virtualDisplay();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setUpMediaProjection() {
        mResultData = ((App) getApplication()).getIntent();
        mResultCode = ((App) getApplication()).getResult();
        mMediaProjectionManager1 = ((App) getApplication()).getMediaProjectionManager();
        mMediaProjection = mMediaProjectionManager1.getMediaProjection(mResultCode, mResultData);
        Log.i(TAG, "mMediaProjection defined");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void virtualDisplay() {
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                windowWidth, windowHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);
        Log.i(TAG, "virtual displayed");
    }

    /***
     * 对图片进行裁剪,获得二维码部分
     * @param bitmapSource
     */
    private Bitmap handleBitmap(Bitmap bitmapSource) {
        Rect rect = pictureRect;
        if (rect == null) {
            return bitmapSource;
        }
        Bitmap bitmapQrcode = Bitmap.createBitmap(rect.width(), rect.height(), bitmapSource.getConfig());
        Canvas canvas = new Canvas(bitmapQrcode);
        canvas.drawBitmap(bitmapSource, rect, new RectF(0, 0, rect.width(), rect.height()), null);
        return bitmapQrcode;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startCapture() {
        Image image = mImageReader.acquireLatestImage();
        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        image.close();
        Log.i(TAG, "image data captured");
        if (bitmap != null) {
//            bitmap = handleBitmap(bitmapSource);
            CalcTime calcTime = new CalcTime();
            String data = ErweimaUtils.parseQRcodeBitmap(bitmap);
            calcTime.printResult("识别耗时");
            Tools.showToast(data);
            LogUtils.d(TAG, "code=" + data);
            mFloatLayout.setVisibility(View.VISIBLE);
            //二维码
            if (MyOrders.getFirstOrderItem() != null)
                MyOrders.getFirstOrderItem().setQrCode(data);
            if (data == null) {
                Tools.saveBitmapAsFile(getApplicationContext(), bitmap, stringTag);
            }
            QRCodeString = data;
            isOK = true;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void tearDownMediaProjection() {
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
        Log.i(TAG, "mMediaProjection undefined");
    }

    private void stopVirtual() {
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        mVirtualDisplay = null;
        Log.i(TAG, "virtual display stopped");
    }

    @Override
    public void onDestroy() {
        // to remove mFloatLayout from windowManager
        super.onDestroy();
        if (mFloatLayout != null) {
            mWindowManager.removeView(mFloatLayout);
        }
        tearDownMediaProjection();
        Log.i(TAG, "application destroy");
        stopVirtual();

        wakeLock.release();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d(TAG, "onStartCommand" + (intent == null));
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_CATCH_PICTURE:
                    if (mFloatView != null)
                        mFloatView.performClick();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public final static String ACTION_CATCH_PICTURE = "action_key";


    public static void doCapture(Context mContext, Rect rect) {
        isOK = false;
        pictureRect = rect;
        Intent intent = new Intent(mContext, CaptureScreenService.class);
        intent.setAction(ACTION_CATCH_PICTURE);
        mContext.startService(intent);
        LogUtils.d(TAG, "doCapture");
    }

    public static void doCapture(Context mContext, String tag) {
        isOK = false;
        stringTag = tag;
        Intent intent = new Intent(mContext, CaptureScreenService.class);
        intent.setAction(ACTION_CATCH_PICTURE);
        mContext.startService(intent);
        LogUtils.d(TAG, "doCapture");
    }

    private static Rect pictureRect;

    public static boolean isOK = false;

    public static String QRCodeString;//二维码字符串


    public static String getQRCodeString() {
        return QRCodeString;
    }

    public static String stringTag = "";//用于设置为什么出现问题,好截图看到情况说明


    public static void setStringTag(String stringTag) {
        CaptureScreenService.stringTag = stringTag;
    }
}