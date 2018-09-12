package com.autodo;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.alibaba.fastjson.JSON;
import com.autodo.capture.CaptureScreenService;
import com.autodo.lottery.OrderItem;
import com.autodo.logic.LogicHolder;
import com.autodo.lottery.MyOrders;
import com.autodo.lottery.SelectItem;
import com.autodo.mainjob.DialogHandler;
import com.autodo.mainjob.MainActivityHandler;
import com.autodo.mainjob.exception.CodeErrorException;
import com.autodo.mainjob.exception.TimeOutException;
import com.autodo.tools.HanderLoopHelper;
import com.autodo.tools.IComCallBacks;
import com.autodo.tools.LogUtils;
import com.autodo.utils.Tools;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class AccessibilityCPService extends AccessibilityService {


    HanderLoopHelper loopHelper;

    {
        loopHelper = new HanderLoopHelper();
        loopHelper.setLoopCallBacks(new IComCallBacks() {
            @Override
            public void call(Object obj) {
                LogUtils.d(TAG, "循环执行isRunning" + isRunning);
                if (isRunning) {
                    return;
                }
                //暂停中
                if (CaptureScreenService.isPause) {
                    return;
                }
                //仅当MainActivity在后面时
                if (!MainActivity.isOnBackground) {
                    return;
                }
                OrderItem item = MyOrders.getFirstOrderItem();
                if (item != null) {
                    isRunning = true;
                    int errorCode = item.isCheckOK();
                    if (errorCode == StatusCode.STATUS_SURE) {
                        try {
                            LogUtils.d(TAG, "处理中..." + JSON.toJSONString(item));
                            holder.ensureHunheguoguan();
                            holder.clear();
                            for (SelectItem selectItem : item.selectItems) {
                                holder.selectSelections(selectItem);
                            }
                            holder.nextJiesuanPage();
                            holder.setPassWayInfo(item);
                            holder.checkLogin();
                            holder.checkQrCodeSence();
                        } catch (CodeErrorException e) {
                            MyOrders.endHandleOrderItem(item, e.getErrorCode());
                            e.printStackTrace();
                        } catch (TimeOutException e2) {
                            e2.printStackTrace();

                            //两次超时
                            item.timeOutExceptionCount++;
                            if (item.timeOutExceptionCount >= 2) {
                                //上传超过,结果
                                MyOrders.endHandleOrderItem(item, StatusCode.ERROR_OVER_TIME);
                            } else {
                                //继续执行,回到混合过关首页
                                int count = 0;
                                while (!holder.isInHunheguoguan()) {
                                    performGlobalAction(GLOBAL_ACTION_BACK);
                                    Tools.sleep(3000);
                                    count++;
                                    if (count == 3) {
                                        break;
                                    }
                                }
                                isRunning = false;
                            }
                        }
                    } else {
                        MyOrders.endHandleOrderItem(item, errorCode);
                    }
                }
            }
        });
    }

    public static boolean isRunning = false;//控制正在进行中的

    boolean isServiceRunning = false;//服务只进一次

    LogicHolder holder = new LogicHolder(this);

    /***
     * 处理事件
     * @param event
     */
    private void handleEvent(String event) {
        switch (event) {
            case "com.rdf.pdk.plugin.PluginProxySingleTaskFragmentActivity"://首页
                LogUtils.d("进入竞彩足球");
                new MainActivityHandler(this);
                break;
            case "com.rdf.pdk.plugin.PluginProxyActivity"://插件化Activity
//                if (holder.isInLoginSence()) {
//                    new LoginHandler(this);
//                } else if (holder.isInJesuan()) {
//                    if (GlobalControl.getInstance().getGroupItem() != null) {
//                        new JiesuanHandlder(this);
//                    }
//                } else if (holder.isInHunheguoguan()) {
//
//                } else if (holder.isInQrCodeSence()) {//处理二维码场景
//                    new QrcHandler(this);
//                }
                break;
            case "android.app.AlertDialog"://出现对话框,点确认
                new DialogHandler(this);
                break;
            case "android.widget.FrameLayout":
//                Tools.printRecycle(getRootInActiveWindow());
                break;
        }
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String className = event.getClassName().toString();
        LogUtils.d("当前执行类名：" + className);
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null) {
            return;
        }
        //处理事件
        handleEvent(className);
        if (isServiceRunning) {
            LogUtils.d("isRunning");
            return;
        }
        isServiceRunning = true;
        loopHelper.startLoopDelayed(200);
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
