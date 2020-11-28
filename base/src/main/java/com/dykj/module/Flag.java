package com.dykj.module;

/**
 * @author WZ
 * @date 2019/5/23.
 * 严格要求自己，对每一行代码负责
 * description：
 */
public class Flag {
    /**
     * 登录
     */
    public static final String CACHE_LOGIN_DATA = "CACHE_LOGIN_DATA";

    /**
     * 设置地址
     */
    public static final String CACHE_SET_AREA = "CACHE_SET_AREA";
    /**
     * 搜索关键字
     */
    public static final String CACHE_SEARCH_KEY = "CACHE_SEARCH_KEY";

    /**
     * 公共配置区域 商品标签
     */
    public static final String CACHE_CONFIG_KEY = "CACHE_CONFIG_KEY";

    public static double APP_SIZE = 0;

    public enum Event {
        //跳转登录
       JUMP_LOGIN,
        //跳转tab1
        JUMP_TAB1,
        //跳转tab2
        JUMP_TAB2,
        //选择地址
        SELECT_ADDRESS,
        //更多地址
        SELECT_ADDRESS_MORE,
        //选择银行卡
        SELECT_BANK_CARD,
        //微信支付
        WECHAT_PAY
    }

    /**
     * 是否是第一次
     */
    public  static  final String IS_FIRST = "IS_FIRST";
}
