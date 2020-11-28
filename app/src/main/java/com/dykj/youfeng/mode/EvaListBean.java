package com.dykj.youfeng.mode;

import java.util.List;

/**
 * Created by lcjingon 2019/11/8.
 * description:
 */

public class EvaListBean {

    /**
     * now_page : 1
     * all_page : 1
     * list : [{"order_id":"1155","geval_goodsid":"65","geval_goodsname":"亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣 ","geval_goodsimage":"http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg"}]
     */

    public int now_page;
    public int all_page;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * order_id : 1155
         * geval_goodsid : 65
         * geval_goodsname : 亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣
         * geval_goodsimage : http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg
         */

        public String order_id;
        public String geval_goodsid;
        public String geval_goodsname;
        public String geval_goodsimage;
        public String geval_goodsprice;
    }
}
