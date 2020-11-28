package com.dykj.youfeng.mode;

import java.util.List;

public class BonusIndexBean {
    /**
     * info : {"id":389,"name":"bonusAll","value":"10000","dataFlag":1,"notice":"月奖金","day_start":"2019-11-01 00:00:00","day":"2019-11-11 08:45:24","day_last":"2019-11-30 00:00:00"}
     * periods : 2019/11
     * month : 11
     * periods_list : ["2019/1","2019/2","2019/3","2019/4","2019/5","2019/6","2019/7","2019/8","2019/9","2019/10","2019/11","2019/12"]
     */

    public InfoBean info;
    public String periods;
    public String month;
    public List<String> periods_list;


    public static class InfoBean {
        /**
         * id : 389
         * name : bonusAll
         * value : 10000
         * dataFlag : 1
         * notice : 月奖金
         * day_start : 2019-11-01 00:00:00
         * day : 2019-11-11 08:45:24
         * day_last : 2019-11-30 00:00:00
         */

        public int id;
        public String name;
        public String value;
        public int dataFlag;
        public String notice;
        public String day_start;
        public String day;
        public String day_last;
        public String day_now;
        public String day_end;

            
    }
}
