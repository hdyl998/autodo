package com.autodo.alipaycapture;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

import com.autodo.mainjob.BaseHandler;
import com.autodo.tools.LogUtils;
import com.autodo.utils.Tools;

/**
 * <p>Created by Administrator on 2018/9/17.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class SetTextHandler extends BaseHandler {


    public SetTextHandler(AccessibilityService service) {
        super(service);
    }


    @Override
    public boolean handle() {
        resetRoot();
        PayInfoItem infoItem = AlipayDatas.getInstance().getCurrentItem();
        if (infoItem == null) {
            return false;
        }
        LogUtils.d(String.format("当前处理第%d个,共%d个,还有%d个,", AlipayDatas.getInstance().mCurrent, AlipayDatas.getInstance().listInfoItem.size(),
                AlipayDatas.getInstance().listInfoItem.size() - AlipayDatas.getInstance().mCurrent
        ));

        AccessibilityNodeInfo edit = findFirstById("com.alipay.mobile.antui:id/input_edit");
        Tools.setText(edit, infoItem.money);
        sleep(100);
        //点击下一步
        Tools.doClick(service, "com.alipay.mobile.payee:id/payee_NextBtn");//下一步
        return false;
    }

}
