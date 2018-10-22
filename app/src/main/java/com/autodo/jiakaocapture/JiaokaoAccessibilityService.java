package com.autodo.jiakaocapture;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

import com.autodo.alipaycapture.SetTextHandler;
import com.autodo.chongding.ChongdingDataHandlder;
import com.autodo.tools.LogUtils;

import java.util.HashSet;

/**
 * <p>Created by Administrator on 2018/9/14.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class JiaokaoAccessibilityService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        LogUtils.d(event.getClassName().toString());
        if (getRootInActiveWindow() == null) {
            return;
        }
        handEvent(event.getClassName().toString());
    }

    SetTextHandler handler;
    HashSet<String> hashSet = new HashSet<>();


    private void handEvent(String event) {
        new ChongdingDataHandlder(this,hashSet).handle();

    }

    @Override
    public void onInterrupt() {

    }
}
