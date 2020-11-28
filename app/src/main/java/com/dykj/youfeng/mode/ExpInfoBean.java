package com.dykj.youfeng.mode;

import java.util.List;

/**
 * Created by lcjingon 2019/11/11.
 * description:
 */

public class ExpInfoBean {

    /**
     * message : ok
     * nu : YT3103827899455
     * ischeck : 1
     * condition : F00
     * com : yuantong
     * status : 200
     * state : 3
     * data : [{"time":"2019-09-12 19:21:43","ftime":"2019-09-12 19:21:43","context":"客户签收人: 刘管庄镇圆通 已签收 感谢使用圆通速递，期待再次为您服务 如有疑问请联系：15969900859，投诉电话：0633-2265114"},{"time":"2019-09-12 14:35:19","ftime":"2019-09-12 14:35:19","context":"【山东省日照市莒县公司】 派件中 派件人: 盛大海 电话 15969900859 如有疑问，请联系：0633-2265114"},{"time":"2019-09-12 14:19:24","ftime":"2019-09-12 14:19:24","context":"【山东省日照市莒县公司】 已收入"},{"time":"2019-09-12 06:32:36","ftime":"2019-09-12 06:32:36","context":"【临沂转运中心】 已发出 下一站 【山东省日照市莒县】"},{"time":"2019-09-12 04:01:48","ftime":"2019-09-12 04:01:48","context":"【临沂转运中心公司】 已收入"},{"time":"2019-09-11 20:36:28","ftime":"2019-09-11 20:36:28","context":"【潍坊转运中心】 已发出 下一站 【临沂转运中心】"},{"time":"2019-09-11 20:05:08","ftime":"2019-09-11 20:05:08","context":"【潍坊转运中心公司】 已收入"},{"time":"2019-09-11 01:57:13","ftime":"2019-09-11 01:57:13","context":"【杭州转运中心】 已发出 下一站 【潍坊转运中心】"},{"time":"2019-09-11 01:55:02","ftime":"2019-09-11 01:55:02","context":"【杭州转运中心公司】 已收入"},{"time":"2019-09-10 22:26:47","ftime":"2019-09-10 22:26:47","context":"【浙江省杭州市萧山区新街分公司】 已发出 下一站 【杭州转运中心】"},{"time":"2019-09-10 20:08:16","ftime":"2019-09-10 20:08:16","context":"【浙江省杭州市萧山区新街分公司公司】 已打包"},{"time":"2019-09-10 20:06:54","ftime":"2019-09-10 20:06:54","context":"【浙江省杭州市萧山区新街分公司公司】 已收件 取件人: 陈力杰 (13587666121)"}]
     * kuaidi_name : null
     */

    public String message;
    public String nu;
    public String ischeck;
    public String condition;
    public String com;
    public String status;
    public String state;
    public Object kuaidi_name;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * time : 2019-09-12 19:21:43
         * ftime : 2019-09-12 19:21:43
         * context : 客户签收人: 刘管庄镇圆通 已签收 感谢使用圆通速递，期待再次为您服务 如有疑问请联系：15969900859，投诉电话：0633-2265114
         */

        public String time;
        public String ftime;
        public String context;
    }
}
