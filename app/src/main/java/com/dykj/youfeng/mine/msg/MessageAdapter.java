package com.dykj.youfeng.mine.msg;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.SystemMegBean;
import com.dykj.module.util.BaseToolsUtil;

import androidx.annotation.NonNull;

public class MessageAdapter extends BaseQuickAdapter<SystemMegBean.ListBean, BaseViewHolder> {
    public MessageAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SystemMegBean .ListBean item) {
        helper.setText(R.id.tv_title,item.title)
                .setText(R.id.tv_time,item.add_time)
                .setText(R.id.tv_content, BaseToolsUtil.StripHT(item.content));


      /*  ImageView view = helper.getView(R.id.iv_sign);
        view.setVisibility(item.is_read == 0? View.VISIBLE :  View.GONE);*/


    }
}
