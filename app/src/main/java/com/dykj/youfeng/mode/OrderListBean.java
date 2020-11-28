package com.dykj.youfeng.mode;

import java.util.List;

/**
 * Created by lcjingon 2019/11/7.
 * description:
 */

public class OrderListBean {

    /**
     * now_page : 1
     * all_page : 1
     * list : {"order_detail":{"order_list":{"order_info":[{"order_id":"1154","order_sn":"2000000000121701","pay_sn":"580626437991988010","store_id":"121","store_name":"无抵抗","buyer_id":"10","add_time":"1573093991","payment_code":null,"payment_time":null,"shipping_time":null,"finnshed_time":"0","goods_amount":60,"shipping_fee":"5.00","order_state":"10","refund_state":"0","lock_state":"0","delete_state":"0","evaluation_state":"0","refund_amount":"0.00","delay_time":"0","shipping_code":"","freight_id_type":"83_kuaidi","tousu_state":"0","shipping_name":null,"order_desc":"order_detail","points_fee":"0.00","points_num":"0","goods_count":"1","order_goods":[{"rec_id":"1201","order_id":"1154","goods_id":"65","goods_name":"亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣 ","goods_price":"55.00","goods_num":"1","goods_image":"http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg","goods_pay_price":"55.00","store_id":"121","buyer_id":"10","goods_type":"1","goods_types_id":"0","gc_id":"1136","promotions_id":null,"og_type":"0","goods_spec":"","goods_spec_name":"","freight_id":"83","freight_type":"kuaidi","freight_fee":"5.00","refund_state":0,"goods_charge_service":"0","points_fee":"0.00","points_num":"0","refund_money":"55.00"}]},{"order_id":"1153","order_sn":"2000000000121601","pay_sn":"860626437931119010","store_id":"121","store_name":"无抵抗","buyer_id":"10","add_time":"1573093931","payment_code":null,"payment_time":null,"shipping_time":null,"finnshed_time":"0","goods_amount":60,"shipping_fee":"5.00","order_state":"10","refund_state":"0","lock_state":"0","delete_state":"0","evaluation_state":"0","refund_amount":"0.00","delay_time":"0","shipping_code":"","freight_id_type":"83_kuaidi","tousu_state":"0","shipping_name":null,"order_desc":"order_detail","points_fee":"0.00","points_num":"0","goods_count":"1","order_goods":[{"rec_id":"1200","order_id":"1153","goods_id":"65","goods_name":"亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣 ","goods_price":"55.00","goods_num":"1","goods_image":"http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg","goods_pay_price":"55.00","store_id":"121","buyer_id":"10","goods_type":"1","goods_types_id":"0","gc_id":"1136","promotions_id":null,"og_type":"0","goods_spec":"","goods_spec_name":"","freight_id":"83","freight_type":"kuaidi","freight_fee":"5.00","refund_state":0,"goods_charge_service":"0","points_fee":"0.00","points_num":"0","refund_money":"55.00"}]}]}}}
     */

    public int now_page;
    public int all_page;
    public ListBean list;

