package com.dykj.youfeng.mode;

import java.util.List;

/**
 * Created by lcjingon 2019/11/5.
 * description:
 */

public class CarListBean {

    /**
     * now_page : 1
     * all_page : 1
     * list : [{"store_info":{"store_id":"121","store_name":"无抵抗","store_logo":"/Uploads/store_pic/2019-07-17/5d2e73c0d1478.png"},"goods_info":[{"id":"468","member_id":"11","member_name":"","goods_id":"65","goods_name":"亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣 ","goods_price":"55.00","goods_img":"http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg","goods_num":"1","goods_amount":"55.00","store_id":"121","store_name":"无抵抗","add_time":"1572862379","goods_spec":"42_635","goods_spec_name":"颜色:白色","is_order":null,"goods_type":"1","probuy_id":null,"is_form":"0","goods_charge_service":null}]}]
     */

    public int now_page;
    public int all_page;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * store_info : {"store_id":"121","store_name":"无抵抗","store_logo":"/Uploads/store_pic/2019-07-17/5d2e73c0d1478.png"}
         * goods_info : [{"id":"468","member_id":"11","member_name":"","goods_id":"65","goods_name":"亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣 ","goods_price":"55.00","goods_img":"http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg","goods_num":"1","goods_amount":"55.00","store_id":"121","store_name":"无抵抗","add_time":"1572862379","goods_spec":"42_635","goods_spec_name":"颜色:白色","is_order":null,"goods_type":"1","probuy_id":null,"is_form":"0","goods_charge_service":null}]
         */

        public StoreInfoBean store_info;
        public List<GoodsInfoBean> goods_info;

        public static class StoreInfoBean {
            /**
             * store_id : 121
             * store_name : 无抵抗
             * store_logo : /Uploads/store_pic/2019-07-17/5d2e73c0d1478.png
             */

            public String store_id;
            public String store_name;
            public String store_logo;
        }


    }
    public static class GoodsInfoBean {
        /**
         * id : 468
         * member_id : 11
         * member_name :
         * goods_id : 65
         * goods_name : 亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣
         * goods_price : 55.00
         * goods_img : http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg
         * goods_num : 1
         * goods_amount : 55.00
         * store_id : 121
         * store_name : 无抵抗
         * add_time : 1572862379
         * goods_spec : 42_635
         * goods_spec_name : 颜色:白色
         * is_order : null
         * goods_type : 1
         * probuy_id : null
         * is_form : 0
         * goods_charge_service : null
         */

        public String id;
        public String member_id;
        public String member_name;
        public String goods_id;
        public String goods_name;
        public String goods_price;
        public String goods_img;
        public String goods_num;
        public String goods_amount;
        public String store_id;
        public String store_name;
        public String add_time;
        public String goods_spec;
        public String goods_spec_name;
        public String is_order;
        public String goods_type;
        public String probuy_id;
        public String is_form;
        public String goods_charge_service;
        public boolean select;
        public String goods_state;
    }
}
