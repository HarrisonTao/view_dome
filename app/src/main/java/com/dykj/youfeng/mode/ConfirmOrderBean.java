package com.dykj.youfeng.mode;

import java.util.List;

/**
 * Created by lcjingon 2019/11/5.
 * description:
 */

public class ConfirmOrderBean {

    /**
     * goods_info : {"goods_name":"t恤女大码真丝汉服中老年女装棉麻连衣裙外套女衬衫上衣雪纺衫潮8","goods_image":"http://zhiyous.diyunkeji.com/Uploads/2019-07-25/5d390cc4dad6b.jpg","spec_name":"","num":1,"price":50,"transport_id":"83"}
     * store_info : {"store_id":"121","store_name":"无抵抗","store_type":"1"}
     * send_type : ["kuaidi"]
     * address_info : {"address_id":"161","member_id":"11","true_name":"吕大虾","province_id":"110000","city_id":"110100","area_id":"110101","area_info":"啦啦啦","address":null,"tel_phone":"18835255863","youbian":null,"is_default":"0","province":null,"city":null,"area":null}
     * member_info : {"is_point":1,"member_points":"0","member_points_pay":0}
     */

    public GoodsInfoBean goods_info;
    public StoreInfoBean store_info;
    public AddressInfoBean addressInfo;
    public MemberInfoBean member_info;
    public List<String> send_type;

    public GoodsInfoBean getGoods_info() {
        return goods_info;
    }

    public void setGoods_info(GoodsInfoBean goods_info) {
        this.goods_info = goods_info;
    }

    public StoreInfoBean getStore_info() {
        return store_info;
    }

    public void setStore_info(StoreInfoBean store_info) {
        this.store_info = store_info;
    }

    public AddressInfoBean getAddress_info() {
        return addressInfo;
    }

    public void setAddress_info(AddressInfoBean address_info) {
        this.addressInfo = address_info;
    }

    public MemberInfoBean getMember_info() {
        return member_info;
    }

    public void setMember_info(MemberInfoBean member_info) {
        this.member_info = member_info;
    }

    public List<String> getSend_type() {
        return send_type;
    }

    public void setSend_type(List<String> send_type) {
        this.send_type = send_type;
    }

    public static class GoodsInfoBean {
        /**
         * goods_name : t恤女大码真丝汉服中老年女装棉麻连衣裙外套女衬衫上衣雪纺衫潮8
         * goods_image : http://zhiyous.diyunkeji.com/Uploads/2019-07-25/5d390cc4dad6b.jpg
         * spec_name :
         * num : 1
         * price : 50
         * transport_id : 83
         */

        public String goods_name;
        public String goods_image;
        public String spec_name;
        public int num;
        public String price;
        public String transport_id;
        public String goods_points;

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_image() {
            return goods_image;
        }

        public void setGoods_image(String goods_image) {
            this.goods_image = goods_image;
        }

        public String getSpec_name() {
            return spec_name;
        }

        public void setSpec_name(String spec_name) {
            this.spec_name = spec_name;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTransport_id() {
            return transport_id;
        }

        public void setTransport_id(String transport_id) {
            this.transport_id = transport_id;
        }
    }

    public static class StoreInfoBean {
        /**
         * store_id : 121
         * store_name : 无抵抗
         * store_type : 1
         */

        public String store_id;
        public String store_name;
        public String store_type;

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public String getStore_name() {
            return store_name;
        }

        public void setStore_name(String store_name) {
            this.store_name = store_name;
        }

        public String getStore_type() {
            return store_type;
        }

        public void setStore_type(String store_type) {
            this.store_type = store_type;
        }
    }

    public static class AddressInfoBean {
        /**
         * address_id : 161
         * member_id : 11
         * true_name : 吕大虾
         * province_id : 110000
         * city_id : 110100
         * area_id : 110101
         * area_info : 啦啦啦
         * address : null
         * tel_phone : 18835255863
         * youbian : null
         * is_default : 0
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

    public static class MemberInfoBean {
        /**
         * is_point : 1
         * member_points : 0
         * member_points_pay : 0
         */

        public int is_point;
        public String member_points;
        public String member_points_pay;
    }
}
