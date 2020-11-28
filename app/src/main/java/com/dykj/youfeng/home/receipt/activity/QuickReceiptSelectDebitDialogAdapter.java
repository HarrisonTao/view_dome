package com.dykj.youfeng.home.receipt.activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.DebitcardListBean;
import com.dykj.module.util.GlideUtils;

import androidx.annotation.NonNull;

public class QuickReceiptSelectDebitDialogAdapter extends BaseQuickAdapter<DebitcardListBean, BaseViewHolder> {
    public QuickReceiptSelectDebitDialogAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DebitcardListBean item) {
        String bank_num = item.bank_num;
        GlideUtils.setImage(helper.getView(R.id.tv_bank_logo),item.logo);
        helper.setText(R.id.tv_bank_name,item.bank_name)
                .setText(R.id.tv_bank_end_number,"("+bank_num.substring(bank_num.length()-4)+")");
    }
}
