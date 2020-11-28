package com.dykj.youfeng.home.receipt.activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.CreditcardListBean;
import com.dykj.module.util.GlideUtils;

import androidx.annotation.NonNull;

public class QuickReceiptSelectCreditDialogAdapter extends BaseQuickAdapter<CreditcardListBean.ListBean, BaseViewHolder> {
    public QuickReceiptSelectCreditDialogAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CreditcardListBean.ListBean item) {
        GlideUtils.setImage(helper.getView(R.id.tv_bank_logo),item.logo);
        helper.setText(R.id.tv_bank_name,item.bank_name)
                .setText(R.id.tv_bank_end_number,"("+item.bank_num+")");
    }
}
