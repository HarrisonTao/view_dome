package com.dykj.youfeng.mode;

import java.io.Serializable;

public class PlanDetailBean implements Serializable {
    public String type;   // 数据类型
    public String creditId;   // 信用卡id
    public String repId;   // 计划id

    public int planType;   // 计划类型   1 是正常还款 2是一卡多换
    public String repayment_id;

}
