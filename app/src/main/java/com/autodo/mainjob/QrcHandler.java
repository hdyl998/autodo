package com.autodo.mainjob;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

import com.autodo.App;
import com.autodo.utils.Tools;
import com.autodo.capture.CaptureScreenService;
import com.autodo.tools.LogUtils;

/**
 * 二维码识别场景,需要在子线程中处理,防止占用截图的主线程资源
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class QrcHandler extends BaseHandler {
    public QrcHandler(AccessibilityService service) {
        super(service);
    }


    private static final String TAG = "QrcHandler";

    @Override
    public boolean handle() {
        AccessibilityNodeInfo moneyNode = findFirstById("com.other:id/money_num");
        String strMoney = moneyNode.getText() + "";
        strMoney = strMoney.substring(1, strMoney.length() - 1);

        int money = Integer.parseInt(strMoney);
        LogUtils.d(TAG, "all money" + money);

        AccessibilityNodeInfo nodeInfo = findFirstById("com.other:id/detail");
        String strInfo = nodeInfo.getText() + "";
        int indexGong = strInfo.indexOf("共");
        int indexZhang = strInfo.indexOf("张");
        int indexZhu = strInfo.indexOf("注");
        int indexBei = strInfo.indexOf("倍");
        int zhang = Integer.parseInt(strInfo.substring(indexGong + 1, indexZhang));
        int zhu = Integer.parseInt(strInfo.substring(indexZhang + 1, indexZhu));
        int bei = Integer.parseInt(strInfo.substring(indexZhu + 1, indexBei));

        LogUtils.d(TAG, "zhang" + zhang);

        LogUtils.d(TAG, "zhu" + zhu);

        LogUtils.d(TAG, "bei" + bei);


        AccessibilityNodeInfo picNode = findFirstById("com.other:id/qrCode");
        //获得在屏幕上的位置
        Rect rect = new Rect();
        picNode.getBoundsInScreen(rect);

        //放大部分像素
        rect.left -= 5;
        rect.right += 5;
        rect.top -= 5;
        rect.bottom += 5;
        //执行截屏
        CaptureScreenService.doCapture(App.getApp());
        //等待截图完成
        new Thread() {
            int weatingMaxTime = 10000;

            @Override
            public void run() {
                while (!CaptureScreenService.isOK) {

                    Tools.sleep(500);
                    weatingMaxTime -= 500;

                    if (weatingMaxTime <= 0) {
                        break;
                    }
                }
                Tools.doClick(root.getChild(0));
                Tools.sleep(1000);
            }
        }.start();
        return true;
    }
}
