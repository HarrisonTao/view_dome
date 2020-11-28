package com.dykj.youfeng.home.repayment.adapter;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.PlanInfoBean;

import androidx.annotation.NonNull;

public class PlanDetailAdapter extends BaseQuickAdapter<PlanInfoBean.PlanBean, BaseViewHolder> {
    public PlanDetailAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PlanInfoBean.PlanBean item) {
        int type = item.type;
        if (1 == type) {
            helper.setText(R.id.tv_xf_money, "消费 :￥" + item.money);
        } else if (2 == type) {
            if (!TextUtils.isEmpty(mType)){
                helper.setText(R.id.tv_xf_money, "还款 :￥" + item.money);
            }else {
                helper.setText(R.id.tv_xf_money, "代付 :￥" + item.money);
            }
        }
        helper.setText(R.id.tv_time, item.run_time);
        TextView tvStatus = helper.getView(R.id.tv_status);
        int status = item.status;    //0是未执行 1是执行中 2是执行成功 3是失败
        ImageView ivType = helper.getView(R.id.iv_type);
        switch (status) {
            case 0:
                tvStatus.setText("未执行");
                ivType.setImageResource(R.mipmap.ic_wait);
                break;

            case 1:
                tvStatus.setText("执行中");
                ivType.setImageResource(R.mipmap.ic_wait);
                break;

            case 2:
                tvStatus.setText("已执行");
                ivType.setImageResource(R.mipmap.ic_wanc);
                break;

            case 3:
                tvStatus.setText("已失败");
                ivType.setImageResource(R.mipmap.ic_fail);
                break;
        }
    }

    private String mType;
    public void setType(String type) {
        this.mType = type;
    }
}
