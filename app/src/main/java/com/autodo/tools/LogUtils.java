package com.autodo.tools;

import android.util.Log;

import com.autodo.MainActivity;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class LogUtils {

    private final static boolean isOutputLog = true;

    public static void d(String d) {
        if (isOutputLog) {
            Log.e("ttt", d);
        }
    }

    public static void d(String tag, String d) {
        if (isOutputLog) {
            Log.e(tag, d);
        }
    }

    public static void print(String log_tag, String s) {
        if (isOutputLog) {
            Log.e(log_tag, s);
        }
    }
}
