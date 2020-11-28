package com.dykj.youfeng.mall.adapter;

import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.HasEvaListBean;
import com.dykj.module.util.GlideUtils;
import com.dykj.module.util.date.DateFormatUtil;


import java.util.List;

/**
 * Created by lcjing on 2019/11/3.
 */
public class HasEvaListAdapter extends BaseQuickAdapter<HasEvaListBean.ListBean, BaseViewHolder> {

    public HasEvaListAdapter(int layoutResId, List<HasEvaListBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HasEvaListBean.ListBean item) {
        GlideUtils.setImage(helper.getView(R.id.riv_head), R.mipmap.moren_tx,item.getMember_avatar());
        helper.setText(R.id.tv_name, item.getMember_name());
        helper.setText(R.id.tv_time, DateFormatUtil.getTime(item.getGeval_addtime()));
        helper.setText(R.id.tv_type, item.getSpec_name());
        helper.setText(R.id.tv_content, item.getGeval_content());
//        RecyclerView rvImg = helper.getView(R.id.rv_img);

        GlideUtils.setImage(helper.getView(R.id.iv_goods),item.getGeval_goodsimage());
        helper.setText(R.id.tv_goods_name, item.getGeval_goodsname());
        helper.setText(R.id.tv_price, "￥"+item.getGeval_goodsprice());


        if (item.getGeval_images() != null) {
            if (item.getGeval_images().size() > 2 && !StringUtils.isEmpty(item.getGeval_images().get(2))) {
                helper.getView(R.id.iv_3).setVisibility(View.VISIBLE);
                GlideUtils.setImage(helper.getView(R.id.iv_3), item.getGeval_images().get(2).trim());
            } else {
                helper.getView(R.id.iv_3).setVisibility(View.GONE);
            }
            if (item.getGeval_images().size() > 1 && !StringUtils.isEmpty(item.getGeval_images().get(1))) {
                helper.getView(R.id.iv_2).setVisibility(View.VISIBLE);
                GlideUtils.setImage(helper.getView(R.id.iv_2), item.getGeval_images().get(1).trim());
            } else {
                helper.getView(R.id.iv_2).setVisibility(View.GONE);
            }
            if (item.getGeval_images().size() > 0 && !StringUtils.isEmpty(item.getGeval_images().get(0)) && item.getGeval_images().get(0).length() > 2) {
                helper.getView(R.id.iv_1).setVisibility(View.VISIBLE);
                GlideUtils.setImage(helper.getView(R.id.iv_1), item.getGeval_images().get(0).trim());
            } else {
                helper.getView(R.id.iv_1).setVisibility(View.GONE);
            }
        }

    }

}
