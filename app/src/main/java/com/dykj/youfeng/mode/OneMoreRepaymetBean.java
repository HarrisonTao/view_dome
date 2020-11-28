package com.dykj.youfeng.mode;

import java.io.Serializable;
import java.util.List;

public class OneMoreRepaymetBean implements Serializable {

    /**
     * card : {"bank_num":"**** **** **** 0475","name":"招商银行","logo":"http://yx.diyunkeji.com//static/upload/5afa37e60c5a7.png","repayment_date":"17","statement_date":"10","realname":"王连君"}
     * oneCard : {"bank_num":"**** **** **** 4994","name":"中信银行","logo":"http://yx.diyunkeji.com//static/upload/5afa374339519.png","repayment_date":"15","statement_date":"4"}
     * repayment : {"repaymentMoney":2000,"repaymentFee":2,"paymentFee":11.18,"feeMoney":13.18,"minMoney":1043.28,"plan":[{"sort":1,"type":"1","run_time":1573607883,"money":339.87,"fee_money":1.87},{"sort":2,"type":"1","run_time":1573621041,"money":334.85,"fee_money":1.85},{"sort":3,"type":"1","run_time":1573625469,"money":362.1,"fee_money":2},{"sort":4,"type":"2","run_time":1573626891,"money":1030.1,"fee_money":1},{"sort":5,"type":"1","run_time":1573623397,"money":333.84,"fee_money":1.84},{"sort":6,"type":"1","run_time":1573626606,"money":328.81,"fee_money":1.81},{"sort":7,"type":"1","run_time":1573630764,"money":327.81,"fee_money":1.81},{"sort":8,"type":"2","run_time":1573639982,"money":984,"fee_money":1}],"startDate":"2019-11-13","endDate":"2019-11-13","allNum":2}
     */

    public CardBean card;
    public Object oneCard;
    public RepaymentBean repayment;


    public static class CardBean implements Serializable{
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
        public String mCreditcardId;

    }

    public static class OneCardBean implements Serializable{
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

    public static class RepaymentBean implements Serializable{
        /**
         * repaymentMoney : 2000
         * repaymentFee : 2
         * paymentFee : 11.18
         * feeMoney : 13.18
         * minMoney : 1043.28
         * plan : [{"sort":1,"type":"1","run_time":1573607883,"money":339.87,"fee_money":1.87},{"sort":2,"type":"1","run_time":1573621041,"money":334.85,"fee_money":1.85},{"sort":3,"type":"1","run_time":1573625469,"money":362.1,"fee_money":2},{"sort":4,"type":"2","run_time":1573626891,"money":1030.1,"fee_money":1},{"sort":5,"type":"1","run_time":1573623397,"money":333.84,"fee_money":1.84},{"sort":6,"type":"1","run_time":1573626606,"money":328.81,"fee_money":1.81},{"sort":7,"type":"1","run_time":1573630764,"money":327.81,"fee_money":1.81},{"sort":8,"type":"2","run_time":1573639982,"money":984,"fee_money":1}]
         * startDate : 2019-11-13
         * endDate : 2019-11-13
         * allNum : 2
         */

        public double repaymentMoney;
        public double repaymentFee;
        public double paymentFee;
        public double feeMoney;
        public double minMoney;
        public String startDate;
        public String endDate;
        public int allNum;
        public List<PlanBean> plan;

    }
}
