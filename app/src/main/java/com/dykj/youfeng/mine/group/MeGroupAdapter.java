package com.dykj.youfeng.mine.group;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.GroupBean;

import androidx.annotation.NonNull;

public class MeGroupAdapter extends BaseQuickAdapter<GroupBean.ListBean, BaseViewHolder> {
    public MeGroupAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, GroupBean.ListBean item) {
        helper.setText(R.id.tv_name, TextUtils.isEmpty(item.realname)?"未实名":item.realname)
                .setText(R.id.tv_levelName,"("+item.levelName+")")
                .setText(R.id.tv_time,item.regTime)
                .setText(R.id.tv_phone,item.phone);
    }
}
