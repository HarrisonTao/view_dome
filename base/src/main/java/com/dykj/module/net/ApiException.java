package com.dykj.module.net;

/**
 * 异常处理的一个类
 */
class ApiException extends RuntimeException {

    ApiException(String errorCode, String errorMessage) {
        super(errorMessage);
    }
}
