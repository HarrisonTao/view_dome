package com.dykj.youfeng.mall.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.OrderListBean;
import com.dykj.module.util.GlideUtils;

import java.util.List;

/**
 * Created by lcjing on 2019/7/3.
 */
public class OrderListGoodsAdapter extends BaseQuickAdapter<OrderListBean.OrderInfoBean.OrderGoodsBean, BaseViewHolder> {

    public OrderListGoodsAdapter(int layoutResId, List<OrderListBean.OrderInfoBean.OrderGoodsBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, OrderListBean.OrderInfoBean.OrderGoodsBean item) {
        GlideUtils.setImage(helper.getView(R.id.iv_goods_img), item.goods_image);
        helper.setText(R.id.tv_goods_name, item.goods_name)
                .setText(R.id.tv_goods_type, item.goods_spec_name)
                .setText(R.id.tv_goods_price, "￥" + item.goods_price)
                .setText(R.id.tv_count, "x" + item.goods_num);
//                .setText(R.id.tv_reduce, "积分抵扣-￥" + "5.20")
//                .setText(R.id.tv_tot_count, "共" + "1" + "件商品")
//                .setText(R.id.tv_money, "￥" + "223");
//        //根据订单状态控制按钮的显示隐藏 以及相应的点击事件
//        helper.setText(R.id.tv_status, "待收货")
//                .setGone(R.id.tv_cancel, false)
//                .setGone(R.id.tv_go_pay, false)
//                .setGone(R.id.tv_exp, true)
//                .setGone(R.id.tv_receipt, true)
//                .setGone(R.id.tv_delete, false)
//                .setGone(R.id.tv_remark, false)
//                .addOnClickListener(R.id.tv_cancel)
//                .addOnClickListener(R.id.tv_go_pay)
//                .addOnClickListener(R.id.tv_exp)
//                .addOnClickListener(R.id.tv_receipt)
//                .addOnClickListener(R.id.tv_delete)
//                .addOnClickListener(R.id.tv_remark);


    }

}
