package com.dykj.youfeng.view.adapter;


import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.GoodsInfoBean;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * Created by lcjing on 2019/10/28.
 */
public class GoodsSpecAdapter extends BaseQuickAdapter<GoodsInfoBean.GoodInfoBean.GoodsSpeccBean, BaseViewHolder> {

    public GoodsSpecAdapter(List<GoodsInfoBean.GoodInfoBean.GoodsSpeccBean> data, SelectCall selectCall) {
        super(R.layout.item_goods_sp, data);
        this.selectCall=selectCall;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsInfoBean.GoodInfoBean.GoodsSpeccBean item) {
        helper.setText(R.id.tv_name, item.sp_name);
        final TagFlowLayout mFlowLayout = helper.getView(R.id.tfl);
        final GoodsTagAdapter adapter = new GoodsTagAdapter(item.goods_sp);
        adapter.setContext(mContext);
        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                item.sp_child_id= item.goods_sp.get(position).id;
                item.sp_child_name=item.goods_sp.get(position).name;
                for (int i = 0; i <item.goods_sp.size(); i++) {
                    item.goods_sp.get(i).select = i == position;
                }
                adapter.notifyDataChanged();
                if (selectCall!=null) {
                    selectCall.call();
                }
                return true;
            }
        });
        mFlowLayout.setAdapter(adapter);
    }

    private SelectCall selectCall;

    public interface SelectCall{
        void call();
    }

}
