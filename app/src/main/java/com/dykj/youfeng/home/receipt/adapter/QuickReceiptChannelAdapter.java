package com.dykj.youfeng.home.receipt.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.QuickChannelBean;
import com.dykj.youfeng.tools.ArithUtil;

import androidx.annotation.NonNull;

public class QuickReceiptChannelAdapter extends BaseQuickAdapter<QuickChannelBean.ChannelBean, BaseViewHolder> {
    public QuickReceiptChannelAdapter(int layoutResId) {
        super(layoutResId);
    }


  private   QuickChannelBean.ChannelBean bean;
    @Override
    protected void convert(@NonNull BaseViewHolder helper, QuickChannelBean.ChannelBean item) {

        float fRate ,fCost;
        try {
            fRate = Float.parseFloat(item.rate);
            fCost=Float.parseFloat(item.cost);
        }catch (Exception e){
            fRate = 0;
            fCost=0;
        }
        helper.setText(R.id.tv_channel_name, item.name)
                .setText(R.id.tv_fl, "费率:" + (fRate / 100) + "%+" + item.cost + "元/笔")
                .setText(R.id.tv_time, "交易时间:" + item.time)
                .setText(R.id.tv_edu, "额度：单笔限制最低消费" + item.minMoney + "元" + "\n" +"     单笔限制最高消费" + item.maxMoney + "元")
                .setText(R.id.tv_money, "到账金额：￥0.00")
                .addOnClickListener(R.id.tv_look_bank);

        ImageView checkBox = helper.getView(R.id.check_box);
        checkBox.setImageResource(item.isCheck ? R.mipmap.btn_xuanz_small : R.mipmap.btn_xuan_small);
        if(item.isCheck){
            bean=item;
        }


        String minMoney = item.minMoney;
        float skMoney = Float.parseFloat(money);
        if (skMoney < Float.parseFloat(minMoney)) {
            helper.setText(R.id.tv_money, "¥0.00");
        } else {
            skMoney = skMoney - skMoney * fRate / 10000 - fCost;
            helper.setText(R.id.tv_money, "¥" + ArithUtil.formatNum(skMoney));
        }
    }

    public QuickChannelBean.ChannelBean getChanne(){
        return  bean;
    }


    private String money = "0";

    public void setMoney(String money) {
        this.money = money;
    }


}
