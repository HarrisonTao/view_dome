package com.dykj.youfeng.mode;

import java.util.List;

public class SignListBean {


    /**
     * info : [{"id":4,"memberId":7,"affectPoints":"8.00","accountPoints":"13.00","info":"签到奖励积分","add_time":"2019-11-08 10:12:01","add_ip":""},{"id":2,"memberId":7,"affectPoints":"5.00","accountPoints":"5.00","info":"签到奖励积分","add_time":"2019-11-01 10:04:55","add_ip":""}]
     * count : 2
     */

    public int count;
    public List<InfoBean> info;


    public static class InfoBean {
        /**
         * id : 4
         * memberId : 7
         * affectPoints : 8.00
         * accountPoints : 13.00
         * info : 签到奖励积分
         * add_time : 2019-11-08 10:12:01
         * add_ip :
         */

        public int id;
        public int memberId;
        public String affectPoints;
        public String accountPoints;
        public String info;
        public String add_time;
        public String add_ip;
        public String sumPoints;
    }
}
