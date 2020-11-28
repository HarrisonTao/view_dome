package com.dykj.module.util;

import java.util.ArrayList;

/**
 * 作   者:  DLQ
 * 创建时间： 2019/5/24
 * 功能说明：
 */
public class TestData {
    private static final TestData ourInstance = new TestData();

    public static TestData getInstance() {
        return ourInstance;
    }

    private TestData() {
    }

    public ArrayList<String> getStringData(int num){
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            strings.add(""+i);
        }
        return strings;
    }
}
