package com.dykj.module.net;

import android.text.TextUtils;

public class HttpException extends RuntimeException {
    private static final long serialVersionUID = -2199603193956026137L;
    private static final String SERVICE_ERROR = "请求服务器异常";

    public HttpException(String detailMessage) {
        super(detailMessage);
    }

    @Override
    public String getMessage() {
        return TextUtils.isEmpty(super.getMessage()) ? "请求服务器异常" : super.getMessage();
    }
}
