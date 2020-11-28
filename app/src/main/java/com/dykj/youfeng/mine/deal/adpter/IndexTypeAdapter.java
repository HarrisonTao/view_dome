package com.dykj.youfeng.mine.deal.adpter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;

/**
 * Created by  zhaoyanhui
 * Describe : 计划适配器
 * on :2019-09-06
 */
public class IndexTypeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public IndexTypeAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper,String item) {
        helper.setText(R.id.tv_bank_name,item);
    }
}
