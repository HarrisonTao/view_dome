package com.dykj.youfeng.vip.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.DialogUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.dykj.module.view.dialog.ConfirmDialog;
import com.dykj.youfeng.R;
import com.dykj.youfeng.dialog.PayPassDialog;
import com.dykj.youfeng.mine.set.SettingDealPswActivity;
import com.dykj.youfeng.mode.PayResult;
import com.dykj.youfeng.mode.PayWxBean;
import com.dykj.youfeng.mode.VipBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.view.PayPassView;
import com.google.gson.Gson;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VipActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.backText)
    ImageView backText;
    @BindView(R.id.vipImage)
    ImageView vipImage;
    @BindView(R.id.vipText)
    TextView vipText;
    @BindView(R.id.titleText)
    TextView titleText;
    @BindView(R.id.vipRelatice)
    RelativeLayout vipRelatice;
    @BindView(R.id.openVipButton)
    Button openVipButton;
    @BindView(R.id.repayText)
    TextView repayText;
    @BindView(R.id.quickRateText)
    TextView quickRateText;
    /**
     * 设置view 数据
     *
     * @param vipBean
     */

    DecimalFormat mDf = new DecimalFormat("0.00");

    private ImageView ivWx, ivali, ivAccount;
    private String type = "wechat";
    private String upLevelType;
    private double levelPrice=0;  // 支付的金额
    private static final int SDK_PAY_FLAG = 200;

    @Override
    public int intiLayout() {
        return R.layout.activity_vip_layout;
    }

    @Override
    public void initData() {
        QMUIStatusBarHelper.setStatusBarDarkMode(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void doBusiness() {
        //requestUpInfo();
        vipInfo();
    }


    @OnClick({R.id.backText, R.id.openVipButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backText:
                finish();
                break;
            case R.id.openVipButton:
                showDialog(levelPrice+"");
                break;
        }
    }


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
                    if (TextUtils.equals(resultStatus, "6001")) {
                        DyToast.getInstance().warning("您已取消该交易!");
                    } else if (TextUtils.equals(resultStatus, "9000")) {
                        DyToast.getInstance().success("订单支付成功");
                        EventBus.getDefault().postSticky(AppCacheInfo.mRefreshUserInfo);
                        requestUpInfo();
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };


    /**
     * 获取开通VIP参数
     */
    public void vipInfo() {
        String token = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");


        HttpFactory.getInstance().vipInfo(token)
                .compose(HttpObserver.schedulers(VipActivity.this))
                .as(HttpObserver.life(VipActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<VipBean>>() {
                    @Override
                    public void success(HttpResponseData<VipBean> data) {
                        Log.d("wtf", "@#" + new Gson().toJson(data));

                        if ("9999".equals(data.status)) {
                            VipBean vipBean = data.getData();
                            repayText.setText(Html.fromHtml("智能还款费率<br />小额 0."+vipBean.levelList.repayRateMin+"%+"+vipBean.levelList.repayCost+
                                    "，大额 0."+vipBean.levelList.repayRateMax+"%+"+vipBean.levelList.repayCost) );

                            quickRateText.setText(Html.fromHtml("快捷收款费率 <br />0."+vipBean.levelList.quickRate+"+"+vipBean.levelList.quickCost));

                            openVipButton.setText("立即开通 ￥"+vipBean.levelList.levelPrice);
                            levelPrice=vipBean.levelList.levelPrice;

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
     * 升级
     */
    private void requestUpInfo() {
        HttpFactory.getInstance().upVip(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""))
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(VipActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<VipBean>>() {
                    @Override
                    public void success(HttpResponseData<VipBean> data) {
                        if ("9999".equals(data.status)) {
                            VipBean vipBean = data.getData();
                           // setViewData(vipBean);
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
     * 支付 dialog
     *
     * @param levelPrice
     */
    private void showDialog(final String levelPrice) {
        type = "wechat";
        DialogUtils.getInstance().with(getAty())
                .setlayoutPosition(Gravity.BOTTOM)
                .setlayoutPading(0, 0, 0, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FROM_BOTTOM)
                .setlayoutId(R.layout.dialog_vip_pay)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    TextView tvMoney = view.findViewById(R.id.tv_money);
                    tvMoney.setText("¥" + levelPrice);

                    ivWx = view.findViewById(R.id.iv_wx);
                    ivali = view.findViewById(R.id.iv_ali);
                    ivAccount = view.findViewById(R.id.iv_account);
                    view.findViewById(R.id.rl_wechat).setOnClickListener(VipActivity.this);
                    view.findViewById(R.id.rl_ali).setOnClickListener(VipActivity.this);
                    view.findViewById(R.id.rl_account).setOnClickListener(VipActivity.this);
                    view.findViewById(R.id.tv_submit_pay).setOnClickListener(VipActivity.this);
                    TextView account = view.findViewById(R.id.tv_account);
                    account.setText("¥" + MMKV.defaultMMKV().decodeString(AppCacheInfo.mAccountMoney, "0"));
                })
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_wechat:
                switchIv(ivWx, R.mipmap.ic_xuanz_s, ivali, R.mipmap.ic_xuanz);
                type = "wechat";
                break;

            case R.id.rl_ali:
                switchIv(ivali, R.mipmap.ic_xuanz_s, ivWx, R.mipmap.ic_xuanz);
                type = "alipay";
                break;

            case R.id.rl_account:
                ivali.setImageResource(R.mipmap.ic_xuanz);
                ivWx.setImageResource(R.mipmap.ic_xuanz);
                ivAccount.setImageResource(R.mipmap.ic_xuanz_s);
                type = "account";
                break;

            case R.id.tv_submit_pay:  // 提交
                if (type.equals("account")) {
                    accountCommit();
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
     * 微信支付
     */
    private void weChatCommit() {
        HttpFactory.getInstance().wechatBuy(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), "1")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(VipActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<PayWxBean>>() {
                    @Override
                    public void success(HttpResponseData<PayWxBean> data) {
                        if ("9999".equals(data.status)) {
                            PayWxBean payWxBean = data.getData();
                            if (null != payWxBean) {
                                EventBus.getDefault().postSticky(AppCacheInfo.mUpVip);
                                String appId = payWxBean.appid;
                                IWXAPI api = WXAPIFactory.createWXAPI(VipActivity.this, appId);
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
        HttpFactory.getInstance().aliPay(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), "1")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(VipActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<String>>() {
                    @Override
                    public void success(HttpResponseData<String> data) {
                        if ("9999".equals(data.status)) {
                            String info = data.getData();
                            if (null != info) {
                                Runnable payRunnable = () -> {
                                    PayTask alipay = new PayTask(VipActivity.this);
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

    /**
     * 余额支付
     */
    private void accountCommit() {
        int passType = MMKV.defaultMMKV().decodeInt(AppCacheInfo.mPayPass, 0);
        if (passType == 0) {
            new ConfirmDialog(this, () ->
                    startAct(getAty(), SettingDealPswActivity.class)
                    , "您还没设置交易密码", "去设置").setLifecycle(getLifecycle()).show();
            return;
        }
        String accountMoney = MMKV.defaultMMKV().decodeString(AppCacheInfo.mAccountMoney, "0");
        double mUserAccountMoney = Double.parseDouble(accountMoney);
        if (mUserAccountMoney >= levelPrice) {  // 如果余额满足的话  弹出余额支付框
            yuePay();
        } else {
            DyToast.getInstance().warning("余额不足,请选择其他支付方式.");
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
                        dialog.dismiss();
                        //关闭回调
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
        HttpFactory.getInstance().acountBuy(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), passContent, "1")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(VipActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            requestUpInfo();
                            EventBus.getDefault().postSticky(AppCacheInfo.mRefreshUserInfo);
                            dialog.dismiss();
                            DyToast.getInstance().success(data.message);
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


    /**
     * 微信成功的回调
     *
     * @param event
     */
    @Subscribe(sticky = true)
    public void onEvent(String event) {
        if (!TextUtils.isEmpty(event) && event.equals(AppCacheInfo.wxPaySuccess)) {
            requestUpInfo();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
