package com.dykj.youfeng.home.repayment.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.base.BaseWebViewActivity;
import com.dykj.module.entity.BaseWebViewData;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.CountDownUtil;
import com.dykj.module.util.DialogUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.SmartRefreshUtils;
import com.dykj.module.util.toast.DyToast;
import com.dykj.youfeng.R;
import com.dykj.youfeng.home.repayment.adapter.CreditCardMsgAdapter;
import com.dykj.youfeng.mode.CardpaymentBean;
import com.dykj.youfeng.mode.CreditcardListBean;
import com.dykj.youfeng.mode.DsBindCardData;
import com.dykj.youfeng.mode.IpBean;
import com.dykj.youfeng.mode.JftSmsBean;
import com.dykj.youfeng.mode.PlanDetailBean;
import com.dykj.youfeng.mode.SelectCreditBean;
import com.dykj.youfeng.mode.ShopInputBean;
import com.dykj.youfeng.mode.SmsBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 信用卡管理
 */
public class CreditCardMsgActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {

    private static final int SDK_PAY_FLAG = 200;
    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.mSmartRefresh)
    SmartRefreshLayout mSmartRefresh;
    private CreditCardMsgAdapter mAdapter = new CreditCardMsgAdapter(this,R.layout.item_credit_msg);
    private int mPage;
    private CreditcardListBean.ListBean mItemCreditcardBean;
    private String mCreditId;
    private String mType;
    private String mChannelType;
    private CountDownUtil mCount;
    private String tradeNo = "";
    private String ip = "";
    private String mRepaymentMode;


    @Override
    protected void onRestart() {
        super.onRestart();
        requestCreditCardList(true);

    }

    //*************************************jft**********************start***********************************
    private void checkJftCardStatus(String code) {
        String token = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getInstance().jftIsBindCard(token, mItemCreditcardBean.id + "", code)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(CreditCardMsgActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("8888".equals(data.getStatus())) {
                            //用户未绑卡
                            jftApplySms(code, false);
                        } else if ("9999".equals(data.getStatus())) {
                            //用户已绑卡 制定计划
                            if(TextUtils.isEmpty(mRepaymentMode)){
                                return;
                            }
                            if(mRepaymentMode.equals("手动")){
                                startAct(EnactPlanActivity.class, mItemCreditcardBean);
                            }else {
                                startAct(RepaymentOneActivity.class, mItemCreditcardBean);
                            }
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
     * 绑卡 验证码 <jft>
     *
     * @param cardpaymentBean
     */
    private void showJftSmsDialog(String code, JftSmsBean.InfoBean cardpaymentBean) {
        DialogUtils.getInstance()
                .with(this)
                .setlayoutPosition(Gravity.CENTER)
                .setlayoutPading(150, 0, 150, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FROM_BOTTOM)
                .setlayoutId(R.layout.dialog_bind_code)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    EditText etCode = view.findViewById(R.id.et_code);
                    TextView tvPhone = view.findViewById(R.id.tv_phone);
                    TextView tvGetCode = view.findViewById(R.id.tv_get_code);
                    mCount = new CountDownUtil(tvGetCode);
                    mCount.startCount();
                    tvPhone.setText(TextUtils.isEmpty(cardpaymentBean.phone) ? "" : cardpaymentBean.phone);
                    tvGetCode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            jftApplySms(code, true);
                        }
                    });
                    view.findViewById(R.id.tv_submit).setOnClickListener(v -> {
                        String smsCode = etCode.getText().toString().trim();
                        if (TextUtils.isEmpty(smsCode)) {
                            DyToast.getInstance().warning("请输入验证码!");
                        } else {
                            jftConfirm(code, smsCode);
                            DialogUtils.dismiss();
                        }
                    });
                })
                .show();
    }

    private void jftApplySms(String code, boolean reGet) {
        String token = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getInstance().jftApplySms(token, mItemCreditcardBean.id + "", code)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(CreditCardMsgActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<JftSmsBean>() {
                    @Override
                    public void success(JftSmsBean data) {
                        if ("8888".equals(data.status)) {
                            if (TextUtils.equals(code, "3013")) {

                            } else {
                                tradeNo = data.info.tradeno;
                                //用户未绑卡
                                if (reGet && mCount != null) {
                                    mCount.startCount();
                                } else {
                                    showJftSmsDialog(code, data.info);
                                }
                            }
                        } else if ("9999".equals(data.status)) {
                            //用户已绑卡 制定计划
                            startAct(EnactPlanActivity.class, mItemCreditcardBean);
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
    /***
     * 是否进件
     * @param code
     */
    private void jftIsJoin(final String code,String repaymentMode,String card_num) {
        HttpFactory.getInstance()
                .jftIsJoin(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), code,card_num)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(CreditCardMsgActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("8888".equals(data.status)) {
                            //用户未进件
                            startAct(getAty(), ShopInputJftActivity.class, code);
                        } else if ("9999".equals(data.getStatus()) || "1017".equals(data.getStatus())) {
                            //用户已进件   判断卡的状态

                            BaseWebViewData viewData = new BaseWebViewData();
                            viewData.title = "签约";
                            viewData.content = mItemCreditcardBean.url+"&token="+ MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
                            Log.d("wtf", mItemCreditcardBean.url+"&token="+ MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
                            startAct(getAty(), BaseWebViewActivity.class, viewData);
                        }else if("1018".equals(data.getStatus())){
                            //用户已绑卡 制定计划
                            startAct(EnactPlanActivity.class, mItemCreditcardBean);
                        } else {
                            DyToast.getInstance().error(data.getMessage());
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));
    }



    private void cardInfoAdd() {
        ConcurrentHashMap<String, Object> params = new ConcurrentHashMap<>();
        params.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        params.put("phone", mItemCreditcardBean.phone);
        params.put("bank_num", mItemCreditcardBean.bank_num);
       // params.put("code", mChannle);
        params.put("bank_number", mItemCreditcardBean.bank_num);
        params.put("bank_name", mItemCreditcardBean.bank_name );

        HttpFactory.getInstance().cardInfoAdd(params)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            BaseWebViewData viewData = new BaseWebViewData();
                            viewData.title = "签约";
                            viewData.content = mItemCreditcardBean.url+"&token="+ MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
                            Log.d("wtf", mItemCreditcardBean.url+"&token="+ MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
                            startAct(getAty(), BaseWebViewActivity.class, viewData);
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


    private void jftConfirm(String code, String sms) {
        String token = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>(4);
        map.put("token", token);
        map.put("cardid", mItemCreditcardBean.id);
        map.put("tradeno", tradeNo);
        map.put("smscode", sms);
        map.put("code", code);
        HttpFactory.getInstance().jftApplyConfirm(map)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(CreditCardMsgActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.getStatus())) {
                            //用户已绑卡 制定计划
                            startAct(EnactPlanActivity.class, mItemCreditcardBean);
                        } else {
                            DyToast.getInstance().warning(data.getMessage());
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
     * 畅捷 大小额是否绑卡
     */
    private void checkCjCard() {
        ConcurrentHashMap<String, Object> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        concurrentHashMap.put("cardId", mItemCreditcardBean.id + "");   // 卡片Id
        concurrentHashMap.put("channel", mChannelType);
        HttpFactory.getInstance().changjieCard(concurrentHashMap)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(CreditCardMsgActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<CardpaymentBean>>() {
                    @Override
                    public void success(HttpResponseData<CardpaymentBean> data) {
                        if ("9999".equals(data.status)) {
                            // 开卡成功
                            startAct(EnactPlanActivity.class, mItemCreditcardBean);
                        } else if ("8888".equals(data.status)) {
                            CardpaymentBean data1 = data.getData();
                            showBindSmsDialog2(data1);
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
     * 绑卡 验证码 <畅捷>
     *
     * @param cardpaymentBean
     */
    private void showBindSmsDialog2(CardpaymentBean cardpaymentBean) {
        DialogUtils.getInstance()
                .with(this)
                .setlayoutPosition(Gravity.CENTER)
                .setlayoutPading(150, 0, 150, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FROM_BOTTOM)
                .setlayoutId(R.layout.dialog_bind_code)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    EditText etCode = view.findViewById(R.id.et_code);
                    TextView tvPhone = view.findViewById(R.id.tv_phone);
                    tvPhone.setText(TextUtils.isEmpty(cardpaymentBean.phone) ? "" : cardpaymentBean.phone);

                    view.findViewById(R.id.tv_submit).setOnClickListener(v -> {
                        String smsCode = etCode.getText().toString().trim();
                        if (TextUtils.isEmpty(smsCode)) {
                            DyToast.getInstance().warning("请输入验证码!");
                        } else {
                            cjConfirm2(cardpaymentBean.orderNo, smsCode);
                            DialogUtils.dismiss();
                        }
                    });
                })
                .show();
    }

    /**
     * 畅捷绑卡 <一卡多还>
     *
     * @param orderNo
     * @param smsCode
     */
    private void cjConfirm2(String orderNo, String smsCode) {
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        map.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        map.put("cardId", mItemCreditcardBean.id + "");
        map.put("orderNo", orderNo);
        map.put("smsCode", smsCode);
        map.put("channel", mChannelType);
        HttpFactory.getInstance().changjiecardconfirm(map)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(CreditCardMsgActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<SmsBean>>() {
                    @Override
                    public void success(HttpResponseData<SmsBean> data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            String signStatus = data.getData().signStatus;
                            if ("2".equals(signStatus)) {
                                startAct(EnactPlanActivity.class, mItemCreditcardBean);
                            } else if ("3".equals(signStatus)) {
                                DyToast.getInstance().warning("开通失败");
                            } else if ("4".equals(signStatus)) {
                                DyToast.getInstance().warning("绑卡状态失效");
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

    @Subscribe
    public void onEvent(String event) {
        if (TextUtils.isEmpty(event)) {
            return;
        }
        if (event.equals(AppCacheInfo.mRefreshCreditCardList)) {
            requestCreditCardList(true);
        }
        if (event.equals(AppCacheInfo.mSubmitRepayMent)) {
            requestCreditCardList(true);
//            repaymentPlanSuccess = true;
        }
    }

    /**
     * 信用卡列表
     */
    private void requestCreditCardList(boolean isRefresh) {
        if (isRefresh) {
            mPage = 1;
        } else {
            mPage++;
        }
        Log.d("wtf","http://yfapi.xunyunsoft.com/api/creditcard/listing?token="+MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        HttpFactory.getInstance().creditcardList(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), "2", mPage + "", "10")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(CreditCardMsgActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<CreditcardListBean>>() {
                    @Override
                    public void success(HttpResponseData<CreditcardListBean> data) {
                        if ("9999".equals(data.status)) {
                            CreditcardListBean data1 = data.getData();
                            if (null != data1) {
                                SmartRefreshUtils.loadMore(mAdapter, mPage, data1.page, data1.list, mSmartRefresh);
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

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mItemCreditcardBean = mAdapter.getData().get(position);
        int repayment_status = mItemCreditcardBean.repayment_status;
//        if (1 == repayment_status) {
//            DyToast.getInstance().warning("该卡片有计划执行,请选择其他卡片!");
//            return;
//        }

        if ("oneMore".equals(mType)) {                                   // 一卡还多卡  畅捷小额
            if (mCreditId.equals(mItemCreditcardBean.id + "")) {
                DyToast.getInstance().warning("不能选择相同的信用卡");
                return;
            }
//            join_Status();
            checkGft();
        }
    }

    /////////////////////////////// 一卡多还 start ////////////////////////

    /**
     * 国付通 状态
     */
    private void checkGft() {
        HttpFactory.getInstance().gftChannel(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mItemCreditcardBean.id + "")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(CreditCardMsgActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<JftSmsBean>>() {
                    @Override
                    public void success(HttpResponseData<JftSmsBean> data) {
                        if ("9999".equals(data.status)) {   // 用户已进件
                            String code = data.getData().code;
                            switch (code) {
                                case "0":
                                    //不用重新注册，可直接交易
                                    setResultData();
                                    break;

                                case "1080004":
                                    //不用重新注册，可交易，需要签约卡
                                    isSignCard();
                                    break;


                                case "1020000":
                                    //子商户号未注册
                                    gftJoin_Status();
                                    break;


                                case "1090015":
                                    //该银行渠道不支持该子商户交易，请重新注册子商户
                                    gftJoin_Status();
                                    break;


                                case "1060000":
                                    DyToast.getInstance().warning("不支持该银行");
                                    break;


                                case "1000008":
                                    DyToast.getInstance().warning("未开通接口权限");
                                    break;


                                default:

                                    break;
                            }
                        } else {
                            DyToast.getInstance().warning(data.getMessage());
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
     * 国付通 是否注册
     */
    private void gftJoin_Status() {
        HttpFactory.getInstance().gftJoin_status(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mItemCreditcardBean.id + "")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(CreditCardMsgActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {   // 用户已进件
//                            银行卡状态 是否绑卡
                            isSignCard();
                        } else if ("7777".equals(mStatus)) {  // 用户未进件
                            // 进件
                            gftShopCard();
                        } else {
                            DyToast.getInstance().warning(data.getMessage());
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));
    }

    /***
     * 国付通 进件
     */
    private void gftShopCard() {
        HttpFactory.getInstance().gftReg(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mItemCreditcardBean.id + "")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(CreditCardMsgActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            // 用户已进件成功 签约
                            isSignCard();
                        } else {
                            DyToast.getInstance().warning(data.getMessage());
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
     * 国付通 是否签约
     */
    private void isSignCard() {
        HttpFactory.getInstance().gftIsBindCrad(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mItemCreditcardBean.id + "")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(CreditCardMsgActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            // 绑卡成功
                            setResultData();
                        } else if ("8888".equals(mStatus)) {
                            showBindSmsDialog();
                        } else {
                            DyToast.getInstance().warning(data.getMessage());
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
     * 绑卡 验证码
     */
    private void showBindSmsDialog() {
        DialogUtils.getInstance()
                .with(this)
                .setlayoutPosition(Gravity.CENTER)
                .setlayoutPading(150, 0, 150, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FROM_BOTTOM)
                .setlayoutId(R.layout.dialog_bind_code)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    view.findViewById(R.id.tv_get_code).setVisibility(View.GONE);
                    EditText etCode = view.findViewById(R.id.et_code);
                    TextView tvPhone = view.findViewById(R.id.tv_phone);
                    tvPhone.setText(TextUtils.isEmpty(mItemCreditcardBean.phone) ? "" : mItemCreditcardBean.phone);

                    view.findViewById(R.id.tv_submit).setOnClickListener(v -> {
                        String smsCode = etCode.getText().toString().trim();
                        if (TextUtils.isEmpty(smsCode)) {
                            DyToast.getInstance().warning("请输入验证码!");
                        } else {
                            cftSmsConfirm(smsCode);
                            DialogUtils.dismiss();
                        }
                    });
                })
                .show();
    }

    /**
     * 国付通绑卡确认
     *
     * @param smsCode
     */
    private void cftSmsConfirm(String smsCode) {
        HttpFactory.getInstance().gftConfirmSms(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mItemCreditcardBean.id + "", smsCode)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(CreditCardMsgActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            // 绑卡成功
                            setResultData();
                        } else {
                            DyToast.getInstance().warning(data.getMessage());
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));
    }


    /////////////////////////////// 一卡多还 end ////////////////////////




    /**
     * 检测通道是否注册  <一卡多还>
     */
    private void join_Status() {
        HttpFactory.getInstance().join_status(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), "cjmin")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(CreditCardMsgActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {   // 用户已进件
//                            银行卡状态 是否绑卡
                            checkCard();
                        } else if ("7777".equals(mStatus)) {  // 用户未进件
                            ShopInputBean bean = new ShopInputBean();
                            bean.type = "oneMoreRepay";
                            bean.channle = "cjmin";
                            startAct(ShopInputActivity.class, bean);
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
     * 检测用户是否绑卡 <一卡多还>
     */
    private void checkCard() {
        HttpFactory.getInstance().cardpaymentStatus(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), "cjmin", mItemCreditcardBean.id + "")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(CreditCardMsgActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<CardpaymentBean>>() {
                    @Override
                    public void success(HttpResponseData<CardpaymentBean> data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            CardpaymentBean cardpaymentBean = data.getData();
                            String changjieStatus = cardpaymentBean.changjieStatus;
                            if ("2".equals(changjieStatus)) {
                                // 绑卡成功
                                if ("oneMore".equals(mType)) {
                                    setResultData();
                                }
                            } else {
                                showBindSmsDialog(cardpaymentBean);
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
     * 返回上一层
     */
    private void setResultData() {
        String bank_num = mItemCreditcardBean.bank_num;
        Intent i = new Intent();
        i.putExtra("cardId", mItemCreditcardBean.id + "");
        i.putExtra("cardInfo", mItemCreditcardBean.bank_name + "  尾号(" + bank_num.substring(bank_num.length() - 4) + ")");
        setResult(3, i);
        finish();
    }

    /**
     * 绑卡 验证码 <一卡多还>
     *
     * @param cardpaymentBean
     */
    private void showBindSmsDialog(CardpaymentBean cardpaymentBean) {
        DialogUtils.getInstance()
                .with(this)
                .setlayoutPosition(Gravity.CENTER)
                .setlayoutPading(150, 0, 150, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FROM_BOTTOM)
                .setlayoutId(R.layout.dialog_bind_code)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    EditText etCode = view.findViewById(R.id.et_code);
                    TextView tvPhone = view.findViewById(R.id.tv_phone);
                    tvPhone.setText(TextUtils.isEmpty(cardpaymentBean.phone) ? "" : cardpaymentBean.phone);

                    view.findViewById(R.id.tv_submit).setOnClickListener(v -> {
                        String smsCode = etCode.getText().toString().trim();
                        if (TextUtils.isEmpty(smsCode)) {
                            DyToast.getInstance().warning("请输入验证码!");
                        } else {
                            cjConfirm(cardpaymentBean.orderNo, smsCode);
                            DialogUtils.dismiss();
                        }
                    });
                })
                .show();
    }

    /**
     * 畅捷绑卡 <一卡多还>
     *
     * @param orderNo
     * @param smsCode
     */
    private void cjConfirm(String orderNo, String smsCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        map.put("id", mItemCreditcardBean.id + "");
        map.put("orderNo", orderNo);
        map.put("smsCode", smsCode);
        HttpFactory.getInstance().cjConfirmCard(map)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(CreditCardMsgActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            if ("oneMore".equals(mType)) {
                                setResultData();
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

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        mItemCreditcardBean = mAdapter.getData().get(position);
        if (1 == mItemCreditcardBean.repayment_status) {     // 查看计划
            if (1 == mItemCreditcardBean.repayment_type) {         // 普通还款计划
                // 查看计划
                PlanDetailBean bean = new PlanDetailBean();
                bean.planType = 1;
                bean.type = "";
                bean.creditId = mItemCreditcardBean.id + "";
                bean.repayment_id=mItemCreditcardBean.repayment_id;
                startAct(PlanDetailActivity.class, bean);
            } else {
                DyToast.getInstance().warning("该计划不是智能还款制定,请选择其他查看方式");
            }
        } else if ((0 == mItemCreditcardBean.repayment_status)) {          // 制定计划
            mItemCreditcardBean.channel = mChannelType;
            if ("oneMore".equals(mType)) {      // 这个参数是 一卡还多卡 跳进来的,不需要制定计划
                return;
            }
            if ("OtherRepay".equals(mType)) {   // 非畅捷通道 <环迅通道>
                startAct(EnactPlanActivity.class, mItemCreditcardBean);
            } else if ("jft".equals(mType)) {
                String code = mChannelType.replace("jft", "");
                code = code.replace("Jft", "");

                jftIsJoin(code,"手动还款",mItemCreditcardBean.card_bank_num);

            } else if (TextUtils.equals("Ds", mType)) {
                checkDsIsCard();
            } else if (TextUtils.equals("Kjt", mType)) {
                checkKjtIsCard();
            } else {                                // 畅捷通道  检测是否绑卡
                checkCjCard();
            }
        }
    }




    /**
     * 得仕 是否开卡
     */
    private void checkDsIsCard() {
        HttpFactory.getInstance().dsIsCard(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mItemCreditcardBean.id + "")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(CreditCardMsgActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            // 开卡成功
                            startAct(EnactPlanActivity.class, mItemCreditcardBean);
                        } else if ("8888".equals(data.status)) {
                            dsOpenCard();
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
     * 得仕开卡
     */
    private void dsOpenCard() {
        ip = TextUtils.isEmpty(ip) ? NetworkUtils.getIPAddress(true) : ip;
        HttpFactory.getInstance().dsOpenCard(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""),
                mItemCreditcardBean.id + "", ip)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(CreditCardMsgActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<DsBindCardData>>() {
                    @Override
                    public void success(HttpResponseData<DsBindCardData> data) {
                        if ("9999".equals(data.status)) {
                            // 开卡成功
                            startAct(EnactPlanActivity.class, mItemCreditcardBean);
                        } else if ("8888".equals(data.status)) {
                            DsBindCardData cardData = data.getData();
                            startAct(getAty(), BaseWebViewActivity.class, new BaseWebViewData("签约", cardData.html, false));
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

    private void getIP() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://ip-api.com/json/?lang=zh-CN").get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ip = NetworkUtils.getIPAddress(true);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String json = response.body().string();
                    LogUtils.e(json);
                    IpBean ipBean = new Gson().fromJson(json, IpBean.class);
                    if (TextUtils.equals(ipBean.status, "success")) {
                        ip = ipBean.query;
                    } else {
                        ip = NetworkUtils.getIPAddress(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ip = NetworkUtils.getIPAddress(true);
                }
            }
        });
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_recycler;
    }


    @Override
    public void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mSmartRefresh.setBackgroundColor(getResources().getColor(R.color.bg_color));
        SelectCreditBean creditBean = (SelectCreditBean) getIntentData();
        if (null != creditBean) {
            mCreditId = creditBean.creditId;
            mType = creditBean.type;
            mChannelType = creditBean.channelType;
            mRepaymentMode = creditBean.repaymentMode;
        }
        if ("oneMore".equals(mType)) {
            initTitle("选择消费信用卡", R.mipmap.nav_add);
            mAdapter.setVisibility(true);
        } else {
            initTitle("信用卡管理", R.mipmap.nav_add);
        }
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.no_card_empety, null);
        inflate.findViewById(R.id.tv_add_credit).setOnClickListener(v -> rightClick());
        mAdapter.setEmptyView(inflate);

        getIP();
    }


    @Override
    public void doBusiness() {
        requestCreditCardList(true);

        mSmartRefresh.setOnRefreshListener(refreshLayout -> requestCreditCardList(true));

        mSmartRefresh.setOnLoadMoreListener(refreshLayout -> requestCreditCardList(false));
    }


    @Override
    public void rightClick() {
        checkBindCredit();
    }


    //************************************jft*******************end***********************************


    /**
     * 检测是否支付首次绑信用卡
     */
    private void checkBindCredit() {
        startAct(this, AddCreditCardActivity.class);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }









    ////////////////////////////////////////////////////// 快捷通 /////////////////////////////

    /**
     * 快捷通 是否绑卡
     */
    private void checkKjtIsCard() {
        HttpFactory.getInstance().kjtIsBindCard(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mItemCreditcardBean.id + "")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("8888".equals(data.getStatus())) {
                            //用户未绑卡
                            kjtBankCard(true);
                        } else if ("9999".equals(data.getStatus())) {
                            //用户已绑卡 制定计划
                            startAct(EnactPlanActivity.class, mItemCreditcardBean);
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
     * 快捷通 绑卡
     *
     * @param isBind
     */
    private void kjtBankCard(boolean isBind) {
        HttpFactory.getInstance().kjtSendSms(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mItemCreditcardBean.id + "")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<JftSmsBean>() {
                    @Override
                    public void success(JftSmsBean data) {
                        if ("8888".equals(data.status)) {
                            tradeNo = data.info.tradeno;
                            //用户未绑卡
                            if (isBind && mCount != null) {
                                mCount.startCount();
                            } else {
                                showKjtSmsDialog(data.info);
                            }
                        } else if ("9999".equals(data.status)) {
                            //用户已绑卡 制定计划
                            startAct(EnactPlanActivity.class, mItemCreditcardBean);
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
     * 快捷通 弹出短信
     *
     * @param info
     */
    private void showKjtSmsDialog(JftSmsBean.InfoBean info) {
        DialogUtils.getInstance()
                .with(this)
                .setlayoutPosition(Gravity.CENTER)
                .setlayoutPading(150, 0, 150, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FROM_BOTTOM)
                .setlayoutId(R.layout.dialog_bind_code)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    EditText etCode = view.findViewById(R.id.et_code);
                    TextView tvPhone = view.findViewById(R.id.tv_phone);
                    TextView tvGetCode = view.findViewById(R.id.tv_get_code);
                    mCount = new CountDownUtil(tvGetCode);
                    mCount.startCount();
                    tvPhone.setText(TextUtils.isEmpty(info.phone) ? "" : info.phone);
                    tvGetCode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            kjtBankCard(true);
                        }
                    });
                    view.findViewById(R.id.tv_submit).setOnClickListener(v -> {
                        String smsCode = etCode.getText().toString().trim();
                        if (TextUtils.isEmpty(smsCode)) {
                            DyToast.getInstance().warning("请输入验证码!");
                        } else {
                            kjtConfirm(info.tradeno, smsCode);
                            DialogUtils.dismiss();
                        }
                    });
                })
                .show();
    }

    /**
     * 确认绑卡
     *
     * @param tradeno
     * @param smsCode
     */
    private void kjtConfirm(String tradeno, String smsCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        map.put("cardid", mItemCreditcardBean.id + "");
        map.put("smscode", smsCode);
        map.put("tradeno", tradeno);
        HttpFactory.getInstance().kjtBindCard(map)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            //用户已绑卡 制定计划
                            startAct(EnactPlanActivity.class, mItemCreditcardBean);
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

    ////////////////////////////////////////// 快捷通  end /////////////////



}
