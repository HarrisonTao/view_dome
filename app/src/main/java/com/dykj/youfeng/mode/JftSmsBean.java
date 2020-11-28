package com.dykj.youfeng.mode;

/**
 * Created by lcjingon 2019/11/28.
 * description:
 */

public class JftSmsBean {

    /**
     * status : 8888
     * message : 短信发送成功！
     * info : {"tradeno":"20191203083808703990","phone":"13777086096"}
     */

    public String status;
    public String message;
    public InfoBean info;

    

    public static class InfoBean {
        /**
         * tradeno : 20191203083808703990
         * phone : 13777086096
         */

        public String tradeno;
        public String phone;
        public String url;
    }


    public String bizOrderNumber;
    public String orderNo;
    public String code;

}
