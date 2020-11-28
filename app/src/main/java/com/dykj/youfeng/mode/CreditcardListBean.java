package com.dykj.youfeng.mode;

import java.io.Serializable;
import java.util.List;

public class CreditcardListBean {

    /**
     * page : 1
     * list : [{"id":2,"bank_name":"浦发银行","repayment_status":0,"bank_num":"4111","statement_date":"5","repayment_date":"14","money":"30000.00","validity":"2410","cvn":"366","is_cj":0,"agreeid":"0","logo":"http://yx.diyunkeji.com/static/upload/5afa382ea1179.png","color":"#456fdc","cj_type":"110002","code":"110002","merchantCode":"0","phone":"13853100685","idcard":"371522199409255721","day":14}]
     */

    public int page;
    public List<ListBean> list;


    public static class ListBean implements Serializable {
        /**
         * repayment_money
         * id : 2
         * bank_name : 浦发银行
         * repayment_status : 0
         * bank_num : 4111
         * statement_date : 5
         * repayment_date : 14
         * money : 30000.00
         * validity : 2410
         * cvn : 366
         * is_cj : 0
         * agreeid : 0
         * logo : http://yx.diyunkeji.com/static/upload/5afa382ea1179.png
         * color : #456fdc
         * cj_type : 110002
         * code : 110002
         * merchantCode : 0
         * phone : 13853100685
         * idcard : 371522199409255721
         * day : 14
         * repayment_type
         */

        public String repayment_money;
        public int id = -1;
        public String bank_name;
        public int repayment_status;
        public String bank_num;
        public String statement_date;
        public String repayment_date;
        public String money;
        public String validity;
        public String cvn;
        public int is_cj;
        public String agreeid;
        public String logo;
        public String color;
        public String cj_type;
        public String code;
        public String merchantCode;
        public String phone;
        public String idcard;
        public int day;
        public String channel;
        public int repayment_type;   // 还款类型   1 是正常还款 2是宜卡多换
        public String url;

        public String card_bank_num;

        public String repayment_id;
        /**
         * 背景图
         */
        public int viewShape=0;


        public ListBean(String bank_name) {
            this.bank_name = bank_name;
        }
    }
}
