package com.autodo.mainjob;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

import com.autodo.utils.Tools;

/**
 * 对话框处理器
 * <p>Created by Administrator on 2018/9/6.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class DialogHandler extends BaseHandler {
    public DialogHandler(AccessibilityService service) {
        super(service);
    }

    @Override
    public boolean handle() {
        AccessibilityNodeInfo ok = Tools.recycleFindNodeInfoById(root, "cn.gov.lottery:id/tv_confirm");
        Tools.doClick(ok);
        return true;
    }
}