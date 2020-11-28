package com.dykj.youfeng.mode;

import com.blankj.utilcode.util.StringUtils;

/**
 * Created by lcjingon 2019/12/19.
 * description:
 */

public class SpecResultBean {

    /**
     * price : 400
     * kucun : 59
     * huohao : 006
     */

    private String price;
    private String kucun;
    private String huohao;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getKucun() {
        if (StringUtils.isEmpty(kucun)) {
            return "0";
        }
        return kucun;
    }

    public void setKucun(String kucun) {

        this.kucun = kucun;
    }

    public String getHuohao() {
        return huohao;
    }

    public void setHuohao(String huohao) {
        this.huohao = huohao;
    }
}
