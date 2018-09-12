package com.autodo.config;

/**
 * <p>Created by Administrator on 2018/9/11.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class MyConfig {
    public boolean isCheckInfo = false;

    private final static MyConfig config = new MyConfig();

    public static MyConfig getInstance() {
        return config;
    }
}
