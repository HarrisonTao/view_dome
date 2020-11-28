package com.dykj.youfeng.mode;

import java.util.List;

/**
 * Created by lcjingon 2019/11/6.
 * description:
 */

public class ConfirmOrderCarBean {

    /**
     * store_info : {"store_id":"121","store_name":"无抵抗","store_type":"1","store_logo":"/Uploads/store_pic/2019-07-17/5d2e73c0d1478.png"}
     * member_info : {"is_point":0,"member_points":"0","member_points_pay":0,"use_points":43}
     * address_info : {"address_id":"162","member_id":"11","true_name":"吕大树","province_id":"110000","city_id":"110100","area_id":"110101","area_info":"讨论这个","address":null,"tel_phone":"15588880000","youbian":null,"is_default":"1","province":null,"city":null,"area":null}
     * goods_count : {"goods_num":"4","goods_amount":"860.00"}
     * info : {"car_list":[{"id":"468","member_id":"11","member_name":"","goods_id":"65","goods_name":"亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣 ","goods_price":"55.00","goods_img":"http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg","goods_num":"3","goods_amount":"165.00","store_id":"121","store_name":"无抵抗","add_time":"1572862379","goods_spec":"42_635","goods_spec_name":"颜色:白色","is_order":null,"goods_type":"1","probuy_id":null,"is_form":"0","goods_charge_service":null},{"id":"469","member_id":"11","member_name":"","goods_id":"61","goods_name":"t恤女大码真丝汉服中老年女装棉麻连衣裙外套女衬衫上衣雪纺衫潮8","goods_price":"50.00","goods_img":"http://zhiyous.diyunkeji.com/Uploads/2019-07-25/5d390cc4dad6b.jpg","goods_num":"1","goods_amount":"50.00","store_id":"121","store_name":"无抵抗","add_time":"1572933612","goods_spec":null,"goods_spec_name":"","is_order":null,"goods_type":"1","probuy_id":null,"is_form":"0","goods_charge_service":null}],"transport_way":"83"}
     */

    public StoreInfoBean store_info;
    public MemberInfoBean member_info;
    public AddressInfoBean addressInfo;
    public GoodsCountBean goods_count;
    public InfoBean info;

    public static class StoreInfoBean {
        /**
         * store_id : 121
         * store_name : 无抵抗
         * store_type : 1
         * store_logo : /Uploads/store_pic/2019-07-17/5d2e73c0d1478.png
         */

        public String store_id;
        public String store_name;
        public String store_type;
        public String store_logo;
    }

    public static class MemberInfoBean {
        /**
         * is_point : 0
         * member_points : 0
         * member_points_pay : 0
         * use_points : 43
         */

        public int is_point;
        public String member_points;
        public int member_points_pay;
        public int use_points;
    }

    public static class AddressInfoBean {
        /**
         * address_id : 162
         * member_id : 11
         * true_name : 吕大树
         * province_id : 110000
         * city_id : 110100
         * area_id : 110101
         * area_info : 讨论这个
         * address : null
         * tel_phone : 15588880000
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


        public String getAddress_id() {
            return address_id;
        }

        public void setAddress_id(String address_id) {
            this.address_id = address_id;
        }

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getTrue_name() {
            return true_name;
        }

        public void setTrue_name(String true_name) {
            this.true_name = true_name;
        }

        public String getProvince_id() {
            return province_id;
        }

        public void setProvince_id(String province_id) {
            this.province_id = province_id;
        }

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getArea_id() {
            return area_id;
        }

        public void setArea_id(String area_id) {
            this.area_id = area_id;
        }

        public String getArea_info() {
            return area_info;
        }

        public void setArea_info(String area_info) {
            this.area_info = area_info;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTel_phone() {
            return tel_phone;
        }

        public void setTel_phone(String tel_phone) {
            this.tel_phone = tel_phone;
        }

        public String getYoubian() {
            return youbian;
        }

        public void setYoubian(String youbian) {
            this.youbian = youbian;
        }

        public String getIs_default() {
            return is_default;
        }

        public void setIs_default(String is_default) {
            this.is_default = is_default;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }
    }

    public static class GoodsCountBean {
        /**
         * goods_num : 4
         * goods_amount : 860.00
         */

        public String goods_num;
        public String goods_amount;
    }

    public static class InfoBean {
        /**
         * car_list : [{"id":"468","member_id":"11","member_name":"","goods_id":"65","goods_name":"亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣 ","goods_price":"55.00","goods_img":"http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg","goods_num":"3","goods_amount":"165.00","store_id":"121","store_name":"无抵抗","add_time":"1572862379","goods_spec":"42_635","goods_spec_name":"颜色:白色","is_order":null,"goods_type":"1","probuy_id":null,"is_form":"0","goods_charge_service":null},{"id":"469","member_id":"11","member_name":"","goods_id":"61","goods_name":"t恤女大码真丝汉服中老年女装棉麻连衣裙外套女衬衫上衣雪纺衫潮8","goods_price":"50.00","goods_img":"http://zhiyous.diyunkeji.com/Uploads/2019-07-25/5d390cc4dad6b.jpg","goods_num":"1","goods_amount":"50.00","store_id":"121","store_name":"无抵抗","add_time":"1572933612","goods_spec":null,"goods_spec_name":"","is_order":null,"goods_type":"1","probuy_id":null,"is_form":"0","goods_charge_service":null}]
         * transport_way : 83
         */

        public String transport_way;
        public List<CarListBean> car_list;


    }

    public StoreInfoBean getStore_info() {
        return store_info;
    }

    public void setStore_info(StoreInfoBean store_info) {
        this.store_info = store_info;
    }

    public MemberInfoBean getMember_info() {
        return member_info;
    }

    public void setMember_info(MemberInfoBean member_info) {
        this.member_info = member_info;
    }

    public AddressInfoBean getAddress_info() {
        return addressInfo;
    }

    public void setAddress_info(AddressInfoBean address_info) {
        this.addressInfo = address_info;
    }

    public GoodsCountBean getGoods_count() {
        return goods_count;
    }

    public void setGoods_count(GoodsCountBean goods_count) {
        this.goods_count = goods_count;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class CarListBean {
        /**
         * id : 468
         * member_id : 11
         * member_name :
         * goods_id : 65
         * goods_name : 亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣
         * goods_price : 55.00
         * goods_img : http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg
         * goods_num : 3
         * goods_amount : 165.00
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
        public String goods_points;
    }

}
