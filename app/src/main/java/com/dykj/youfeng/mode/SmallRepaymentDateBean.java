package com.dykj.youfeng.mode;

public class SmallRepaymentDateBean {
    private String year;
    private String month;
    private String day;
    private boolean check;//是否选中(最终状态)
    private boolean clickCheck;//是否选中(点击状态)
    private boolean cantCheck;//是否不能选择


    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isCantCheck() {
        return cantCheck;
    }

    public void setCantCheck(boolean cantCheck) {
        this.cantCheck = cantCheck;
    }

    public boolean isClickCheck() {
        return clickCheck;
    }

    public void setClickCheck(boolean clickCheck) {
        this.clickCheck = clickCheck;
    }

}
