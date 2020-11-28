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
import com.dykj.youfeng.mode.BankListBean;
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
import com.dykj.module.util.toast.DyToast;
import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商户进件
 */
public class ShopInputJftActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_idle)
    TextView tvIdle;
    @BindView(R.id.et_bank_account_no)
    EditText etBankAccountNo;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;

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
    private String mChannle;


    @Override
    public int intiLayout() {
        return R.layout.activity_jft_shop_input;
    }

    @Override
    public void initData() {
        initTitle("进件");
        getListBanks();

        if (null != getIntentData()) {
            mChannle =    (String) getIntentData();
        }
    }


    @Override
    public void doBusiness() {
        String  realName=MMKV.defaultMMKV().decodeString(AppCacheInfo.mRealname, "");
        tvName.setText(MMKV.defaultMMKV().decodeString(AppCacheInfo.mRealname, ""));
        tvIdle.setText(MMKV.defaultMMKV().decodeString(AppCacheInfo.mIdcard, ""));
        mCountDownTimer = new CountDownUtil(tvCode);
    }


    @OnClick({R.id.ll_bank, R.id.tv_code, R.id.tv_submit})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.ll_bank:
                if (null != mBankItemList) {
                    showBankDialog();
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
                if (TextUtils.isEmpty(mCode)) {
                    DyToast.getInstance().warning("请获取验证码!");
                    return;
                }
                if (!mCode.equals(smsCode)) {
                    DyToast.getInstance().warning("验证码有误");
                    return;
                }


                submit();

                break;
            default:
                break;
        }
    }


    /**
     * 进件 提交
     */
    private void submit() {
        ConcurrentHashMap<String, Object> params = new ConcurrentHashMap<>();
        params.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        params.put("phone", etPhone.getText().toString());
        params.put("bank_num", etBankAccountNo.getText().toString());
        params.put("code", mChannle);
        params.put("bank_number", mSelectBankNumber);
        params.put("bank_name", tvBankName.getText().toString().trim() );
        Log.d("wtf",new Gson().toJson(params));
        HttpFactory.getInstance().cardInfoAdd(params)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            MMKV.defaultMMKV().encode(AppCacheInfo.merchantCode, "1");
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
                .as(HttpObserver.life(ShopInputJftActivity.this))
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
     * 获取发卡银行
     */
    private void getListBanks() {
        HttpFactory.getInstance().bankList(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""))
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(ShopInputJftActivity.this))
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
