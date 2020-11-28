package com.dykj.module.util;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * 作   者:  DLQ
 * 创建时间： 2018/12/26
 * 功能说明：选择器内容
 */

public class WheelPickerBean implements IPickerViewData {

    public String id;
    public String context;
    public String code;

    public WheelPickerBean(String id, String context,String code) {
        this.id = id;
        this.context = context;
        this.code = code;
    }

    public WheelPickerBean(String id, String context) {
        this.id = id;
        this.context = context;
    }

    public WheelPickerBean(String context) {
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String getPickerViewText() {
        return context;
    }
}
