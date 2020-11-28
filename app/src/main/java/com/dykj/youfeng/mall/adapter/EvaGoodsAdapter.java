package com.dykj.youfeng.mall.adapter;

import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.GoodsEvaBean;
import com.dykj.module.util.GlideUtils;
import com.dykj.module.util.date.DateFormatUtil;

import java.util.List;

/**
 * Created by lcjing on 2018/12/26.
 * 商品评价
 */

public class EvaGoodsAdapter extends BaseQuickAdapter<GoodsEvaBean.ListBean, BaseViewHolder> {


    public EvaGoodsAdapter(int layoutResId, List<GoodsEvaBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final GoodsEvaBean.ListBean item) {
        helper.setText(R.id.tv_name, item.getNickname());
        String spceName = item.getSpec_name();
        if (StringUtils.isEmpty(spceName) || "null".equals(spceName)) {
            spceName = "";
        }
        helper.setText(R.id.tv_type, spceName);
        helper.setText(R.id.tv_content, item.getGeval_content());
        helper.setText(R.id.tv_date, DateFormatUtil.getTime(item.getGeval_addtime()));


        GlideUtils.setImageRound(helper.getView(R.id.riv_head), R.mipmap.moren_tx, item.getAvatar());

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
