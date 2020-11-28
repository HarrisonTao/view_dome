package com.dykj.youfeng.home.fragment;

import java.util.ArrayList;
import java.util.List;

public class TextUtils {
    public static List<String> getListData(){
        List<String> mList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            mList.add("");
        }
        return mList;
    }
}
