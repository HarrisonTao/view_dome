package com.dykj.youfeng.home.rank.adpter;

import android.graphics.Color;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.AgentListBean;
import com.dykj.module.util.GlideUtils;

import androidx.annotation.NonNull;

public class RankAdapter extends BaseQuickAdapter<AgentListBean, BaseViewHolder> {
    public RankAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, AgentListBean item) {
        helper.setText(R.id.tv_name,item.nickName)
                .setText(R.id.tv_number_people,"团队总人数"+item.count)
                .setText(R.id.tv_ls,item.money+"")
                .setText(R.id.tv_bonus,item.bonus+"");

        GlideUtils.setImage(helper.getView(R.id.iv_header),item.image);


        TextView tvNoun = helper.getView(R.id.tv_noun);
        int sort = item.sort;

        if (1 == sort) {
            tvNoun.setText("1");
            tvNoun.setTextColor(Color.WHITE);
            tvNoun.setBackgroundResource(R.mipmap.one);
        } else if (2 == sort) {
            tvNoun.setText("2");
            tvNoun.setTextColor(Color.WHITE);
            tvNoun.setBackgroundResource(R.mipmap.two);
        } else if (3 == sort) {
            tvNoun.setText("3");
            tvNoun.setTextColor(Color.WHITE);
            tvNoun.setBackgroundResource(R.mipmap.three);
        } else {
            tvNoun.setText(sort + "");
            tvNoun.setTextColor(Color.BLACK);
            tvNoun.setBackgroundResource(0);
        }
    }
}
