package com.dykj.youfeng.home.repayment.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.contrarywind.adapter.WheelAdapter;
import com.contrarywind.view.WheelView;
import com.dykj.youfeng.R;
import com.dykj.youfeng.dialog.PayPassDialog;
import com.dykj.youfeng.mine.set.SettingDealPswActivity;
import com.dykj.youfeng.mode.AreaBean;
import com.dykj.youfeng.mode.BankListBean;
import com.dykj.youfeng.mode.PayResult;
import com.dykj.youfeng.mode.PayWxBean;
import com.dykj.youfeng.mode.SmsBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.tools.BankUtils;
import com.dykj.youfeng.view.PayPassView;
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
import com.dykj.module.view.dialog.ConfirmDialog;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加储蓄卡
 */

public class AddDepositCardActivity extends BaseActivity implements View.OnClickListener {
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
    @BindView(R.id.tv_step3_rl_province)
    TextView tvStep3RlProvince;
    @BindView(R.id.tv_select_province)
    TextView tvSelectProvince;
    @BindView(R.id.rl_province)
    RelativeLayout rlProvince;
    @BindView(R.id.tv_step3_city)
    TextView tvStep3City;
    @BindView(R.id.tv_select_city)
    TextView tvSelectCity;
    @BindView(R.id.rl_city)
    RelativeLayout rlCity;
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

    private String mCode;   // 验证码
    private List<BankListBean> mBankItemList;   // 发卡银行列表
    private String mSelectBankNumber;  // 选中发卡银行
    private String mProvince;   // 省份名称
    private String mCity;       // 城市名称
    private String mPid = "";   // 省份id


    private ImageView ivWx, ivali, ivAccount;
    private String type;
    private static final int SDK_PAY_FLAG = 200;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
					/*
					9000	订单支付成功
					8000	正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
					4000	订单支付失败
					5000	重复请求s
					6001	用户中途取消
					6002	网络连接出错
					6004	支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
					其它	其它支付错误
					*/
                    Log.e("resultStatus", "resultStatus：" + resultStatus);
                    if (android.text.TextUtils.equals(resultStatus, "6001")) {
                        DyToast.getInstance().warning("您已取消该交易!");
                        finish();
                    } else if (android.text.TextUtils.equals(resultStatus, "9000")) {
                        DyToast.getInstance().success("订单支付成功");
                        EventBus.getDefault().postSticky(AppCacheInfo.mRefreshUserInfo);
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };


    @Override
    public int intiLayout() {
        return R.layout.activity_add_deposit_card;
    }

    @Override
    public void initData() {
        initTitle("添加储蓄卡");
        getListBanks();
        checkBindCredit();
    }


    /**
     * 检测是否支付首次<储蓄卡></> //信用卡
     */
    private void checkBindCredit() {
        int bindPayStatus = MMKV.defaultMMKV().decodeInt(AppCacheInfo.checkPay, 0);
        if (0 == bindPayStatus) {  // 支付10 绑卡手续费 ....
            showBindCradDialog();
        }
    }

    @Override
    public void doBusiness() {

        tvName.setText(MMKV.defaultMMKV().decodeString(AppCacheInfo.mRealname, ""));
        tvNumber.setText(MMKV.defaultMMKV().decodeString(AppCacheInfo.mIdcard, ""));
        mCountDownTimer = new CountDownUtil(tvGetCode);
    }


    @OnClick({R.id.iv_click, R.id.rl_bank,
            R.id.rl_province, R.id.rl_city,
            R.id.tv_getCode, R.id.btn_submit})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.iv_click:
                DyToast.getInstance().warning("暂不支持识别功能，请手动操作");
                break;

            case R.id.rl_bank:                  // 选择发卡行
                if (null != mBankItemList) {
                    showBankDialog();
                } else {
                    getListBanks();
                }
                break;

            case R.id.rl_province:             //请选择省份
                tvSelectProvince.setText("请选择省份");
                mCity = null;
                try {
                    BaseToolsUtil.hiddenKeyboard(AddDepositCardActivity.this);
                } catch (Exception e) {
                    Log.e("", e.getLocalizedMessage());
                }
                getArea("", true);
                break;

            case R.id.rl_city:       // 选择城市
                try {
                    BaseToolsUtil.hiddenKeyboard(AddDepositCardActivity.this);
                } catch (Exception e) {
                    Log.e("", e.getLocalizedMessage());
                }
                if (!TextUtils.isEmpty(mPid)) {
                    getArea(mPid, false);
                } else {
                    DyToast.getInstance().warning("请先选择省份!");
                }
                break;


            case R.id.tv_getCode:
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

