package com.dykj.youfeng.mine.yue;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.BalanceBean;

import androidx.annotation.NonNull;

public class BalanceAdapter extends BaseQuickAdapter<BalanceBean .ListBean, BaseViewHolder> {
    public BalanceAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BalanceBean.ListBean item) {
        helper.setText(R.id.tv_time, item.add_time);

        TextView tv = helper.getView(R.id.tv_name);
        TextView tvAffectMoney = helper.getView(R.id.tv_phone);
        int moneyType = item.moneyType;
        switch (moneyType){
            case 1:
                tv.setText("收益入账");
                tvAffectMoney.setText("+"+item.affectMoney);
                break;

            case 2:
                tv.setText("提现申请");
                tvAffectMoney.setText("-"+item.affectMoney);
                break;

            case 3:
                tv.setText("提现成功");
                tvAffectMoney.setText("-"+item.affectMoney);
                break;

            case 4:
                tv.setText("提现失败");
                tvAffectMoney.setText("+"+item.affectMoney);
                break;

            case 5:
                tv.setText("购买VIP");
                tvAffectMoney.setText("-"+item.affectMoney);
                break;

            case 6:
                tv.setText("购买商品");
                tvAffectMoney.setText("-"+item.affectMoney);
                break;

            case 7:
                tv.setText("商品退款");
                tvAffectMoney.setText("+"+item.affectMoney);
                break;

            case 15:
                tv.setText("绑卡支付");
                tvAffectMoney.setText("-"+item.affectMoney);
                break;

        }
    }
}
