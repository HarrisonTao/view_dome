package com.dykj.module.net;

public interface HttpListener<T> {
    /**
     * 成功返回数据
     *
     * @param data
     */
    void success(T data);

    /**
     * 错误或异常抛出
     *
     * @param data
     */
    void error(Throwable data);
}
