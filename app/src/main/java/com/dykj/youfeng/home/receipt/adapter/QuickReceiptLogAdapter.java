package com.dykj.youfeng.home.receipt.adapter;

import android.graphics.Color;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.QuickListBean;


import androidx.annotation.NonNull;

public class QuickReceiptLogAdapter extends BaseQuickAdapter<QuickListBean.ListBean, BaseViewHolder> {
    public QuickReceiptLogAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, QuickListBean.ListBean item) {
        helper.setText(R.id.tv_time, item.add_time)
                .setText(R.id.tv_money, "到账金额:￥" + item.account_money)
                .setText(R.id.tv_fee_money, "手续费:￥" + item.fee_money)
                .setText(R.id.tv_repayment_money, "收款金额:￥" + item.money);

        TextView tvType = helper.getView(R.id.tv_type);
        int status = item.status;   // （0.未完成 1.成功 2.失败 3.进行中 4.进行中）
        switch (status) {
            case 0:
                tvType.setText("未完成");
                tvType.setTextColor(tvType.getContext().getResources().getColor(R.color.font_ff99));
                break;
            case 1:
                tvType.setText("收款成功");
                tvType.setTextColor(tvType.getContext().getResources().getColor(R.color.main_color));
                break;
            case 2:
                tvType.setText("收款失败");
                tvType.setTextColor(Color.RED);
                break;
            case 3:
            case 4:
                tvType.setText("收款中");
                tvType.setTextColor(tvType.getContext().getResources().getColor(R.color.main_color));
                break;
        }

    }
}
