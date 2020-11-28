package com.dykj.youfeng.mode;

import java.util.List;

public class IndexBillList {

    /**
     * page : 2
     * list : [{"id":64018,"memberId":7,"type":3,"money":"200.00","fee_money":"3.20","out_bank_num":"6225768608467047","in_bank_num":"6214835413654516","info":"信用卡畅捷渠道快捷收款成功！","add_time":"2019-11-07 17:18:24","add_ip":"123.232.6.106","bank_name":"招商银行"},{"id":14,"memberId":7,"type":1,"money":"279.00","fee_money":"1.00","out_bank_num":"4392268327334562","in_bank_num":"4392268327334562","info":"信用卡代还还款成功","add_time":"2019-02-12 08:52:42","add_ip":"::1","bank_name":null},{"id":13,"memberId":7,"type":1,"money":"143.09","fee_money":"1.09","out_bank_num":"4392268327334562","in_bank_num":"4392268327334562","info":"信用卡代还消费成功","add_time":"2019-02-12 08:49:14","add_ip":"::1","bank_name":null},{"id":12,"memberId":7,"type":1,"money":"138.05","fee_money":"1.05","out_bank_num":"4392268327334562","in_bank_num":"4392268327334562","info":"信用卡代还消费成功","add_time":"2019-02-12 08:41:40","add_ip":"::1","bank_name":null},{"id":11,"memberId":7,"type":1,"money":"150.00","fee_money":"1.00","out_bank_num":"4392268327334562","in_bank_num":"4392268327334562","info":"信用卡代还还款成功","add_time":"2019-02-07 15:10:04","add_ip":"::1","bank_name":null},{"id":10,"memberId":7,"type":1,"money":"150.00","fee_money":"1.00","out_bank_num":"4392268327334562","in_bank_num":"4392268327334562","info":"信用卡代还还款成功","add_time":"2019-02-07 14:06:37","add_ip":"::1","bank_name":null},{"id":8,"memberId":7,"type":1,"money":"124.69","fee_money":"0.69","out_bank_num":"4392268327334562","in_bank_num":"4392268327334562","info":"信用卡代还消费成功","add_time":"2019-02-07 13:48:44","add_ip":"::1","bank_name":null},{"id":7,"memberId":7,"type":1,"money":"105.58","fee_money":"0.58","out_bank_num":"4392268327334562","in_bank_num":"4392268327334562","info":"信用卡代还消费成功","add_time":"2019-02-07 13:48:35","add_ip":"::1","bank_name":null},{"id":6,"memberId":7,"type":1,"money":"123.00","fee_money":"1.00","out_bank_num":"4392268327334562","in_bank_num":"4392268327334562","info":"信用卡代还还款成功","add_time":"2019-02-07 12:43:59","add_ip":"::1","bank_name":null},{"id":5,"memberId":7,"type":1,"money":"124.69","fee_money":"0.69","out_bank_num":"4392268327334562","in_bank_num":"4392268327334562","info":"信用卡代还消费成功","add_time":"2019-02-06 23:10:26","add_ip":"::1","bank_name":null}]
     */

    public int page;
    public List<ListBean> list;


    public static class ListBean {
        /**
         * id : 64018
         * memberId : 7
         * type : 3
         * money : 200.00
         * fee_money : 3.20
         * out_bank_num : 6225768608467047
         * in_bank_num : 6214835413654516
         * info : 信用卡畅捷渠道快捷收款成功！
         * add_time : 2019-11-07 17:18:24
         * add_ip : 123.232.6.106
         * bank_name : 招商银行
         */

        public int id;
        public int memberId;
        public int type;
        public String money;
        public String fee_money;
        public String out_bank_num;
        public String in_bank_num;
        public String info;
        public String add_time;
        public String add_ip;
        public String bank_name;

    }
}
