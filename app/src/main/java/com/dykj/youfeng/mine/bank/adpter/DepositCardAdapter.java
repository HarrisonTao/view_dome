package com.dykj.youfeng.mine.bank.adpter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.DebitcardListBean;
import com.dykj.module.util.GlideUtils;

import androidx.annotation.NonNull;

public class DepositCardAdapter extends BaseQuickAdapter<DebitcardListBean, BaseViewHolder> {
    public DepositCardAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DebitcardListBean item) {
        helper.setText(R.id.tv_bank_name, item.bank_name)
                .setText(R.id.tv_card_number, item.bank_num)
                .addOnClickListener(R.id.iv_delete);


        GlideUtils.setImage(helper.getView(R.id.iv_bank_logo), item.logo);
    }
}
