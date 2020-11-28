package com.dykj.youfeng.mode;

import java.util.List;

public class SystemMegBean {
        /**
         * page : 1
         * list : [{"id":5,"title":"智优生活怎么玩？","content":"http://yx.diyunkeji.com/api/article/content.html?articleId=5","add_time":"2019-08-11 01:04:05"}]
         */

        public int page;
        public List<ListBean> list;

       
        public static class ListBean {
            /**
             * id : 5
             * title : 智优生活怎么玩？
             * content : http://yx.diyunkeji.com/api/article/content.html?articleId=5
             * add_time : 2019-08-11 01:04:05
             */

            public int id;
            public String title;
            public String content;
            public String add_time;
            public String url;
            public int is_read;
    }
}
