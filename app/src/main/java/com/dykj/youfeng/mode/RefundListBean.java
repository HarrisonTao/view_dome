package com.dykj.youfeng.mode;

import java.util.List;

/**
 * Created by lcjingon 2019/11/8.
 * description:
 */

public class RefundListBean {

    /**
     * now_page : 1
     * all_page : 0
     * list : [{"order_id":"1161","store_id":"121","goods_id":"61","refund_type":"0","refund_state":"0","lock_state":"1","store_name":"无抵抗","store_logo":"/Uploads/store_pic/2019-07-17/5d2e73c0d1478.png","order_goods":{"rec_id":"1212","order_id":"1161","goods_id":"61","goods_name":"t恤女大码真丝汉服中老年女装棉麻连衣裙外套女衬衫上衣雪纺衫潮8","goods_price":"50.00","goods_num":"2","goods_image":"http://zhiyous.diyunkeji.com/Uploads/2019-07-25/5d390cc4dad6b.jpg","goods_pay_price":"100.00","store_id":"121","buyer_id":"10","goods_type":"1","goods_types_id":"0","gc_id":"1137","promotions_id":null,"og_type":"0","goods_spec":null,"goods_spec_name":"","freight_id":"83","freight_type":"kuaidi","freight_fee":"5.00","refund_state":"0","goods_charge_service":null,"points_fee":"0.00","points_num":"0"}}]
     */

    public int now_page;
    public int all_page;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * order_id : 1161
         * store_id : 121
         * goods_id : 61
         * refund_type : 0
         * refund_state : 0
         * lock_state : 1
         * store_name : 无抵抗
         * store_logo : /Uploads/store_pic/2019-07-17/5d2e73c0d1478.png
         * order_goods : {"rec_id":"1212","order_id":"1161","goods_id":"61","goods_name":"t恤女大码真丝汉服中老年女装棉麻连衣裙外套女衬衫上衣雪纺衫潮8","goods_price":"50.00","goods_num":"2","goods_image":"http://zhiyous.diyunkeji.com/Uploads/2019-07-25/5d390cc4dad6b.jpg","goods_pay_price":"100.00","store_id":"121","buyer_id":"10","goods_type":"1","goods_types_id":"0","gc_id":"1137","promotions_id":null,"og_type":"0","goods_spec":null,"goods_spec_name":"","freight_id":"83","freight_type":"kuaidi","freight_fee":"5.00","refund_state":"0","goods_charge_service":null,"points_fee":"0.00","points_num":"0"}
         */

        public String order_id;
        public String store_id;
        public String goods_id;
        public String refund_type;
        public String refund_state;
        public String lock_state;
        public String store_name;
        public String store_logo;
        public OrderGoodsBean order_goods;

        public static class OrderGoodsBean {
            /**
             * rec_id : 1212
             * order_id : 1161
             * goods_id : 61
             * goods_name : t恤女大码真丝汉服中老年女装棉麻连衣裙外套女衬衫上衣雪纺衫潮8
             * goods_price : 50.00
             * goods_num : 2
             * goods_image : http://zhiyous.diyunkeji.com/Uploads/2019-07-25/5d390cc4dad6b.jpg
             * goods_pay_price : 100.00
             * store_id : 121
             * buyer_id : 10
             * goods_type : 1
             * goods_types_id : 0
             * gc_id : 1137
             * promotions_id : null
             * og_type : 0
             * goods_spec : null
             * goods_spec_name :
             * freight_id : 83
             * freight_type : kuaidi
             * freight_fee : 5.00
             * refund_state : 0
             * goods_charge_service : null
             * points_fee : 0.00
             * points_num : 0
             */

            public String rec_id;
            public String order_id;
            public String goods_id;
            public String goods_name;
            public String goods_price;
            public String goods_num;
            public String goods_image;
            public String goods_pay_price;
            public String store_id;
            public String buyer_id;
            public String goods_type;
            public String goods_types_id;
            public String gc_id;
            public Object promotions_id;
            public String og_type;
            public Object goods_spec;
            public String goods_spec_name;
            public String freight_id;
            public String freight_type;
            public String freight_fee;
            public String refund_state;
            public Object goods_charge_service;
            public String points_fee;
            public String points_num;
        }
    }
}
