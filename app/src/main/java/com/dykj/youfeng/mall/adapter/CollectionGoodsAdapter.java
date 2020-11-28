package com.dykj.youfeng.mall.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.CollectListBean;
import com.dykj.module.util.GlideUtils;

import java.util.List;

/**
 *
 * @author lcjing
 * @date 2018/12/24
 */
public class CollectionGoodsAdapter extends BaseQuickAdapter<CollectListBean.ListBean, BaseViewHolder> {



    public CollectionGoodsAdapter(int layoutResId, List<CollectListBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final CollectListBean.ListBean item) {
        helper.setText(R.id.tv_name, item.goods_name);
        helper.setText(R.id.tv_price, item.goods_price);
        helper.setText(R.id.tv_count, item.num+"人收藏");
        GlideUtils.setImage(helper.getView(R.id.iv_content), item.goods_img);
        helper.addOnClickListener(R.id.tv_add_car).addOnClickListener(R.id.tv_remove)
        .addOnClickListener(R.id.ll_content);
    }
}
