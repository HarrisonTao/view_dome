package com.dykj.youfeng.home.base;

import java.io.Serializable;

public class Card implements Serializable {


     public String id;

    public String bank_name;//	string	名称

    public String money;//	decimal	计划还款金额
    public String   fee_money;//	decimal	手续费
    public String      repayment_date;
}
