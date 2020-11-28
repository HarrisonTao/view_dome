package com.dykj.youfeng.mode;

import java.util.List;

/**
 * Created by lcjingon 2019/11/11.
 * description:
 */

public class HasEvaListBean {

    /**
     * now_page : 1
     * all_page : 1
     * list : [{"geval_id":"253","geval_orderid":"1162","geval_content":"老K咯了老K咯了图库","geval_addtime":"1573438799","geval_goodsid":"65","geval_goodsname":"亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣 ","geval_goodsprice":"55.00","geval_goodsimage":"http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg","geval_image":"http://yxs.diyunkeji.com/Uploads/avatar/5dc8c5367101e.jpeg;","geval_frommemberid":"10","store_reply":null,"member_avatar":"","member_name":"183oynd0579","geval_images":["http://yxs.diyunkeji.com/Uploads/avatar/5dc8c5367101e.jpeg",""],"spec_name":"颜色:白色"},{"geval_id":"254","geval_orderid":"1162","geval_content":"兔兔","geval_addtime":"1573438799","geval_goodsid":"61","geval_goodsname":"t恤女大码真丝汉服中老年女装棉麻连衣裙外套女衬衫上衣雪纺衫潮8","geval_goodsprice":"50.00","geval_goodsimage":"http://zhiyous.diyunkeji.com/Uploads/2019-07-25/5d390cc4dad6b.jpg","geval_image":"","geval_frommemberid":"10","store_reply":null,"member_avatar":"","member_name":"183oynd0579","geval_images":[],"spec_name":"颜色:白色"}]
     */

    public int now_page;
    public int all_page;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * geval_id : 253
         * geval_orderid : 1162
         * geval_content : 老K咯了老K咯了图库
         * geval_addtime : 1573438799
         * geval_goodsid : 65
         * geval_goodsname : 亲爱的热爱的杨紫佟年鱿小鱼同款秋2019新女宽松绒衫上衣白色卫衣
         * geval_goodsprice : 55.00
         * geval_goodsimage : http://zhiyous.diyunkeji.com/Uploads/2019-07-31/5d40f9edeb215.jpg
         * geval_image : http://yxs.diyunkeji.com/Uploads/avatar/5dc8c5367101e.jpeg;
         * geval_frommemberid : 10
         * store_reply : null
         * member_avatar :
         * member_name : 183oynd0579
         * geval_images : ["http://yxs.diyunkeji.com/Uploads/avatar/5dc8c5367101e.jpeg",""]
         * spec_name : 颜色:白色
         */

        public String geval_id;
        public String geval_orderid;
        public String geval_content;
        public String geval_addtime;
        public String geval_goodsid;
        public String geval_goodsname;
        public String geval_goodsprice;
        public String geval_goodsimage;
        public String geval_image;
        public String geval_frommemberid;
        public String store_reply;
        public String member_avatar;
        public String member_name;
        public String spec_name;
        public List<String> geval_images;

        public String getGeval_id() {
            return geval_id;
        }

        public void setGeval_id(String geval_id) {
            this.geval_id = geval_id;
        }

        public String getGeval_orderid() {
            return geval_orderid;
        }

        public void setGeval_orderid(String geval_orderid) {
            this.geval_orderid = geval_orderid;
        }

        public String getGeval_content() {
            return geval_content;
        }

        public void setGeval_content(String geval_content) {
            this.geval_content = geval_content;
        }

        public String getGeval_addtime() {
            return geval_addtime;
        }

        public void setGeval_addtime(String geval_addtime) {
            this.geval_addtime = geval_addtime;
        }

        public String getGeval_goodsid() {
            return geval_goodsid;
        }

        public void setGeval_goodsid(String geval_goodsid) {
            this.geval_goodsid = geval_goodsid;
        }

        public String getGeval_goodsname() {
            return geval_goodsname;
        }

        public void setGeval_goodsname(String geval_goodsname) {
            this.geval_goodsname = geval_goodsname;
        }

        public String getGeval_goodsprice() {
            return geval_goodsprice;
        }

        public void setGeval_goodsprice(String geval_goodsprice) {
            this.geval_goodsprice = geval_goodsprice;
        }

        public String getGeval_goodsimage() {
            return geval_goodsimage;
        }

        public void setGeval_goodsimage(String geval_goodsimage) {
            this.geval_goodsimage = geval_goodsimage;
        }

        public String getGeval_image() {
            return geval_image;
        }

        public void setGeval_image(String geval_image) {
            this.geval_image = geval_image;
        }

        public String getGeval_frommemberid() {
            return geval_frommemberid;
        }

        public void setGeval_frommemberid(String geval_frommemberid) {
            this.geval_frommemberid = geval_frommemberid;
        }

        public String getStore_reply() {
            return store_reply;
        }

        public void setStore_reply(String store_reply) {
            this.store_reply = store_reply;
        }

        public String getMember_avatar() {
            return member_avatar;
        }

        public void setMember_avatar(String member_avatar) {
            this.member_avatar = member_avatar;
        }

        public String getMember_name() {
            return member_name;
        }

        public void setMember_name(String member_name) {
            this.member_name = member_name;
        }

        public String getSpec_name() {
            return spec_name;
        }

        public void setSpec_name(String spec_name) {
            this.spec_name = spec_name;
        }

        public List<String> getGeval_images() {
            return geval_images;
        }

        public void setGeval_images(List<String> geval_images) {
            this.geval_images = geval_images;
        }
    }
}
