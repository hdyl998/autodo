package com.autodo.utils;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class SystemUtils {


    public static boolean isServiceRunning(Context context, Class<?> clazz) {
        return isServiceRunning(context, clazz.getName());
    }

    /**
     * 判断服务是否开启
     *
     * @return
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if ("".equals(ServiceName) || ServiceName == null)
            return false;
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(100);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAccessiblilityServiceEnabled(Context mContext, String name) {
        AccessibilityManager am = (AccessibilityManager) mContext.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> serviceInfos = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
//        List<AccessibilityServiceInfo> installedAccessibilityServiceList = am.getInstalledAccessibilityServiceList();
//        for (AccessibilityServiceInfo info : installedAccessibilityServiceList) {
//            Log.d("MainActivity", "all -->" + info.getId());
//            if (name.equals(info.getId())) {
//                return true;
//            }
//        }com.autodo.AccessibilityCPService
        String fullName = String.format("%s/%s", mContext.getPackageName(), name);
        for (AccessibilityServiceInfo info : serviceInfos) {
            Log.d("MainActivity", "1all -->" + info.getId());
            if (fullName.equals(info.getId())) {
                return true;
            }
        }
        return false;
    }
}
