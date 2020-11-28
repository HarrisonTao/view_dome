package com.dykj.youfeng.mode;

import java.util.List;

/**
 * Created by lcjingon 2019/11/6.
 * description:
 */

public class CollectListBean {

    /**
     * now_page : 1
     * all_page : 1
     * list : [{"id":"226","member_id":"11","member_name":null,"goods_id":"61","goods_name":"t恤女大码真丝汉服中老年女装棉麻连衣裙外套女衬衫上衣雪纺衫潮8","goods_price":"50.00","goods_img":"http://zhiyous.diyunkeji.com/Uploads/2019-07-25/5d390cc4dad6b.jpg","store_id":"121","store_name":"无抵抗","type":"1","probuy_id":"0","add_time":"1572862881","num":"9"}]
     */

    public int now_page;
    public int all_page;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * id : 226
         * member_id : 11
         * member_name : null
         * goods_id : 61
         * goods_name : t恤女大码真丝汉服中老年女装棉麻连衣裙外套女衬衫上衣雪纺衫潮8
         * goods_price : 50.00
         * goods_img : http://zhiyous.diyunkeji.com/Uploads/2019-07-25/5d390cc4dad6b.jpg
         * store_id : 121
         * store_name : 无抵抗
         * type : 1
         * probuy_id : 0
         * add_time : 1572862881
         * num : 9
         */

        public String id;
        public String member_id;
        public String member_name;
        public String goods_id;
        public String goods_name;
        public String goods_price;
        public String goods_img;
        public String store_id;
        public String store_name;
        public String type;
        public String probuy_id;
        public String add_time;
        public String num;
    }
}
