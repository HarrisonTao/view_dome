package com.dykj.youfeng.home.sign.adpter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.SignListBean;

import androidx.annotation.NonNull;

public class SignLogAdapter extends BaseQuickAdapter<SignListBean.InfoBean, BaseViewHolder> {
    public SignLogAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SignListBean.InfoBean item) {
        helper.setText(R.id.tv_time,item.add_time)
                .setText(R.id.tv_integral,"+"+item.affectPoints +"积分");
    }
}
