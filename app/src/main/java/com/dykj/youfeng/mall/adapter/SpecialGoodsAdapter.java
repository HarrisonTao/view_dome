package com.dykj.youfeng.mall.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.GoodsBean;
import com.dykj.module.util.GlideUtils;

import java.util.List;

/**
 * Created by lcjing on 2019/7/3.
 */
public class SpecialGoodsAdapter extends BaseQuickAdapter<GoodsBean, BaseViewHolder> {

    public SpecialGoodsAdapter(int layoutResId, List<GoodsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsBean item) {
        helper.setText(R.id.tv_goods_name, item.goods_name).setText(R.id.tv_goods_price,"￥"+item.goods_price);
//        Glide.with(mContext).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1571919352455&di=2895f4a8ba5e5cbe389679d1e54d4a1e&imgtype=0&src=http%3A%2F%2Fwww.t-jiaju.com%2FtujjJDAzJDIxL1RCMVJHOVFGViQ2Y0UkNiQ2JDZfISEkMyQ3.jpg")
//                .into((ImageView) helper.getView(R.id.iv_goods_img));
        GlideUtils.setMallImage(helper.getView(R.id.iv_goods_img),item.goods_img);
    }



}
