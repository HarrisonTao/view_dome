package com.dykj.youfeng.mode;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lcjingon 2019/11/7.
 * description:
 */

public class OrderInfoBean {

    /**
     * order_id : 1160
     * order_sn : 2000000000122301
     * pay_sn : 580626451399620010
     * store_id : 121
     * store_name :
     * buyer_id : 10
     * add_time : 1573107399
     * payment_code : ylpay
     * payment_time : 1573107404
     * shipping_time : null
     * finnshed_time : 0
     * goods_amount : 110
     * shipping_fee : 5.00
     * order_state : 20
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
     * freight_id : 83
     * type : kuaidi
     * store_qq : 123456
     * store_logo : /Uploads/store_pic/2019-07-17/5d2e73c0d1478.png
     * store_type : 1
     * goods_list : [{"rec_id":"1209","order_id":"1160","goods_id":"61","goods_name":"t恤女大码真丝汉服中老年女装棉麻连衣裙外套女衬衫上衣雪纺衫潮8","goods_price":"50.00","goods_num":"1","goods_image":"http://zhiyous.diyunkeji.com/Uploads/2019-07-25/5d390cc4dad6b.jpg","goods_pay_price":"50.00","store_id":"121","buyer_id":"10","goods_type":"1","goods_types_id":"0","gc_id":"1137","promotions_id":null,"og_type":"0","goods_spec":null,"goods_spec_name":"","freight_id":"83","freight_type":"kuaidi","freight_fee":"5.00","refund_state":"0","goods_charge_service":null,"points_fee":"0.00","points_num":"0","refund_amount":null,"lock_state":"0","buyer_is_submit":"0","refund_money":"50.00"},{"rec_id":"1210","order_id":"1160","goods_id":"65","goods_name":"亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣 ","goods_price":"55.00","goods_num":"1","goods_image":"http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg","goods_pay_price":"55.00","store_id":"121","buyer_id":"10","goods_type":"1","goods_types_id":"0","gc_id":"1136","promotions_id":null,"og_type":"0","goods_spec":"42_634","goods_spec_name":"颜色:黑色","freight_id":"83","freight_type":"kuaidi","freight_fee":"5.00","refund_state":"0","goods_charge_service":null,"points_fee":"0.00","points_num":"0","refund_amount":null,"lock_state":"0","buyer_is_submit":"0","refund_money":"55.00"}]
     * order_common : {"reciver_info":{"address_id":"164","member_id":"10","true_name":"嘿嘿测试","province_id":"370000","city_id":"370100","area_id":"370102","area_info":"测试一下","address":null,"tel_phone":"18363668855","youbian":null,"is_default":"1","province":null,"city":null,"area":null},"order_message":""}
     * goods_spec_name :
     * goods_spec :
     * goods_allmoney : 105
     * order_amount : 110
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
    public String freight_id;
    public String type;
    public String store_qq;
    public String store_logo;
    public String store_type;
    public OrderCommonBean order_common;
    public String goods_spec_name;
    public String goods_spec;
    public float goods_allmoney;
    public float order_amount;
    public List<GoodsListBean> goods_list;

    public static class OrderCommonBean {
        /**
         * reciver_info : {"address_id":"164","member_id":"10","true_name":"嘿嘿测试","province_id":"370000","city_id":"370100","area_id":"370102","area_info":"测试一下","address":null,"tel_phone":"18363668855","youbian":null,"is_default":"1","province":null,"city":null,"area":null}
         * order_message :
         */

        public ReciverInfoBean reciver_info;
        public String order_message;


    }
    public static class ReciverInfoBean {
        /**
         * address_id : 164
         * member_id : 10
         * true_name : 嘿嘿测试
         * province_id : 370000
         * city_id : 370100
         * area_id : 370102
         * area_info : 测试一下
         * address : null
         * tel_phone : 18363668855
         * youbian : null
         * is_default : 1
         * province : null
         * city : null
         * area : null
         */

        public String address_id;
        public String member_id;
        public String true_name;
        public String province_id;
        public String city_id;
        public String area_id;
        public String area_info;
        public String address;
        public String tel_phone;
        public String youbian;
        public String is_default;
        public String province;
        public String city;
        public String area;
    }
    public static class GoodsListBean implements Serializable {
        /**
         * rec_id : 1209
         * order_id : 1160
         * goods_id : 61
         * goods_name : t恤女大码真丝汉服中老年女装棉麻连衣裙外套女衬衫上衣雪纺衫潮8
         * goods_price : 50.00
         * goods_num : 1
         * goods_image : http://zhiyous.diyunkeji.com/Uploads/2019-07-25/5d390cc4dad6b.jpg
         * goods_pay_price : 50.00
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
         * refund_amount : null
         * lock_state : 0
         * buyer_is_submit : 0
         * refund_money : 50.00
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
        public String refund_state;
        public String goods_charge_service;
        public String points_fee;
        public String points_num;
        public String refund_amount;
        public String lock_state;
        public String buyer_is_submit;
        public String refund_money;
    }
}
