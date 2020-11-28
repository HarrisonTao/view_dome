package com.dykj.youfeng.mode;

import java.util.List;

public class CashApplyBean {
    /**
     * debit_card : [{"id":412,"bank_num":"6226 **** **** 6118","bank_name":"民生银行"}]
     * accountMoney : 604.00
     * memberCashFee : 2
     * memberCashTax : 6
     * info : 每笔提现金额最低5元,扣税点6%,提现每笔手续费2元。
     */

    public String accountMoney;
    public String memberCashFee;
    public String memberCashTax;
    public String info;
    public String cashMinMoney;

    public List<DebitCardBean> debit_card;


    public static class DebitCardBean {
        /**
         * id : 412
         * bank_num : 6226 **** **** 6118
         * bank_name : 民生银行
         */

        public int id;
        public String bank_num;
        public String bank_name;

           
    }
}
