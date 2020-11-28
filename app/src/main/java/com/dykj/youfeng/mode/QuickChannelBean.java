package com.dykj.youfeng.mode;

import java.io.Serializable;
import java.util.List;

public class QuickChannelBean {

    /**
     * channel : [{"name":"畅捷收款(6大银行)","rate":"65","cost":"2","content":"光大、招商、交通、邮政、兴业、浦发","minMoney":"200","time":"8:00-21:00","status":"1","type":"CjA"},{"name":"畅捷收款(10大银行)","rate":"65","cost":"2","content":"工商、中信、中国、上海、平安、民生、建设、华夏、广发、北京、农业","minMoney":"200","time":"8:00-21:00","status":"1","type":"CjB"},{"name":"畅捷收款(40家地方性银行)","rate":"65","cost":"2","content":"除以上银行外的区域性银行","minMoney":"200","time":"8:00-21:00","status":"1","type":"CjC"},{"name":"易宝收款","rate":"65","cost":"2","content":"易宝收款通道描述","minMoney":"100","time":"8:00-21:00","status":"1","type":"yb"}]
     * credit_card : {"id":5,"bank_name":"招商银行","bank_num":"6225758335840475","bank_number":"308584000013","logo":"http://yx.diyunkeji.com/Uploads/banklogo/5afa37e60c5a7.png"}
     * debit_card : null
     */

    public Object credit_card;
    public Object debit_card;
    public List<ChannelBean> channel;


    public static class CreditCardBean {
        /**
         * id : 5
         * bank_name : 招商银行
         * bank_num : 6225758335840475
         * bank_number : 308584000013
         * logo : http://yx.diyunkeji.com/Uploads/banklogo/5afa37e60c5a7.png
         */

        public int id;
        public String bank_name;
        public String bank_num;
        public String bank_number;
        public String logo;


    }

    public static class ChannelBean  implements Serializable {
        /**
         * name : 畅捷收款(6大银行)
         * rate : 65 费率
         * cost : 2  手续费
         * content : 光大、招商、交通、邮政、兴业、浦发
         * minMoney : 200
         * time : 8:00-21:00
         * status : 1
         * type : CjA
         */

        public String name;
        public String rate;
        public String cost;
        public String content;
        public String minMoney;
        public String maxMoney;
        public String time;
        public String status;
        public String type;
        public boolean isCheck;

    }
}
