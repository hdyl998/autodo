package com.autodo;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.autodo.tools.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class MyAccessibilityService111 extends AccessibilityService {


    public final static class MyData {
        public String orderId = "周日068";
        public List<String> seletions = new ArrayList<>();

        {
            seletions.add("2:0");
            seletions.add("0");
            seletions.add("1");
            seletions.add("胜胜");
            seletions.add("胜平");
            seletions.add("负平");
        }
    }


    MyData myData = new MyData();

    boolean isRunning = true;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        LogUtils.d(Thread.currentThread() + "thread");
        //此处写事件的处理，常用eventType和className来做判断。
        int eventType = event.getEventType();
        String className = event.getClassName().toString();
        LogUtils.d("当前事件类型：" + eventType);
        LogUtils.d("当前执行类名：" + className);

        AccessibilityNodeInfo root = getRootInActiveWindow();

        if (root == null) {
            LogUtils.d("ROOT NULL");
            return;
        }

//        if (isRunning == false) {
//            return;
//        }
//        try {
        AccessibilityNodeInfo tt1 = findByText(root, "混合过关");
        if (tt1 != null) {
            AccessibilityNodeInfo listView = findById(root, "com.esun.ui:id/expandlistview");
            if (listView == null) {
                return;
            }
            while (true) {
                List<AccessibilityNodeInfo> lists = listView.findAccessibilityNodeInfosByViewId("com.esun.ui:id/spt_content");
                boolean isFind = false;
                for (AccessibilityNodeInfo info : lists) {
                    AccessibilityNodeInfo currentItem = findByText(info, myData.orderId);
                    if (currentItem != null) {

                        AccessibilityNodeInfo expandAll = findById(currentItem.getParent().getParent(), "com.esun.ui:id/spt_more_tv");
                        LogUtils.d("展开全部" + (expandAll == null));
                        if (expandAll != null) {
                            handleSelect(expandAll);
                            LogUtils.d("find" + myData.orderId);
                            isFind = true;
                        }
                    }
                }
                if (!isFind) {
                    listView.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                    sleep(1);
                } else {
                    break;
                }
            }
        }
        //根据不同的eventType和className，来进行具体的事件处理
    }

    private void handleSelect(AccessibilityNodeInfo nodeInfo) {


        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);

        sleep(400);
        AccessibilityNodeInfo curRoot = getRootInActiveWindow();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (String aa : myData.seletions) {
            AccessibilityNodeInfo aaa = findByText(curRoot, aa);
            if (aaa != null)
                aaa.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            sleep(100);
        }
        AccessibilityNodeInfo OK = findByText(curRoot, "确认");
        if (OK != null)
            OK.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        isRunning = false;//停止
    }


    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private AccessibilityNodeInfo findByText(AccessibilityNodeInfo root, String string) {
        List<AccessibilityNodeInfo> info = root.findAccessibilityNodeInfosByText(string);
        if (info == null) {
            return null;
        }
        LogUtils.d(root.getClassName() + "findx" + string);
        if (info.size() >= 1) {
            return info.get(0);
        }
        return null;
    }

    private AccessibilityNodeInfo findById(AccessibilityNodeInfo root, String string) {
        LogUtils.d(root.getClassName() + "findx" + string);
        List<AccessibilityNodeInfo> info = root.findAccessibilityNodeInfosByViewId(string);
        if (info == null) {
            LogUtils.d("空");
            return null;
        }
        if (info.size() >= 1) {
            return info.get(0);
        }
        LogUtils.d("空");
        return null;
    }

    private static final String TAG = "MyAccessibilityService";

    @Override
    public void onInterrupt() {

    }


    private void fun() {
    }


}
