package com.dykj.youfeng.mall.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.StringUtils;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mine.set.SettingDealPswActivity;
import com.dykj.youfeng.mode.AddressBean;
import com.dykj.youfeng.mode.ConfirmOrderBean;
import com.dykj.youfeng.mode.FreightBean;
import com.dykj.youfeng.mode.PayResult;
import com.dykj.youfeng.mode.PayWxBean;
import com.dykj.youfeng.mode.SubmitOrderBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.tools.ArithUtil;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.DialogUtils;
import com.dykj.module.util.GlideUtils;
import com.dykj.module.util.MessageWrap;
import com.dykj.module.util.MoneyValueFilter;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.MyWorkGround;
import com.dykj.module.util.TextChangedListener;
import com.dykj.module.util.toast.DyToast;
import com.dykj.module.view.dialog.ConfirmDialog;
import com.dykj.module.view.dialog.RemindDialog;
import com.dykj.module.view.paypass.PayPasswordDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author lcj
 * @date: 2019-18-17
 * @describe: 确认订单页面
 */
public class ConfirmOrderActivity extends BaseActivity {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;

    @BindView(R.id.iv_content)
    ImageView ivContent;
    @BindView(R.id.tv_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.tv_goods_type)
    TextView tvGoodsType;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.tv_send_type)
    TextView tvSendType;
    @BindView(R.id.tv_exp_money)
    TextView tvExpMoney;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.layout_empty)
    RelativeLayout layoutEmpty;

    @BindView(R.id.tv_exchange)
    TextView tvExchange;

    @BindView(R.id.rl_send_type)
    View rlSendType;
    @BindView(R.id.v_tran)
    View vTran;
    @BindView(R.id.ll_express)
    RelativeLayout llExpress;
    @BindView(R.id.tv_tot_count)
    TextView tvTotCount;
    @BindView(R.id.tv_tot_money)
    TextView tvTotMoney;
    @BindView(R.id.et_point)
    EditText etPoint;
    @BindView(R.id.tv_available_point)
    TextView tvAvailablePoint;
    @BindView(R.id.tv_redus)
    TextView tvRedus;
    @BindView(R.id.iv_point)
    ImageView ivPoint;
    @BindView(R.id.ll_point)
    LinearLayout llPoint;


    @Override
    public int intiLayout() {
        return R.layout.activity_confirm_order;
    }

    @Override
    public void initData() {
        initTitle("确认订单");
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void doBusiness() {
        getOrderInfo();
        etPoint.addTextChangedListener(new TextChangedListener() {
            @Override
            public void afterTextChanged(Editable editable) {
                changPoint();
            }
        });
        //防止 软键盘遮挡输入框
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        MyWorkGround.assistActivity(this);
        //限制最多输入两位小数
        etPoint.setFilters(new InputFilter[]{new MoneyValueFilter()});
    }

    private void changPoint() {
        if (StringUtils.isEmpty(etPoint.getText().toString())) {
//                    etPoint.setText(ArithUtil.formatIntNum(memberPoint));
            tvMoney.setText("￥" + ArithUtil.formatNum(allMoney + fee));
            tvRedus.setText("-" + ArithUtil.formatNum(0));
            return;
        }
        if (Float.parseFloat(etPoint.getText().toString()) > canUsePoint) {
            DyToast.getInstance().error("最多可用积分" + canUsePoint);
            etPoint.setText(ArithUtil.formatIntNum(canUsePoint));
        }
        float point = 0;
        if (!StringUtils.isEmpty(etPoint.getText().toString())) {
            point = Float.parseFloat(etPoint.getText().toString());
            tvRedus.setText(String.format("-%s", ArithUtil.formatNum(point)));
        }
        if (!usePoint) {
            point = 0;
        }
        tvMoney.setText("￥" + ArithUtil.formatNum(allMoney + fee - point));
    }


    private String addressId;
    //三个钱 商品单价x商品数量-抵扣金+运费
    private float allMoney, canUsePoint, fee;
    private int num;

    private void getOrderInfo() {
        Map<String, String> params = (Map<String, String>) getIntentData();
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        params.put("token", mToken);
        HttpFactory.getMallInstance().orderInfo(params)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<JsonObject>>() {
                    @Override
                    public void success(HttpResponseData<JsonObject> data) {

                        if ("9999".equals(data.status)) {
                            String json = data.getData().toString();
                            ConfirmOrderBean confirmOrderBean = new Gson().fromJson(json, ConfirmOrderBean.class);
                            if (data.getData().get("address_info").toString().length() > 4) {
                                ConfirmOrderBean.AddressInfoBean addressBean = new Gson().fromJson(data.getData().get("address_info").toString(), ConfirmOrderBean.AddressInfoBean.class);
                                confirmOrderBean.setAddress_info(addressBean);
                            }
                            setData(confirmOrderBean);
                        } else {
                            new RemindDialog(ConfirmOrderActivity.this, data.message)
                                    .setLifecycle(getLifecycle()).setCallBack(() -> finish()).setCancel(false).show();
//                            DyToast.getInstance().error(data.message);
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(MessageWrap messageWrap) {
        if ("dismiss".equals(messageWrap.message)) {
            setResult(1);
            finish();
        }
    }

    private void setData(ConfirmOrderBean s) {
        if (s.getAddress_info() == null || StringUtils.isEmpty(s.getAddress_info().getAddress_id())) {
            layoutEmpty.setVisibility(View.VISIBLE);
            llAddress.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            llAddress.setVisibility(View.VISIBLE);
            tvName.setText("收货人："+s.getAddress_info().getTrue_name());
            tvPhone.setText(s.getAddress_info().getTel_phone());
            tvAddress.setText(s.getAddress_info().getProvince() + " " + s.getAddress_info().getCity() + " " + s.getAddress_info().getArea() + " " + s.getAddress_info().getArea_info());
            addressId = s.getAddress_info().getAddress_id();
        }

        num = s.goods_info.num;
        GlideUtils.setImage(ivContent, s.getGoods_info().getGoods_image());
        tvGoodsName.setText(s.getGoods_info().getGoods_name());
        tvNum.setText(String.valueOf(s.getGoods_info().getNum()));
        tvGoodsType.setText(s.getGoods_info().getSpec_name());
        tvPrice.setText("￥" + s.getGoods_info().getPrice());
        tvTotCount.setText("共" + num + "件商品");
        allMoney = Float.parseFloat(s.getGoods_info().getPrice()) * s.getGoods_info().getNum();
        tvTotMoney.setText("￥" + ArithUtil.formatNum(allMoney));
        tvMoney.setText("￥" + ArithUtil.formatNum(allMoney));

        getExpMoney();
        if (s.member_info.is_point == 1) {
            canUsePoint = Float.parseFloat(s.member_info.member_points);

        }
        if (canUsePoint > 0) {
            if (canUsePoint > allMoney * Float.parseFloat(s.getGoods_info().goods_points) / 100) {
                canUsePoint = allMoney * Float.parseFloat(s.getGoods_info().goods_points) / 100;
            }
            usePoint = true;
            ivPoint.setImageResource(R.mipmap.btn_xuanz_c);
            llPoint.setVisibility(View.VISIBLE);
            etPoint.setText(ArithUtil.formatIntNum(canUsePoint));
            tvAvailablePoint.setText("（可用积分" + ArithUtil.formatIntNum(canUsePoint) + "）");
            tvRedus.setText("-" + ArithUtil.formatNum(canUsePoint));
        } else {
            usePoint = false;
            llPoint.setVisibility(View.GONE);
        }

    }

    //    确认订单-获取运费
    private void getExpMoney() {
        if (StringUtils.isEmpty(addressId)) {
            return;
        }
        Map<String, String> params = (Map<String, String>) getIntentData();
        params.put("address_id",addressId);
        params.put("goods_num",num+"");
        HttpFactory.getMallInstance().getFreight(params)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<FreightBean>>() {
                    @Override
                    public void success(HttpResponseData<FreightBean> data) {
                        if ("9999".equals(data.status)) {
                            if (StringUtils.isEmpty(data.getData().fee)) {
                                fee = 0;
                            } else {
                                fee = Float.parseFloat(data.getData().fee);
                            }

                            if (fee == 0) {
                                tvExpMoney.setText("￥0.00");
                            } else {
                                tvExpMoney.setText("￥" + data.getData().fee);
                            }
                            float point = 0;
                            if (!StringUtils.isEmpty(etPoint.getText().toString())) {
                                point = Float.parseFloat(etPoint.getText().toString());
                            }

                            tvMoney.setText(String.format("￥%s", ArithUtil.formatNum(allMoney + fee - point)));
                            tvSendType.setText("物流配送");
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

    private boolean usePoint;

    @OnClick({R.id.ll_address, R.id.ll_express, R.id.layout_empty, R.id.ll_point, R.id.tv_exchange})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.ll_address:
            case R.id.layout_empty:
                Bundle bundle = new Bundle();
                bundle.putBoolean("isSelect", true);
                startAct(AddressListActivity.class, true, 1);
                break;
            case R.id.ll_point:
                usePoint = !usePoint;
                if (usePoint) {
                    ivPoint.setImageResource(R.mipmap.btn_xuanz_c);
                } else {
                    ivPoint.setImageResource(R.mipmap.btn_xuanz_big);
                }
                changPoint();
                break;
            case R.id.ll_express:

                break;
            case R.id.tv_exchange:
                commit();
//                getPayType();
                break;
            default:
                break;
        }
    }


    private void getPayType() {
        DialogUtils.getInstance()
                .with(this)
                .setlayoutPosition(Gravity.BOTTOM)
                .setlayoutAnimaType(DialogUtils.ANIM_FROM_BOTTOM)
                .setlayoutId(R.layout.dialog_pay_type)
                .setlayoutPading(0, 0, 0, 0)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    ImageView ivAccount, ivWx, ivZfb;
                    ivAccount = view.findViewById(R.id.iv_account);
                    ivWx = view.findViewById(R.id.iv_wx);
                    ivZfb = view.findViewById(R.id.iv_zfb);
                    view.findViewById(R.id.iv_close).setOnClickListener(view1 -> DialogUtils.dismiss());
                    view.findViewById(R.id.ll_account).setOnClickListener(view12 -> {
                        payType = "ylpay";
                        ivAccount.setImageResource(R.mipmap.btn_xuanz_c);
                        ivWx.setImageResource(R.mipmap.btn_xuanz_big);
                        ivZfb.setImageResource(R.mipmap.btn_xuanz_big);
                    });
                    view.findViewById(R.id.ll_wx).setOnClickListener(view13 -> {
                        payType = "wxpay";
                        ivAccount.setImageResource(R.mipmap.btn_xuanz_big);
                        ivWx.setImageResource(R.mipmap.btn_xuanz_c);
                        ivZfb.setImageResource(R.mipmap.btn_xuanz_big);
                    });
                    view.findViewById(R.id.ll_zfb).setOnClickListener(view14 -> {
                        payType = "zfpay";
                        ivAccount.setImageResource(R.mipmap.btn_xuanz_big);
                        ivWx.setImageResource(R.mipmap.btn_xuanz_big);
                        ivZfb.setImageResource(R.mipmap.btn_xuanz_c);
                    });
                    payType = "zfpay";
                    view.findViewById(R.id.tv_confirm).setOnClickListener(view15 -> {
//                                if (!StringUtils.isEmpty(paySn)) {
//                                    pay(paySn);
//                                    return;
//                                }

                        pay(paySn);
                    });
                })
                .show();
    }

    //    确认订单-提交订单
    private void commit() {
        if (StringUtils.isEmpty(addressId)) {
            DyToast.getInstance().warning("请完善收货信息");
            return;
        }
        Map<String, String> params = (Map<String, String>) getIntentData();
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        params.put("token", mToken);
//        params.put("freight_id_type", transId + "_" + expType);
        params.put("address_id", addressId);
        params.put("goods_num", params.get("num"));
        if (usePoint && !StringUtils.isEmpty(etPoint.getText().toString())) {
            params.put("member_points", etPoint.getText().toString());
            params.put("member_points_pay", ArithUtil.formatNum(Float.parseFloat(etPoint.getText().toString())));
        } else {
            params.put("member_points", "");
            params.put("member_points_pay", "");
        }
        params.put("info", etRemark.getText().toString());
        HttpFactory.getMallInstance().submitOrder(params)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<SubmitOrderBean>>() {
                    @Override
                    public void success(HttpResponseData<SubmitOrderBean> data) {
                        if ("9999".equals(data.status)) {
                            paySn = data.getData().pay_sn;
//                            pay(data.getData().pay_sn);
                            getPayType();
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


    //支付方式
    private String payType = "";
    private String paySn = "";


    private void pay(String pay_sn) {
        this.paySn = pay_sn;
        Map<String, String> params = new HashMap<>();
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        params.put("token", mToken);
        params.put("pay_sn", paySn);
        params.put("pay_type", payType);
        if (usePoint) {
            params.put("points", etPoint.getText().toString());
        } else {
            params.put("points", "");
        }
        params.put("info", etRemark.getText().toString());
        switch (payType) {
            case "ylpay":
                if (MMKV.defaultMMKV().decodeInt(AppCacheInfo.mPayPass, 0) == 1) {
                    new PayPasswordDialog(this)
                            .setDialogClick(password -> {
                                params.put("pass", password);
                                payMoney(params);
                            })
                            .setLifecycle(getLifecycle()).show();
                } else {
                    new ConfirmDialog(this, () ->
                            startAct(getAty(), SettingDealPswActivity.class, 0, 2)
                            , "您还没设置交易密码", "去设置").setLifecycle(getLifecycle()).show();
                }
                break;
            case "wxpay":
                payMoneyWx(params);
                break;
            case "zfpay":
                payMoneyAli(params);
                break;
            default:
                break;
        }


    }


    //    确认订单-订单余额支付
    private void payMoney(Map<String, String> params) {

        HttpFactory.getMallInstance().payGoods(params)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            startAct(PaySuccessActivity.class, tvMoney.getText().toString());
                            DyToast.getInstance().success(data.getMessage());
                            setResult(1);
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

    //    确认订单-订单微信支付
    private void payMoneyWx(Map<String, String> params) {

        HttpFactory.getMallInstance().payGoodsWx(params)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<PayWxBean>>() {
                    @Override
                    public void success(HttpResponseData<PayWxBean> data) {
                        if ("9999".equals(data.status)) {
                            PayWxBean payWxBean = data.getData();
                            if (null != payWxBean) {
                                EventBus.getDefault().postSticky(AppCacheInfo.wxOrderPay);
                                String appId = payWxBean.appid;
                                IWXAPI api = WXAPIFactory.createWXAPI(ConfirmOrderActivity.this, appId);
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

    //    确认订单-订单支付宝支付
    private void payMoneyAli(Map<String, String> params) {

        HttpFactory.getMallInstance().payGoodsAli(params)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<String>>() {
                    @Override
                    public void success(HttpResponseData<String> data) {
                        if ("9999".equals(data.status)) {
                            Runnable payRunnable = () -> {
                                PayTask alipay = new PayTask(ConfirmOrderActivity.this);
                                Map<String, String> result = alipay.payV2(data.getData(), true);
                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = result;
                                handler.sendMessage(msg);
                            };
                            //必须异步调用
                            Thread payThread = new Thread(payRunnable);
                            payThread.start();
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


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                    @SuppressWarnings("unchecked") PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        startAct(PaySuccessActivity.class, tvMoney.getText().toString());
                        DyToast.getInstance().success("支付成功");
                        setResult(1);
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        DyToast.getInstance().error("支付失败");
                    }
                    MyLogger.dLog().e(resultInfo);
                    break;
                }
                default:
                    break;
            }
        }
    };


    /**
     * 微信成功的回调
     *
     * @param event
     */
    @Subscribe(sticky = true)
    public void onEvent(String event) {
        if (!TextUtils.isEmpty(event) && event.equals(AppCacheInfo.wxOrderPaySuccess)) {
//            requestUpInfo();
            startAct(PaySuccessActivity.class, tvMoney.getText().toString());
            DyToast.getInstance().success("支付成功");
            ConfirmOrderActivity.this.setResult(1);
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        vTran.setVisibility(View.GONE);
        DialogUtils.dismiss();
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {//选择收货地址
            AddressBean listBean = (AddressBean) data.getSerializableExtra("data");
            layoutEmpty.setVisibility(View.GONE);
            llAddress.setVisibility(View.VISIBLE);
            tvName.setText("收货人："+listBean.true_name);
            tvPhone.setText(listBean.tel_phone);
            tvAddress.setText(listBean.province + " " + listBean.city + " " + listBean.area + " " + listBean.area_info);
            addressId = listBean.address_id;
            getExpMoney();
        } else if (requestCode == 2) {
            pay(paySn);
        }
    }

}
