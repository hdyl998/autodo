package com.autodo.mainjob;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

import com.autodo.utils.Tools;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public abstract class BaseHandler {

    public AccessibilityService service;
    public AccessibilityNodeInfo root;

    boolean isResultOK;

    public BaseHandler(AccessibilityService service) {
        this.service = service;
        root = service.getRootInActiveWindow();
        isResultOK = handle();
    }

    public boolean isResultOK() {
        return isResultOK;
    }

    public abstract boolean handle();


    public final AccessibilityNodeInfo findFirstById(String string) {
        return Tools.findFirstById(root, string);
    }

    public final AccessibilityNodeInfo findFirstByText(String string) {
        return Tools.findFirstByText(root, string);
    }

    public final void sleep(long millis) {
        Tools.sleep(millis);
    }
}
