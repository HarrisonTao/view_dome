package com.dykj.youfeng.mode;

import java.util.List;

/**
 * Created by lcjingon 2019/11/7.
 * description:
 */

public class CancelReasonBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * reason : 0
         * msg : 改买其他商品
         */

        public String reason;
        public String msg;
    }
}