    public static class ListBean {
        /**
         * order_detail : {"order_list":{"order_info":[{"order_id":"1154","order_sn":"2000000000121701","pay_sn":"580626437991988010","store_id":"121","store_name":"无抵抗","buyer_id":"10","add_time":"1573093991","payment_code":null,"payment_time":null,"shipping_time":null,"finnshed_time":"0","goods_amount":60,"shipping_fee":"5.00","order_state":"10","refund_state":"0","lock_state":"0","delete_state":"0","evaluation_state":"0","refund_amount":"0.00","delay_time":"0","shipping_code":"","freight_id_type":"83_kuaidi","tousu_state":"0","shipping_name":null,"order_desc":"order_detail","points_fee":"0.00","points_num":"0","goods_count":"1","order_goods":[{"rec_id":"1201","order_id":"1154","goods_id":"65","goods_name":"亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣 ","goods_price":"55.00","goods_num":"1","goods_image":"http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg","goods_pay_price":"55.00","store_id":"121","buyer_id":"10","goods_type":"1","goods_types_id":"0","gc_id":"1136","promotions_id":null,"og_type":"0","goods_spec":"","goods_spec_name":"","freight_id":"83","freight_type":"kuaidi","freight_fee":"5.00","refund_state":0,"goods_charge_service":"0","points_fee":"0.00","points_num":"0","refund_money":"55.00"}]},{"order_id":"1153","order_sn":"2000000000121601","pay_sn":"860626437931119010","store_id":"121","store_name":"无抵抗","buyer_id":"10","add_time":"1573093931","payment_code":null,"payment_time":null,"shipping_time":null,"finnshed_time":"0","goods_amount":60,"shipping_fee":"5.00","order_state":"10","refund_state":"0","lock_state":"0","delete_state":"0","evaluation_state":"0","refund_amount":"0.00","delay_time":"0","shipping_code":"","freight_id_type":"83_kuaidi","tousu_state":"0","shipping_name":null,"order_desc":"order_detail","points_fee":"0.00","points_num":"0","goods_count":"1","order_goods":[{"rec_id":"1200","order_id":"1153","goods_id":"65","goods_name":"亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣 ","goods_price":"55.00","goods_num":"1","goods_image":"http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg","goods_pay_price":"55.00","store_id":"121","buyer_id":"10","goods_type":"1","goods_types_id":"0","gc_id":"1136","promotions_id":null,"og_type":"0","goods_spec":"","goods_spec_name":"","freight_id":"83","freight_type":"kuaidi","freight_fee":"5.00","refund_state":0,"goods_charge_service":"0","points_fee":"0.00","points_num":"0","refund_money":"55.00"}]}]}}
         */

        public OrderDetailBean order_detail;


    }

    public static class OrderDetailBean {
        /**
         * order_list : {"order_info":[{"order_id":"1154","order_sn":"2000000000121701","pay_sn":"580626437991988010","store_id":"121","store_name":"无抵抗","buyer_id":"10","add_time":"1573093991","payment_code":null,"payment_time":null,"shipping_time":null,"finnshed_time":"0","goods_amount":60,"shipping_fee":"5.00","order_state":"10","refund_state":"0","lock_state":"0","delete_state":"0","evaluation_state":"0","refund_amount":"0.00","delay_time":"0","shipping_code":"","freight_id_type":"83_kuaidi","tousu_state":"0","shipping_name":null,"order_desc":"order_detail","points_fee":"0.00","points_num":"0","goods_count":"1","order_goods":[{"rec_id":"1201","order_id":"1154","goods_id":"65","goods_name":"亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣 ","goods_price":"55.00","goods_num":"1","goods_image":"http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg","goods_pay_price":"55.00","store_id":"121","buyer_id":"10","goods_type":"1","goods_types_id":"0","gc_id":"1136","promotions_id":null,"og_type":"0","goods_spec":"","goods_spec_name":"","freight_id":"83","freight_type":"kuaidi","freight_fee":"5.00","refund_state":0,"goods_charge_service":"0","points_fee":"0.00","points_num":"0","refund_money":"55.00"}]},{"order_id":"1153","order_sn":"2000000000121601","pay_sn":"860626437931119010","store_id":"121","store_name":"无抵抗","buyer_id":"10","add_time":"1573093931","payment_code":null,"payment_time":null,"shipping_time":null,"finnshed_time":"0","goods_amount":60,"shipping_fee":"5.00","order_state":"10","refund_state":"0","lock_state":"0","delete_state":"0","evaluation_state":"0","refund_amount":"0.00","delay_time":"0","shipping_code":"","freight_id_type":"83_kuaidi","tousu_state":"0","shipping_name":null,"order_desc":"order_detail","points_fee":"0.00","points_num":"0","goods_count":"1","order_goods":[{"rec_id":"1200","order_id":"1153","goods_id":"65","goods_name":"亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣 ","goods_price":"55.00","goods_num":"1","goods_image":"http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg","goods_pay_price":"55.00","store_id":"121","buyer_id":"10","goods_type":"1","goods_types_id":"0","gc_id":"1136","promotions_id":null,"og_type":"0","goods_spec":"","goods_spec_name":"","freight_id":"83","freight_type":"kuaidi","freight_fee":"5.00","refund_state":0,"goods_charge_service":"0","points_fee":"0.00","points_num":"0","refund_money":"55.00"}]}]}
         */

