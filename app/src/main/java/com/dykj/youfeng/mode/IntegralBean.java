package com.dykj.youfeng.mode;

import java.util.List;

public class IntegralBean {

    /**
     * points : 0
     * page : 2
     * list : [{"affectPoints":89,"info":"购买商品","add_time":1567041662},{"affectPoints":85,"info":"购买商品","add_time":1566993648},{"affectPoints":85,"info":"购买商品","add_time":1566993314},{"affectPoints":85,"info":"购买商品","add_time":1566992269},{"affectPoints":85,"info":"购买商品","add_time":1566991338},{"affectPoints":660,"info":"购买商品赠送积分","add_time":1566976468},{"affectPoints":660,"info":"购买商品赠送积分","add_time":1566976426},{"affectPoints":660,"info":"购买商品赠送积分","add_time":1566976408},{"affectPoints":660,"info":"购买商品赠送积分","add_time":1566976160},{"affectPoints":660,"info":"购买商品赠送积分","add_time":1566976146}]
     */

    public int points;
    public int page;
    public List<ListBean> list;


    public static class ListBean {
        /**
         * affectPoints : 89
         * info : 购买商品
         * add_time : 1567041662
         */

        public int affectPoints;
        public String info;
        public int add_time;

           
    }
}
