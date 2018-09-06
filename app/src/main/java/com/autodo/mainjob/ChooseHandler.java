package com.autodo.mainjob;

import android.accessibilityservice.AccessibilityService;

/**
 * //选择下单界面
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class ChooseHandler extends BaseHandler {
    public ChooseHandler(AccessibilityService service) {
        super(service);
    }

    @Override
    public boolean handle() {
        return true;
    }
}
