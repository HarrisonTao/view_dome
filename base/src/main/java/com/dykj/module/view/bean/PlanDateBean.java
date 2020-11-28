package com.dykj.module.view.bean;

/**
 * @author 9
 * @version com.dy.aoyou.credit.repay.widget
 * @since 2019/3/24
 * ┃　　　　　　　┃
 * ┃　　　━　　　┃
 * ┃　┳┛　┗┳　┃糟糕，憋不住拉出来了啦～
 * ┃　　　　　　　┃
 * ┃　╰┬┬┬╯　┃
 * ┃　　╰—╯　  ┃
 * ┗━┓　　　┏━┛
 * 　 ┃　　　┃
 * 　  ┃　　　┗━━━┓
 * 　　  ┃　　　　　　　┣━━━━┓
 * 　　  ┃　　　　　　　┏━━━━┛ 　　◢
 * 　 ┗┓┓┏━┳┓┏┛ 　　　　　　◢◤◢◣
 * 　　 　 ┃┫┫　┃┫┫ 　　　　　　◢◣◢◤█◣
 * 　　 　┗┻┛　┗┻┛ 　　　　　◢█◢◣◥◣█◣
 */
public class PlanDateBean {
    private String type;// 时间，"null"占位
    private String day;
    private String time;
    private boolean select;
    private boolean enable;

    public PlanDateBean(String type) {
        this.type = type;
    }

    //##################################
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
