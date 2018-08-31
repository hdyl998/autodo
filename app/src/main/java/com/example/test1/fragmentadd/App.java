package com.example.test1.fragmentadd;

import android.app.Application;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class App extends Application {


    static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static App getApp() {
        return instance;
    }

    public static void showToast(String text) {
        Toast.makeText(getApp(), text, Toast.LENGTH_SHORT).show();
    }
}
