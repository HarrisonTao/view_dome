package com.dykj.youfeng.mode;

import java.io.Serializable;

public class SelectCreditBean implements Serializable {
    public String type;
    public String creditId;

    public String channelType;


    /**
     * 手动 还是 一键  佳付通通道
     */
    public String repaymentMode;
}
