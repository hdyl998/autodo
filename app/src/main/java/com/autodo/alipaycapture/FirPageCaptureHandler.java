package com.autodo.alipaycapture;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.autodo.App;
import com.autodo.capture.CaptureScreenService;
import com.autodo.mainjob.BaseHandler;
import com.autodo.tools.LogUtils;
import com.autodo.utils.Tools;

/**
 * <p>Created by Administrator on 2018/9/17.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class FirPageCaptureHandler extends BaseHandler {
    public FirPageCaptureHandler(AccessibilityService service) {
        super(service);
    }

    @Override
    public boolean handle() {
        LogUtils.d("执行个人收钱界面");
        AccessibilityNodeInfo nodeInfo = findFirstByText("设置金额");
        if (nodeInfo != null) {
            Tools.doClick(nodeInfo);//去第二个页面
        } else {
            AccessibilityNodeInfo nodeInfoClearMoney = findFirstByText("清除金额");
            if (nodeInfoClearMoney != null) {
                CaptureScreenService.doCapture(App.getApp(), AlipayDatas.getInstance().getCurrentItem().money);
                //截图,收集信息
                //之后点击
                new Thread() {
                    @Override
                    public void run() {
                        int weatingMaxTime = 10000;
                        while (!CaptureScreenService.isOK) {
                            Tools.sleep(500);
                            weatingMaxTime -= 500;
                            if (weatingMaxTime < 0) {
                                break;
                            }
                        }
                        if (weatingMaxTime < 0) {
                            LogUtils.d("截图超时,终止程序!");
                        } else {
                            //设置好二维码
                            if (AlipayDatas.getInstance().getCurrentItem() != null) {
                                AlipayDatas.getInstance().getCurrentItem().url = CaptureScreenService.getQRCodeString();
                                if (!TextUtils.isEmpty(AlipayDatas.getInstance().getCurrentItem().url)) {
                                    AlipayDatas.getInstance().saveNewData();
                                }
                                LogUtils.d("截图成功,URL是" + AlipayDatas.getInstance().getCurrentItem().url);
                            }
                            if (AlipayDatas.getInstance().nextItem()) {
                                Tools.doClick(nodeInfoClearMoney);
                                Tools.sleep(100);
                                AccessibilityNodeInfo nodeInfo = findFirstByText("设置金额");
                                if (nodeInfo != null) {
                                    Tools.doClick(nodeInfo);//去第二个页面
                                }
                            } else {
                                LogUtils.d("任务完成!");
                            }
                        }
                    }
                }.start();
            }
        }
        return false;
    }
}
