package com.dykj.youfeng.mode;

import java.io.Serializable;
import java.util.List;

public class BalanceBean implements Serializable {
    /**
     * accountMoney : 604.00
     * all : 0.00
     * today : 0
     * page : 1
     * list : [{"affectMoney":"198.00","moneyType":5,"add_time":"2019-10-30 11:19:20","info":"升级VIP"},{"affectMoney":"198.00","moneyType":5,"add_time":"2019-10-30 11:11:22","info":"升级VIP"}]
     */

    public String accountMoney;
    public String all;
    public String today;
    public int page;
    public List<ListBean> list;


    public static class ListBean implements Serializable {
        /**
         * affectMoney : 198.00
         * moneyType : 5
         * add_time : 2019-10-30 11:19:20
         * info : 升级VIP
         */

        public String affectMoney;
        public int moneyType;
        public String add_time;
        public String info;
    }
}
