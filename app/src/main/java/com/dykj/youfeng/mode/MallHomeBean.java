package com.dykj.youfeng.mode;

import java.util.List;

/**
 * Created by lcjingon 2019/11/4.
 * description:
 */

public class MallHomeBean {


    public List<BannerBean> banner;
    public List<GoodsBean> goods_commend;
    public List<CategoryBean> category;
    public List<GoodsBean> goods_salenum;

    public static class BannerBean {
        /**
         * title : 商城首页Banner
         * image : http://app.zhiyoulifes.com//static/upload/0c3b102489f337f8/a825489759ebd1f8.jpg
         */

        public String title;
        public String image;
        public String url;
    }



    public static class CategoryBean {
        /**
         * gc_id : 78
         * gc_name : 热门推荐
         * parent_id : 0
         * type_pic : http://zhiyous.diyunkeji.com/Uploads/goods_class/2019-07-25/5d3926a6cb493.jpg
         * floor_desc : 全国优质工厂 放心好货
         */

        public String gc_id;
        public String gc_name;
        public String parent_id;
        public String type_pic;
        public String floor_desc;
    }

}
