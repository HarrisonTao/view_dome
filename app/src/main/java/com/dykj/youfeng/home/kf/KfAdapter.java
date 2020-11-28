package com.dykj.youfeng.home.kf;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.ChatBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class KfAdapter extends BaseQuickAdapter<ChatBean, BaseViewHolder> {


    public KfAdapter(int layoutResId, @Nullable List<ChatBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ChatBean item) {
        if (item.isKf) {
            helper.setGone(R.id.ll_kf, true);
            helper.setGone(R.id.ll_user, false);
            helper.setText(R.id.tv_kf, item.msg);
            helper.setGone(R.id.tv_time, !TextUtils.isEmpty(item.time));
            helper.setText(R.id.tv_time, item.time);

        } else {
            helper.setGone(R.id.ll_kf, false);
            helper.setGone(R.id.tv_time, false);
            helper.setGone(R.id.ll_user, true);
            helper.setText(R.id.tv_user, item.msg);
        }
    }
}
