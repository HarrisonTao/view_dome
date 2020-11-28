/*
package com.dykj.youfeng.home.up_vip;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.dykj.youfeng.view.ProgressBarView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.w3c.dom.Comment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class UpVipActivity extends BaseActivity implements View.OnClickListener {
    private static final int SDK_PAY_FLAG = 200;
    @BindView(R.id.vip_recycler)
    RecyclerView vipRecycler;
    @BindView(R.id.tv_vip_grade)
    TextView tvVipGrade;
    @BindView(R.id.tv_vip_money)
    TextView tvVipMoney;
    @BindView(R.id.tv_strat)
    TextView tvStrat;
    @BindView(R.id.progress)
    ProgressBarView progress;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.tv_vip_info)
    TextView tvVipInfo;
    @BindView(R.id.tv_dredge)
    TextView tvDredge;
    @BindView(R.id.rl_scale)
    RelativeLayout rlScale;
    @BindView(R.id.ll_header_bg)
    LinearLayout llHeaderBg;
    */
/**
     * 设置view 数据
     *
     * @param vipBean
     *//*


    DecimalFormat mDf = new DecimalFormat("0.00");
    private VipAdapter vipAdapter = new VipAdapter(R.layout.item_up_vip);
    private ImageView ivWx, ivali, ivAccount;
    private String type = "wechat";
    private String upLevelType;
    private double levelPrice;  // 支付的金额
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
					*/
/*
					9000	订单支付成功
					8000	正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
					4000	订单支付失败
					5000	重复请求s
					6001	用户中途取消
					6002	网络连接出错
					6004	支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
					其它	其它支付错误
					*//*

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

    @Override
    public int intiLayout() {
        return R.layout.activity_up_vip;
    }

    @Override
    public void initData() {
        QMUIStatusBarHelper.setStatusBarDarkMode(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        vipRecycler.setAdapter(vipAdapter);
    }

    @Override
    public void doBusiness() {
        requestUpInfo();
    }

    */
/**
     * 升级
     *//*

    private void requestUpInfo() {
        HttpFactory.getInstance().upVip(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""))
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(UpVipActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<VipBean>>() {
                    @Override
                    public void success(HttpResponseData<VipBean> data) {
                        if ("9999" .equals(data.status)) {
                            VipBean vipBean = data.getData();
                            setViewData(vipBean);
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
    //格式化小数
    private void setViewData(VipBean vipBean) {
     */
