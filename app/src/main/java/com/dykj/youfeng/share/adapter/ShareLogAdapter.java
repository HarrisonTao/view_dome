package com.dykj.youfeng.share.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.ShareBean;


import androidx.annotation.NonNull;

public class ShareLogAdapter extends BaseQuickAdapter<ShareBean.ListBean, BaseViewHolder> {
    public ShareLogAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ShareBean.ListBean item) {
        helper.setText(R.id.tv_user_name, TextUtils.isEmpty(item.realname) ? "未实名":item.realname)
                .setText(R.id.tv_share_time,item.regTime)
                .setText(R.id.tv_user_phone,item.phone);
    }
}
