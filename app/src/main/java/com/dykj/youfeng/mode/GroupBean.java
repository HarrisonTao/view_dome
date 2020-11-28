package com.dykj.youfeng.mode;

import java.util.List;

public class GroupBean {

    /**
     * page : 1
     * teamCount : 5
     * directCount : 2
     * list : [{"id":4,"nickName":"","realname":"","phone":"444****4444","regTime":"2019-10-28 14:46:17","level":3,"levelName":"黄金VIP会员"},{"id":1,"nickName":"测试","realname":"张三","phone":"111****1111","regTime":"2019-01-24 08:48:43","level":3,"levelName":"黄金VIP会员"}]
     */

    public int page;
    public int teamCount;
    public int jtteamCount;
    public int directCount;
    public double teamMonthTotalSum;
    public double teamTotalSum;

    public List<ListBean> list;

    public List<ListBean> jtlist;

    public static class ListBean {
        /**
         * id : 4
         * nickName :
         * realname :
         * phone : 444****4444
         * regTime : 2019-10-28 14:46:17
         * level : 3
         * levelName : 黄金VIP会员
         */

        public int id;
        public String nickName;
        public String realname;
        public String phone;
        public String regTime;
        public int level;
        public String levelName;
        public String tel = "";

    }
}
