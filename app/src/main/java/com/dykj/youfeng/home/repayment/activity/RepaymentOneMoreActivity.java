package com.dykj.youfeng.home.repayment.activity;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.DialogUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.SmartRefreshUtils;
import com.dykj.module.util.toast.DyToast;
import com.dykj.youfeng.R;
import com.dykj.youfeng.home.repayment.adapter.CreditCardMsgAdapter;
import com.dykj.youfeng.mode.CardpaymentBean;
import com.dykj.youfeng.mode.CreditcardListBean;
import com.dykj.youfeng.mode.JftSmsBean;
import com.dykj.youfeng.mode.PlanDetailBean;
import com.dykj.youfeng.mode.ShopInputBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 一卡多还  畅捷小额  cjmin
 */
public class RepaymentOneMoreActivity extends BaseActivity {
    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.mSmartRefresh)
    SmartRefreshLayout mSmartRefresh;

    private CreditCardMsgAdapter mAdapter = new CreditCardMsgAdapter(this,R.layout.item_credit_msg);
    private int mPage;

    private CreditcardListBean.ListBean mItemCreditcardBean;


    @Override
    public int intiLayout() {
        return R.layout.activity_recycler;
    }

    @Override
    public void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mSmartRefresh.setBackgroundColor(ContextCompat.getColor(this,R.color.bg_color));
        initTitle("一卡多还", "还款计划");
        mRecycler.setAdapter(mAdapter);

        View inflate = LayoutInflater.from(this).inflate(R.layout.no_card_empety, null);
        View headerView = LayoutInflater.from(this).inflate(R.layout.add_creditcard_header, null);
        headerView.setOnClickListener(v -> startAct(this, AddCreditCardActivity.class));
        inflate.findViewById(R.id.tv_add_credit).setOnClickListener(v -> startAct(this, AddCreditCardActivity.class));
        mAdapter.setEmptyView(inflate);
        mAdapter.addHeaderView(headerView);

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            mItemCreditcardBean = mAdapter.getData().get(position);
            int repayment_status = mItemCreditcardBean.repayment_status;

            if (1 == repayment_status) {     // 查看计划
                if (2 == mItemCreditcardBean.repayment_type) {
                    PlanDetailBean bean = new PlanDetailBean();
                    bean.planType = 2;
                    bean.type = "credit";
                    bean.creditId = mItemCreditcardBean.id + "";
                    startAct(PlanDetailActivity.class, bean);
                } else {
                    DyToast.getInstance().warning("该计划不是一卡多还制定,请选择其他查看方式");
                }
            } else if (0 == repayment_status) {   //  制定计划
//                join_Status();

                checkGft();

            }

        });
    }


    /**
     * 国付通 状态
     */
    private void checkGft() {
        HttpFactory.getInstance().gftChannel(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mItemCreditcardBean.id + "")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(RepaymentOneMoreActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<JftSmsBean>>() {
                    @Override
                    public void success(HttpResponseData<JftSmsBean> data) {
                        if ("9999".equals(data.status)) {   // 用户已进件
                            String code = data.getData().code;
                            switch (code) {
                                case "0":
                                    //不用重新注册，可直接交易
                                    startAct(OneMoreEnactPlanActivity.class, mItemCreditcardBean);
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
                .as(HttpObserver.life(RepaymentOneMoreActivity.this))
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
                .as(HttpObserver.life(RepaymentOneMoreActivity.this))
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
                .as(HttpObserver.life(RepaymentOneMoreActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            // 绑卡成功
                            startAct(OneMoreEnactPlanActivity.class, mItemCreditcardBean);
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
                .as(HttpObserver.life(RepaymentOneMoreActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            // 绑卡成功
                            startAct(OneMoreEnactPlanActivity.class, mItemCreditcardBean);
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


    /////////////////////////////////////// 国付通 end //////////////////

    /**
     * 检测通道是否注册
     */
    private void join_Status() {
        HttpFactory.getInstance().join_status(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), "cjmin")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(RepaymentOneMoreActivity.this))
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
     * 检测用户是否绑卡
     */
    private void checkCard() {
        HttpFactory.getInstance().cardpaymentStatus(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), "cjmin", mItemCreditcardBean.id + "")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(RepaymentOneMoreActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<CardpaymentBean>>() {
                    @Override
                    public void success(HttpResponseData<CardpaymentBean> data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            CardpaymentBean cardpaymentBean = data.getData();
                            String changjieStatus = cardpaymentBean.changjieStatus;
                            if ("2".equals(changjieStatus)) {
                                // 绑卡成功
                                startAct(OneMoreEnactPlanActivity.class, mItemCreditcardBean);
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


    @Override
    public void doBusiness() {
        requestCreditCardList(true);
        mSmartRefresh.setOnRefreshListener(refreshLayout -> requestCreditCardList(true));

        mSmartRefresh.setOnLoadMoreListener(refreshLayout -> requestCreditCardList(false));
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

        HttpFactory.getInstance().creditcardList(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), "2", mPage + "", "10")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(RepaymentOneMoreActivity.this))
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


    /**
     * 绑卡 验证码
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
     * 畅捷绑卡
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
                .as(HttpObserver.life(RepaymentOneMoreActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            startAct(OneMoreEnactPlanActivity.class, mItemCreditcardBean);
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
    public void rightClick() {
        startAct(RepaymentPlanActivity.class, "oneMoreRepayment");
    }

    @Subscribe
    public void onEvent(String event) {
        if (TextUtils.isEmpty(event)) {
            return;
        }
        if (event.equals(AppCacheInfo.mRefreshCreditCardList)
                || event.equals(AppCacheInfo.mSubmitOneMoreRepayMent)
                || event.equals(AppCacheInfo.mCancelPlan)) {
            requestCreditCardList(true);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
