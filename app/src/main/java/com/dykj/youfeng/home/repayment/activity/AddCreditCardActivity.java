package com.dykj.youfeng.home.repayment.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
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
import com.dykj.module.util.WheelPickerUtils;
import com.dykj.module.util.toast.DyToast;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加信用卡
 */
public class AddCreditCardActivity extends BaseActivity {
    @BindView(R.id.tv_step3_name)
    TextView tvStep3Name;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_step3_user_number)
    TextView tvStep3UserNumber;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_step3_user_car_number)
    TextView tvStep3UserCarNumber;
    @BindView(R.id.et_credit_num)
    EditText etCreditNum;
    @BindView(R.id.iv_click)
    ImageView ivClick;
    @BindView(R.id.tv_step3_bank_name)
    TextView tvStep3BankName;
    @BindView(R.id.tv_select_bank)
    TextView tvSelectBank;
    @BindView(R.id.rl_bank)
    RelativeLayout rlBank;
    @BindView(R.id.tv_cvn2)
    TextView tvCvn2;
    @BindView(R.id.et_cvn)
    EditText etCvn;
    @BindView(R.id.rl_cvn)
    RelativeLayout rlCvn;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_validity)
    TextView tvValidity;
    @BindView(R.id.rl_time)
    RelativeLayout rlTime;
    @BindView(R.id.tv_bill)
    TextView tvBill;
    @BindView(R.id.tv_statement_date)
    TextView tvStatementDate;
    @BindView(R.id.tv_haikuan)
    TextView tvHaikuan;
    @BindView(R.id.tv_repayment_date)
    TextView tvRepaymentDate;
    @BindView(R.id.tv_step3_edu)
    TextView tvStep3Edu;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.rl_edu)
    RelativeLayout rlEdu;
    @BindView(R.id.tv_step3_user_phone_number)
    TextView tvStep3UserPhoneNumber;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.rl_phone)
    RelativeLayout rlPhone;
    @BindView(R.id.tv_step3_user_code)
    TextView tvStep3UserCode;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_getCode)
    TextView tvGetCode;
    @BindView(R.id.rl_code)
    RelativeLayout rlCode;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private CountDownUtil mCountDownTimer;
    private List<BankListBean> mBankItemList;  // 发卡行
    private String mSelectBankNumber;
    private ArrayList<String> dayList;
    private String hkDay;
    private String zdDay;
    private String mCode;


    @Override
    public int intiLayout() {
        return R.layout.activity_add_creditcard;
    }

    @Override
    public void initData() {
        initTitle("添加信用卡");
        tvName.setText(MMKV.defaultMMKV().decodeString(AppCacheInfo.mRealname, ""));
        tvNumber.setText(MMKV.defaultMMKV().decodeString(AppCacheInfo.mIdcard, ""));
        mCountDownTimer = new CountDownUtil(tvGetCode);
    }

    @Override
    public void doBusiness() {
        initdayList();
    }


    @OnClick({R.id.tv_select_bank,
            R.id.iv_click,
            R.id.tv_validity,
            R.id.tv_statement_date,
            R.id.tv_repayment_date,
            R.id.tv_getCode,
            R.id.btn_submit})
    public void onClickView(View view) {
        String phone = etPhone.getText().toString().trim();
        switch (view.getId()) {
            case R.id.tv_select_bank:   // 开户行
                if (mBankItemList == null) {
                    getListBanks();
                } else {
                    showBankDialog();
                }

                break;
            case R.id.iv_click:
                DyToast.getInstance().warning("暂不支持识别功能，请手动操作");
                break;
            case R.id.tv_validity:   // 有限期
                try {
                    BaseToolsUtil.hiddenKeyboard(AddCreditCardActivity.this);
                } catch (Exception e) {
                    Log.e("添加信用卡", e.getLocalizedMessage());
                }
                showTimeDialog();
                break;

            case R.id.tv_statement_date:  // 账单日
                showMoneyDayDialog(false);
                break;

            case R.id.tv_repayment_date:  // 还款日
                showMoneyDayDialog(true);
                break;

            case R.id.tv_getCode:  // 发送验证码
                if (TextUtils.isEmpty(phone)) {
                    DyToast.getInstance().warning("请输入手机号!");
                    return;
                }
                if (!BaseToolsUtil.isCellphone(phone)) {
                    DyToast.getInstance().warning("手机号格式不正确!");
                    return;
                }
                getCode(phone);
                break;

            case R.id.btn_submit:
                String creditNum = etCreditNum.getText().toString().trim();
                if (TextUtils.isEmpty(creditNum)) {
                    DyToast.getInstance().warning("请输入信用卡卡号");
                    return;
                }

                if (!BankUtils.checkBankCard(creditNum)) {
                    DyToast.getInstance().warning("信用卡卡号不合法!");
                    return;
                }

                if (TextUtils.isEmpty(mSelectBankNumber)) {
                    DyToast.getInstance().warning("请选择开户行");
                    return;
                }

                String cvn = etCvn.getText().toString().trim();
                if (TextUtils.isEmpty(cvn)) {
                    DyToast.getInstance().warning("请输入信用卡背面三位安全码");
                    return;
                }


                String validity = tvValidity.getText().toString().trim();
                if (TextUtils.isEmpty(validity)) {
                    DyToast.getInstance().warning("请选择信用卡有效期");
                    return;
                }

                if (TextUtils.isEmpty(zdDay)) {
                    DyToast.getInstance().warning("请选择账单日");
                    return;
                }

                if (TextUtils.isEmpty(hkDay)) {
                    DyToast.getInstance().warning("请选择还款日");
                    return;
                }

                String money = etMoney.getText().toString().trim();
                if (TextUtils.isEmpty(money)) {
                    DyToast.getInstance().warning("请输入卡片额度");
                    return;
                }


                if (TextUtils.isEmpty(phone)) {
                    DyToast.getInstance().warning("请输入手机号!");
                    return;
                }
                if (!BaseToolsUtil.isCellphone(phone)) {
                    DyToast.getInstance().warning("手机号格式不正确!");
                    return;
                }


                String code = etCode.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    DyToast.getInstance().warning("请输入短信验证码!");
                    return;
                }

                if (!mCode.equals(code)) {
                    DyToast.getInstance().warning("短信验证码错误!");
                    return;
                }


                HashMap<String, Object> parmas = new HashMap<>();
                parmas.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
                parmas.put("banksId", mSelectBankNumber);
                parmas.put("money", money);
                parmas.put("bank_num", creditNum);
                parmas.put("validity", validity);
                parmas.put("cvn", cvn);
                parmas.put("statement_date", zdDay);
                parmas.put("repayment_date", hkDay);
                parmas.put("phone", phone);
                submit(parmas);
                break;
        }
    }

    /**
     * 添加信用卡
     *
     * @param parmas
     */
    private void submit(HashMap<String, Object> parmas) {
        HttpFactory.getInstance().addCreditcard(parmas)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(AddCreditCardActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            DyToast.getInstance().success("添加成功");
                            EventBus.getDefault().post(AppCacheInfo.mRefreshCreditCardList);
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
     * 账单日
     */
    private void showMoneyDayDialog(final boolean isHK) {
        DialogUtils.getInstance()
                .with(this)
                .setlayoutPosition(Gravity.BOTTOM)
                .setlayoutPading(0, 0, 0, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FROM_BOTTOM)
                .setlayoutId(R.layout.dialog_recyclerview)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    TextView tvTitle = view.findViewById(R.id.tv_tile);
                    tvTitle.setText(isHK ? "选择还款日" : "选择账单日");
                    WheelView wheelView = view.findViewById(R.id.wheelview);
                    setMoeyWheelView(wheelView, isHK);
                    view.findViewById(R.id.tv_ok).setOnClickListener(v -> {
                        if (isHK) {   // 还款日
                            tvRepaymentDate.setText("每月" + dayList.get(wheelView.getCurrentItem()) + "日");
                            hkDay = dayList.get(wheelView.getCurrentItem());

                        } else {
                            tvStatementDate.setText("每月" + dayList.get(wheelView.getCurrentItem()) + "日");
                            zdDay = dayList.get(wheelView.getCurrentItem());
                        }
                        DialogUtils.dismiss();
                    });
                    view.findViewById(R.id.tv_cancel).setOnClickListener(v -> DialogUtils.dismiss());
                })
                .show();
    }


    private void setMoeyWheelView(WheelView wheelView, final boolean is) {
        wheelView.setCyclic(false);
        wheelView.setAdapter(new ArrayWheelAdapter(dayList));
    }

    private void initdayList() {
        dayList = new ArrayList<>();
        dayList.clear();
        for (int i = 1; i < 31; i++) {
            dayList.add(i + "");
        }
    }


    /**
     * 有效期
     */
    private void showTimeDialog() {
        WheelPickerUtils.getInstance().showTimeWheelPicker2(this, "", false, new WheelPickerUtils.TimePickerListener() {
            @Override
            public void showTime(Date date) {
                SimpleDateFormat sf = new SimpleDateFormat("yyMM");
                String str = sf.format(date);
                tvValidity.setText(str);
            }
        });
    }


    /**
     * 获取发卡银行
     */
    private void getListBanks() {
        HttpFactory.getInstance().bankList(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""))
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(AddCreditCardActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<List<BankListBean>>>() {
                    @Override
                    public void success(HttpResponseData<List<BankListBean>> data) {
                        if ("9999".equals(data.status)) {
                            List<BankListBean> bankListBeanList = data.getData();
                            if (null != bankListBeanList) {
                                mBankItemList = bankListBeanList;
                                showBankDialog();
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
                    view.findViewById(R.id.tv_ok).setOnClickListener(v -> {
                        mSelectBankNumber = mBankItemList.get(wheelView.getCurrentItem()).id + "";
                        tvSelectBank.setText(mBankItemList.get(wheelView.getCurrentItem()).name);
                        DialogUtils.dismiss();
                    });
                    view.findViewById(R.id.tv_cancel).setOnClickListener(v -> DialogUtils.dismiss());
                })
                .show();
    }

    /**
     * 获取验证码
     *
     * @param mPhone
     */
    private void getCode(String mPhone) {
        mCountDownTimer.startCount();
        HttpFactory.getInstance().sendSms(mPhone, "bindBank")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(AddCreditCardActivity.this))
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.onFinish();
        }
    }

}
