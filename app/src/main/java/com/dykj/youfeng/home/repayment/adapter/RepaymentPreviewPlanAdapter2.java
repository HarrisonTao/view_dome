package com.dykj.youfeng.home.repayment.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.RepaymentPreviewPlanBean;

import androidx.annotation.NonNull;

public class RepaymentPreviewPlanAdapter2 extends BaseQuickAdapter<RepaymentPreviewPlanBean.PlanBean, BaseViewHolder> {
    public RepaymentPreviewPlanAdapter2(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RepaymentPreviewPlanBean.PlanBean item) {
        int type = item.repayment_item_type;
        TextView tvType = helper.getView(R.id.tv_type);
        if (1 == type) {
            tvType.setText("消费");
        } else if (2 == type) {
            tvType.setText("还款");
        }

        helper.setText(R.id.tv_time, item.repayment_time)
                .setText(R.id.tv_money, "￥" + item.repayment_money)
                .setText(R.id.tv_fee_money, "手续费￥ " + item.repayment_fee_money);

    }
}
