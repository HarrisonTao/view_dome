package com.dykj.youfeng.mode;

import java.util.List;

/**
 * Created by lcjingon 2019/12/5.
 * description:
 */

public class GoodsCateBean {

    /**
     * status : 9999
     * message : 请求成功！
     * info : {"goods_class":[{"gc_id":"23","gc_name":"百货","parent_id":"0","type_pic":"http://yxs.diyunkeji.com/Uploads/goods_class/2019-12-04/5de7759e523b9.png"},{"gc_id":"55","gc_name":"洗护","parent_id":"0","type_pic":"http://yxs.diyunkeji.com/Uploads/goods_class/2019-12-04/5de777865c0da.png"},{"gc_id":"1565","gc_name":"美妆","parent_id":"0","type_pic":"http://yxs.diyunkeji.com/Uploads/goods_class/2019-12-04/5de7760baaff0.png"},{"gc_id":"28","gc_name":"食品","parent_id":"0","type_pic":"http://yxs.diyunkeji.com/Uploads/goods_class/2019-12-04/5de77429873e4.png"},{"gc_id":"65","gc_name":"男装","parent_id":"0","type_pic":"http://yxs.diyunkeji.com/Uploads/goods_class/2019-12-04/5de76f38c67d1.png"},{"gc_id":"10","gc_name":"女装","parent_id":"0","type_pic":"http://yxs.diyunkeji.com/Uploads/goods_class/2019-12-04/5de76fd3865fc.png"},{"gc_id":"1560","gc_name":"箱包","parent_id":"0","type_pic":"http://yxs.diyunkeji.com/Uploads/goods_class/2019-12-04/5de7771448261.png"},{"gc_id":"1569","gc_name":"鞋靴","parent_id":"0","type_pic":"http://yxs.diyunkeji.com/Uploads/goods_class/2019-12-04/5de7765cb63f1.png"},{"gc_id":"1570","gc_name":"家电","parent_id":"0","type_pic":"http://yxs.diyunkeji.com/Uploads/goods_class/2019-12-04/5de7774d7a83b.png"},{"gc_id":"1574","gc_name":"数码","parent_id":"0","type_pic":"http://yxs.diyunkeji.com/Uploads/goods_class/2019-12-04/5de7700e6329c.png"}],"list":[{"gc_id":"34","gc_name":"一叶子","parent_id":"23","type_pic":"http://zhiyous.diyunkeji.com/Uploads/goods_class/2019-08-02/5d441833dfe50.png","child":[{"gc_id":"1179","gc_name":"面霜","parent_id":"34","type_pic":"http://zhiyous.diyunkeji.com/Uploads/goods_class/2019-08-02/5d44188d6f2ef.png"},{"gc_id":"35","gc_name":"面膜","parent_id":"34","type_pic":"http://zhiyous.diyunkeji.com/Uploads/goods_class/2019-08-02/5d44184776e18.png"},{"gc_id":"1575","gc_name":"儿童牙膏","parent_id":"34","type_pic":"http://yxs.diyunkeji.com/Uploads/goods_class/2019-12-04/5de7a69684121.jpg"},{"gc_id":"1180","gc_name":"洗面奶","parent_id":"34","type_pic":"http://zhiyous.diyunkeji.com/Uploads/goods_class/2019-08-02/5d441912b53c6.png"}]},{"gc_id":"24","gc_name":"森码","parent_id":"23","type_pic":null,"child":[{"gc_id":"32","gc_name":"长裙","parent_id":"24","type_pic":"http://zhiyous.diyunkeji.com/Uploads/goods_class/2019-08-02/5d441a51aa549.png"},{"gc_id":"31","gc_name":"外套","parent_id":"24","type_pic":"http://zhiyous.diyunkeji.com/Uploads/goods_class/2019-08-02/5d441a1897020.png"},{"gc_id":"1196","gc_name":"长裤","parent_id":"24","type_pic":"http://zhiyous.diyunkeji.com/Uploads/goods_class/2019-08-02/5d441a764dcec.jpg"}]}]}
     */


