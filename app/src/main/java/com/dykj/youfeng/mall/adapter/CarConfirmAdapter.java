package com.dykj.youfeng.mall.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.ConfirmOrderCarBean;
import com.dykj.module.util.GlideUtils;

import java.util.List;


/**
 * Created by lcjing on 2019/7/3.
 */
public class CarConfirmAdapter extends BaseQuickAdapter<ConfirmOrderCarBean.CarListBean, BaseViewHolder> {

    public CarConfirmAdapter(int layoutResId, List<ConfirmOrderCarBean.CarListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ConfirmOrderCarBean.CarListBean item) {
        helper.setText(R.id.tv_goods_name, item.goods_name)
                .setText(R.id.tv_goods_type, item.goods_spec_name)
                .setText(R.id.tv_goods_price, "￥" +item.goods_price)
                .setText(R.id.tv_num, item.goods_num);
        GlideUtils.setMallImage(helper.getView(R.id.iv_goods_img),item.goods_img);
    }


}
