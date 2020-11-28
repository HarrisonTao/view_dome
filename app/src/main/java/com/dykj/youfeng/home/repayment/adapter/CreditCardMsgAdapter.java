package com.dykj.youfeng.home.repayment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.CreditcardListBean;
import com.dykj.module.util.GlideUtils;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class CreditCardMsgAdapter extends BaseQuickAdapter<CreditcardListBean.ListBean, BaseViewHolder> {
    private Context context;
    public CreditCardMsgAdapter(Context context,int layoutResId) {
        super(layoutResId);
        this.context=context;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void convert(@NonNull BaseViewHolder helper, CreditcardListBean.ListBean item) {
        GlideUtils.setImage(helper.getView(R.id.iv_bank_logo), item.logo);

          helper.setText(R.id.tv_bank_name, item.bank_name)
                .setText(R.id.tv_end_number,  item.bank_num )
                .setText(R.id.tv_repayment_money, TextUtils.isEmpty(item.repayment_money) ? "0" : item.repayment_money)
                .setText(R.id.tv_repayment_date, item.repayment_date )
                .setText(R.id.tv_statement_date,  item.statement_date )
                .addOnClickListener(R.id.viewLinear);

        int repayment_status = item.repayment_status;

       LinearLayout tvMakePlan = helper.getView(R.id.viewLinear);

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor(item.color));
        gd.setCornerRadii(getCornerRadii(8,8,8,8));
        tvMakePlan.setBackground(gd);
            /*
        if (0 == repayment_status) {  // 未指定计划
            tvMakePlan.setText("制定计划");
           // tvMakePlan.setTextColor(tvMakePlan.getContext().getColor(R.color.main_color));
            tvMakePlan.setBackgroundResource(R.drawable.shape_round_5dp);
        } else if (1 == repayment_status) {  // 计划执行中
            tvMakePlan.setText("查看计划");
            tvMakePlan.setTextColor(Color.WHITE);
            tvMakePlan.setBackgroundResource(R.drawable.bg_deep_shap_5dp);
        }

            */

        if (mIsVisibility) {
            tvMakePlan.setVisibility(View.INVISIBLE);
        }
    }

    private boolean mIsVisibility = false;

    public void setVisibility(boolean isVisibility) {
        this.mIsVisibility = isVisibility;
    }

    private float [] getCornerRadii(float leftTop,float rightTop, float leftBottom,float rightBottom){
        //这里返回的一个浮点型的数组，一定要有8个元素，不然会报错
        return floatArrayOf(dp2px(leftTop), dp2px(leftTop), dp2px(rightTop),
                dp2px(rightTop),dp2px(rightBottom), dp2px(rightBottom),dp2px(leftBottom),dp2px(leftBottom));
    }

    private float[] floatArrayOf(float dp2px, float dp2px1, float dp2px2, float dp2px3, float dp2px4, float dp2px5, float dp2px6, float dp2px7) {
        float array[]={dp2px,dp2px1,dp2px2,dp2px3,dp2px4,dp2px5,dp2px6,dp2px7};
        return  array;
    }

    private float dp2px(float dpVal){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

}
