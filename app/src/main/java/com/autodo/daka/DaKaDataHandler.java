package com.autodo.daka;

import android.accessibilityservice.AccessibilityService;

import com.autodo.mainjob.BaseHandler;

/**
 * <p>文件描述：<p>
 * <p>作者：HanDong<p>
 * <p>创建时间：2019/9/9<p>
 * <p>更改时间：2019/9/9<p>
 * <p>版本号：1<p>
 */
public class DaKaDataHandler extends BaseHandler {
    public DaKaDataHandler(AccessibilityService service) {
        super(service);
    }

    @Override
    public boolean handle() {
        return false;
    }
}