            case R.id.btn_submit:
                String bankNumder = etCreditNum.getText().toString().trim();
                if (TextUtils.isEmpty(bankNumder)) {
                    DyToast.getInstance().warning("请输入储蓄卡卡号!");
                    return;
                }
                if (!BankUtils.checkBankCard(bankNumder)) {
                    DyToast.getInstance().warning("储蓄卡卡号不合法!");
                    return;
                }

                if (TextUtils.isEmpty(mSelectBankNumber)) {
                    DyToast.getInstance().warning("请选择发卡银行");
                    return;
                }

                if (TextUtils.isEmpty(mProvince)) {
                    DyToast.getInstance().warning("请选择省份");
                    return;
                }
                if (TextUtils.isEmpty(mCity)) {
                    DyToast.getInstance().warning("请选择城市");
                    return;
                }

                String mPhone2 = etPhone.getText().toString();
                if (TextUtils.isEmpty(mPhone2)) {
                    DyToast.getInstance().warning("请输入手机号码!");
                    return;
                }
                if (!BaseToolsUtil.isCellphone(mPhone2)) {
                    DyToast.getInstance().warning("请输入手机格式不正确!");
                    return;
                }

                if (TextUtils.isEmpty(mCode)) {
                    DyToast.getInstance().warning("请输入验证码!");
                    return;
                }

