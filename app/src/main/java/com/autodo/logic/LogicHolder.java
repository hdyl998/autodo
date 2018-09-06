package com.autodo.logic;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

import com.autodo.App;
import com.autodo.capture.CaptureScreenService;
import com.autodo.tools.LogUtils;
import com.autodo.Tools;

import java.util.HashMap;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class LogicHolder {


    public final static String[] supportSelectionsIds = new String[]{
            "com.jjc:id/ll_pop_spf_0", "com.jjc:id/ll_pop_spf_1", "com.jjc:id/ll_pop_spf_2",
            "com.jjc:id/ll_pop_rqspf_0", "com.jjc:id/ll_pop_rqspf_1", "com.jjc:id/ll_pop_rqspf_2",
            "com.jjc:id/ll_pop_bqspf_0", "com.jjc:id/ll_pop_bqspf_1", "com.jjc:id/ll_pop_bqspf_2",
            "com.jjc:id/ll_pop_bqspf_3", "com.jjc:id/ll_pop_bqspf_4", "com.jjc:id/ll_pop_bqspf_5",
            "com.jjc:id/ll_pop_bqspf_6", "com.jjc:id/ll_pop_bqspf_7", "com.jjc:id/ll_pop_bqspf_8",
            "com.jjc:id/ll_pop_zjq_0", "com.jjc:id/ll_pop_zjq_1", "com.jjc:id/ll_pop_zjq_2", "com.jjc:id/ll_pop_zjq_3", "com.jjc:id/ll_pop_zjq_4", "com.jjc:id/ll_pop_zjq_5", "com.jjc:id/ll_pop_zjq_6", "com.jjc:id/ll_pop_zjq_7",
            "com.jjc:id/ll_pop_bf_0", "com.jjc:id/ll_pop_bf_1", "com.jjc:id/ll_pop_bf_2", "com.jjc:id/ll_pop_bf_3", "com.jjc:id/ll_pop_bf_4",
            "com.jjc:id/ll_pop_bf_5", "com.jjc:id/ll_pop_bf_6", "com.jjc:id/ll_pop_bf_7", "com.jjc:id/ll_pop_bf_8", "com.jjc:id/ll_pop_bf_9",
            "com.jjc:id/ll_pop_bf_10", "com.jjc:id/ll_pop_bf_11", "com.jjc:id/ll_pop_bf_12",
            "com.jjc:id/ll_pop_bf_13", "com.jjc:id/ll_pop_bf_14", "com.jjc:id/ll_pop_bf_15", "com.jjc:id/ll_pop_bf_16", "com.jjc:id/ll_pop_bf_17",

            "com.jjc:id/ll_pop_bf_18", "com.jjc:id/ll_pop_bf_19", "com.jjc:id/ll_pop_bf_20", "com.jjc:id/ll_pop_bf_21", "com.jjc:id/ll_pop_bf_22",
            "com.jjc:id/ll_pop_bf_23", "com.jjc:id/ll_pop_bf_24", "com.jjc:id/ll_pop_bf_25", "com.jjc:id/ll_pop_bf_26", "com.jjc:id/ll_pop_bf_27",
            "com.jjc:id/ll_pop_bf_28", "com.jjc:id/ll_pop_bf_29", "com.jjc:id/ll_pop_bf_30"};

    //投注页面的ID
    public final static String[] sfpRqspfSelectionIds = {"com.jjc:id/jz_spf_win_ll", "com.jjc:id/jz_spf_ping_ll", "com.jjc:id/jz_spf_fu_ll",
            "com.jjc:id/jz_rqspf_win_ll", "com.jjc:id/jz_rqspf_ping_ll", "com.jjc:id/jz_rqspf_fu_ll"
    };

    AccessibilityService service;


    public LogicHolder(AccessibilityService service) {
        this.service = service;
    }

    /***
     * 判断是不是选择对话框
     * @return
     */
    public boolean isInSelectDialog() {
        return Tools.isInSceneById(service, "com.jjc:id/hh_content_sv");
    }

    /***
     * 判断是不是选择界面
     * @return
     */
    public boolean isInHunheguoguan() {
        return Tools.isInSceneById(service, "com.jjc:id/elv_sport_expand") && Tools.isInSceneById(service, "com.jjc:id/tv_game_no");
    }


    //是否是结算界面
    public boolean isInJesuan() {
        return Tools.isInSceneById(service, "com.jjc:id/ll_jc_add_edit_match");
    }


    /**
     * 选中所有的选项
     *
     * @param selectItem
     * @return
     */
    public boolean selectAllDialogSelections(SelectItem selectItem) {
        if (isInSelectDialog()) {
            AccessibilityNodeInfo curRoot = service.getRootInActiveWindow();
            for (int select : selectItem.seletions) {

                //查找文本
                AccessibilityNodeInfo aaa = Tools.findFirstById(curRoot, supportSelectionsIds[select]);
                if (aaa != null) {
                    Tools.doClick(aaa);//点击操作
                } else {
                    //点击失败
                    LogUtils.d(TAG, select + " click failed");
                    return false;
                }
                Tools.sleep(150);
            }
            AccessibilityNodeInfo OK = Tools.findFirstById(curRoot, "com.jjc:id/hh_pop_confirm");
            if (OK != null) {
                OK.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                return true;//执行成功
            }
        }
        LogUtils.d(TAG, "执行selectAllDialogSelections失败");
        return false;
    }


    private static final String TAG = "LogicHolder";

    /***
     * 胜平负让球胜平负选项
     * @param selectItem
     * @return
     */
    public boolean selectSpfRqSpfSelections(AccessibilityNodeInfo itemNodeInfo, SelectItem selectItem) {
        for (int select : selectItem.seletions) {
            String id = sfpRqspfSelectionIds[select];
            AccessibilityNodeInfo nodeInfo = Tools.findFirstById(itemNodeInfo, id);
            if (nodeInfo == null) {
                return false;
            }
            Tools.doClick(nodeInfo);
            Tools.sleep(20);
        }
        return true;
    }

    /***
     * 去结算界面
     */
    public void nextJiesuanPage() {
        //确保在混合过关内
        while (!isInHunheguoguan()) {
            Tools.sleep(100);
        }
        //确保在混合过关内
        AccessibilityNodeInfo submit = Tools.findFirstById(service.getRootInActiveWindow(), "com.jjc:id/btn_submit");
        Tools.doClick(submit);
        Tools.showToast("点击下一步");
        Tools.sleep(900);
    }

    /**
     * 请空列表
     */
    public void clear() {

        AccessibilityNodeInfo nodeInfo = Tools.findFirstById(service.getRootInActiveWindow(), "com.jjc:id/btn_clear_choosenum");
        Tools.doClick(nodeInfo);
        Tools.sleep(100);
    }


    public void ensureHunheguoguan() {
        //确保在混合过关内
        while (!isInHunheguoguan()) {
            Tools.sleep(100);
        }
        AccessibilityNodeInfo nodeInfo = Tools.findFirstByText(service.getRootInActiveWindow(), "混合过关");
        if (nodeInfo != null) {
            LogUtils.d(nodeInfo.toString());
            Tools.doClick(nodeInfo);
            Tools.sleep(50);
            Tools.printRecycle(service.getRootInActiveWindow());
            return;
        }
        nodeInfo = Tools.findFirstById(service.getRootInActiveWindow(), "cn.gov.lottery:id/ll_top_title");
        Tools.doClick(nodeInfo);
        Tools.sleep(50);
        Tools.printRecycle(nodeInfo);
    }


    /***
     * 选中选项
     * @param selectItem
     */
    public boolean selectSelections(SelectItem selectItem) {
        //确保在混合过关内
        while (!isInHunheguoguan()) {
            Tools.sleep(100);
        }
        //判定场景,在混合过关界面
        //查找listView
        AccessibilityNodeInfo listView = Tools.findFirstById(service.getRootInActiveWindow(), "com.jjc:id/elv_sport_expand");
        if (listView != null) {
            AccessibilityNodeInfo itemNodeInfo = findNodeInfoByOrderId(listView, selectItem.orderId);
            if (itemNodeInfo == null) {
                Tools.showToast("没有找到" + selectItem.orderId);
                LogUtils.d(TAG, "没有找到" + selectItem.orderId);
                return false;
            }
            //全是胜平负或让球胜平负
            if (selectItem.isAllSpfRqSpf()) {
                return selectSpfRqSpfSelections(itemNodeInfo, selectItem);
            } else {
                AccessibilityNodeInfo expandAll = Tools.findFirstById(itemNodeInfo, "com.jjc:id/ll_other_select");
                Tools.doClick(expandAll);//展开
                Tools.sleep(300);
                return selectAllDialogSelections(selectItem);
            }
        } else {
            LogUtils.d(TAG, "不能找到listView");
        }
        return false;
    }

    private boolean scorll2Top(AccessibilityNodeInfo listView) {
        String lastOrderId = null;
        //退到顶部
        int time = 50;
        while (time > 0) {
            listView.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
            Tools.sleep(100);
            time--;
            AccessibilityNodeInfo nodeInfo = Tools.findFirstById(listView, "com.jjc:id/tv_game_no");
            String textOrderId = nodeInfo.getText().toString();
            if (textOrderId.equals(lastOrderId)) {
                LogUtils.d(TAG, "滚动到顶部");
                return true;
            }
            lastOrderId = textOrderId;
        }
        return false;
    }


    /***
     * 通过orderId去定位到对应的nodeItem
     * @param listView
     * @param orderId
     * @return
     */
    public AccessibilityNodeInfo findNodeInfoByOrderId(AccessibilityNodeInfo listView, String orderId) {
        LogUtils.d("findNodeInfoByOrderId", orderId);
        //先查找
        AccessibilityNodeInfo nodeInfo = Tools.findFirstByText(listView, orderId);
        if (nodeInfo != null) {
            LogUtils.d("tttt", nodeInfo.getText().toString());
            AccessibilityNodeInfo parent = nodeInfo.getParent();
            return parent;
        }
        scorll2Top(listView);
        //找不到再滚动
        String lastOrderId = null;
        while (true) {
            AccessibilityNodeInfo newNodeInfo = Tools.findFirstByText(listView, orderId);
            if (newNodeInfo != null) {
                LogUtils.d("tttt", newNodeInfo.getText().toString());
                AccessibilityNodeInfo parent = newNodeInfo.getParent();
                return parent;//listView一条的根view
            }

            //取第一个竞彩编号,看是否需要向下滑动,向上滑动
            nodeInfo = Tools.findFirstById(listView, "com.jjc:id/tv_game_no");
            String textOrderId = nodeInfo.getText().toString();
            if (textOrderId.equals(lastOrderId)) {
                LogUtils.d(TAG, "找不到了" + textOrderId);
                return null;
            }
            lastOrderId = textOrderId;
            //向下滚
            listView.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            Tools.sleep(100);
//            HelpItem curHelpItem = new HelpItem(textOrderId);
//            int value = findHelpItem.compareTo(curHelpItem);
//            LogUtils.d(TAG, "value" + value);
//            switch (value) {
//                case 1:
//                    listView.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
//                    break;
//                case -1:
//                    listView.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
//                    break;
//            }
        }
    }


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


    /***
     * 设置倍数,选中几串几
     * @param item
     */
    public void setPassWayInfo(GroupSelectItem item) {

        //确保在混合过关内
        while (!isInJesuan()) {
            Tools.sleep(200);
        }
        AccessibilityNodeInfo rootNodeInfo = service.getRootInActiveWindow();

        //过关方式多种
        if (item.listPassway.size() > 1) {
            AccessibilityNodeInfo chuanNodeInfo = Tools.findFirstById(rootNodeInfo, "com.jjc:id/gc_gg_title_tv");
            Tools.doClick(chuanNodeInfo);

            String currentSelectId = hashMapIds.get(item.selectItemList.size());
            AccessibilityNodeInfo node = Tools.findFirstById(rootNodeInfo, currentSelectId);
            Tools.doClick(node);
            Tools.sleep(100);
            for (int index : item.listPassway) {
                String id = hashMapIds.get(index);
                if (id != null) {
                    AccessibilityNodeInfo id1 = Tools.findFirstById(rootNodeInfo, id);
                    Tools.doClick(id1);
                    Tools.sleep(100);
                } else {
                    LogUtils.d(TAG, "id 不存在 index=" + index);
                }
            }
        }
        try {
            AccessibilityNodeInfo mBeishu = Tools.recycleFindEditText(rootNodeInfo);
            Tools.setText(mBeishu, item.times + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Tools.doClick(Tools.findFirstById(rootNodeInfo, "com.jjc:id/btn_confirm_buy"));
        Tools.sleep(300);
    }

    /***
     * 在登录界面
     * @return
     */
    public boolean isInLoginSence() {
        return Tools.isInSceneById(service, "com.help:id/sporttery_logo");
    }

    private final static String MOBILE = "15310872381";
    private final static String PWD = "141916lgdon";

    public void checkLogin() {
        Tools.sleep(500);
        //判定是否在登录界面
        if (isInLoginSence()) {
            AccessibilityNodeInfo root = service.getRootInActiveWindow();
            AccessibilityNodeInfo mobile = Tools.findFirstById(root, "com.help:id/et_login_phone");
            Tools.setText(mobile, MOBILE);
            Tools.sleep(20);
            AccessibilityNodeInfo pwd = Tools.findFirstById(root, "com.help:id/et_login_password");
            Tools.setText(pwd, PWD);
            //点击确定
            Tools.doClick(service, "com.help:id/tv_login");
            Tools.showToast("登录中");
            Tools.sleep(1500);
        }
    }

    public void checkQrCodeSence() {
        while (!isInQrCodeSence()) {
            Tools.sleep(1000);
        }
        doQrCodeSence();
    }


    public boolean isInQrCodeSence() {
        return Tools.isInSceneById(service, "com.other:id/money_num");
    }

    public boolean isInDialogSence() {
        return Tools.isInSceneById(service, "cn.gov.lottery:id/tv_dialog_title");
    }

    /***
     * 检查是否在温馨提示对话框中
     */
    public void checkDialogSence() {
        if (isInDialogSence()) {
            AccessibilityNodeInfo ok = Tools.findFirstById(service.getRootInActiveWindow(), "cn.gov.lottery:id/tv_confirm");
            Tools.doClick(ok);
        }
    }


    /***
     * 获取二维码
     */
    public void doQrCodeSence() {
        AccessibilityNodeInfo root = service.getRootInActiveWindow();

        AccessibilityNodeInfo moneyNode = Tools.findFirstById(root, "com.other:id/money_num");
        String strMoney = moneyNode.getText() + "";
        strMoney = strMoney.substring(1, strMoney.length() - 1);

        int money = Integer.parseInt(strMoney);
        LogUtils.d(TAG, "all money" + money);

        AccessibilityNodeInfo nodeInfo = Tools.findFirstById(root, "com.other:id/detail");
        String strInfo = nodeInfo.getText() + "";
        int indexGong = strInfo.indexOf("共");
        int indexZhang = strInfo.indexOf("张");
        int indexZhu = strInfo.indexOf("注");
        int indexBei = strInfo.indexOf("倍");
        int zhang = Integer.parseInt(strInfo.substring(indexGong + 1, indexZhang));
        int zhu = Integer.parseInt(strInfo.substring(indexZhang + 1, indexZhu));
        int bei = Integer.parseInt(strInfo.substring(indexZhu + 1, indexBei));

        LogUtils.d(TAG, "zhang" + zhang);

        LogUtils.d(TAG, "zhu" + zhu);

        LogUtils.d(TAG, "bei" + bei);


        AccessibilityNodeInfo picNode = Tools.findFirstById(root, "com.other:id/qrCode");
        //获得在屏幕上的位置
        Rect rect = new Rect();
        picNode.getBoundsInScreen(rect);

        //放大部分像素,用于截屏
        rect.left -= 5;
        rect.right += 5;
        rect.top -= 5;
        rect.bottom += 5;
        //执行截屏
        CaptureScreenService.doCapture(App.getApp(), rect);

        Tools.doClick(Tools.findFirstById(root, "cn.gov.lottery:id/ll_title_back"));
        Tools.sleep(1000);
    }


//    private final static class HelpItem implements Comparable<HelpItem> {
//        public String orderId;
//
//
//        private String week;//日,一,二,三,四,五,六
//        private String num;
//
//        public HelpItem(String orderId) {
//            this.orderId = orderId;
//            week = orderId.substring(1, 2);
//            num = orderId.substring(2);
//        }
//
//        @Override
//        public int compareTo(@NonNull HelpItem o) {
//            //竞彩编号相同
//            if (o.orderId.equals(this.orderId))
//                return 0;
//
//            //周次相同,是比较数字编号
//            if (o.week.equals(this.week)) {
//                return this.num.compareTo(o.num);
//            }
//            //周次不同,则比较周次
//            //周日>周一>周二>周3>周四>周五>
//
//            int anothWeek = texts.indexOf(o.week);
//            int curWeek = texts.indexOf(week);
//
//            int chaWeek = anothWeek - curWeek;
//            if (Math.abs(chaWeek) > 3) {
//
//            }
//            return chaWeek;
//        }
//
//        public final static String texts = "日一二三四五六日一二三四五六";
//    }

}
