package com.dykj.youfeng.mall.activity;


import android.widget.TextView;

import com.dykj.youfeng.R;
import com.dykj.module.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class PaySuccessActivity extends BaseActivity {

    @BindView(R.id.tv_pay_moeny)
    TextView tvPayMoeny;
    @BindView(R.id.tv_know)
    TextView tvKnow;

    @Override
    public int intiLayout() {
        return R.layout.activity_pay_success;
    }

    @Override
    public void initData() {
        initTitle("支付成功");
    }

    @Override
    public void doBusiness() {
        tvPayMoeny.setText(getIntentData().toString());
    }


    @OnClick(R.id.tv_know)
    public void onViewClicked() {
        finish();
    }
}
