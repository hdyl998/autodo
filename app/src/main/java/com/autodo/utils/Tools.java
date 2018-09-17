package com.autodo.utils;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.autodo.App;
import com.autodo.tools.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 一些工具
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class Tools {


    /***
     * 查找第一个控件通过文本
     * @param root
     * @param string
     * @return
     */
    public static AccessibilityNodeInfo findFirstByText(AccessibilityNodeInfo root, String string) {
        if (root == null) {
            LogUtils.d("findFirstByText", "root 为空");
            return null;
        }
        List<AccessibilityNodeInfo> lists = root.findAccessibilityNodeInfosByText(string);
        if (isListEmpty(lists)) {
            LogUtils.d("findFirstByText", "查找为空" + string);
            return null;
        }
        LogUtils.d("findFirstByText", "查找成功! (参数)" + string + "(实际)" + lists.get(0).getText());
        return lists.get(0);
    }


    /***
     * 查找第一个控件通过id
     * @param root
     * @param string
     * @return
     */
    public static AccessibilityNodeInfo findFirstById(AccessibilityNodeInfo root, String string) {
        if (root == null) {
            LogUtils.d("findFirstById", "root 为空");
            return null;
        }
        List<AccessibilityNodeInfo> lists = root.findAccessibilityNodeInfosByViewId(string);

        if (isListEmpty(lists)) {
            LogUtils.d("findFirstById", "查找为空id " + string);
            return null;
        }
        LogUtils.d("findFirstById", "查找成功id " + string);
        return lists.get(0);

    }

    /***
     * 列表是否为空
     * @param mList
     * @return
     */
    public static boolean isListEmpty(List<?> mList) {
        if (mList == null || mList.isEmpty()) {
            return true;
        }
        return false;
    }


    /***
     * 执行点击操作
     * @param info
     */
    public static void doClick(AccessibilityNodeInfo info) {
        if (info == null) {
            LogUtils.d("doClick", "info is null");
            return;
        }
        if (info.isClickable() && info.isEnabled()) {
            info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            doClick(info.getParent());//以防点击事件设置在了父类(为了扩大点击面积)
        }
    }

    /***
     * 休息时长
     * @param millis
     */
    public static void sleep(long millis) {
        SystemClock.sleep(millis);
    }


    /***
     *
     * 是否在一个场景里面
     * @param service
     * @param id
     * @return
     */
    public static boolean isInSceneById(AccessibilityService service, String id) {
        return service.getRootInActiveWindow() != null && Tools.findFirstById(service.getRootInActiveWindow(), id) != null;
    }


    /***
     *是否在一个场景里面
     * @param service
     * @param text
     * @return
     */
    public static boolean isInSceneByText(AccessibilityService service, String text) {
        return service.getRootInActiveWindow() != null && Tools.findFirstByText(service.getRootInActiveWindow(), text) != null;
    }


    public static void showToast(String msg) {
        Toast.makeText(App.getApp(), msg, 0).show();
    }


    private static final String editTextClassName = "android.widget.EditText";

    /**
     * 根据固定的类名循环查找到输入框,查找到目标输入框后即退出循环，适用于界面只有一个输入框的情况
     *
     * @param node
     * @return
     */
    public static AccessibilityNodeInfo recycleFindEditText(AccessibilityNodeInfo node) {
        //edittext下面必定没有子元素，所以放在此时判断
        if (node.getChildCount() == 0) {
            if (editTextClassName.equals(node.getClassName())) {
                return node;
            }
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                if (node.getChild(i) != null) {
                    AccessibilityNodeInfo result = recycleFindEditText(node.getChild(i));
                    if (result == null) {
                        continue;
                    } else {
                        return result;
                    }
                }
            }
        }
        return null;
    }

    public static void setText(AccessibilityNodeInfo nodeInfo, String text) {
        if (nodeInfo == null) {
            LogUtils.d("setText", "nodeinfo is null");
            return;
        }
        Bundle arguments = new Bundle();
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
    }


    public static void doClick(AccessibilityService service, String id) {
        doClick(findFirstById(service.getRootInActiveWindow(), id));
    }


    public static Bitmap captureScreen(Activity activity) {
        activity.getWindow().getDecorView().setDrawingCacheEnabled(true);
        Bitmap bmp = activity.getWindow().getDecorView().getDrawingCache();
        return bmp;
    }

    //判断当前界面显示的是哪个Activity
    public static String getTopActivity(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        Log.d("Chunna.zheng", "pkg:" + cn.getPackageName());//包名
        Log.d("Chunna.zheng", "cls:" + cn.getClassName());//包名加类名
        return cn.getClassName();
    }

    /***
     * 保存图片
     * @param mContext
     * @param bitmap
     */
    public static void saveBitmapAsFile(Context mContext, Bitmap bitmap, String tag) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(String.format("截图识别失败%s_yyyy_MM_dd_hh_mm_ss", tag));
            String strDate = dateFormat.format(new java.util.Date());
            String pathImage = Environment.getExternalStorageDirectory().getPath() + "/Pictures/";
            String nameImage = pathImage + strDate + ".png";
            File fileImage = new File(nameImage);
            if (!fileImage.exists()) {
                fileImage.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(fileImage);
            if (out != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(fileImage);
                media.setData(contentUri);
                mContext.sendBroadcast(media);
                Tools.showToast("screen image saved");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Tools.showToast("保存文件失败" + e);
        }
    }


    private static final String TAG = "Tools";

    public static void printRecycle(AccessibilityNodeInfo info) {
        if (info.getChildCount() == 0) {
            Log.i(TAG, "child widget----------------------------" + info.getClassName());
            Log.i(TAG, "showDialog:" + info.canOpenPopup());
            Log.i(TAG, "Text：" + info.getText());
            Log.i(TAG, "windowId:" + info.getWindowId());
            Log.i(TAG, "resId:" + info.getViewIdResourceName());
        } else {
            for (int i = 0; i < info.getChildCount(); i++) {
                if (info.getChild(i) != null) {
                    printRecycle(info.getChild(i));
                }
            }
        }
    }

    public static AccessibilityNodeInfo recycleFindNodeInfoById(AccessibilityNodeInfo info, String id) {
        AccessibilityNodeInfo infoResult = null;
        if (info.getChildCount() == 0) {
            if (id.equals(info.getViewIdResourceName())) {
                return info;
            }
        } else {
            int count = info.getChildCount();
            for (int i = 0; i < count; i++) {
                if (info.getChild(i) != null) {
                    infoResult = recycleFindNodeInfoById(info.getChild(i), id);
                    if (infoResult != null) {
                        return infoResult;
                    }
                }
            }
        }
        return infoResult;
    }

    public static AccessibilityNodeInfo recycleFindNodeInfoByText(AccessibilityNodeInfo info, String text) {
        AccessibilityNodeInfo infoResult = null;
        if (info.getChildCount() == 0) {
            if (text.equals(info.getText().toString())) {
                return info;
            }
        } else {
            int count = info.getChildCount();
            for (int i = 0; i < count; i++) {
                if (info.getChild(i) != null) {
                    infoResult = recycleFindNodeInfoByText(info.getChild(i), text);
                    if (infoResult != null) {
                        return infoResult;
                    }
                }
            }
        }
        return infoResult;
    }
}
