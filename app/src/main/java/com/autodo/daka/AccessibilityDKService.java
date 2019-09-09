package com.autodo.daka;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.alibaba.fastjson.JSON;
import com.autodo.MainActivity;
import com.autodo.StatusCode;
import com.autodo.capture.CaptureScreenService;
import com.autodo.chongding.ChongdingDataHandlder;
import com.autodo.logic.LogicHolder;
import com.autodo.lottery.MyOrders;
import com.autodo.lottery.OrderItem;
import com.autodo.lottery.SelectItem;
import com.autodo.mainjob.DialogHandler;
import com.autodo.mainjob.MainActivityHandler;
import com.autodo.mainjob.exception.CodeErrorException;
import com.autodo.mainjob.exception.TimeOutException;
import com.autodo.tools.HanderLoopHelper;
import com.autodo.tools.IComCallBacks;
import com.autodo.tools.LogUtils;
import com.autodo.utils.Tools;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class AccessibilityDKService extends AccessibilityService {


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        LogUtils.d(event.getClassName().toString());
        if (getRootInActiveWindow() == null) {
            return;
        }
        handEvent(event.getClassName().toString());
    }
    private void handEvent(String event) {
        new DaKaDataHandler(this).handle();
    }
    @Override
    public void onInterrupt() {

    }
}
