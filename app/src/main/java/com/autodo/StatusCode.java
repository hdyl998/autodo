package com.autodo;

/**
 * <p>Created by Administrator on 2018/9/11.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class StatusCode {

    public final static int STATUS_OK = 1;//大于0以上都是成功的
    public final static int STATUS_SURE = 0;//大于0以上都是成功的

    public final static int ERROR_NOT_ZHUSHU_MONEY_BEI = -1;
    public final static int ERROR_NOT_FIND_MATCH = -2;
    public final static int ERROR_NOT_FIND_SELECTION = -3;

    public final static int STATUS_DOING = -4;
    public final static int ERROR_CAPTURE_TIMEOUT = -5;
    public final static int STATUS_NOT_DO = -6;

    public final static int ERROR_NOT_SUPPORT_SINGLE_PASSWAY = -7;
    public final static int ERROR_MONEY_MORE_THAN_10000 = -8;
    public final static int ERROR_MATCH_MORE_THAN_8 = -9;
    public final static int ERROR_PASSWAY_NOT_EXIST = -10;
    public final static int ERROR_OVER_TIME = -11;

    public final static int ERROR_NOT_KNOWN = -999;


    public static String code2String(int code) {
        switch (code) {
            case STATUS_DOING:
                return "处理中...";
            case STATUS_NOT_DO:
                return "还没处理";
            case STATUS_OK:
                return "处理成功!";
            case ERROR_NOT_ZHUSHU_MONEY_BEI:
                return "注数或钱数或倍数错误";
            case ERROR_NOT_FIND_MATCH:
                return "没找到该比赛";
            case ERROR_NOT_FIND_SELECTION:
                return "没找到该选项";
            case ERROR_CAPTURE_TIMEOUT:
                return "截图超时";
            case ERROR_NOT_SUPPORT_SINGLE_PASSWAY:
                return "不支持单关";
            case ERROR_MATCH_MORE_THAN_8:
                return "比赛超过8场";
            case ERROR_MONEY_MORE_THAN_10000:
                return "单笔订单超过10000";
            case ERROR_PASSWAY_NOT_EXIST:
                return "过关方式不存在";
            case ERROR_OVER_TIME:
                return "处理超时";
            default:
                return "未知错误";
        }
    }

}
