/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.dykj.youfeng.baidu.parser;


import com.dykj.youfeng.baidu.exception.FaceError;

/**
 * JSON解析
 * @param <T>
 */
public interface Parser<T> {
    T parse(String json) throws FaceError, FaceError;
}
