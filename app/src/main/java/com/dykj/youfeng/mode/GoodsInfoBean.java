package com.dykj.youfeng.mode;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcjingon 2019/11/4.
 * description:
 */

public class GoodsInfoBean {

//    String json="{\"total_evaluate\" : 3,\n" +
//            "     \"total_evaluate_img\":\" 0\",\n" +
//            "    \"good_info\" : {\"goods_id\":\"65\",\"goods_name\":\"亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣 \",\"goods_img\":\"http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg\",\"goods_price\":\"55.00\",\"prime_price\":\"128.00\",\"goods_image\":[\"http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg\",\"http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9f0cc149.jpg\"],\"goods_ad\":\"亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣 \",\"goods_video_img\":null,\"goods_video\":null,\"store_id\":\"121\",\"goods_storage\":\"1000\",\"goods_salenum\":\"55\",\"goods_collect\":\"5\",\"evaluation_good_star\":\"5\",\"goods_spec\":{\"42_634\":{\"price\":\"55\",\"kucun\":964,\"huohao\":\"100001\"},\"42_635\":{\"price\":\"55\",\"kucun\":978,\"huohao\":\"100002\"}},\"is_order\":null,\"goods_attr\":false,\"gc_id\":\"1136\",\"goods_body\":\"&lt;p&gt;&lt;img src=&quot;/Uploads/php/upload/image/20190731/1564539382979016.jpg&quot; title=&quot;1564539382979016.jpg&quot; alt=&quot;O1CN01kL4DJE1nuHtKkOnck_!!823935149.jpg&quot;/&gt;&lt;/p&gt;\",\"goods_points\":\"50\",\"goods_specc\":[{\"sp_id\":\"42\",\"sp_name\":\"颜色\",\"child\":[\"634\",\"635\"],\"goods_sp\":[{\"id\":\"634\",\"name\":\"黑色\"},{\"id\":\"635\",\"name\":\"白色\"}]}],\"car_count\":\"0\",\"keep_goods\":\"0\",\n" +
//            " \"evaluate_goods\" : [{\"geval_id\":\"244\",\"geval_content\":\"\",\"geval_addtime\":\"2019.08.01 15:15\",\"geval_frommembername\":null,\"geval_image\":\"\",\"geval_frommemberid\":\"318\",\"avatar\":\"\",\"nickname\":null,\"geval_images\":[]}],\n" +
//            "     \"serviceQQ\" : 123456789}}";
    /**
     * total_evaluate : 3
     * total_evaluate_img : 0
     * good_info : {"goods_id":"65","goods_name":"亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣 ","goods_img":"http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg","goods_price":"55.00","prime_price":"128.00","goods_image":["http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg","http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9f0cc149.jpg"],"goods_ad":"亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣 ","goods_video_img":null,"goods_video":null,"store_id":"121","goods_storage":"1000","goods_salenum":"55","goods_collect":"5","evaluation_good_star":"5","goods_spec":{"42_634":{"price":"55","kucun":964,"huohao":"100001"},"42_635":{"price":"55","kucun":978,"huohao":"100002"}},"is_order":null,"goods_attr":false,"gc_id":"1136","goods_body":"&lt;p&gt;&lt;img src=&quot;/Uploads/php/upload/image/20190731/1564539382979016.jpg&quot; title=&quot;1564539382979016.jpg&quot; alt=&quot;O1CN01kL4DJE1nuHtKkOnck_!!823935149.jpg&quot;/&gt;&lt;/p&gt;","goods_points":"50","goods_specc":[{"sp_id":"42","sp_name":"颜色","child":["634","635"],"goods_sp":[{"id":"634","name":"黑色"},{"id":"635","name":"白色"}]}],"car_count":"0","keep_goods":"0"}
     * evaluate_goods : [{"geval_id":"244","geval_content":"","geval_addtime":"2019.08.01 15:15","geval_frommembername":null,"geval_image":"","geval_frommemberid":"318","avatar":"","nickname":null,"geval_images":[]}]
     * serviceQQ : 123456789
     */

    public String total_evaluate;
    public String total_evaluate_img;
    public GoodInfoBean good_info;
    public String serviceQQ;
    public List<EvaluateGoodsBean> evaluate_goods;

