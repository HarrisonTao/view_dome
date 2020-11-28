/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.dykj.youfeng.baidu.utils;


import com.dykj.youfeng.baidu.exception.FaceError;

public interface OnResultListener<T> {
    void onResult(T result);

    void onError(FaceError error);
}