    private List<GoodsClassBean> goods_class;
    private List<ListBean> list;

    public List<GoodsClassBean> getGoods_class() {
        return goods_class;
    }

    public void setGoods_class(List<GoodsClassBean> goods_class) {
        this.goods_class = goods_class;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class GoodsClassBean {
        /**
         * gc_id : 23
         * gc_name : 百货
         * parent_id : 0
         * type_pic : http://yxs.diyunkeji.com/Uploads/goods_class/2019-12-04/5de7759e523b9.png
         */

        private String gc_id;
        private String gc_name;
        private String parent_id;
        private String type_pic;

        public String getGc_id() {
            return gc_id;
        }

        public void setGc_id(String gc_id) {
            this.gc_id = gc_id;
        }

        public String getGc_name() {
            return gc_name;
        }

        public void setGc_name(String gc_name) {
            this.gc_name = gc_name;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public String getType_pic() {
            return type_pic;
        }

        public void setType_pic(String type_pic) {
            this.type_pic = type_pic;
        }
    }

    public static class ListBean {
        /**
         * gc_id : 34
         * gc_name : 一叶子
         * parent_id : 23
         * type_pic : http://zhiyous.diyunkeji.com/Uploads/goods_class/2019-08-02/5d441833dfe50.png
         * child : [{"gc_id":"1179","gc_name":"面霜","parent_id":"34","type_pic":"http://zhiyous.diyunkeji.com/Uploads/goods_class/2019-08-02/5d44188d6f2ef.png"},{"gc_id":"35","gc_name":"面膜","parent_id":"34","type_pic":"http://zhiyous.diyunkeji.com/Uploads/goods_class/2019-08-02/5d44184776e18.png"},{"gc_id":"1575","gc_name":"儿童牙膏","parent_id":"34","type_pic":"http://yxs.diyunkeji.com/Uploads/goods_class/2019-12-04/5de7a69684121.jpg"},{"gc_id":"1180","gc_name":"洗面奶","parent_id":"34","type_pic":"http://zhiyous.diyunkeji.com/Uploads/goods_class/2019-08-02/5d441912b53c6.png"}]
         */

        private String gc_id;
        private String gc_name;
        private String parent_id;
        private String type_pic;
        private List<ChildBean> child;

        public String getGc_id() {
            return gc_id;
        }

        public void setGc_id(String gc_id) {
            this.gc_id = gc_id;
        }

        public String getGc_name() {
            return gc_name;
        }

        public void setGc_name(String gc_name) {
            this.gc_name = gc_name;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public String getType_pic() {
            return type_pic;
        }

        public void setType_pic(String type_pic) {
            this.type_pic = type_pic;
        }

        public List<ChildBean> getChild() {
            return child;
        }

        public void setChild(List<ChildBean> child) {
            this.child = child;
        }

        public static class ChildBean {
            /**
             * gc_id : 1179
             * gc_name : 面霜
             * parent_id : 34
             * type_pic : http://zhiyous.diyunkeji.com/Uploads/goods_class/2019-08-02/5d44188d6f2ef.png
             */

            private String gc_id;
            private String gc_name;
            private String parent_id;
            private String type_pic;

            public String getGc_id() {
                return gc_id;
            }

            public void setGc_id(String gc_id) {
                this.gc_id = gc_id;
            }

            public String getGc_name() {
                return gc_name;
            }

            public void setGc_name(String gc_name) {
                this.gc_name = gc_name;
            }

            public String getParent_id() {
                return parent_id;
            }

            public void setParent_id(String parent_id) {
                this.parent_id = parent_id;
            }

            public String getType_pic() {
                return type_pic;
            }

            public void setType_pic(String type_pic) {
                this.type_pic = type_pic;
            }
        }
    }

}
