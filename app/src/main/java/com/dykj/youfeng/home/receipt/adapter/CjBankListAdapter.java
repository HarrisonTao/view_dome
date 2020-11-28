package com.dykj.youfeng.home.receipt.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.CjBankBean;

import androidx.annotation.NonNull;

public class CjBankListAdapter extends BaseQuickAdapter<CjBankBean,BaseViewHolder> {

    public CjBankListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CjBankBean item) {
        helper.setText(R.id.tv_bank_name,item.name);
    }
}
