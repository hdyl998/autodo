package com.autodo.lottery;

import java.util.HashMap;

/**
 * <p>Created by Administrator on 2018/9/12.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class ConstDatas {
    public final static int PAGE_TIME_OUT = 15000;//每个页面如果超过指定秒没响应则抛出此异常

    public final static String weekArrays[] = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    public final static String[] strings = new String[]{
            "13", "11", "10",
            "23", "21", "20",
            "3-3", "3-1", "3-0",
            "1-3", "1-1", "1-0",
            "0-3", "0-1", "0-0",
            "0", "1", "2", "3", "4", "5", "6", "7",
            "1:0", "2:0", "2:1", "3:0", "3:1",
            "3:2", "4:0", "4:1", "4:2", "5:0",
            "5:1", "5:2", "胜其它",
            "0:0", "1:1", "2:2", "3:3", "平其它",

            "0:1", "0:2", "1:2", "0:3", "1:3",
            "2:3", "0:4", "1:4", "2:4", "0:5",
            "1:5", "2:5", "负其它"};
    public final static HashMap<String, Integer> serverKeys;

    static {
        int i = 0;
        serverKeys = new HashMap<>(strings.length);
        for (String str : strings) {
            serverKeys.put(str, i);
            i++;
        }
    }
}
