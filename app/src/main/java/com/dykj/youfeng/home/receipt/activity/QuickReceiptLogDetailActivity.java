package com.dykj.youfeng.home.receipt.activity;

import android.widget.TextView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.QuickInfoBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.tencent.mmkv.MMKV;

import butterknife.BindView;

public class QuickReceiptLogDetailActivity extends BaseActivity {
    @BindView(R.id.tv_money_explain)
    TextView tvMoneyExplain;
    @BindView(R.id.tv_arrival_money)
    TextView tvArrivalMoney;
    @BindView(R.id.tv_channel_name)
    TextView tvChannelName;
    @BindView(R.id.tv_channel_fl)
    TextView tvChannelFl;
    @BindView(R.id.tv_credit_card_num)
    TextView tvCreditCardNum;
    @BindView(R.id.tv_bank_card_num)
    TextView tvBankCardNum;
    @BindView(R.id.tv_order_no)
    TextView tvOrderNo;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_info)
    TextView tvInfo;

    @Override
    public int intiLayout() {
        return R.layout.activity_log_details;
    }

    @Override
    public void initData() {
        initTitle("记录详情");

        if (null != getIntentData()) {
            String infoId = (String) getIntentData();
            requestInfo(infoId);
        }

    }


    @Override
    public void doBusiness() {

    }


    /***
     * 收款详情
     * @param infoId
     */
    private void requestInfo(String infoId) {
        HttpFactory.getInstance().quickInfo(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), infoId)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<QuickInfoBean>>() {
                    @Override
                    public void success(HttpResponseData<QuickInfoBean> data) {
                        if ("9999".equals(data.status)) {
                            setViewData(data.getData());
                        } else {
                            DyToast.getInstance().error(data.message);
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));
    }


    /**
     * 设置view
     *
     * @param data
     */
    private void setViewData(QuickInfoBean data) {
        tvArrivalMoney.setText("￥"+data.account_money);
        tvChannelName.setText(data.type_name);
        tvChannelFl.setText((data.pay_rate / 100.0) + "%+" + data.cash_money + "元/笔");
        tvCreditCardNum.setText(data.credit_name + "(" + data.credit_num.substring(data.credit_num.length() - 4) + ")");
        tvBankCardNum.setText(data.debit_name + "(" + data.debit_num.substring(data.debit_num.length() - 4) + ")");
        tvOrderNo.setText(data.order_no);
        tvTime.setText(data.add_time);
        tvPhone.setText(data.phone);

        int status = data.status;
        switch (status) {
            case 0:
                tvInfo.setText("未完成");
                break;
            case 1:
                tvInfo.setText("收款成功");
                break;
            case 2:
                tvInfo.setText("收款失败");
                break;
            case 3:
            case 4:
                tvInfo.setText("收款中");
                break;
        }
    }

}
