package com.dykj.youfeng.mine.deal.adpter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.CreditcardListBean;

/**
 * Created by  zhaoyanhui
 * Describe : 计划适配器
 * on :2019-09-06
 */
public class IndexCreeditAdapter extends BaseQuickAdapter<CreditcardListBean.ListBean, BaseViewHolder> {
    public IndexCreeditAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper,CreditcardListBean.ListBean item) {
        helper.setText(R.id.tv_bank_name,item.bank_name);
    }
}
