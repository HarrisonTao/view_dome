package com.dykj.module.entity;

import java.io.Serializable;

/**
 * @author WZ
 * @date 2019/5/23.
 * 严格要求自己，对每一行代码负责
 * description：
 */
public class BaseWebViewData implements Serializable {
    public String title;
    public String content;
    public boolean isOut;

    public BaseWebViewData() {
    }

    public BaseWebViewData(String title, String content, Boolean isOut) {
        this.title = title;
        this.content = content;
        this.isOut = isOut;
    }
}
