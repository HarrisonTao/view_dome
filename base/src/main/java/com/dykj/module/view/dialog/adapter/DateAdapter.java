package com.dykj.module.view.dialog.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.module.R;
import com.dykj.module.view.bean.DateBean;

import java.util.List;

/**
 * Created by lcjing on 2019/3/20.
 */

public class DateAdapter extends BaseQuickAdapter<DateBean, BaseViewHolder> {


    public DateAdapter(List<DateBean> data) {
        super(R.layout.item_repayment_calendar, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DateBean item) {
        helper.setVisible(R.id.fl_bg,item.isShow()&&item.isSelect());
        helper.setVisible(R.id.tv_date,item.isShow());

        helper.setTextColor(R.id.tv_date,item.isEnable()?
                Color.parseColor("#FF333333"): Color.parseColor("#FF888888"));
        if (item.isEnable()) {
            helper.setTextColor(R.id.tv_date,item.isSelect()?
                    Color.parseColor("#FFFFFFFF"): Color.parseColor("#FF333333"));
        }

        helper.setText(R.id.tv_date,item.getDay());

    }
}