                if (!mCode.equals(etCode.getText().toString())) {
                    DyToast.getInstance().warning("请校验验证码!");
                    return;
                }
                submit();
                break;

        }
    }


    /**
     * 提交
     */
    private void submit() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        params.put("banksId", mSelectBankNumber);
        params.put("bank_num", etCreditNum.getText().toString().trim());
        params.put("phone", etPhone.getText().toString());
        params.put("province", mProvince);
        params.put("city", mCity);

        HttpFactory.getInstance().addDebitcard(params)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(AddDepositCardActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            DyToast.getInstance().success("添加成功");
                            EventBus.getDefault().post(AppCacheInfo.mRefreshDebitcardList);
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
                .as(HttpObserver.life(AddDepositCardActivity.this))
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
                WheelPickerUtils.getInstance().initCustomOptionPicker(getAty(), "请选择省份", beans, new WheelPickerUtils.CurrentOption() {
                    @Override
                    public void onCurrentOption(int current) {
                        mProvince = beans.get(current).getContext();
                        tvSelectProvince.setText(beans.get(current).getContext());
                        mPid = beans.get(current).getId();
                    }
                });
            } else {
                WheelPickerUtils.getInstance().initCustomOptionPicker(getAty(), "请选择城市", beans, new WheelPickerUtils.CurrentOption() {
                    @Override
                    public void onCurrentOption(int current) {
                        mCity = beans.get(current).getContext();
                        tvSelectCity.setText(beans.get(current).getContext());
                    }
                });
            }
        }
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
                .as(HttpObserver.life(AddDepositCardActivity.this))
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
                .as(HttpObserver.life(AddDepositCardActivity.this))
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
     * 绑卡弹框
     */
    private void showBindCradDialog() {
        DialogUtils.getInstance()
                .with(this)
                .setlayoutPosition(Gravity.CENTER)
                .setlayoutPading(150, 0, 150, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FROM_BOTTOM)
                .setlayoutId(R.layout.dialog_bind_credit)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    view.findViewById(R.id.tv_go_pay).setOnClickListener(v -> {
                        DialogUtils.dismiss();
                        payDialog();
                    });

                    view.findViewById(R.id.tv_cancel).setOnClickListener(v ->{
                        DialogUtils.dismiss();
                        finish();
                    });

                })
                .show();
    }

    /**
     * 支付弹框
     */
    private void payDialog() {
        type = "wechat";
        DialogUtils.getInstance()
                .with(this)
                .setlayoutPosition(Gravity.CENTER)
                .setlayoutPading(150, 0, 150, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FROM_BOTTOM)
                .setlayoutId(R.layout.dialog_pay)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    ivWx = view.findViewById(R.id.iv_wx);
                    ivali = view.findViewById(R.id.iv_ali);
                    ivAccount = view.findViewById(R.id.iv_yue);
                    view.findViewById(R.id.rl_wx).setOnClickListener(AddDepositCardActivity.this);
                    view.findViewById(R.id.rl_ali).setOnClickListener(AddDepositCardActivity.this);
                    view.findViewById(R.id.rl_yue).setOnClickListener(AddDepositCardActivity.this);
                    view.findViewById(R.id.tv_go_pay).setOnClickListener(AddDepositCardActivity.this);
                })
                .show(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_wx:
                switchIv(ivWx, R.mipmap.ic_xuanz_s, ivali, R.mipmap.ic_xuanz);
                type = "wechat";
                break;

            case R.id.rl_ali:
                switchIv(ivali, R.mipmap.ic_xuanz_s, ivWx, R.mipmap.ic_xuanz);
                type = "alipay";
                break;

            case R.id.rl_yue:
                ivali.setImageResource(R.mipmap.ic_xuanz);
                ivWx.setImageResource(R.mipmap.ic_xuanz);
                ivAccount.setImageResource(R.mipmap.ic_xuanz_s);
                type = "account";
                break;

            case R.id.tv_go_pay:  // 提交
                if (type.equals("account")) {
                    int passType = MMKV.defaultMMKV().decodeInt(AppCacheInfo.mPayPass, 0);
                    String aMoney = MMKV.defaultMMKV().decodeString(AppCacheInfo.mAccountMoney, "");
                    if (passType == 0) {
                        new ConfirmDialog(this, () ->
                                startAct(getAty(), SettingDealPswActivity.class)
                                , "您还没设置交易密码", "去设置").setLifecycle(getLifecycle()).show();
                        return;
                    }

                    if (android.text.TextUtils.isEmpty(aMoney)) {
                        DyToast.getInstance().warning("余额不足,请切换其他支付方式");
                        return;
                    }
                    double dMoney = Double.parseDouble(aMoney);
                    if (dMoney >= 10) {
                        yuePay();
                    } else {
                        DyToast.getInstance().warning("余额不足,请切换其他支付方式");
                    }
                } else if (type.equals("alipay")) {
                    aliCommit();
                } else if (type.equals("wechat")) {
                    weChatCommit();
                }
                DialogUtils.dismiss();
                break;
        }
    }


    /**
     * 余额支付弹框
     */
    private void yuePay() {
        final PayPassDialog dialog = new PayPassDialog(this);
        dialog.getPayViewPass()
                .setPayClickListener(new PayPassView.OnPayClickListener() {
                    @Override
                    public void onPassFinish(String passContent) {
                        //6位输入完成回调
                        submitAccount(passContent, dialog);
                    }

                    @Override
                    public void onPayClose() {
                       finish();
                    }
                });
    }

    /**
     * 提交余额支付
     *
     * @param passContent
     * @param dialog
     */
    private void submitAccount(String passContent, PayPassDialog dialog) {
        HttpFactory.getInstance().acountBuy(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), passContent, "2")
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(AddDepositCardActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            dialog.dismiss();
                            DyToast.getInstance().success(data.message);
                            EventBus.getDefault().postSticky(AppCacheInfo.mRefreshUserInfo);
                        } else {
                            DyToast.getInstance().error(data.message);
                            finish();
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
     * 微信支付
     */
    private void weChatCommit() {
        HttpFactory.getInstance().wechatBuy(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), "2")
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(AddDepositCardActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<PayWxBean>>() {
                    @Override
                    public void success(HttpResponseData<PayWxBean> data) {
                        if ("9999".equals(data.status)) {
                            PayWxBean payWxBean = data.getData();
                            if (null != payWxBean) {
                                EventBus.getDefault().postSticky(AppCacheInfo.mBindCredit);
                                String appId = payWxBean.appid;
                                IWXAPI api = WXAPIFactory.createWXAPI(AddDepositCardActivity.this, appId);
                                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                api.registerApp(appId);
                                PayReq req = new PayReq();
                                req.appId = appId;
                                req.partnerId = payWxBean.partnerid;
                                req.prepayId = payWxBean.prepayid;
                                req.nonceStr = payWxBean.noncestr;
                                req.timeStamp = payWxBean.timestamp;
                                req.packageValue = "Sign=WXPay";
                                req.sign = payWxBean.sign;
                                //req.extData = "app data"; // optional
                                api.sendReq(req);
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
     * 阿里支付
     */
    private void aliCommit() {
        HttpFactory.getInstance().aliPay(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), "2")
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(AddDepositCardActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<String>>() {
                    @Override
                    public void success(HttpResponseData<String> data) {
                        if ("9999".equals(data.status)) {
                            String info = data.getData();
                            if (null != info) {
                                Runnable payRunnable = () -> {
                                    PayTask alipay = new PayTask(AddDepositCardActivity.this);
                                    Map<String, String> result = alipay.payV2(info, true);
                                    Message msg = new Message();
                                    msg.what = SDK_PAY_FLAG;
                                    msg.obj = result;
                                    mHandler.sendMessage(msg);
                                };
                                Thread payThread = new Thread(payRunnable);
                                payThread.start();
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


    private void switchIv(ImageView ivali, int p, ImageView ivWx, int p2) {
        ivali.setImageResource(p);
        ivWx.setImageResource(R.mipmap.ic_xuanz);
        ivAccount.setImageResource(p2);
    }


}
