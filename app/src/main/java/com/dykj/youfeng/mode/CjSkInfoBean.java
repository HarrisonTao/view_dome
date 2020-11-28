package com.dykj.youfeng.mode;

import java.io.Serializable;

public class CjSkInfoBean implements Serializable {

    /**
     * realname : 赵艳辉
     * idcard : 371522199409255721
     * to_bank_num : 5218991042050085
     * to_bank_name : 交通银行
     * out_bank_num : 6225768608467047
     * out_bank_name : 招商银行
     * money : 200.00
     * fee_money : 3.20
     * phone : 13969578460
     */

    public String realname;
    public String idcard;
    public String to_bank_num;
    public String to_bank_name;
    public String out_bank_num;
    public String out_bank_name;
    public String money;
    public String fee_money;
    public String phone;

    // 提交界面需要的
    public String channelType;
    public String crediteid;
    public String debitid;

}
