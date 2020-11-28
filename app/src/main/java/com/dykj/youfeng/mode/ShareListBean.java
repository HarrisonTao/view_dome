package com.dykj.youfeng.mode;

import java.util.List;

public class ShareListBean {
    /**
     * shareTitle : 快还生活
     * shareContent : 移动支付生态链综合服务商，智能还款，美化账单，养卡提额，提高信用。
     * url : http://kaduoduo.diyunkeji.com/api/share/login?key=485
     * list : [{"image":"http://kaduoduo.diyunkeji.com/static/upload/75061a008afce17f/9d5d57a0fad781b5.jpg"},{"image":"http://kaduoduo.diyunkeji.com/static/upload/cf315f31f07fe774/474ef9c956a21258.jpg"},{"image":"http://kaduoduo.diyunkeji.com/static/upload/f8c038d7e9d41096/527aab050a5714d6.jpg"}]
     */

    public String shareTitle;
    public String shareContent;
    public String url;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * image : http://kaduoduo.diyunkeji.com/static/upload/75061a008afce17f/9d5d57a0fad781b5.jpg
         */
        public String image;

    }
}
