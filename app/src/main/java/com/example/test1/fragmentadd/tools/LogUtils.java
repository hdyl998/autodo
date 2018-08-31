package com.example.test1.fragmentadd.tools;

import android.util.Log;

import com.example.test1.fragmentadd.MainActivity;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class LogUtils {

    private final static boolean isOutputLog = true;

    public static void d(String d) {
        if (isOutputLog) {
            Log.e("ttt", d);
            MainActivity.listStrings.add(d);
        }
    }

    public static void d(String tag, String d) {
        if (isOutputLog) {
            Log.e(tag, d);
            MainActivity.listStrings.add(tag + ":" + d);
        }
    }
}
