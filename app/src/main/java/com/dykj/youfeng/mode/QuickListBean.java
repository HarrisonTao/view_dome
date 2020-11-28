package com.dykj.youfeng.mode;

import java.util.List;

public class QuickListBean {


    /**
     * now_page : 1
     * all_page : 1
     * list : [{"id":1429,"money":"200.00","fee_money":"3.20","account_money":"196.80","status":3,"add_time":"2019-11-07 11:22:07"}]
     */

    public int now_page;
    public int all_page;
    public List<ListBean> list;


    public static class ListBean {
        /**
         * id : 1429
         * money : 200.00
         * fee_money : 3.20
         * account_money : 196.80
         * status : 3
         * add_time : 2019-11-07 11:22:07
         */

        public int id;
        public String money;
        public String fee_money;
        public String account_money;
        public int status;
        public String add_time;

          
    }
}
