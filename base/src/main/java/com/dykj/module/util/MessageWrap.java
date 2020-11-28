package com.dykj.module.util;

import java.util.HashMap;

/**
 * Created by lcjing on 2019/5/20.
 */
public class MessageWrap {
    public final String message;
    public final HashMap<String, Object> map;

    public static MessageWrap getInstance(String message) {
        return new MessageWrap(message, null);
    }

    public static MessageWrap getInstance(String message, HashMap<String, Object> map) {
        return new MessageWrap(message, map);
    }
    private MessageWrap(String message, HashMap<String, Object> map) {
        this.message = message;
        this.map = map;
    }
}
