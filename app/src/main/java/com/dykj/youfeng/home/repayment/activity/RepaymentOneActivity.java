package com.dykj.youfeng.home.repayment.activity;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.GlideUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.WheelPickerBean;
import com.dykj.module.util.WheelPickerUtils;
import com.dykj.module.util.toast.DyToast;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.AreaBean;
import com.dykj.youfeng.mode.CreditcardListBean;
import com.dykj.youfeng.mode.RepaymentPreviewBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 一键还款  默认是佳付通大额
 *
 * @author zhaoyanhui
 */
public class RepaymentOneActivity extends BaseActivity {
    @BindView(R.id.iv_bank_logo)
    ImageView ivBankLogo;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.tv_end_number)
    TextView tvEndNumber;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_statement_date)
    TextView tvStatementDate;
    @BindView(R.id.tv_repayment_date)
    TextView tvRepaymentDate;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.et_min_money)
    EditText etMinMoney;
    @BindView(R.id.tv_plan_submit)
    TextView tvPlanSubmit;
    @BindView(R.id.tv_city)
    TextView tvCity;

    private String mChannel, mCreditcardId;
    private CreditcardListBean.ListBean mCreditCarsBean;
    private String mProvince,mProvinceId,mCityId;

    @Override
    public int intiLayout() {
        return R.layout.activity_repayment_one;
    }

    @Override
    public void initData() {
        initTitle("制定计划");
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void doBusiness() {
        mCreditCarsBean = (CreditcardListBean.ListBean) getIntentData();
        if (null != mCreditCarsBean) {
            GlideUtils.setImage(ivBankLogo, mCreditCarsBean.logo);
            String idcard = mCreditCarsBean.idcard;
            tvBankName.setText(mCreditCarsBean.bank_name);
            tvEndNumber.setText("(" + idcard.substring(idcard.length() - 4) + ")");
            tvMoney.setText(mCreditCarsBean.money);
            tvStatementDate.setText("账单日 " + mCreditCarsBean.statement_date + "号");
            tvRepaymentDate.setText("还款日 " + mCreditCarsBean.repayment_date + "号");
            mCreditcardId = mCreditCarsBean.id + "";
            mChannel = mCreditCarsBean.channel;
        }
    }

    @OnClick(R.id.tv_plan_submit)
    public void onViewClicked() {
        String money = etMoney.getText().toString().trim();
        String minMoney = etMinMoney.getText().toString().trim();

        if(TextUtils.isEmpty(money)){
            DyToast.getInstance().warning("请设置还款金额");
            return;
        }

        if(TextUtils.isEmpty(minMoney)){
            DyToast.getInstance().warning("请输入预留金额");
            return;
        }

        if (Double.parseDouble(minMoney) < 300) {
            DyToast.getInstance().warning("预留金额不得小于300元");
            return;
        }


        if (TextUtils.isEmpty(mCityId)) {
            DyToast.getInstance().warning("请选择消费城市!");
            return;
        }

        Map<String ,Object> map = new HashMap<>();
        map .put("token",MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        map.put("cardId",mCreditcardId);
        map.put("money",money);
        map.put("minMoney",minMoney);
        map.put("areacode",mCityId);
        HttpFactory.getInstance().getRepaymentOnePreview(map)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<RepaymentPreviewBean>>() {
                    @Override
                    public void success(HttpResponseData<RepaymentPreviewBean> data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            startAct(PreviewPlanActivity.class, mCreditCarsBean);    // 提交计划成功
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


    @OnClick(R.id.tv_city)
    public void selcectCity(){
        // 选择地区
        getArea("", true);
    }

    private void getArea(String id, boolean isProvince) {
        HttpFactory.getInstance().getJftArea(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), id)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(RepaymentOneActivity.this))
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
                    mProvinceId = beans.get(current).getId();
                    String mPid = beans.get(current).getId();
                    getArea(mPid, false);
                });
            } else {
                WheelPickerUtils.getInstance().initCustomOptionPicker(getAty(), "请选择城市", beans, current -> {
                  String  mCity = beans.get(current).getContext();
                    mCityId = beans.get(current).getId();
                    tvCity.setText(mProvince + mCity);
                });
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void onEvent(String onEvent) {
        if (!TextUtils.isEmpty(onEvent) && onEvent.equals(AppCacheInfo.mSubmitRepayMent)) {
            finish();
        }
    }
}