/*   List<VipBean.LevelListBean> levelList = vipBean.levelList;
        List<VipBean.LevelListBean> newList = new ArrayList<>();
        if (null != levelList) {
            for (int i = 0; i < levelList.size(); i++) {
                VipBean.LevelListBean levelListBean = levelList.get(i);
                if (levelListBean.isShow.equals("1")) {
                    newList.add(levelListBean);
                }
            }
        }*//*


      //  vipAdapter.setNewData(newList);

        tvVipGrade.setText(vipBean.levelName);
        // 当前等级
        String currentLevel = vipBean.level;
        int newCountList = newList.size();

        for (int i = 0; i < newCountList; i++) {
            VipBean.LevelListBean bean = newList.get(i);
            String levelId = bean.levelId;

            if (currentLevel.equals(levelId)) {
                setBg(i);

                if (currentLevel.equals("1")) {
                    // 普通会员
                    if (i != newCountList - 1) {
                        VipBean.LevelListBean levelListBean = newList.get(i + 1);
                        levelPrice = levelListBean.levelPrice;
                        tvVipMoney.setText("￥" + levelPrice);
                        tvVipMoney.setTextColor(ContextCompat.getColor(this,R.color.font_ff33));

                        tvVipGrade.setTextColor(ContextCompat.getColor(this,R.color.font_ff33));

                        tvVipInfo.setText("升级" + levelListBean.levelName + "享受更多会员权益");
                        tvVipInfo.setTextColor(ContextCompat.getColor(this,R.color.color_bd77));

                        tvDredge.setVisibility(levelListBean.is_pay.equals("0") ? View.VISIBLE : View.GONE);
                        tvDredge.setOnClickListener(v -> showDialog(levelPrice + ""));
                    }
                } else if (i == newCountList - 1) {
                    // 最高一级
                    tvVipMoney.setVisibility(View.GONE);
                    rlScale.setVisibility(View.GONE);
                    tvDredge.setVisibility(View.GONE);
                    tvVipInfo.setText("您当前已是最高级别会员");
                    tvVipInfo.setTextSize(18);
                    tvVipInfo.setTextColor(Color.WHITE);
                } else {
                    // 其他等级
                    tvVipMoney.setVisibility(View.GONE);
                    rlScale.setVisibility(View.VISIBLE);
                    // 进度条
                    tvStrat.setText(vipBean.reCount + "");
                    tvEnd.setText(vipBean.needReCount + "");

                    String needReCount = vipBean.needReCount;
                    int nCount = TextUtils.isEmpty(needReCount) ? 0 : Integer.parseInt(needReCount);
                    if (nCount == 0) {
                        progress.setProgress(0);
                    } else {
                        float num = (float) vipBean.reCount / nCount;
                        String format = mDf.format(num);
                        double v = Double.parseDouble(format) * 100;
                        progress.setProgress((int) v);
                    }
                    if (i != newCountList - 1) {
                        VipBean.LevelListBean nextLevelBean = newList.get(i + 1);
                        tvVipInfo.setText("您还差" + vipBean.hasReCount + "名直推" + bean.levelName + "便可以免费升级为" + nextLevelBean.levelName);
                    }
                    tvVipInfo.setTextColor(Color.WHITE);
                    tvDredge.setVisibility(View.GONE);
                }
                break;
            }

        }


//        switch (currentLevel) {
//            case "1":
//                // 普通会员
//                VipBean.LevelListBean levelListBean = newList.get(1);
//                levelPrice = levelListBean.levelPrice;
//                tvVipMoney.setText("￥" + levelPrice);
//                tvVipInfo.setText("升级vip会员享受更多会员权益");
//                llHeaderBg.setBackgroundResource(R.mipmap.putong_bg);
//                tvDredge.setVisibility(0 == levelListBean.is_pay ? View.VISIBLE : View.GONE);
//
//                tvDredge.setOnClickListener(v -> showDialog(levelPrice + ""));
//                break;
//
//            case "2":
//                // vip
//                tvVipMoney.setVisibility(View.GONE);
//                rlScale.setVisibility(View.VISIBLE);
//                // 进度条
//                tvStrat.setText(vipBean.reCount + "");
//                tvEnd.setText(vipBean.needReCount + "");
//
//                String needReCount = vipBean.needReCount;
//                int nCount = TextUtils.isEmpty(needReCount) ? 0 : Integer.parseInt(needReCount);
//                if (nCount == 0) {
//                    progress.setProgress(0);
//                } else {
//
//                    float num = (float) vipBean.reCount / nCount;
//                    String format = mDf.format(num);
//                    double v = Double.parseDouble(format) * 100;
//                    progress.setProgress((int) v);
//                }
//
//                tvVipInfo.setText("您还差" + vipBean.hasReCount + "名直推VIP会员便可以免费升级为黄金VIP");
//                llHeaderBg.setBackgroundResource(R.mipmap.vip_bg);
//                tvDredge.setVisibility(View.GONE);
//                break;
//
//            case "3":
//                // 黄金
//                tvVipMoney.setVisibility(View.GONE);
//                rlScale.setVisibility(View.VISIBLE);
//                // 进度条
//                tvStrat.setText(vipBean.reCount + "");
//                tvEnd.setText(vipBean.needReCount + "");
//
//                String needReCount1 = vipBean.needReCount;
//                int needReCount2 = TextUtils.isEmpty(needReCount1) ? 0 : Integer.parseInt(needReCount1);
//
//                if (needReCount2 == 0) {
//                    progress.setProgress(0);
//                } else {
//
//                    float num = (float) vipBean.reCount / needReCount2;
//                    String format = mDf.format(num);
//                    double v = Double.parseDouble(format) * 100;
//                    progress.setProgress((int) v);
//                }
//                tvVipInfo.setText("您还差" + vipBean.hasReCount + "名直推黄金会员便可以免费升级为钻石VIP");
//                llHeaderBg.setBackgroundResource(R.mipmap.huangj_bg);
//                tvDredge.setVisibility(View.GONE);
//                break;
//
//
//            case "4":
//                // 钻石
//                tvVipMoney.setVisibility(View.GONE);
//                rlScale.setVisibility(View.GONE);
//                llHeaderBg.setBackgroundResource(R.mipmap.zuanshi_bg);
//                tvDredge.setVisibility(View.GONE);
//                tvVipInfo.setText("您当前已是最高级别会员");
//                tvVipInfo.setTextSize(18);
//                break;
//
//        }
    }

    private void setBg(int position) {
        switch (position % 6) {
            case 0:
                // 普通会员
                llHeaderBg.setBackgroundResource(R.drawable.putong_bg2);
                break;

            case 1:
                // vip会员
                llHeaderBg.setBackgroundResource(R.drawable.vip_bg2);
                break;

            case 2:
                llHeaderBg.setBackgroundResource(R.drawable.huangj_bg2);
                break;

            case 3:
                // 钻石会员
                llHeaderBg.setBackgroundResource(R.drawable.zuanshi_bg2);
                break;


            case 4:
                // 钻石会员
                llHeaderBg.setBackgroundResource(R.drawable.yinzuan_bg2);
                break;


            case 5:
                // 钻石会员
                llHeaderBg.setBackgroundResource(R.drawable.yincang_bg2);
                break;
                default:
                    break;
        }
    }


    */
