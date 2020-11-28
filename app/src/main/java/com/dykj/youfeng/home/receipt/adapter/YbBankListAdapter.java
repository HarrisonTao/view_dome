package com.dykj.youfeng.home.receipt.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.YbBankList;

import androidx.annotation.NonNull;

public class YbBankListAdapter extends BaseQuickAdapter<YbBankList.ListBean,BaseViewHolder> {

    public YbBankListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, YbBankList.ListBean item) {
        helper.setText(R.id.tv_bank_name,item.name);
    }
}
