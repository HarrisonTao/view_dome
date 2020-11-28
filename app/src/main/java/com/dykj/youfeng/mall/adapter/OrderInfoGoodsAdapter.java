package com.dykj.youfeng.mall.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.OrderInfoBean;
import com.dykj.module.util.GlideUtils;

import java.util.List;

/**
 * Created by lcjing on 2019/7/3.
 */
public class OrderInfoGoodsAdapter extends BaseQuickAdapter<OrderInfoBean.GoodsListBean, BaseViewHolder> {

    public OrderInfoGoodsAdapter(int layoutResId, List<OrderInfoBean.GoodsListBean> data) {
        super(layoutResId, data);
    }

    private boolean showRefund;

    public void setShowRefund(boolean showRefund) {
        this.showRefund = showRefund;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderInfoBean.GoodsListBean item) {
        GlideUtils.setImage(helper.getView(R.id.iv_goods_img), item.goods_image);
        helper.setText(R.id.tv_goods_name, item.goods_name)
                .setText(R.id.tv_goods_type, item.goods_spec_name)
                .setText(R.id.tv_goods_price, "￥" + item.goods_price)
                .setText(R.id.tv_count, "x" + item.goods_num);
        helper.setGone(R.id.tv_shouhou,showRefund).addOnClickListener(R.id.tv_shouhou);
    }

}
