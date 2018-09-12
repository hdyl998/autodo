package com.autodo.mainjob.exception;

/**
 * 其它明确的异常
 * <p>Created by Administrator on 2018/9/12.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class CodeErrorException extends RuntimeException {


    int errorCode;

    String info;

    public CodeErrorException(int errorCode, String info) {
        this.errorCode = errorCode;
        this.info = info;
    }

    public int getErrorCode() {
        return errorCode;
    }


    public String getInfo() {
        return info;
    }
}
