package com.dykj.youfeng.mine.yue.cash;

import android.graphics.Color;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.CashListBean;

import androidx.annotation.NonNull;

public class TakeOutLogAdapter extends BaseQuickAdapter<CashListBean .ListBean, BaseViewHolder> {
    public TakeOutLogAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CashListBean .ListBean item) {
        helper.setText(R.id.tv_time,item.add_time);

        TextView tvMoney = helper.getView(R.id.tv_money);
        TextView tvStatus = helper.getView(R.id.tv_status);

//        提现状态：0-待审核，1-成功，2-驳回
        int status = item.status;
        switch (status){
            case 0 :
                tvStatus.setText("待审核");
                tvMoney.setText(item.money);
                tvStatus.setTextColor(Color.parseColor("#FF2E5CFF"));
                break;

            case 1:
                tvStatus.setText("成功");
                tvMoney.setText("-"+item.money);
                tvStatus.setTextColor(Color.parseColor("#FF2E5CFF"));
                break;

            case 2 :
                tvStatus.setText("驳回");
                tvMoney.setText(item.money);
                tvStatus.setTextColor(Color.RED);
                break;
        }
    }
}
