package com.dykj.youfeng.mode;

import com.google.gson.annotations.SerializedName;

/**
 * Created by  zhaoyanhui
 * Describe :
 * on :2019-09-17
 */
public class PayWxBean {
    /**
     * appid : wx77906710b0b9e367
     * noncestr : 9qUIDs4Trnc8E6n877a99PiL5fEJvFou
     * package : Sign=WXPay
     * partnerid : WX2019030909115638992
     * prepayid : wx090911551743493319e699aa2140526851
     * timestamp : 1552093917
     * sign : 6464F2E79BB74952E6015B6448A3D029
     */

    public String appid;
    public String noncestr;
    @SerializedName("package")
    public String packageX;
    public String partnerid;
    public String prepayid;
    public String timestamp;
    public String sign;
    
}
