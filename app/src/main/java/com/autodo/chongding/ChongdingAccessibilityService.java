package com.autodo.chongding;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

import com.autodo.alipaycapture.SetTextHandler;
import com.autodo.jiakaocapture.CollectAllDataHandlder;
import com.autodo.tools.LogUtils;
import com.autodo.utils.Tools;

/**
 * <p>Created by Administrator on 2018/9/14.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class ChongdingAccessibilityService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        LogUtils.d(event.getClassName().toString());
        if (getRootInActiveWindow() == null) {
            return;
        }
        handEvent(event.getClassName().toString());
    }


    private void handEvent(String event) {
        switch (event) {
            case "com.handsgo.jiakao.android.practice_refactor.activity.PracticeActivity":

                Tools.sleep(2000);
                new CollectAllDataHandlder(this);
                break;
            case "0":
                break;
        }

    }

    @Override
    public void onInterrupt() {

    }
}
