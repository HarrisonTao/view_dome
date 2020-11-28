package com.dykj.youfeng.mall.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.EvaListBean;
import com.dykj.module.util.GlideUtils;

import java.util.List;

/**
 * Created by lcjing on 2019/7/3.
 */
public class UnEvaListAdapter extends BaseQuickAdapter<EvaListBean.ListBean, BaseViewHolder> {

    public UnEvaListAdapter(int layoutResId, List<EvaListBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EvaListBean.ListBean item) {


        RecyclerView rvImg=helper.getView(R.id.rv_img);
        GlideUtils.setImage(helper.getView(R.id.iv_goods),item.geval_goodsimage);
        helper.setText(R.id.tv_goods_name, item.geval_goodsname);
        helper.setText(R.id.tv_price, "￥"+item.geval_goodsprice);
        helper.addOnClickListener(R.id.tv_eva);

    }

}
