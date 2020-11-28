package com.dykj.module.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * @author WZ
 * @date 2019/5/16.
 * 严格要求自己，对每一行代码负责
 * description：
 */
public class Base64Utils {
    private static final int BUFFER_SIZE = 1024;

    public Base64Utils() {
    }

    public static String encrypt(byte[] str) {
        try {
            return new String(Base64.encode(str, 0, str.length, 0), "UTF-8");
        } catch (UnsupportedEncodingException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static byte[] decrypt(String str) {
        if (str != null && str.length() != 0) {
            try {
                byte[] encode = str.getBytes("UTF-8");
                return Base64.decode(encode, 0, encode.length, 0);
            } catch (UnsupportedEncodingException var2) {
                var2.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}
