package com.dykj.youfeng.mode;

import java.util.List;

public class VipBean {

    /**
     * needReCount : 0
     * reCount : 0
     * hasReCount : 0
     * level : 1
     * levelName : 普通会员
     * levelList : [{"levelName":"普通会员","levelPrice":0,"levelUpInfo":"免费会员","is_pay":1,"repayRateMax":"80","repayRateMin":"70","quickRate":"65","repayCost":"1","quickCost":"2"},{"levelName":"VIP会员","levelPrice":"198","levelUpInfo":"￥198元升级","is_pay":0,"repayRateMax":"70","repayRateMin":"50","quickRate":"60","repayCost":"1","quickCost":"2"},{"levelName":"黄金VIP会员","levelPrice":0,"levelUpInfo":"直推2名VIP会员升级","is_pay":0,"repayRateMax":"70","repayRateMin":"50","repayCost":"1","quickCost":"2"},{"levelName":"钻石VIP会员","levelPrice":0,"levelUpInfo":"直推5名VIP会员升级","is_pay":0,"repayRateMax":"70","repayRateMin":"50","repayCost":"1","quickCost":"2"}]
     */

    public String needReCount;
    public int reCount;
    public int hasReCount;
    public String level;
    public String levelName;
    public LevelListBean levelList;


    public static class LevelListBean {
        /**
         * levelName : 普通会员
         * levelPrice : 0
         * levelUpInfo : 免费会员
         * is_pay : 1
         * repayRateMax : 80
         * repayRateMin : 70
         * quickRate : 65
         * repayCost : 1
         * quickCost : 2
         */

        public String levelName;
        public double levelPrice;
        public String levelUpInfo;
        public String is_pay;
        public String repayRateMax;
        public String repayRateMin;
        public String quickRate;
        public String repayCost;
        public String quickCost;
        public String isShow;
        public String levelId;
           
    }
}
