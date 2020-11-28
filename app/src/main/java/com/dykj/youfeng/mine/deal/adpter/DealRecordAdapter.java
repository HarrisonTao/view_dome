package com.dykj.youfeng.mine.deal.adpter;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.IndexBillList;

import androidx.annotation.NonNull;

public class DealRecordAdapter extends BaseQuickAdapter<IndexBillList.ListBean, BaseViewHolder> {
    public DealRecordAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, IndexBillList.ListBean item) {
        int type = item.type;
//        1-代还消费，2-代还还款，3-快捷收款

        TextView tvTpye = helper.getView(R.id.tv_recharge_title);
        switch (type) {
            case 1:
                tvTpye.setText("代还消费:¥" + item.money);
                break;

            case 2:
                tvTpye.setText("代还还款:¥" + item.money);
                break;

            case 3:
                tvTpye.setText("快捷收款:¥" + item.money);
                break;
        }

        helper.setText(R.id.tv_recharge_date, item.add_time).setText(R.id.tv_recharge_type, item.info);
        TextView tvBank = helper.getView(R.id.tv_recharge_bankname);

        String in_bank_num = item.in_bank_num;
        String substring = in_bank_num.substring(in_bank_num.length() - 4);
        String bankName = TextUtils.isEmpty(item.bank_name) ? "" : item.bank_name;
        tvBank.setText(bankName + "(" + substring + ")");


    }
}
