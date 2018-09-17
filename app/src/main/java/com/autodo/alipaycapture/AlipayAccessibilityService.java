package com.autodo.alipaycapture;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.autodo.tools.LogUtils;
import com.autodo.utils.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Created by Administrator on 2018/9/14.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class AlipayAccessibilityService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        LogUtils.d(event.getClassName().toString());
        if (getRootInActiveWindow() == null) {
            return;
        }
        handEvent(event.getClassName().toString());
    }

    SetTextHandler handler;

    private void handEvent(String event) {
        switch (event) {
            case "com.alipay.mobile.payee.ui.PayeeQRActivity":
                new FirPageCaptureHandler(AlipayAccessibilityService.this);
                break;
            case "com.alipay.mobile.payee.ui.PayeeQRSetMoneyActivity":
                if (handler == null) {
                    handler = new SetTextHandler(this);
                } else {
                    handler.handle();
                }
                break;
        }

    }

    @Override
    public void onInterrupt() {

    }
}