/**
     * 支付 dialog
     *
     * @param levelPrice
     *//*

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
                    view.findViewById(R.id.rl_wechat).setOnClickListener(UpVipActivity.this);
                    view.findViewById(R.id.rl_ali).setOnClickListener(UpVipActivity.this);
                    view.findViewById(R.id.rl_account).setOnClickListener(UpVipActivity.this);
                    view.findViewById(R.id.tv_submit_pay).setOnClickListener(UpVipActivity.this);
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

    */
/**
     * 微信支付
     *//*

    private void weChatCommit() {
        HttpFactory.getInstance().wechatBuy(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), "1")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(UpVipActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<PayWxBean>>() {
                    @Override
                    public void success(HttpResponseData<PayWxBean> data) {
                        if ("9999" .equals(data.status)) {
                            PayWxBean payWxBean = data.getData();
                            if (null != payWxBean) {
                                EventBus.getDefault().postSticky(AppCacheInfo.mUpVip);
                                String appId = payWxBean.appid;
                                IWXAPI api = WXAPIFactory.createWXAPI(UpVipActivity.this, appId);
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


    */
/**
     * 阿里支付
     *//*

    private void aliCommit() {
        HttpFactory.getInstance().aliPay(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), "1")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(UpVipActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<String>>() {
                    @Override
                    public void success(HttpResponseData<String> data) {
                        if ("9999" .equals(data.status)) {
                            String info = data.getData();
                            if (null != info) {
                                Runnable payRunnable = () -> {
                                    PayTask alipay = new PayTask(UpVipActivity.this);
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

    */
/**
     * 余额支付
     *//*

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

    */
/**
     * 余额支付弹框
     *//*

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


    */
/**
     * 提交余额支付
     *
     * @param passContent
     * @param dialog
     *//*

    private void submitAccount(String passContent, PayPassDialog dialog) {
        HttpFactory.getInstance().acountBuy(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), passContent, "1")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(UpVipActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999" .equals(data.status)) {
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


    */
/**
     * 微信成功的回调
     *
     * @param event
     *//*

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
}
*/
