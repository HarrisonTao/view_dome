package com.dykj.youfeng.mode.receipt;

/**
 * @author LewChich
 * @date: 2019-11-22
 * @describe:
 */
public class Ipv4Bean {
//    {"code":0,"data":{"ip":"112.232.192.128","country":"中国",
//    "area":"","region":"山东","city":"济南","county":"XX","isp":"联通","country_id":"CN",
//    "area_id":"","region_id":"370000","city_id":"370100","county_id":"xx","isp_id":"100026"}}
    public int code;
    public DataBean data;

    public static class DataBean {
        public String ip;
        public String country;
        public String area;
        public String region;
        public String city;
        public String county;
        public String isp;
        public String country_id;
        public String area_id;
        public String region_id;
        public String city_id;
        public String county_id;
        public String isp_id;
    }
}
