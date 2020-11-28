package com.dykj.youfeng.mode;

import android.widget.EditText;

import java.util.List;

/**
 * Created by lcjingon 2019/11/11.
 * description:
 */

public class EvaInfoBean {

    /**
     * rec_id : 1213
     * order_id : 1162
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
     * goods_spec : 42_635
     * goods_spec_name : 颜色:白色
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
    public List<String> imgs;
    private transient  EditText etContent;

    public EditText getEtContent() {
        return etContent;
    }

    public void setEtContent(EditText etContent) {
        this.etContent = etContent;
    }
}
