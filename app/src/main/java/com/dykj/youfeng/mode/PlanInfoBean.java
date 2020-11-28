package com.dykj.youfeng.mode;

import java.util.List;

public class PlanInfoBean {

    /**
     * status : 9999
     * message : 请求成功
     * info : {"info":{"id":3,"repayment_money":"2000.00","fee_money":"13.17","has_pay_money":"0.00","has_repayment_money":"0.00","has_repayment_num":0,"has_fee_money":"0.00","has_frequency_money":"0.00","status":1,"cardId":5,"oneCardId":6,"repayment_num":2,"add_time":"2019-11-04","start_time":"2019-11-13","end_time":"2019-11-13","orderNo":"201911041614535"},"plan":[{"id":16,"type":1,"orderNo":"cjmin2019110415212403","fee_money":"1.84","money":"333.84","status":0,"run_time":"2019-11-13 11:05:47"},{"id":17,"type":1,"orderNo":"cjmin2019110415212413","fee_money":"1.84","money":"333.84","status":0,"run_time":"2019-11-13 13:15:35"},{"id":18,"type":1,"orderNo":"cjmin2019110415212423","fee_money":"2.06","money":"373.15","status":0,"run_time":"2019-11-13 15:38:38"},{"id":19,"type":2,"orderNo":"cjmin2019110415212433","fee_money":"1.00","money":"1034.09","status":0,"run_time":"2019-11-13 19:01:42"},{"id":20,"type":1,"orderNo":"cjmin2019110415212443","fee_money":"1.82","money":"330.82","status":0,"run_time":"2019-11-13 14:31:04"},{"id":21,"type":1,"orderNo":"cjmin2019110415212453","fee_money":"1.81","money":"328.81","status":0,"run_time":"2019-11-13 15:50:35"},{"id":22,"type":1,"orderNo":"cjmin2019110415212463","fee_money":"1.80","money":"326.80","status":0,"run_time":"2019-11-13 16:15:30"},{"id":23,"type":2,"orderNo":"cjmin2019110415212473","fee_money":"1.00","money":"980.00","status":0,"run_time":"2019-11-13 18:06:59"}],"card":{"bank_num":"**** **** **** 0475","name":"招商银行","logo":"http://yx.diyunkeji.com//static/upload/5afa37e60c5a7.png","repayment_date":"17","statement_date":"10","realname":"王连君"},"oneCard":{"bank_num":"**** **** **** 4994","name":"中信银行","logo":"http://yx.diyunkeji.com//static/upload/5afa374339519.png","repayment_date":"15","statement_date":"4"}}
     */
    /**
     * info : {"id":3,"repayment_money":"2000.00","fee_money":"13.17","has_pay_money":"0.00","has_repayment_money":"0.00","has_repayment_num":0,"has_fee_money":"0.00","has_frequency_money":"0.00","status":1,"cardId":5,"oneCardId":6,"repayment_num":2,"add_time":"2019-11-04","start_time":"2019-11-13","end_time":"2019-11-13","orderNo":"201911041614535"}
     * plan : [{"id":16,"type":1,"orderNo":"cjmin2019110415212403","fee_money":"1.84","money":"333.84","status":0,"run_time":"2019-11-13 11:05:47"},{"id":17,"type":1,"orderNo":"cjmin2019110415212413","fee_money":"1.84","money":"333.84","status":0,"run_time":"2019-11-13 13:15:35"},{"id":18,"type":1,"orderNo":"cjmin2019110415212423","fee_money":"2.06","money":"373.15","status":0,"run_time":"2019-11-13 15:38:38"},{"id":19,"type":2,"orderNo":"cjmin2019110415212433","fee_money":"1.00","money":"1034.09","status":0,"run_time":"2019-11-13 19:01:42"},{"id":20,"type":1,"orderNo":"cjmin2019110415212443","fee_money":"1.82","money":"330.82","status":0,"run_time":"2019-11-13 14:31:04"},{"id":21,"type":1,"orderNo":"cjmin2019110415212453","fee_money":"1.81","money":"328.81","status":0,"run_time":"2019-11-13 15:50:35"},{"id":22,"type":1,"orderNo":"cjmin2019110415212463","fee_money":"1.80","money":"326.80","status":0,"run_time":"2019-11-13 16:15:30"},{"id":23,"type":2,"orderNo":"cjmin2019110415212473","fee_money":"1.00","money":"980.00","status":0,"run_time":"2019-11-13 18:06:59"}]
     * card : {"bank_num":"**** **** **** 0475","name":"招商银行","logo":"http://yx.diyunkeji.com//static/upload/5afa37e60c5a7.png","repayment_date":"17","statement_date":"10","realname":"王连君"}
     * oneCard : {"bank_num":"**** **** **** 4994","name":"中信银行","logo":"http://yx.diyunkeji.com//static/upload/5afa374339519.png","repayment_date":"15","statement_date":"4"}
     */

    public InfoBean info;
    public CardBean card;
    public Object oneCard;
    public List<PlanBean> plan;


    public static class InfoBean {
        /**
         * id : 3
         * repayment_money : 2000.00
         * fee_money : 13.17
         * has_pay_money : 0.00
         * has_repayment_money : 0.00
         * has_repayment_num : 0
         * has_fee_money : 0.00
         * has_frequency_money : 0.00
         * status : 1
         * cardId : 5
         * oneCardId : 6
         * repayment_num : 2
         * add_time : 2019-11-04
         * start_time : 2019-11-13
         * end_time : 2019-11-13
         * orderNo : 201911041614535
         */

        public int id;
        public String repayment_money;
        public Double fee_money;
        public String has_pay_money;
        public String has_repayment_money;
        public int has_repayment_num;
        public String has_fee_money;
        public String has_frequency_money;//已还款次数费
        public int status;
        public int cardId;
        public int oneCardId;
        public int repayment_num;
        public String add_time;
        public String start_time;
        public String end_time;
        public String orderNo;
        public String frequency_money;   // 还款次数费
        public String channel_type;


    }

    public static class CardBean {
        /**
         * bank_num : **** **** **** 0475
         * name : 招商银行
         * logo : http://yx.diyunkeji.com//static/upload/5afa37e60c5a7.png
         * repayment_date : 17
         * statement_date : 10
         * realname : 王连君
         */

        public String bank_num;
        public String name;
        public String logo;
        public String repayment_date;
        public String statement_date;
        public String realname;


    }

    public static class OneCardBean {
        /**
         * bank_num : **** **** **** 4994
         * name : 中信银行
         * logo : http://yx.diyunkeji.com//static/upload/5afa374339519.png
         * repayment_date : 15
         * statement_date : 4
         */

        public String bank_num;
        public String name;
        public String logo;
        public String repayment_date;
        public String statement_date;

    }

    public static class PlanBean {
        /**
         * id : 16
         * type : 1
         * orderNo : cjmin2019110415212403
         * fee_money : 1.84
         * money : 333.84
         * status : 0
         * run_time : 2019-11-13 11:05:47
         */

        public int id;
        public int type;
        public String orderNo;
        public String fee_money;
        public String money;
        public int status;
        public String run_time;


    }


}
