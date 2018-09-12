package com.autodo.mainjob.exception;

/**
 * 某个页面超时
 * <p>Created by Administrator on 2018/9/12.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class TimeOutException extends RuntimeException {
    public TimeOutException(String msg) {
        super(msg);
    }
}
