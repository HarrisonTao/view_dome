package com.dykj.youfeng.mall.adapter;

import android.graphics.Color;

import com.blankj.utilcode.util.SpanUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.ExpInfoBean;

import java.util.List;

/**
 * Created by lcjing on 2019/11/3.
 */
public class ExpressAdapter extends BaseQuickAdapter<ExpInfoBean.DataBean, BaseViewHolder> {

    public ExpressAdapter(int layoutResId, List<ExpInfoBean.DataBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ExpInfoBean.DataBean item) {
        if (helper.getLayoutPosition() == 0) {
            helper.setGone(R.id.v_line_top, false);
            helper.setBackgroundColor(R.id.v_line_bottom, Color.parseColor("#FF315DFF"));
            helper.setBackgroundRes(R.id.v_status, R.drawable.bg_oval_main);
            helper.setText(R.id.tv_des, new SpanUtils().append(item.context)
                    .setForegroundColor(Color.parseColor("#FF315DFF")).create());
            helper.setText(R.id.tv_time, new SpanUtils().append(item.time)
                    .setForegroundColor(Color.parseColor("#FF315DFF")).create());
        } else {
            helper.setGone(R.id.v_line_top, true);
            helper.setBackgroundRes(R.id.v_status, R.drawable.bg_oval_99);
            helper.setBackgroundColor(R.id.v_line_bottom, Color.parseColor("#FF999999"));
            if (helper.getLayoutPosition() == 1) {
                helper.setBackgroundColor(R.id.v_line_top, Color.parseColor("#FF315DFF"));
            } else {
                helper.setBackgroundColor(R.id.v_line_top, Color.parseColor("#FF999999"));
            }
            helper.setText(R.id.tv_des, new SpanUtils().append(item.context)
                    .setForegroundColor(Color.parseColor("#FFB3B3B3")).create());
            helper.setText(R.id.tv_time, new SpanUtils().append(item.time)
                    .setForegroundColor(Color.parseColor("#FFB3B3B3")).create());
        }

        if (helper.getLayoutPosition() == getItemCount() - 1) {
            helper.setGone(R.id.v_line_bottom, false);
        } else {
            helper.setGone(R.id.v_line_bottom, true);
        }
    }

}
