package com.dykj.youfeng.mode;

import java.util.List;

public class ShareBean {
        /**
         * page : 1
         * list : [{"id":4,"nickName":"测试","realname":"测试","phone":"444****4444","regTime":"2019-10-28 14:46:17","level":3,"levelName":"黄金VIP会员"},{"id":1,"nickName":"测试","realname":"张三","phone":"111****1111","regTime":"2019-01-24 08:48:43","level":3,"levelName":"黄金VIP会员"}]
         */

        public int page;
        public List<ListBean> list;

        public static class ListBean {
            /**
             * id : 4
             * nickName : 测试
             * realname : 测试
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
            
    }
}
