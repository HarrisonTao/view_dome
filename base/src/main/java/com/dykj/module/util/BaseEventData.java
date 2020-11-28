package com.dykj.module.util;

import java.io.Serializable;
import java.util.List;

/**
 * @author WZ
 * @date 2019/5/16.
 * 严格要求自己，对每一行代码负责
 * description：
 */

public class BaseEventData<T, E> implements Serializable {
    public int type;
    public T value;
    public List<E> valueList;
    public Enum key;

    public BaseEventData(int type, T value) {
        this.type = type;
        this.value = value;
    }

    public BaseEventData(Enum key, T value) {
        this.key = key;
        this.value = value;
    }

    public BaseEventData(int type, List<E> list) {
        this.type = type;
        this.valueList = list;
    }

    public BaseEventData(Enum key, List<E> list) {
        this.key = key;
        this.valueList = list;
    }

    public BaseEventData(int type) {
        this.type = type;
    }

    public BaseEventData(Enum key) {
        this.key = key;
    }
}
