package com.dykj.youfeng.mode;

import java.util.List;

public class RepaymentPreviewPlanBean {
    /**
     * repayment_money : 2000.00
     * frequency_money : 4
     * fee_money : 16.18
     * all_fee_money : 2004
     * min_money : 572.60
     * channel_type : cj
     * plan : [{"repayment_time":"2019-11-12 09:46:29","repayment_money":"462.20","repayment_item_type":1,"mcc_class_id":"M013"},{"repayment_time":"2019-11-12 14:21:39","repayment_money":"457.49","repayment_item_type":2,"mcc_class_id":"M013"},{"repayment_time":"2019-11-12 17:17:43","repayment_money":"513.00","repayment_item_type":1,"mcc_class_id":"M013"},{"repayment_time":"2019-11-12 20:15:03","repayment_money":"507.89","repayment_item_type":2,"mcc_class_id":"M013"},{"repayment_time":"2019-11-13 11:08:18","repayment_money":"493.00","repayment_item_type":1,"mcc_class_id":"M013"},{"repayment_time":"2019-11-13 14:15:50","repayment_money":"488.04","repayment_item_type":2,"mcc_class_id":"M013"},{"repayment_time":"2019-11-13 16:02:13","repayment_money":"552.00","repayment_item_type":1,"mcc_class_id":"M013"},{"repayment_time":"2019-11-13 18:32:54","repayment_money":"546.58","repayment_item_type":2,"mcc_class_id":"M013"}]
     * card : {"id":5,"memberId":9,"money":"30000.00","bank_num":"**** **** **** 0475","bank_number":"308584000013","bank_name":"招商银行","validity":"2511","cvn":"918","statement_date":"10","repayment_date":"17","phone":"13953139569","add_time":"2019-11-04 09:59:47","add_ip":"112.229.90.95","repayment_status":0,"province":"","city":"","address":"","agreeid":"0","is_cj":0,"is_deleted":1,"signStatus":0,"dataFlag":1,"logo":"http://yx.diyunkeji.com/static/upload/5afa37e60c5a7.png","color":"#e64646"}
     */

    public String repayment_money;
    public double frequency_money;
    public String fee_money;
    public double all_fee_money;
    public String min_money;
    public String channel_type;
    public CardBean card;
    public List<PlanBean> plan;


    public static class CardBean {
        /**
         * id : 5
         * memberId : 9
         * money : 30000.00
         * bank_num : **** **** **** 0475
         * bank_number : 308584000013
         * bank_name : 招商银行
         * validity : 2511
         * cvn : 918
         * statement_date : 10
         * repayment_date : 17
         * phone : 13953139569
         * add_time : 2019-11-04 09:59:47
         * add_ip : 112.229.90.95
         * repayment_status : 0
         * province :
         * city :
         * address :
         * agreeid : 0
         * is_cj : 0
         * is_deleted : 1
         * signStatus : 0
         * dataFlag : 1
         * logo : http://yx.diyunkeji.com/static/upload/5afa37e60c5a7.png
         * color : #e64646
         */

        public int id;
        public int memberId;
        public String money;
        public String bank_num;
        public String bank_number;
        public String bank_name;
        public String validity;
        public String cvn;
        public String statement_date;
        public String repayment_date;
        public String phone;
        public String add_time;
        public String add_ip;
        public int repayment_status;
        public String province;
        public String city;
        public String address;
        public String agreeid;
        public int is_cj;
        public int is_deleted;
        public int signStatus;
        public int dataFlag;
        public String logo;
        public String color;

    }

    public static class PlanBean {
        /**
         * repayment_time : 2019-11-12 09:46:29
         * repayment_money : 462.20
         * repayment_item_type : 1
         * mcc_class_id : M013
         */

        public String repayment_time;
        public String repayment_money;
        public int repayment_item_type;
        public String mcc_class_id;
        public String repayment_fee_money;
        
    }
}
