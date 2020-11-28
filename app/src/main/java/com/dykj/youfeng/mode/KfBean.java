package com.dykj.youfeng.mode;

import java.util.List;

public class KfBean {
    /**
     * start : 1
     * end : 11
     * list : [{"id":1,"question":"什么是智优？","sort":1},{"id":4,"question":"小额还款和大额还款区别是什么？","sort":2},{"id":3,"question":"为什么我的消费没有积分是怎么回事？","sort":3},{"id":7,"question":"还款一扣一还是什么意思？","sort":4},{"id":8,"question":"还款多扣一还是什么意思？","sort":5},{"id":9,"question":"第一次还款和收款为什么还要再次绑定储蓄卡？","sort":6},{"id":5,"question":"如何升级VIP？","sort":7},{"id":6,"question":"如何成为代理？","sort":8},{"id":10,"question":"成为VIP有什么好处？","sort":9},{"id":11,"question":"VIP充值398元押金怎么退还？","sort":10},{"id":12,"question":"为什么要续购VIP?","sort":11}]
     */

    public int start;
    public int end;
    public List<ListBean> list;


    public static class ListBean {
        /**
         * id : 1
         * question : 什么是智优？
         * sort : 1
         */

        public int id;
        public String question;
        public int sort;

    }
}
