package com.dykj.youfeng.home.repayment.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.contrarywind.adapter.WheelAdapter;
import com.contrarywind.view.WheelView;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.AreaBean;
import com.dykj.youfeng.mode.BankListBean;
import com.dykj.youfeng.mode.ShopInputBean;
import com.dykj.youfeng.mode.SmsBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.tools.BankUtils;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.BaseToolsUtil;
import com.dykj.module.util.CountDownUtil;
import com.dykj.module.util.DialogUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.WheelPickerBean;
import com.dykj.module.util.WheelPickerUtils;
import com.dykj.module.util.toast.DyToast;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商户进件
 */
public class ShopInputActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_idle)
    TextView tvIdle;
    @BindView(R.id.et_bank_account_no)
    EditText etBankAccountNo;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_province)
    TextView tvProvice;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    private CountDownUtil mCountDownTimer;

    private String mCode;   // 验证码
    private List<BankListBean> mBankItemList;   // 发卡银行列表
    private String mSelectBankNumber;  // 选中发卡银行
    private String mSelectBankId;  // 选中发卡银行
    private String mProvince;   // 省份名称
    private String mCity;       // 城市名称
    private String mPid = "";   // 省份id
    private String mChannle;
    private String mType;


    @Override
    public int intiLayout() {
        return R.layout.activity_shop_input;
    }

    @Override
    public void initData() {
        initTitle("进件");
        getListBanks();

        if (null != getIntentData()) {
            ShopInputBean shopInputBean = (ShopInputBean) getIntentData();
            mChannle = shopInputBean.channle;
            mType = shopInputBean.type;
        }
    }


    @Override
    public void doBusiness() {
        tvName.setText(MMKV.defaultMMKV().decodeString(AppCacheInfo.mRealname, ""));
        tvIdle.setText(MMKV.defaultMMKV().decodeString(AppCacheInfo.mIdcard, ""));
        mCountDownTimer = new CountDownUtil(tvCode);
    }


    @OnClick({R.id.ll_bank, R.id.ll_province, R.id.ll_city, R.id.tv_code, R.id.tv_submit})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.ll_bank:
                if (null != mBankItemList) {
                    showBankDialog();
                }
                break;

            case R.id.ll_province:
                tvProvice.setText("请选择省份");
                mCity = null;
                try {
                    BaseToolsUtil.hiddenKeyboard(ShopInputActivity.this);
                } catch (Exception e) {
                    Log.e("", e.getLocalizedMessage());
                }
                getArea("", true);
                break;

            case R.id.ll_city:
                try {
                    BaseToolsUtil.hiddenKeyboard(ShopInputActivity.this);
                } catch (Exception e) {
                    Log.e("", e.getLocalizedMessage());
                }
                if (!TextUtils.isEmpty(mPid)) {
                    getArea(mPid, false);
                } else {
                    DyToast.getInstance().warning("请先选择省份!");
                }
                break;

            case R.id.tv_code:
                String mPhone = etPhone.getText().toString();
                if (TextUtils.isEmpty(mPhone)) {
                    DyToast.getInstance().warning("请输入手机号码!");
                    return;
                }
                if (!BaseToolsUtil.isCellphone(mPhone)) {
                    DyToast.getInstance().warning("请输入手机格式不正确!");
                    return;
                }
                getCode(mPhone);
                break;

            case R.id.tv_submit:
                String bankNo = etBankAccountNo.getText().toString().trim();
                String smsCode = etCode.getText().toString().trim();
                if (TextUtils.isEmpty(bankNo)) {
                    DyToast.getInstance().warning("请输入银行卡号!");
                    return;
                }
                if (!BankUtils.checkBankCard(bankNo)) {
                    DyToast.getInstance().warning("非法银行号!");
                    return;
                }
                if (TextUtils.isEmpty(mSelectBankNumber) || TextUtils.isEmpty(mSelectBankId)) {
                    DyToast.getInstance().warning("请选择开户行");
                    return;
                }
                if (TextUtils.isEmpty(mProvince)) {
                    DyToast.getInstance().warning("请选择开户省份!");
                    return;
                }
                if (TextUtils.isEmpty(mCity)) {
                    DyToast.getInstance().warning("请选择开户城市");
                    return;
                }
                if (TextUtils.isEmpty(mCode)) {
                    DyToast.getInstance().warning("请获取验证码!");
                    return;
                }
                if (!mCode.equals(smsCode)) {
                    DyToast.getInstance().warning("验证码有误");
                    return;
                }

                if ("oneMoreRepay".equals(mType)) {
                    submit();
                } else if ("replaceRepay".equals(mType)) {
                    replaceSubmit();
                }
                break;
        }
    }

    /**
     * 代还进件
     */
    private void replaceSubmit() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        params.put("channel", mChannle);
        params.put("bankAccountNo", etBankAccountNo.getText().toString());
        params.put("mobile", etPhone.getText().toString());
        params.put("bankProvince", mProvince);
        params.put("bankCity", mCity);
        params.put("banksId", mSelectBankId);
        params.put("bankSubName", tvBankName.getText().toString());

        HttpFactory.getInstance().changeJieReg(params)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(ShopInputActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            MMKV.defaultMMKV().encode(AppCacheInfo.merchantCode,"1");   // 进件成功
                            EventBus.getDefault().postSticky(AppCacheInfo.mRefreshUserInfo);
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
     * 一卡多还进件 提交
     */
    private void submit() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        params.put("channel", mChannle);
        params.put("bankAccountNo", etBankAccountNo.getText().toString());
        params.put("mobile", etPhone.getText().toString());
        params.put("bankProvince", mProvince);
        params.put("bankCity", mCity);
        params.put("number", mSelectBankNumber);

        HttpFactory.getInstance().cardpaymentReg(params)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(ShopInputActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            MMKV.defaultMMKV().encode(AppCacheInfo.merchantCode,"1");
                            EventBus.getDefault().postSticky(AppCacheInfo.mRefreshUserInfo);
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
     * 获取验证码
     *
     * @param mPhone
     */
    private void getCode(String mPhone) {
        mCountDownTimer.startCount();
        HttpFactory.getInstance().sendSms(mPhone, "channel")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(ShopInputActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<SmsBean>>() {
                    @Override
                    public void success(HttpResponseData<SmsBean> data) {
                        if ("9999".equals(data.status)) {
                            mCode = data.getData().code + "";
                        } else {
                            DyToast.getInstance().warning(data.message);
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
                .as(HttpObserver.life(ShopInputActivity.this))
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
                    tvProvice.setText(beans.get(current).getContext());
                    mPid = beans.get(current).getId();
                });
            } else {
                WheelPickerUtils.getInstance().initCustomOptionPicker(getAty(), "请选择城市", beans, current -> {
                    mCity = beans.get(current).getContext();
                    tvCity.setText(beans.get(current).getContext());
                });
            }
        }
    }


    /**
     * 获取发卡银行
     */
    private void getListBanks() {
        HttpFactory.getInstance().bankList(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""))
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(ShopInputActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<List<BankListBean>>>() {
                    @Override
                    public void success(HttpResponseData<List<BankListBean>> data) {
                        if ("9999".equals(data.status)) {
                            List<BankListBean> bankListBeanList = data.getData();
                            if (null != bankListBeanList) {
                                mBankItemList = bankListBeanList;
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
     * 发卡银行 弹框
     */
    private void showBankDialog() {
        DialogUtils.getInstance()
                .with(this)
                .setlayoutPosition(Gravity.BOTTOM)
                .setlayoutPading(0, 0, 0, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FROM_BOTTOM)
                .setlayoutId(R.layout.dialog_recyclerview)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    TextView tvTitle = view.findViewById(R.id.tv_tile);
                    tvTitle.setText("选择开户行");
                    WheelView wheelView = view.findViewById(R.id.wheelview);
                    wheelView.setCyclic(false);
                    wheelView.setAdapter(new WheelAdapter() {
                        @Override
                        public int getItemsCount() {
                            return mBankItemList == null ? 0 : mBankItemList.size();
                        }

                        @Override
                        public Object getItem(int index) {
                            if (index >= 0 && index < mBankItemList.size()) {
                                return mBankItemList.get(index).name;
                            }
                            return "";
                        }

                        @Override
                        public int indexOf(Object o) {
                            return mBankItemList.indexOf(o);
                        }

                    });
                    wheelView.setOnItemSelectedListener(index -> {
                        mSelectBankId = mBankItemList.get(index).id + "";
                        mSelectBankNumber = mBankItemList.get(index).number + "";
                        tvBankName.setText(mBankItemList.get(index).name);
                    });
                    view.findViewById(R.id.tv_ok).setOnClickListener(v -> DialogUtils.dismiss());
                    view.findViewById(R.id.tv_cancel).setOnClickListener(v -> DialogUtils.dismiss());
                })
                .show();
    }
}
