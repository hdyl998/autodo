package com.autodo.mainjob;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

import com.autodo.utils.Tools;
import com.autodo.lottery.OrderItem;
import com.autodo.tools.LogUtils;

import java.util.HashMap;

/**
 * 结算
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class JiesuanHandlder extends BaseHandler {
    public JiesuanHandlder(AccessibilityService service) {
        super(service);
    }

    private static final String TAG = "JiesuanHandlder";
    public final static HashMap<Integer, String> hashMapIds = new HashMap<>();

    static {
//        hashMapIds.put(1, "com.jjc:id/btn_gg_1c1");
        hashMapIds.put(2, "com.jjc:id/btn_gg_2c1");
        hashMapIds.put(3, "com.jjc:id/btn_gg_3c1");
        hashMapIds.put(4, "com.jjc:id/btn_gg_4c1");
        hashMapIds.put(5, "com.jjc:id/btn_gg_5c1");
        hashMapIds.put(6, "com.jjc:id/btn_gg_6c1");
        hashMapIds.put(7, "com.jjc:id/btn_gg_7c1");
        hashMapIds.put(8, "com.jjc:id/btn_gg_8c1");
    }

    @Override
    public boolean handle() {
        OrderItem item = GlobalControl.getInstance().getGroupItem();

        //过关方式多种
        if (item.listPassway.size() > 1) {
            AccessibilityNodeInfo chuanNodeInfo = findFirstById("com.jjc:id/gc_gg_title_tv");
            Tools.doClick(chuanNodeInfo);

            String currentSelectId = hashMapIds.get(item.selectItems.size());
            AccessibilityNodeInfo node = findFirstById(currentSelectId);
            Tools.doClick(node);
            Tools.sleep(100);
            for (int index : item.listPassway) {
                String id = hashMapIds.get(index);
                if (id != null) {
                    AccessibilityNodeInfo id1 = findFirstById(id);
                    Tools.doClick(id1);
                    Tools.sleep(100);
                } else {
                    LogUtils.d(TAG, "id 不存在 index=" + index);
                }
            }
        }
        try {
            AccessibilityNodeInfo mBeishu = Tools.recycleFindEditText(root);
            Tools.setText(mBeishu, item.times + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Tools.doClick(findFirstById("com.jjc:id/btn_confirm_buy"));
        return true;
    }
}
