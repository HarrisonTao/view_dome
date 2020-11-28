package com.dykj.youfeng.mall.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.OrderListBean;

import java.util.List;

/**
 * Created by lcjing on 2019/7/3.
 */
public class OrderListAdapter extends BaseQuickAdapter<OrderListBean.OrderInfoBean, BaseViewHolder> {

    public OrderListAdapter(int layoutResId, List<OrderListBean.OrderInfoBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, OrderListBean.OrderInfoBean item) {
        String pointsFee=item.points_fee;
        if (StringUtils.isEmpty(pointsFee)) {
            pointsFee="0.00";
        }
        helper.setText(R.id.tv_order_no, "订单号：" + item.order_sn)
                .setText(R.id.tv_reduce, "积分抵扣-￥" + pointsFee)
                .setText(R.id.tv_tot_count, "共" + item.goods_count + "件商品")
                .setText(R.id.tv_money, "￥" + item.goods_amount);
        //根据订单状态控制按钮的显示隐藏 以及相应的点击事件
//       订单状态：0-订单取消，10待支付，20已付款、待发货， 30已发货， 40已收货，60交易完成
        helper.setText(R.id.tv_status, "待收货")
                .setGone(R.id.tv_cancel, false)
                .setGone(R.id.tv_go_pay, false)
                .setGone(R.id.tv_exp, false)
                .setGone(R.id.tv_receipt, false)
                .setGone(R.id.tv_delete, false)
                .setGone(R.id.tv_remark, false);
        helper.addOnClickListener(R.id.tv_cancel)
                .addOnClickListener(R.id.tv_go_pay)
                .addOnClickListener(R.id.tv_exp)
                .addOnClickListener(R.id.tv_receipt)
                .addOnClickListener(R.id.tv_delete)
                .addOnClickListener(R.id.tv_remark)
                .addOnClickListener(R.id.v_order_goods);
        switch (item.order_state) {
            case "0":
                helper.setText(R.id.tv_status, "已取消")
                        .setGone(R.id.tv_delete, true);
                break;
            case "10":
                helper.setText(R.id.tv_status, "待付款")
                        .setGone(R.id.tv_cancel, true)
                        .setGone(R.id.tv_go_pay, true);
                break;
            case "20":
                helper.setText(R.id.tv_status, "待发货");
                break;
            case "30":
                helper.setText(R.id.tv_status, "待收货")
                        .setGone(R.id.tv_exp, true)
                        .setGone(R.id.tv_receipt, true);
                break;
            case "40":
                switch (item.evaluation_state) {
                    case "0":
                        helper.setText(R.id.tv_status, "待评价")
                                .setGone(R.id.tv_exp, true)
                                .setGone(R.id.tv_remark, true);
                        break;
                    case "1":
                        helper.setText(R.id.tv_status, "已评价")
                                .setGone(R.id.tv_exp, true);
                        break;
                    case "2":
                        helper.setText(R.id.tv_status, "过期未评价")
                                .setGone(R.id.tv_exp, true);
                        break;
                    default:
                        break;
                }

//                evaluation_state
//                评价状态:0未评价，1已评价，2已过期未评价
                break;
            case "60":
                helper.setText(R.id.tv_status, "已完成")
                        .setGone(R.id.tv_exp, true);
                break;
            default:
                break;
        }


        OrderListGoodsAdapter adapter = new OrderListGoodsAdapter(R.layout.item_order_goods, item.order_goods);
        ((RecyclerView) helper.getView(R.id.rv_order_goods)).setAdapter(adapter);
    }

}
