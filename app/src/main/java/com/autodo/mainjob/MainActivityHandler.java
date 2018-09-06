package com.autodo.mainjob;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

import com.autodo.Tools;

/**
 * 主界面的处理
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class MainActivityHandler extends BaseHandler {
    public MainActivityHandler(AccessibilityService service) {
        super(service);
    }

    @Override
    public boolean handle() {
        AccessibilityNodeInfo nodeInfo = findFirstByText("竞彩足球");
        if (nodeInfo != null) {
            Tools.doClick(nodeInfo);
        }
        return true;
    }


}
