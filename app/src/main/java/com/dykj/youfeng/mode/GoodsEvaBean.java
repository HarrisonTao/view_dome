package com.dykj.youfeng.mode;

import java.util.List;

/**
 * Created by lcjingon 2019/11/6.
 * description:
 */

public class GoodsEvaBean {

    /**
     * now_page : 1
     * all_page : 1
     * total_evaluate : 5
     * all_score : 5
     * list : [{"geval_id":"252","geval_orderid":"1141","geval_goodsid":"65","geval_content":"显瘦，好看！","geval_addtime":"1572925209","geval_image":"http://yxs.diyunkeji.com/Uploads/avatar/5dc0ef16d2b57.png","geval_frommemberid":"8","store_reply":"谢谢您的评价。祝您身体健康，万事如意。","geval_ordergoodsid":"0","avatar":"","nickname":"156pmqg2258","geval_images":["http://yxs.diyunkeji.com/Uploads/avatar/5dc0ef16d2b57.png"],"spec_name":"颜色:白色"},{"geval_id":"251","geval_orderid":"1137","geval_goodsid":"65","geval_content":"非常不错，喜欢。","geval_addtime":"1572918581","geval_image":"http://yxs.diyunkeji.com/Uploads/avatar/5dc0d5185e279.png;http://yxs.diyunkeji.com/Uploads/avatar/5dc0d522cdcf3.png","geval_frommemberid":"8","store_reply":null,"geval_ordergoodsid":"0","avatar":"","nickname":"156pmqg2258","geval_images":["http://yxs.diyunkeji.com/Uploads/avatar/5dc0d5185e279.png","http://yxs.diyunkeji.com/Uploads/avatar/5dc0d522cdcf3.png"],"spec_name":"颜色:白色"},{"geval_id":"247","geval_orderid":"987","geval_goodsid":"65","geval_content":"","geval_addtime":"1564651006","geval_image":"","geval_frommemberid":"458","store_reply":null,"geval_ordergoodsid":"0","avatar":"","nickname":null,"geval_images":[],"spec_name":""},{"geval_id":"246","geval_orderid":"988","geval_goodsid":"65","geval_content":"","geval_addtime":"1564650857","geval_image":"","geval_frommemberid":"458","store_reply":null,"geval_ordergoodsid":"0","avatar":"","nickname":null,"geval_images":[],"spec_name":""},{"geval_id":"244","geval_orderid":"999","geval_goodsid":"65","geval_content":"","geval_addtime":"1564643748","geval_image":"","geval_frommemberid":"318","store_reply":null,"geval_ordergoodsid":"0","avatar":"","nickname":null,"geval_images":[],"spec_name":""}]
     */

    public int now_page;
    public int all_page;
    public String total_evaluate;
    public String all_score;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * geval_id : 252
         * geval_orderid : 1141
         * geval_goodsid : 65
         * geval_content : 显瘦，好看！
         * geval_addtime : 1572925209
         * geval_image : http://yxs.diyunkeji.com/Uploads/avatar/5dc0ef16d2b57.png
         * geval_frommemberid : 8
         * store_reply : 谢谢您的评价。祝您身体健康，万事如意。
         * geval_ordergoodsid : 0
         * avatar :
         * nickname : 156pmqg2258
         * geval_images : ["http://yxs.diyunkeji.com/Uploads/avatar/5dc0ef16d2b57.png"]
         * spec_name : 颜色:白色
         */

        public String geval_id;
        public String geval_orderid;
        public String geval_goodsid;
        public String geval_content;
        public String geval_addtime;
        public String geval_image;
        public String geval_frommemberid;
        public String store_reply;
        public String geval_ordergoodsid;
        public String avatar;
        public String nickname;
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

        public String getGeval_goodsid() {
            return geval_goodsid;
        }

        public void setGeval_goodsid(String geval_goodsid) {
            this.geval_goodsid = geval_goodsid;
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

        public String getGeval_ordergoodsid() {
            return geval_ordergoodsid;
        }

        public void setGeval_ordergoodsid(String geval_ordergoodsid) {
            this.geval_ordergoodsid = geval_ordergoodsid;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
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