    public static class GoodInfoBean {
        /**
         * goods_id : 65
         * goods_name : 亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣
         * goods_img : http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg
         * goods_price : 55.00
         * prime_price : 128.00
         * goods_image : ["http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg","http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9f0cc149.jpg"]
         * goods_ad : 亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣
         * goods_video_img : null
         * goods_video : null
         * store_id : 121
         * goods_storage : 1000
         * goods_salenum : 55
         * goods_collect : 5
         * evaluation_good_star : 5
         * goods_spec : {"42_634":{"price":"55","kucun":964,"huohao":"100001"},"42_635":{"price":"55","kucun":978,"huohao":"100002"}}
         * is_order : null
         * goods_attr : false
         * gc_id : 1136
         * goods_body : &lt;p&gt;&lt;img src=&quot;/Uploads/php/upload/image/20190731/1564539382979016.jpg&quot; title=&quot;1564539382979016.jpg&quot; alt=&quot;O1CN01kL4DJE1nuHtKkOnck_!!823935149.jpg&quot;/&gt;&lt;/p&gt;
         * goods_points : 50
         * goods_specc : [{"sp_id":"42","sp_name":"颜色","child":["634","635"],"goods_sp":[{"id":"634","name":"黑色"},{"id":"635","name":"白色"}]}]
         * car_count : 0
         * keep_goods : 0
         */

        public String goods_id;
        public String goods_name;
        public String goods_img;
        public String goods_price;
        public String prime_price;
        public String goods_ad;
        public String goods_video_img;
        public String goods_video;
        public String store_id;
        public String goods_storage;
        public String goods_salenum;
        public String goods_collect;
        public String evaluation_good_star;
        public Object goods_spec;
        public String is_order;
        public boolean goods_attr;
        public String gc_id;
        public String goods_body;
        public String goods_points;
        public String car_count;
        public String keep_goods;
        public ArrayList<String> goods_image;
        public List<GoodsSpeccBean> goods_specc;

        public static class GoodsSpecBean {
            /**
             * price : 55
             * kucun : 964
             * huohao : 100001
             */
            public String price;
            public String kucun;
            public String huohao;

        }

        public static class GoodsSpeccBean {
            /**
             * sp_id : 42
             * sp_name : 颜色
             * child : ["634","635"]
             * goods_sp : [{"id":"634","name":"黑色"},{"id":"635","name":"白色"}]
             */

            public String sp_id;
            public String sp_name;
            public String sp_child_id;
            public String sp_child_name;
//            public List<String> child;
            public List<GoodsSpBean> goods_sp;

            public static class GoodsSpBean {
                /**
                 * id : 634
                 * name : 黑色
                 */

                public String id;
                public String name;
                public boolean select;
            }
        }
    }

    public static class EvaluateGoodsBean {
        /**
         * geval_id : 244
         * geval_content :
         * geval_addtime : 2019.08.01 15:15
         * geval_frommembername : null
         * geval_image :
         * geval_frommemberid : 318
         * avatar :
         * nickname : null
         * geval_images : []
         */

        public String geval_id;
        public String geval_content;
        public String geval_addtime;
        public String geval_frommembername;
        public String geval_image;
        public String geval_frommemberid;
        public String avatar;
        public String nickname;
        public List<String> geval_images;
    }

    /**
     * total_evaluate : 0
     * total_evaluate_img : 0
     * good_info : {"goods_id":"66","goods_name":"青春保湿","goods_img":"http://zhiyous.diyunkeji.com/Uploads/2019-08-01/5d42b2486da57.png","goods_price":"89.00","prime_price":"109.00","goods_image":["http://zhiyous.diyunkeji.com/Uploads/2019-08-01/5d42b2486da57.png"],"goods_ad":"巨补水","goods_video_img":"","goods_video":"","store_id":"121","goods_storage":"210","goods_salenum":"0","goods_collect":"0","evaluation_good_star":"5","goods_spec":false,"is_order":null,"goods_attr":false,"gc_id":"35","goods_body":"&lt;p&gt;&lt;img src=&quot;/Uploads/php/upload/image/20190801/1564652259103293.png&quot; title=&quot;1564652259103293.png&quot; alt=&quot;2.png&quot;/&gt;&lt;/p&gt;&lt;p&gt;&lt;img src=&quot;/Uploads/php/upload/image/20190801/1564652296423002.jpg&quot; title=&quot;1564652296423002.jpg&quot; alt=&quot;3.jpg&quot;/&gt;&lt;/p&gt;","goods_points":"5","goods_specc":[],"car_count":"0","keep_goods":"0"}
     * evaluate_goods : []
     * serviceQQ : 123456789
     */


}
