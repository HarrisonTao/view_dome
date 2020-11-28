package com.dykj.youfeng.home.up_vip;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.VipBean;

import androidx.annotation.NonNull;

public class VipAdapter extends BaseQuickAdapter<VipBean.LevelListBean, BaseViewHolder> {
    public VipAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, VipBean.LevelListBean item) {
        helper.setText(R.id.tv_vip_grade, item.levelName)
                .setText(R.id.tv_repayment_fl, "智能还款费率：小额 万" + item.repayRateMin + "+" + item.repayCost + ",大额 万" + item.repayRateMax + "+" + item.repayCost)
                .setText(R.id.tv_sk_fl, "快捷收款费率：万" + item.quickRate + "+" + item.quickCost)
                .setText(R.id.tv_info, item.levelUpInfo);

        View rlBg = helper.getView(R.id.rl_bg);
        int adapterPosition = helper.getAdapterPosition();

        LogUtils.e(adapterPosition + "----" + adapterPosition % 4);
        switch (adapterPosition  % 6) {
            case 0:
                // 普通会员
                rlBg.setBackgroundResource(R.mipmap.putong_bg);
                setBgTextColor(helper,false);
                break;

            case 1:
                // vip会员
                rlBg.setBackgroundResource(R.mipmap.vip_bg);
                setBgTextColor(helper,true);
                break;

            case 2:
                rlBg.setBackgroundResource(R.mipmap.huangj_bg);
                setBgTextColor(helper,true);

                break;

            case 3:
                // 钻石会员
                rlBg.setBackgroundResource(R.mipmap.zuanshi_bg);
                setBgTextColor(helper,false);
                break;

            case 4:
                // 钻石会员
                rlBg.setBackgroundResource(R.mipmap.yinzuan_bg);
                setBgTextColor(helper,false);
                break;

            case 5:
                // 钻石会员
                rlBg.setBackgroundResource(R.mipmap.yincang_bg);
                setBgTextColor(helper,true);
                break;
        }
    }


    private void setBgTextColor(@NonNull BaseViewHolder helper,boolean isWhite){
        Resources resources = mContext.getResources();
        helper.setTextColor(R.id.tv_vip_grade,isWhite ? resources.getColor(R.color.white) :  resources.getColor(R.color.font_ff33));
        helper.setTextColor(R.id.tv_repayment_fl,isWhite ? resources.getColor(R.color.white) :  resources.getColor(R.color.font_ff33));
        helper.setTextColor(R.id.tv_sk_fl,isWhite ? resources.getColor(R.color.white) :  resources.getColor(R.color.font_ff33));
        helper.setTextColor(R.id.tv_info,isWhite ? resources.getColor(R.color.white) :  resources.getColor(R.color.font_ff33));


        TextView tvFl = helper.getView(R.id.tv_repayment_fl);
        TextView tvSk = helper.getView(R.id.tv_sk_fl);


        Drawable  drawable = resources.getDrawable(isWhite ? R.drawable.circle_shape_color_white : R.drawable.circle_shape_color_fon33);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvFl.setCompoundDrawables(drawable, null, null, null);
        tvSk.setCompoundDrawables(drawable, null, null, null);
    }
}
