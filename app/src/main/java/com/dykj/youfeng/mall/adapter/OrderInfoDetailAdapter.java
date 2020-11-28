package com.dykj.youfeng.mall.adapter;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;

import java.util.List;

/**
 * Created by lcjing on 2019/7/3.
 */
public class OrderInfoDetailAdapter extends BaseQuickAdapter<OrderInfoDetailAdapter.Value, BaseViewHolder> {

    public OrderInfoDetailAdapter(int layoutResId, List<Value> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, Value item) {
        if (StringUtils.isEmpty(item.value) || "0".equals(item.value)) {
            helper.setGone(R.id.fl_root, false);
            return;
        }
        helper.setGone(R.id.fl_root, true);
        helper.setText(R.id.tv_name, item.name)
                .setText(R.id.tv_value, item.value);
    }

    public static class Value {
        public String name;
        public String value;

        public Value(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

}
