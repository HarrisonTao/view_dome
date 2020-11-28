package com.dykj.youfeng.home.repayment.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.ChannelList;


import androidx.annotation.NonNull;

public class RepaymentChannelAdapter extends BaseQuickAdapter<ChannelList, BaseViewHolder> {
    public RepaymentChannelAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ChannelList item) {

        Double iRate = item.rate;

        helper.setText(R.id.tv_channel_name, item.name)
                .setText(R.id.tv_content, item.content)
                .setText(R.id.tv_channel_rate,  (iRate / 100) + "%+" + item.cost )
                .setText(R.id.tv_channel_quota,  item.money )
                .setText(R.id.tv_channel_data, item.dayMoney)
                .setText(R.id.tv_channel_info, item.info);

        View llGroup = helper.getView(R.id.ll_group);
        int adapterPosition = helper.getAdapterPosition();
        if (adapterPosition % 3 == 0) {
            llGroup.setBackgroundResource(R.mipmap.tongd_bg1);
        } else if (adapterPosition % 3 == 1) {
            llGroup.setBackgroundResource(R.mipmap.tongd_bg2);
        } else if (adapterPosition % 3 == 2) {
            llGroup.setBackgroundResource(R.mipmap.tongd_bg3);
        }

    }
}
