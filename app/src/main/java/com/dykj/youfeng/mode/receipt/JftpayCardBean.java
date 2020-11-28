package com.dykj.youfeng.mode.receipt;

import java.io.Serializable;

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
public class JftpayCardBean implements Serializable {
    public String channelType;

    @Override
    public String toString() {
        return "JftpayCardBean{" +
                "channelType='" + channelType + '\'' +
                ", crediteid='" + crediteid + '\'' +
                ", debitid='" + debitid + '\'' +
                ", money='" + money + '\'' +
                '}';
    }

    public String crediteid;
    public String debitid;
    public String money;

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getCrediteid() {
        return crediteid;
    }

    public void setCrediteid(String crediteid) {
        this.crediteid = crediteid;
    }

    public String getDebitid() {
        return debitid;
    }

    public void setDebitid(String debitid) {
        this.debitid = debitid;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
