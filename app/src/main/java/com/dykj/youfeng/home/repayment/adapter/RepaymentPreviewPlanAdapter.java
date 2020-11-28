package com.dykj.youfeng.home.repayment.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.PlanBean;
import com.dykj.module.util.DateUtils;


import androidx.annotation.NonNull;

public class RepaymentPreviewPlanAdapter extends BaseQuickAdapter<PlanBean, BaseViewHolder> {
    public RepaymentPreviewPlanAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PlanBean item) {
        String type = item.type;
        TextView tvType = helper.getView(R.id.tv_type);
        if ("1".equals(type)) {
            tvType.setText("消费");
        } else if ("2".equals(type)) {
            tvType.setText("代付");
        }

        helper.setText(R.id.tv_time, DateUtils.getCompleteTime(item.run_time))
                .setText(R.id.tv_money,"￥" + item.money)
                .setText(R.id.tv_fee_money,"手续费￥ "+ item.fee_money);

    }
}
