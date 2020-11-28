package com.dykj.youfeng.mall.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.CarListBean;
import com.dykj.module.util.GlideUtils;

import java.util.List;


/**
 * Created by lcjing on 2019/7/3.
 */
public class CarAdapter extends BaseQuickAdapter<CarListBean.GoodsInfoBean, BaseViewHolder> {

    public CarAdapter(int layoutResId, List<CarListBean.GoodsInfoBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CarListBean.GoodsInfoBean item) {
        helper.setText(R.id.tv_goods_name, item.goods_name)
                .setText(R.id.tv_goods_type, item.goods_spec_name)
                .setText(R.id.tv_goods_price, "￥" + item.goods_price)
                .setText(R.id.tv_num, item.goods_num)
                .setVisible(R.id.tv_down, "0".equals(item.goods_state))
                .setVisible(R.id.ll_down, "0".equals(item.goods_state));
        GlideUtils.setMallImage(helper.getView(R.id.iv_goods_img), item.goods_img);
        helper.setImageResource(R.id.iv_check, item.select ? R.mipmap.btn_xuanz_c : R.mipmap.btn_xuanz_big);
        helper.addOnClickListener(R.id.iv_check)
                .addOnClickListener(R.id.iv_delete)
                .addOnClickListener(R.id.iv_cut)
                .addOnClickListener(R.id.iv_add);
    }


}
