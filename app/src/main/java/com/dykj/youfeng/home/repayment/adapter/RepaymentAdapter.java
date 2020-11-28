package com.dykj.youfeng.home.repayment.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.RepaymentPlanListBean;
import com.dykj.module.util.GlideUtils;


import androidx.annotation.NonNull;

public class RepaymentAdapter extends BaseQuickAdapter<RepaymentPlanListBean.ListBean, BaseViewHolder> {
    public RepaymentAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RepaymentPlanListBean.ListBean item) {
        String fee_money = item.fee_money;
        String frequency_money = item.frequency_money;

        Double countFeeMoney = Double.parseDouble(TextUtils.isEmpty(fee_money) ? "0.0" : fee_money) + Double.parseDouble(TextUtils.isEmpty(frequency_money) ? "0.0" : frequency_money);
        Double repaymetCountMoney = countFeeMoney + Double.parseDouble(TextUtils.isEmpty(item.repayment_money) ? "0.0" : item.repayment_money);

        GlideUtils.setImage(helper.getView(R.id.iv_bank_logo), item.logo);
        helper.setText(R.id.tv_bank_name, item.bank_name + "(" + item.bank_num + ")")
                .setText(R.id.tv_time, item.add_time)
                .setText(R.id.tv_min_money, item.min_money)
                .setText(R.id.has_repayment_money, item.has_repayment_money)
                .setText(R.id.tv_fee_money,  countFeeMoney+"")
                .setText(R.id.tv_count_money,  repaymetCountMoney+"")
                .addOnClickListener(R.id.tv_cancel);


        TextView tvType = helper.getView(R.id.tv_type);
        int status = item.status;
        switch (status) {
            case 0:
                tvType.setText("已取消");
                helper.setVisible(R.id.tv_cancel, false);
                break;

            case 1:
                tvType.setText("进行中");
                helper.setVisible(R.id.tv_cancel, true);
                break;

            case 2:
                tvType.setText("已完成");
                helper.setVisible(R.id.tv_cancel, false);
                break;
            case 3:
                tvType.setText("已失败");
                helper.setVisible(R.id.tv_cancel, false);
                break;
        }

    }
}
