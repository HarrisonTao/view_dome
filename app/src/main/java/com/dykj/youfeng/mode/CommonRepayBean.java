package com.dykj.youfeng.mode;

import java.util.List;

public class CommonRepayBean {

    /**
     * info : {"id":1,"memberId":9,"cardId":5,"repayment_money":"2000.00","fee_money":20.2,"frequency_money":"4.00","min_money":"587.75","one_num":2,"repayment_num":4,"status":1,"start_time":"2019-11-13","end_time":"2019-11-15","has_pay_money":"0.00","has_repayment_money":"0.00","has_fee_money":"0.00","has_frequency_money":"0.00","refund_money":"0.00","refund_time":0,"refund_order_no":"","refund_loanno":"","refund_info":"","add_time":"2019-11-05","add_ip":"112.229.90.95","cancel_status":0,"fail_status":0,"finish_time":0,"result_info":"","channel_type":"cj","repayment_rate":"80.00","repayment_fee":"1.00","orderNo":"cj201911051523079","channel":"cja","province_name":"北京市","city_name":"北京市","has_repayment_num":0,"is_deleted":1,"province_code":"","city_code":"","time":null,"dataFlag":1,"runningTime":0,"selectTime":0}
     * plan : [{"type":1,"money":"219.24","status":0,"run_time":"2019-11-13 09:43:45"},{"type":1,"money":"241.00","status":0,"run_time":"2019-11-13 11:58:15"},{"type":2,"money":"455.54","status":0,"run_time":"2019-11-13 13:39:55"},{"type":1,"money":"236.00","status":0,"run_time":"2019-11-13 16:23:32"},{"type":1,"money":"275.00","status":0,"run_time":"2019-11-13 18:00:07"},{"type":2,"money":"505.89","status":0,"run_time":"2019-11-13 20:16:05"},{"type":1,"money":"243.00","status":0,"run_time":"2019-11-15 09:44:33"},{"type":1,"money":"239.00","status":0,"run_time":"2019-11-15 10:32:44"},{"type":2,"money":"477.12","status":0,"run_time":"2019-11-15 14:36:23"},{"type":1,"money":"265.00","status":0,"run_time":"2019-11-15 15:55:29"},{"type":1,"money":"302.00","status":0,"run_time":"2019-11-15 19:02:06"},{"type":2,"money":"561.45","status":0,"run_time":"2019-11-15 21:31:14"}]
     */

    public InfoBean info;
    public List<PlanInfoBean.PlanBean> plan;
        public BankBean bank;
        public CredBean card;

    public static class InfoBean {
        /**
         * id : 1
         * memberId : 9
         * cardId : 5
         * repayment_money : 2000.00
         * fee_money : 20.2
         * frequency_money : 4.00
         * min_money : 587.75
         * one_num : 2
         * repayment_num : 4
         * status : 1
         * start_time : 2019-11-13
         * end_time : 2019-11-15
         * has_pay_money : 0.00
         * has_repayment_money : 0.00
         * has_fee_money : 0.00
         * has_frequency_money : 0.00
         * refund_money : 0.00
         * refund_time : 0
         * refund_order_no :
         * refund_loanno :
         * refund_info :
         * add_time : 2019-11-05
         * add_ip : 112.229.90.95
         * cancel_status : 0
         * fail_status : 0
         * finish_time : 0
         * result_info :
         * channel_type : cj
         * repayment_rate : 80.00
         * repayment_fee : 1.00
         * orderNo : cj201911051523079
         * channel : cja
         * province_name : 北京市
         * city_name : 北京市
         * has_repayment_num : 0
         * is_deleted : 1
         * province_code :
         * city_code :
         * time : null
         * dataFlag : 1
         * runningTime : 0
         * selectTime : 0
         */

        public int id;
        public int memberId;
        public int cardId;
        public String repayment_money;
        public double fee_money;
        public String frequency_money;
        public String min_money;
        public int one_num;
        public int repayment_num;
        public int status;
        public String start_time;
        public String end_time;
        public String has_pay_money;
        public String has_repayment_money;
        public String has_fee_money;
        public String has_frequency_money;
        public String refund_money;
        public int refund_time;
        public String refund_order_no;
        public String refund_loanno;
        public String refund_info;
        public String add_time;
        public String add_ip;
        public int cancel_status;
        public int fail_status;
        public int finish_time;
        public String result_info;
        public String channel_type;
        public String repayment_rate;
        public String repayment_fee;
        public String orderNo;
        public String channel;
        public String province_name;
        public String city_name;
        public int has_repayment_num;
        public int is_deleted;
        public String province_code;
        public String city_code;
        public Object time;
        public int dataFlag;
        public int runningTime;
        public int selectTime;
    }

    public static class BankBean{
            public String logo="";
            public String color="";
    }

    public static class CredBean{
        public int id;
        public String money;
        public String bank_num;
        public String bank_number;
        public String bank_name;
        public String statement_date;
        public String repayment_date;


    }

//    public static class PlanBean {
//        /**
//         * type : 1
//         * money : 219.24
//         * status : 0
//         * run_time : 2019-11-13 09:43:45
//         */
//
//        public int type;
//        public String money;
//        public int status;
//        public String run_time;
//
//
//    }
}
