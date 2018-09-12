package com.autodo.mainjob;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

import com.autodo.utils.Tools;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class LoginHandler extends BaseHandler {
    public LoginHandler(AccessibilityService service) {
        super(service);
    }

    private final static String MOBILE = "15310872381";
    private final static String PWD = "141916lgdon";

    @Override
    public boolean handle() {
        AccessibilityNodeInfo mobile = Tools.findFirstById(root, "com.help:id/et_login_phone");
        Tools.setText(mobile, MOBILE);
        AccessibilityNodeInfo pwd = Tools.findFirstById(root, "com.help:id/et_login_password");
        Tools.setText(pwd, PWD);
        //点击确定
        Tools.doClick(service, "com.help:id/tv_login");
        Tools.showToast("登录中");
        return true;
    }
}
