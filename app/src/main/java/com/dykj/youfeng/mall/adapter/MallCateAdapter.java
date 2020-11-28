package com.dykj.youfeng.mall.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.GoodsCateBean;
import com.dykj.module.util.GlideUtils;


import java.util.List;

/**
 * Created by lcjing on 2019/7/3.
 */
public class MallCateAdapter extends BaseQuickAdapter<GoodsCateBean.GoodsClassBean, BaseViewHolder> {

    public MallCateAdapter(int layoutResId, List<GoodsCateBean.GoodsClassBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsCateBean.GoodsClassBean item) {
        helper.setText(R.id.tv_name, item.getGc_name());
        GlideUtils.setMallImage(helper.getView(R.id.iv_img),item.getType_pic());
    }

    public static class CateBean {
        public String name;
        public int imgId;

        public CateBean() {
        }

        public CateBean(String name, int imgId) {
            this.name = name;
            this.imgId = imgId;
        }
    }

}
