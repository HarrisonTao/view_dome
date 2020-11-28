package com.dykj.youfeng.mode.receipt;

/**
 * <pre>
 *           .----.
 *        _.'__    `.
 *    .--(Q)(OK)---/$\
 *  .' @          /$$$\
 *  :         ,   $$$$$
 *   `-..__.-' _.-\$$/
 *         `;_:    `"'
 *       .'"""""`.
 *      /,  oo  ,\
 *     //         \\
 *     `-._______.-'
 *     ___`. | .'___
 *    (______|______)
 * </pre>
 * <p>文件描述：<p>
 * <p>作者：lj<p>
 * <p>创建时间：2020/3/10<p>
 * <p>更改时间：2020/3/10<p>
 * <p>版本号：1<p>
 */
public class JftpayInfoBean {
    /**
     * status : 9999
     * message : 请求成功
     * info : {"realname":"王连君","idcard":"371322199109088318","to_bank_num":"6217732506333669",
     * "to_bank_name":"中信银行","out_bank_num":"5201080020794994","out_bank_name":"中信银行",
     * "money":"100.00","fee_money":"0.00","phone":"13953139569"}
     */

    private String status;
    private String message;
    private InfoBean info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * realname : 王连君
         * idcard : 371322199109088318
         * to_bank_num : 6217732506333669
         * to_bank_name : 中信银行
         * out_bank_num : 5201080020794994
         * out_bank_name : 中信银行
         * money : 100.00
         * fee_money : 0.00
         * phone : 13953139569
         */

        private String realname;
        private String idcard;
        private String to_bank_num;
        private String to_bank_name;
        private String out_bank_num;
        private String out_bank_name;
        private String money;
        private String fee_money;
        private String phone;

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getIdcard() {
            return idcard;
        }

        public void setIdcard(String idcard) {
            this.idcard = idcard;
        }

        public String getTo_bank_num() {
            return to_bank_num;
        }

        public void setTo_bank_num(String to_bank_num) {
            this.to_bank_num = to_bank_num;
        }

        public String getTo_bank_name() {
            return to_bank_name;
        }

        public void setTo_bank_name(String to_bank_name) {
            this.to_bank_name = to_bank_name;
        }

        public String getOut_bank_num() {
            return out_bank_num;
        }

        public void setOut_bank_num(String out_bank_num) {
            this.out_bank_num = out_bank_num;
        }

        public String getOut_bank_name() {
            return out_bank_name;
        }

        public void setOut_bank_name(String out_bank_name) {
            this.out_bank_name = out_bank_name;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getFee_money() {
            return fee_money;
        }

        public void setFee_money(String fee_money) {
            this.fee_money = fee_money;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
