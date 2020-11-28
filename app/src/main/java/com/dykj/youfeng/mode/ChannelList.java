package com.dykj.youfeng.mode;


public class ChannelList {

    /**
     * status : 9999
     * message : 请求成功！
     * info : [{"name":"畅捷大额(6家银行)","content":"极速大额还款，落地商户。","info":"光大、招商、交通、邮政、兴业、浦发","money":"10000","dayMoney":"20000","status":"1","rate":"80","cost":"1","type":"CjA"},{"name":"畅捷大额(10家银行)","content":"极速大额还款，落地商户。","info":"工商、中信、中国、上海、平安、民生、建设、华夏、广发、北京、农业","money":"10000","dayMoney":"20000","status":"1","rate":"80","cost":"1","type":"CjB"},{"name":"畅捷大额(40家地方性银行)","content":"极速大额还款，落地商户。","info":"除以上银行外的区域性银行","money":"10000","dayMoney":"20000","status":"1","rate":"80","cost":"1","type":"CjC"},{"name":"畅捷小额","content":"极速小额还款，落地商户。","info":"支持带有银联标识各大银行。","money":"1000","dayMoney":"5000","status":"1","rate":"70","cost":"1","type":"cjmin"},{"name":"环讯小额","content":"单笔1000，单日5000。","info":"单笔1000，单日5000。","money":"1000","dayMoney":"5000","status":"1","rate":"70","cost":"1","type":"hxmin"}]
     */

    /**
     * name : 畅捷大额(6家银行)
     * content : 极速大额还款，落地商户。
     * info : 光大、招商、交通、邮政、兴业、浦发
     * money : 10000
     * dayMoney : 20000
     * status : 1
     * rate : 80
     * cost : 1
     * type : CjA
     */

    public String name;
    public String content;
    public String info;
    public String money;
    public String dayMoney;
    public String status;
    public double rate=0;
    public double cost=0;
    public String type;


}
