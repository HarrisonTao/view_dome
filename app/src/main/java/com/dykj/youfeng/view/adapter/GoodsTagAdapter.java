package com.dykj.youfeng.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.GoodsInfoBean;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;

/**
 * Created by lcjing on 2018/12/28.
 */

public class GoodsTagAdapter extends TagAdapter<GoodsInfoBean.GoodInfoBean.GoodsSpeccBean.GoodsSpBean> {
    public Context context;

    public GoodsTagAdapter(List<GoodsInfoBean.GoodInfoBean.GoodsSpeccBean.GoodsSpBean> datas) {
        super(datas);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View getView(FlowLayout parent, int position, GoodsInfoBean.GoodInfoBean.GoodsSpeccBean.GoodsSpBean typeBean) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_goods_type, parent, false);
        TextView tvName = view.findViewById(R.id.tv_name);
        tvName.setText(typeBean.name);

        if (typeBean.select) {
            tvName.setBackground(context.getResources().getDrawable(R.drawable.rect_goods_type1));
            tvName.setTextColor(context.getResources().getColor(R.color.main));
        } else {
            tvName.setTextColor(context.getResources().getColor(R.color.font_ff33));
            tvName.setBackground(context.getResources().getDrawable(R.drawable.rect_goods_type2));
        }
        return view;
    }


}
