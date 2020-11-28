package com.dykj.youfeng.mine.integral;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.IntegralBean;
import com.dykj.module.util.DateUtils;

import androidx.annotation.NonNull;

public class IntegralAdapter extends BaseQuickAdapter<IntegralBean.ListBean, BaseViewHolder> {
    public IntegralAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, IntegralBean.ListBean item) {
        helper.setText(R.id.tv_name,item.info).setText(R.id.tv_time, DateUtils.getCompleteTime(item.add_time))
                .setText(R.id.tv_phone,item.affectPoints+"");
    }
}
