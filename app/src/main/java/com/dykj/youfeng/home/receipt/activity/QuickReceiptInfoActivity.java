package com.dykj.youfeng.home.receipt.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.AreaBean;
import com.dykj.youfeng.mode.CjSkInfoBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.WheelPickerBean;
import com.dykj.module.util.WheelPickerUtils;
import com.dykj.module.util.toast.DyToast;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 收款
 */
public class QuickReceiptInfoActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_no)
    TextView mTvNo;
    @BindView(R.id.tv_money)
    TextView mTvMoney;
    @BindView(R.id.tv_fee_money)
    TextView mTvFeeMoney;
    @BindView(R.id.tv_debit_card)
    TextView mTvDebitCard;
    @BindView(R.id.tv_credit_card)
    TextView mTvCreditCard;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    private String mProvince;
    private String mCity;
    private CjSkInfoBean mCJSubmitInfo;

    @Override
    public int intiLayout() {
        return R.layout.activity_quickreceipt_info;
    }

    @Override
    public void initData() {
        initTitle("收款信息确认");
        if (null != getIntentData()) {
            mCJSubmitInfo = (CjSkInfoBean) getIntentData();
            mTvName.setText(mCJSubmitInfo.realname);
            mTvNo.setText(mCJSubmitInfo.idcard);
            mTvMoney.setText(mCJSubmitInfo.money);
            mTvFeeMoney.setText(mCJSubmitInfo.fee_money);
            mTvDebitCard.setText(mCJSubmitInfo.to_bank_name + "(" + mCJSubmitInfo.to_bank_num.substring(mCJSubmitInfo.to_bank_num.length() - 4) + ")");
            mTvCreditCard.setText(mCJSubmitInfo.out_bank_name + "(" + mCJSubmitInfo.out_bank_num.substring(mCJSubmitInfo.out_bank_num.length() - 4) + ")");
            mTvPhone.setText(mCJSubmitInfo.phone);

            String channelType = mCJSubmitInfo.channelType;
            llAddress.setVisibility(channelType.equals("yb") ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void doBusiness() {

    }


    @OnClick({R.id.tv_city, R.id.tv_submit})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                String channelType = mCJSubmitInfo.channelType;
                if (!"yb".equals(channelType)){
                    if (TextUtils.isEmpty(mCity)) {
                        DyToast.getInstance().warning("请选择消费城市");
                        return;
                    }
                    cjSubmit();
                }

                break;

            case R.id.tv_city:
                getArea("", true);
                break;
        }
    }

    /***
     * 畅捷 收款提交
     */
    private void cjSubmit() {
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        map.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        map.put("crediteid", mCJSubmitInfo.crediteid);
        map.put("debitid", mCJSubmitInfo.debitid);
        map.put("money", mCJSubmitInfo.money);
        map.put("channel", mCJSubmitInfo.channelType);
        map.put("city_name", mCity);
        HttpFactory.getInstance().cjSkSubmit(map)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            DyToast.getInstance().success(data.message);
                            finish();
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
     * 选择省份
     *
     * @param id
     * @param isProvince
     */
    private void getArea(String id, boolean isProvince) {
        HttpFactory.getInstance().areaList(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), id)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<List<AreaBean>>>() {
                    @Override
                    public void success(HttpResponseData<List<AreaBean>> data) {
                        if ("9999".equals(data.status)) {
                            List<AreaBean> areaBeanList = data.getData();
                            if (null != areaBeanList) {
                                showArea(areaBeanList, isProvince);
                            }
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
     * 显示省市区弹框
     *
     * @param data
     * @param isProvince
     */
    private void showArea(List<AreaBean> data, boolean isProvince) {
        final ArrayList<WheelPickerBean> beans = new ArrayList<>();
        if (data != null && !data.isEmpty()) {
            for (int i = 0; i < data.size(); i++) {
                beans.add(new WheelPickerBean(data.get(i).id + "", data.get(i).name));
            }
            if (isProvince) {
                WheelPickerUtils.getInstance().initCustomOptionPicker(getAty(), "请选择省份", beans, current -> {
                    mProvince = beans.get(current).getContext();
                    getArea(beans.get(current).getId(), false);
                });
            } else {
                WheelPickerUtils.getInstance().initCustomOptionPicker(getAty(), "请选择城市", beans, current -> {
                    mCity = beans.get(current).getContext();
                    tvCity.setText(mProvince + beans.get(current).getContext());
                });
            }
        }
    }


}
