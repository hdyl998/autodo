package com.example.test1.fragmentadd;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.test1.fragmentadd.logic.GroupSelectItem;
import com.example.test1.fragmentadd.logic.LogicHolder;
import com.example.test1.fragmentadd.logic.SelectItem;
import com.example.test1.fragmentadd.tools.HanderLoopHelper;
import com.example.test1.fragmentadd.tools.IComCallBacks;
import com.example.test1.fragmentadd.tools.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class MyAccessibilityService333 extends AccessibilityService {


    //缓存池
    List<GroupSelectItem> groupSelectItemList = new ArrayList<>();

    {
        GroupSelectItem groupSelectItem = new GroupSelectItem();
        groupSelectItemList.add(groupSelectItem);
    }

    HanderLoopHelper loopHelper = new HanderLoopHelper();


    {
        loopHelper.setLoopCallBacks(new IComCallBacks() {
            @Override
            public void call(Object obj) {
                LogUtils.d(TAG, "循环执行isRunning" + isRunning);
                if (isRunning) {
                    return;
                }
                GroupSelectItem item = getFirstGroupItem();
                if (item != null) {
                    isRunning = true;
                    LogUtils.d(TAG, "处理中..." + JSON.toJSONString(item));
                    holder.clear();
                    for (SelectItem selectItem : item.selectItemList) {
                        holder.selectSelections(selectItem);
                    }
                    holder.nextJiesuanPage();
                    Tools.sleep(500);
                    holder.setPassWayInfo(item);

                    holder.checkLogin();
                    //移除已处理完的这个
                    groupSelectItemList.remove(item);
                    isRunning = false;
                }
            }
        });
    }

    boolean isRunning = false;//控制正在进行中的

    boolean isServiceRunning = false;//服务只进一次

    LogicHolder holder = new LogicHolder(this);


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        LogUtils.d(TAG, Thread.currentThread() + "");
        int eventType = event.getEventType();
        String className = event.getClassName().toString();
        LogUtils.d("当前事件类型：" + eventType);
        LogUtils.d("当前执行类名：" + className);
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null) {
            LogUtils.d("ROOT NULL");
            return;
        }
        if (isServiceRunning) {
            LogUtils.d("isRunning");
            return;
        }
        isServiceRunning = true;
        loopHelper.startLoopDelayed(100);

    }


    private GroupSelectItem getFirstGroupItem() {
        if (groupSelectItemList.isEmpty()) {
            return null;
        }
        return groupSelectItemList.get(0);
    }


    private static final String TAG = "MyAccessibilityService";


    @Override
    public void onInterrupt() {
        LogUtils.d(TAG, "服务关闭了...");

    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtils.d(TAG, "服务解绑了,释放资源操作...");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, "服务停止了...");

        loopHelper.onDestory();
    }

}