        public OrderList order_list;
    }

    public static class OrderList {
        public List<OrderInfoBean> order_info;

    }

    public static class OrderInfoBean {
        /**
         * order_id : 1154
         * order_sn : 2000000000121701
         * pay_sn : 580626437991988010
         * store_id : 121
         * store_name : 无抵抗
         * buyer_id : 10
         * add_time : 1573093991
         * payment_code : null
         * payment_time : null
         * shipping_time : null
         * finnshed_time : 0
         * goods_amount : 60
         * shipping_fee : 5.00
         * order_state : 10
         * refund_state : 0
         * lock_state : 0
         * delete_state : 0
         * evaluation_state : 0
         * refund_amount : 0.00
         * delay_time : 0
         * shipping_code :
         * freight_id_type : 83_kuaidi
         * tousu_state : 0
         * shipping_name : null
         * order_desc : order_detail
         * points_fee : 0.00
         * points_num : 0
         * goods_count : 1
         * order_goods : [{"rec_id":"1201","order_id":"1154","goods_id":"65","goods_name":"亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣 ","goods_price":"55.00","goods_num":"1","goods_image":"http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg","goods_pay_price":"55.00","store_id":"121","buyer_id":"10","goods_type":"1","goods_types_id":"0","gc_id":"1136","promotions_id":null,"og_type":"0","goods_spec":"","goods_spec_name":"","freight_id":"83","freight_type":"kuaidi","freight_fee":"5.00","refund_state":0,"goods_charge_service":"0","points_fee":"0.00","points_num":"0","refund_money":"55.00"}]
         */

        public String order_id;
        public String order_sn;
        public String pay_sn;
        public String store_id;
        public String store_name;
        public String buyer_id;
        public String add_time;
        public String payment_code;
        public String payment_time;
        public String shipping_time;
        public String finnshed_time;
        public String goods_amount;
        public String shipping_fee;
        public String order_state;
        public String refund_state;
        public String lock_state;
        public String delete_state;
        public String evaluation_state;
        public String refund_amount;
        public String delay_time;
        public String shipping_code;
        public String freight_id_type;
        public String tousu_state;
        public String shipping_name;
        public String order_desc;
        public String points_fee;
        public String points_num;
        public String goods_count;
        public List<OrderGoodsBean> order_goods;

        public static class OrderGoodsBean {
            /**
             * rec_id : 1201
             * order_id : 1154
             * goods_id : 65
             * goods_name : 亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣
             * goods_price : 55.00
             * goods_num : 1
             * goods_image : http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg
             * goods_pay_price : 55.00
             * store_id : 121
             * buyer_id : 10
             * goods_type : 1
             * goods_types_id : 0
             * gc_id : 1136
             * promotions_id : null
             * og_type : 0
             * goods_spec :
             * goods_spec_name :
             * freight_id : 83
             * freight_type : kuaidi
             * freight_fee : 5.00
             * refund_state : 0
             * goods_charge_service : 0
             * points_fee : 0.00
             * points_num : 0
             * refund_money : 55.00
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
            public String promotions_id;
            public String og_type;
            public String goods_spec;
            public String goods_spec_name;
            public String freight_id;
            public String freight_type;
            public String freight_fee;
            public int refund_state;
            public String goods_charge_service;
            public String points_fee;
            public String points_num;
            public String refund_money;
        }
    }
}
