package com.dykj.youfeng.mall.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.StringUtils;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mall.adapter.OrderInfoDetailAdapter;
import com.dykj.youfeng.mall.adapter.OrderInfoGoodsAdapter;
import com.dykj.youfeng.mine.set.SettingDealPswActivity;
import com.dykj.youfeng.mode.CancelReasonBean;
import com.dykj.youfeng.mode.ExpInfoBean;
import com.dykj.youfeng.mode.OrderInfoBean;
import com.dykj.youfeng.mode.PayResult;
import com.dykj.youfeng.mode.PayWxBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.tools.ArithUtil;
import com.dykj.module.AppConstant;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.DateUtils;
import com.dykj.module.util.DialogUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.dykj.module.view.dialog.ConfirmDialog;
import com.dykj.module.view.dialog.SelectDialog;
import com.dykj.module.view.paypass.PayPasswordDialog;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
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
 * @author lcjing
 * 订单详情
 */
public class OrderInfoActivity extends BaseActivity {


    //    @BindView(R.id.tv_shouhou)
//    TextView tvShouhou;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.tv_goods_money)
    TextView tvGoodsMoney;
    @BindView(R.id.tv_exp_money)
    TextView tvExpMoney;
    @BindView(R.id.tv_tot_money)
    TextView tvTotMoney;
    @BindView(R.id.tv_reduce_moeny)
    TextView tvReduceMoeny;
    @BindView(R.id.tv_pay_moeny)
    TextView tvPayMoeny;
    @BindView(R.id.rv_order_detail)
    RecyclerView rvOrderDetail;
    @BindView(R.id.v_bottom)
    View vBottom;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_go_pay)
    TextView tvGoPay;
    @BindView(R.id.tv_exp)
    TextView tvExp;
    @BindView(R.id.tv_receipt)
    TextView tvReceipt;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.tv_wuliu)
    TextView tvWuliu;
    @BindView(R.id.tv_wuliu_time)
    TextView tvWuliuTime;
    @BindView(R.id.fl_wuliu)
    FrameLayout flWuliu;
    @BindView(R.id.rv_goods)
    RecyclerView rvGoods;
    @BindView(R.id.tv_user_name_phone)
    TextView tvUserNamePhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_des)
    TextView tvDes;

    private OrderInfoDetailAdapter adapter;
    private List<OrderInfoDetailAdapter.Value> infos;


    @Override
    public int intiLayout() {
        return R.layout.activity_order_info;
    }

    @Override
    public void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        QMUIStatusBarHelper.setStatusBarDarkMode(this);
        infos = new ArrayList<>();
        adapter = new OrderInfoDetailAdapter(R.layout.item_order_info_detail, infos);
        rvOrderDetail.setAdapter(adapter);
    }


    @Override
    public void doBusiness() {
        getOrderInfo();
    }


    private void getOrderInfo() {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().orderInfo(mToken, getIntentData().toString())
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<OrderInfoBean>>() {
                    @Override
                    public void success(HttpResponseData<OrderInfoBean> data) {
                        if (data.status.equals(AppConstant.SUCCESS)) {
                            orderSn = data.getData().order_sn;
                            initData(data.getData());
                        } else {
                            DyToast.getInstance().warning(data.getMessage());
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        if (!"数据解析异常".equals(data.getMessage())) {
                            DyToast.getInstance().error(data.getMessage());
                        }
                    }
                }));
    }


    private String goodsImg = "";


    private void initData(OrderInfoBean data) {
        OrderInfoGoodsAdapter goodsAdapter = new OrderInfoGoodsAdapter(R.layout.item_order_info_goods, data.goods_list);
        goodsAdapter.setOnItemClickListener((adapter, view, position) -> startAct(GoodsInfoActivity.class, data.goods_list.get(position).goods_id));
        goodsAdapter.setOnItemChildClickListener((adapter, view, position) ->
                startAct(RefundApplyActivity.class, data.goods_list.get(position), 1001));
        rvGoods.setAdapter(goodsAdapter);
        tvAddress.setText(String.format("%s %s %s %s", data.order_common.reciver_info.province, data.order_common.reciver_info.city, data.order_common.reciver_info.area, data.order_common.reciver_info.area_info));
        tvUserNamePhone.setText(String.format("%s   %s", data.order_common.reciver_info.true_name, data.order_common.reciver_info.tel_phone));

        tvMessage.setText(data.order_common.order_message);
        tvGoodsMoney.setText("￥" + data.goods_allmoney);
        tvExpMoney.setText("￥" + data.shipping_fee);
        tvTotMoney.setText("￥" + data.order_amount);
        String pointsFee = data.points_fee;
        if (StringUtils.isEmpty(pointsFee)) {
            pointsFee = "0.00";
        }
        tvReduceMoeny.setText("￥" + pointsFee);

        tvPayMoeny.setText("￥" + ArithUtil.formatNum(data.order_amount-Float.parseFloat(pointsFee)));
        flWuliu.setVisibility(View.GONE);
        //        订单状态：10待支付，20已付款、待发货， 30已发货， 40已收货，60交易完成
        tvDes.setText("");
        infos.clear();
        infos.add(new OrderInfoDetailAdapter.Value("订单编号", data.order_sn));
        infos.add(new OrderInfoDetailAdapter.Value("下单时间", DateUtils.getTime(data.add_time)));

        adapter.notifyDataSetChanged();
        if (data.payment_code == null) {
            data.payment_code = "";
        }
        switch (data.payment_code) {
            case "ylpay":
                infos.add(new OrderInfoDetailAdapter.Value("支付方式", "余额支付"));
                break;
            case "alipay":
            case "zfpay":
                infos.add(new OrderInfoDetailAdapter.Value("支付方式", "支付宝支付"));
                break;
            case "wechat":
            case "wxpay":
                infos.add(new OrderInfoDetailAdapter.Value("支付方式", "微信支付"));
                break;
            default:
                infos.add(new OrderInfoDetailAdapter.Value("支付方式", "未支付"));
                break;
        }
        infos.add(new OrderInfoDetailAdapter.Value("支付时间", DateUtils.getTime(data.payment_time)));
        infos.add(new OrderInfoDetailAdapter.Value("发货时间", DateUtils.getTime(data.shipping_time)));
        infos.add(new OrderInfoDetailAdapter.Value("物流单号", data.shipping_code));
        infos.add(new OrderInfoDetailAdapter.Value("成交时间", DateUtils.getTime(data.finnshed_time)));

        switch (data.order_state) {
            case "0":
                tvDelete.setVisibility(View.VISIBLE);
                tvStatus.setText("已取消");
                break;
            case "10":
                tvCancel.setVisibility(View.VISIBLE);
                tvGoPay.setVisibility(View.VISIBLE);
                tvStatus.setText("待支付");
                if (getIntent().getBooleanExtra("goPay", false)) {
                    getPayType();
                }
                break;
            case "20":
                tvStatus.setText("待发货");
                goodsAdapter.setShowRefund("0".equals(data.lock_state));
                break;
            case "30":
                getWuliuData();
                goodsAdapter.setShowRefund("0".equals(data.lock_state));
                tvStatus.setText("待收货");
                tvExp.setVisibility(View.VISIBLE);
                tvReceipt.setVisibility(View.VISIBLE);
                break;
            case "40":
                getWuliuData();
//                goodsAdapter.setShowRefund("0".equals(data.lock_state));
                tvStatus.setText("已收货");
                tvExp.setVisibility(View.VISIBLE);
                tvRemark.setVisibility(View.VISIBLE);
                if ("0".equals(data.evaluation_state)) {
                    tvRemark.setVisibility(View.VISIBLE);
                }
                break;
            case "60":
                getWuliuData();
                goodsAdapter.setShowRefund("0".equals(data.lock_state));
                tvStatus.setText("交易完成");
                tvExp.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        if (data.goods_list != null && data.goods_list.size() > 0) {
            goodsImg = data.goods_list.get(0).goods_image;
        }
    }


    private void getWuliuData() {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().getExpress(mToken, getIntentData().toString())
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<ExpInfoBean>>() {
                    @Override
                    public void success(HttpResponseData<ExpInfoBean> data) {
                        if ("9999".equals(data.status)) {
                            if (data.getData().data != null && data.getData().data.size() > 0) {
                                flWuliu.setVisibility(View.VISIBLE);
                                tvWuliu.setText(data.getData().data.get(0).context);
                                tvWuliuTime.setText("更新时间：" + data.getData().data.get(0).time);
                            }
                        } else {
                            flWuliu.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        flWuliu.setVisibility(View.GONE);
                    }
                }));
    }


    private String orderSn = "";

    @OnClick({R.id.fl_wuliu, R.id.tv_cancel, R.id.tv_go_pay, R.id.tv_exp, R.id.tv_receipt, R.id.tv_delete, R.id.tv_remark})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_wuliu:
            case R.id.tv_exp:
                //
                Map<String, String> map = new HashMap<>();
                map.put("orderId", getIntentData().toString());
                map.put("goodsImg", goodsImg);
                startAct(ExpressInfoActivity.class, map);
                break;
            case R.id.tv_cancel:
                getCancelReason(orderSn);
                break;
            case R.id.tv_go_pay:
                getPayType();
                break;
            case R.id.tv_receipt:
                new ConfirmDialog(this, this::receive,
                        "确认收到该商品？", "确认").setLifecycle(getLifecycle()).show();
                break;
            case R.id.tv_delete:
                new ConfirmDialog(this, this::delete,
                        "删除该订单？", "删除").setLifecycle(getLifecycle()).show();
                break;
            case R.id.tv_remark:
                startAct(CommitEvaActivity.class, getIntentData(), 1);

                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        DialogUtils.dismiss();
    }

    private void getCancelReason(String orderSn) {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().getCancelReason(mToken)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<CancelReasonBean>>() {
                    @Override
                    public void success(HttpResponseData<CancelReasonBean> data) {
                        if (data.status.equals(AppConstant.SUCCESS)) {
                            List<String> list = new ArrayList<>();
                            for (int i = 0; i < data.getData().list.size(); i++) {
                                list.add(data.getData().list.get(i).msg);
                            }
                            new SelectDialog(OrderInfoActivity.this,
                                    (position, value) ->
                                            cancel(orderSn, data.getData().list.get(position).reason),
                                    "请选择取消原因", list).setLifecycle(getLifecycle()).show();
                        } else {
                            DyToast.getInstance().warning(data.getMessage());
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        if (!"数据解析异常".equals(data.getMessage())) {
                            DyToast.getInstance().error(data.getMessage());
                        }
                    }
                }));

    }


    private void cancel(String orderSn, String reason) {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().getCancel(mToken, orderSn, reason)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if (data.status.equals(AppConstant.SUCCESS)) {
                            DyToast.getInstance().success(data.message);
                            getOrderInfo();
                        } else {
                            DyToast.getInstance().warning(data.getMessage());
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        if (!"数据解析异常".equals(data.getMessage())) {
                            DyToast.getInstance().error(data.getMessage());
                        }
                    }
                }));
    }

    private void delete() {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().deleteOrder(mToken, getIntentData().toString())
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if (data.status.equals(AppConstant.SUCCESS)) {
                            DyToast.getInstance().success(data.message);
                            setResult(1);
                            finish();
                        } else {
                            DyToast.getInstance().warning(data.getMessage());
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        if (!"数据解析异常".equals(data.getMessage())) {
                            DyToast.getInstance().error(data.getMessage());
                        }
                    }
                }));
    }

    private void receive() {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().confirmReceive(mToken, getIntentData().toString())
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if (data.status.equals(AppConstant.SUCCESS)) {
                            DyToast.getInstance().success(data.message);
                            setResult(1);
                            finish();
                        } else {
                            DyToast.getInstance().warning(data.getMessage());
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        if (!"数据解析异常".equals(data.getMessage())) {
                            DyToast.getInstance().error(data.getMessage());
                        }
                    }
                }));
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
                        Map<String, String> params = new HashMap<>();
                        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
                        params.put("token", mToken);
                        params.put("order_sn", orderSn);
                        params.put("pay_type", payType);
                        switch (payType) {
                            case "zfpay":
                                payMoneyAli(params);
                                break;
                            case "wxpay":
                                payMoneyWx(params);
                                break;
                            default:
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
                        }

                    });
                })
                .show();
    }


    private String payType = "";


    //    确认订单-订单余额支付
    private void payMoney(Map<String, String> params) {
        params.put("pay_type", payType);
        HttpFactory.getMallInstance().payOrder(params)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            DyToast.getInstance().success(data.getMessage());
                            setResult(1);
                            finish();
                            startAct(PaySuccessActivity.class, tvPayMoeny.getText().toString());
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

        HttpFactory.getMallInstance().payOrderWx(params)
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
                                IWXAPI api = WXAPIFactory.createWXAPI(OrderInfoActivity.this, appId);
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
        HttpFactory.getMallInstance().payOrderAli(params)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<String>>() {
                    @Override
                    public void success(HttpResponseData<String> data) {
                        if ("9999".equals(data.status)) {
                            Runnable payRunnable = () -> {
                                PayTask alipay = new PayTask(OrderInfoActivity.this);
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
                        DyToast.getInstance().success("支付成功");
                        setResult(1);
                        finish();
                        startAct(PaySuccessActivity.class, tvPayMoeny.getText().toString());
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
            DyToast.getInstance().success("支付成功");
            this.setResult(1);
            finish();
            startAct(PaySuccessActivity.class, tvPayMoeny.getText().toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == 1) {
            setResult(1);
            finish();
        } else {
            getOrderInfo();
        }


    }
}
