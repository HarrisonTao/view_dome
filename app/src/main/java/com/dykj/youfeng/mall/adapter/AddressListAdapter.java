package com.dykj.youfeng.mall.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.AddressBean;


import java.util.List;

/**
 * Created by lcjing on 2019/7/3.
 */
public class AddressListAdapter extends BaseQuickAdapter<AddressBean, BaseViewHolder> {

    public AddressListAdapter(int layoutResId, List<AddressBean> data) {
        super(layoutResId, data);
    }

    private boolean isSelect;

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressBean item) {
        helper.setText(R.id.tv_name_phone, item.true_name + "   " + item.tel_phone);
        helper.setText(R.id.tv_address, item.province+ "  " + item.city + "  " + item.area+ "  " +
                item.area_info);


        if (isSelect) {
            helper.setGone(R.id.ll_bottom, false);
            helper.setGone(R.id.v_line_bottom,false);
        } else {
            helper.setGone(R.id.ll_bottom, true);
            helper.setGone(R.id.v_line_bottom,true);
            helper.addOnClickListener(R.id.tv_edit)
                    .addOnClickListener(R.id.tv_del)
                    .addOnClickListener(R.id.tv_default);
            helper.setChecked(R.id.cb_default, "1".equals(item.is_default));
//            helper.setText(R.id.cb_default, "1".equals(item.is_default) ? "已设为默认" : "设为默认");
        }
    }

}
