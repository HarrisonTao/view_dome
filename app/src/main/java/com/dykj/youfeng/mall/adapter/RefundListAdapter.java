package com.dykj.youfeng.mall.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.RefundListBean;
import com.dykj.module.util.GlideUtils;

import java.util.List;

/**
 * Created by lcjing on 2019/7/3.
 */
public class RefundListAdapter extends BaseQuickAdapter<RefundListBean.ListBean, BaseViewHolder> {

    public RefundListAdapter(int layoutResId, List<RefundListBean.ListBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, RefundListBean.ListBean item) {
        GlideUtils.setImage(helper.getView(R.id.iv_goods_img), item.order_goods.goods_image);
        helper.setText(R.id.tv_goods_name, item.order_goods.goods_name)
                .setText(R.id.tv_goods_type, item.order_goods.goods_spec_name)
                .setText(R.id.tv_goods_price, "￥" + item.order_goods.goods_price)
                .setText(R.id.tv_count, "x" + item.order_goods.goods_num)
                .addOnClickListener(R.id.tv_info);
//        refund_state	int	1-退款成功
//        lock_state	int	1-退款中

      if ("1".equals(item.lock_state)) {
          helper.setGone(R.id.ll_status, false);
          helper.setGone(R.id.v_status, false);
//            helper.setText(R.id.tv_refund_status, "退款中");
//            helper.setGone(R.id.tv_refund_status, true);
        }else {
          helper.setGone(R.id.ll_status, true);
          helper.setGone(R.id.v_status, true);
      }
      if ("1".equals(item.refund_state)) {
          helper.setText(R.id.tv_refund_status, "退款成功");
          helper.setGone(R.id.tv_refund_status, true);
      }

        switch (item.refund_type) {
            case "0":
//                helper.setText(R.id.tv_refund_type, "申请中");
//                ((TextView) helper.getView(R.id.tv_refund_type))
//                        .setCompoundDrawablesWithIntrinsicBounds(null
//                                , null, null, null);
//                helper.setGone(R.id.tv_refund_status, false);
//                break;
            case "1":
                helper.setText(R.id.tv_refund_type, "仅退款");
                ((TextView) helper.getView(R.id.tv_refund_type))
                        .setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_tuik)
                                , null, null, null);
                break;
            case "2":
                helper.setText(R.id.tv_refund_type, "退款退货");
                ((TextView) helper.getView(R.id.tv_refund_type))
                        .setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_tkth)
                                , null, null, null);
                break;
            default:
                break;
        }


    }

}
