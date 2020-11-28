package com.dykj.youfeng.mode;

import java.util.List;

public class HomeBean {
    public List<BannerBean> banner;
    public List<NoticeBean> notice;
    public List<FootBannerBean> foot_banner;

    public static class BannerBean {
        /**
         * title : 首页banner
         * image : http://yx.diyunkeji.com//static/upload/06f9caff7ec48bbd/99124c24df7c275a.jpg
         * url :
         */

        public String title;
        public String image;
        public String url;

    }

    public static class NoticeBean {
        /**
         * info : 您的好友【何露】进行还款，您获得0.5451元分润。
         */

        public String info;

    }

    public static class FootBannerBean {
        /**
         * title : 首页banner
         * image : http://yx.diyunkeji.com//static/xiaoed_bg.png
         * url :
         */

        public String title;
        public String image;
        public String url;
            
    }
}
