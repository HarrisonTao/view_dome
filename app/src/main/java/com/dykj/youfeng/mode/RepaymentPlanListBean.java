package com.dykj.youfeng.mode;

import java.util.List;

public class RepaymentPlanListBean {

    /**
     * page : 1
     * list : [{"id":3,"cardId":5,"oneCardId":6,"add_time":"2019-11-04 15:21:24","fee_money":"13.17","has_pay_money":"0.00","has_repayment_money":"0.00","status":1,"repayment_money":"2000.00","fail_status":0,"cancel_status":0,"bank_name":"招商银行","bank_num":"0475","logo":"http://yx.diyunkeji.com//static/upload/5afa37e60c5a7.png","color":"#e64646"}]
     */

    public int page;
    public List<ListBean> list;


    public static class ListBean {
        /**
         * id : 3
         * cardId : 5
         * oneCardId : 6
         * add_time : 2019-11-04 15:21:24
         * fee_money : 13.17
         * has_pay_money : 0.00
         * has_repayment_money : 0.00
         * status : 1
         * repayment_money : 2000.00
         * fail_status : 0
         * cancel_status : 0
         * bank_name : 招商银行
         * bank_num : 0475
         * logo : http://yx.diyunkeji.com//static/upload/5afa37e60c5a7.png
         * color : #e64646
         */

        public int id;
        public int cardId;
        public int oneCardId;
        public String add_time;
        public String fee_money;
        public String has_pay_money;
        public String has_repayment_money;
        public int status;
        public String repayment_money;
        public int fail_status;
        public int cancel_status;
        public String bank_name;
        public String bank_num;
        public String logo;
        public String color;
        public String min_money;
        public String frequency_money;
        public String channel_type;

    }
}
