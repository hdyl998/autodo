package com.autodo;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.alibaba.fastjson.JSON;
import com.autodo.logic.GroupSelectItem;
import com.autodo.logic.LogicHolder;
import com.autodo.logic.SelectItem;
import com.autodo.mainjob.DialogHandler;
import com.autodo.mainjob.GlobalControl;
import com.autodo.mainjob.JiesuanHandlder;
import com.autodo.mainjob.LoginHandler;
import com.autodo.mainjob.MainActivityHandler;
import com.autodo.mainjob.QrcHandler;
import com.autodo.tools.HanderLoopHelper;
import com.autodo.tools.IComCallBacks;
import com.autodo.tools.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class AccessibilityCPService extends AccessibilityService {


    //缓存池
    public final static List<GroupSelectItem> groupSelectItemList = new ArrayList<>();


    {
        groupSelectItemList.add(new GroupSelectItem().createOne());
        groupSelectItemList.add(new GroupSelectItem().createTwo());
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
                    if (item.isCheckOK()) {
                        LogUtils.d(TAG, "处理中..." + JSON.toJSONString(item));
                        holder.ensureHunheguoguan();
                        if (true) {
                            return;
                        }
                        holder.clear();
                        for (SelectItem selectItem : item.selectItemList) {
                            holder.selectSelections(selectItem);
                        }
                        holder.nextJiesuanPage();
                        Tools.sleep(500);
                        holder.setPassWayInfo(item);
                        holder.checkDialogSence();
                        holder.checkLogin();
                        holder.checkQrCodeSence();
                    }
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


    private void handleEvent(String event) {
        //主界面
        switch (event) {
            case "com.rdf.pdk.plugin.PluginProxySingleTaskFragmentActivity"://首页
                LogUtils.d("进入竞彩足球");
                new MainActivityHandler(this);
                break;
            case "com.rdf.pdk.plugin.PluginProxyActivity"://插件化Activity
                if (holder.isInLoginSence()) {
                    new LoginHandler(this);
                } else if (holder.isInJesuan()) {
                    if (GlobalControl.getInstance().getGroupItem() != null) {
                        new JiesuanHandlder(this);
                    }
                } else if (holder.isInHunheguoguan()) {

                } else if (holder.isInQrCodeSence()) {//处理二维码场景
                    new QrcHandler(this);
                }
                break;
            case "android.app.AlertDialog"://出现对话框,点确认
                new DialogHandler(this);
                break;
        }
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        LogUtils.d(TAG, Thread.currentThread() + "");
//        int eventType = event.getEventType();
        String className = event.getClassName().toString();
//        LogUtils.d("当前事件类型：" + eventType);
        LogUtils.d("当前执行类名：" + className);
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null) {
            LogUtils.d("ROOT NULL");
            return;
        }
        //处理事件
//        handleEvent(className);
        if (isServiceRunning) {
            LogUtils.d("isRunning");
            return;
        }
        isServiceRunning = true;
        loopHelper.startLoopDelayed(100);
    }


    public static GroupSelectItem getFirstGroupItem() {
        if (groupSelectItemList.isEmpty()) {
            return null;
        }
        return groupSelectItemList.get(0);
    }


    private static final String TAG = "MyAccessibilityService";

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = false;
    }

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
