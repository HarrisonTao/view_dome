package com.dykj.youfeng.mode;

import java.util.List;

public class CashListBean {
    /**
     * page : 1
     * list : [{"id":10,"money":"100.00","cash_money":"92","status":0,"add_time":"2019-03-09 10:17:50"},{"id":1,"money":"1000.00","cash_money":"938","status":0,"add_time":"2019-01-18 10:31:03"},{"id":2,"money":"1000.00","cash_money":"938","status":1,"add_time":"2019-01-18 10:31:03"},{"id":3,"money":"1000.00","cash_money":"938","status":2,"add_time":"2019-01-18 10:31:03"}]
     */

    public int page;
    public List<ListBean> list;


    public static class ListBean {
        /**
         * id : 10
         * money : 100.00
         * cash_money : 92
         * status : 0
         * add_time : 2019-03-09 10:17:50
         */

        public int id;
        public String money;
        public String cash_money;
        public int status;
        public String add_time;

    }
}
