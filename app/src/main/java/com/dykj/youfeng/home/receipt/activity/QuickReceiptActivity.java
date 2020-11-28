package com.dykj.youfeng.home.receipt.activity;

import android.content.Intent;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dykj.youfeng.R;
import com.dykj.youfeng.home.receipt.adapter.QuickReceiptChannelAdapter;
import com.dykj.youfeng.home.repayment.activity.AddCreditCardActivity;
import com.dykj.youfeng.home.repayment.activity.AddDepositCardActivity;
import com.dykj.youfeng.home.repayment.activity.EnactPlanActivity;
import com.dykj.youfeng.home.repayment.activity.ShopInputActivity;
import com.dykj.youfeng.home.repayment.activity.ShopInputJftActivity;
import com.dykj.youfeng.mode.CardpaymentBean;
import com.dykj.youfeng.mode.CjSkInfoBean;
import com.dykj.youfeng.mode.CreditcardListBean;
import com.dykj.youfeng.mode.DebitcardListBean;
import com.dykj.youfeng.mode.JftSmsBean;
import com.dykj.youfeng.mode.QuickChannelBean;
import com.dykj.youfeng.mode.ShopInputBean;
import com.dykj.youfeng.mode.SmsBean;
import com.dykj.youfeng.mode.YbIntentData;
import com.dykj.youfeng.mode.YbSkSubmitBean;
import com.dykj.youfeng.mode.receipt.JftpayCardBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.activity.WebViewActivity;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.entity.BaseWebViewData;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.CountDownUtil;
import com.dykj.module.util.DialogUtils;
import com.dykj.module.util.GlideUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 快捷收款
 */
public class QuickReceiptActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.tv_receipt)
    TextView tvReceipt;
    @BindView(R.id.tv_rmb_res)
    TextView tvRmbRes;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.iv_credit_logo)
    ImageView ivCreditLogo;
    @BindView(R.id.iv_credit_name)
    TextView ivCreditName;
    @BindView(R.id.iv_credit_number)
    TextView ivCreditNumber;
    @BindView(R.id.ll_credit_info)
    LinearLayout llCreditInfo;
    @BindView(R.id.ll_add_credit)
    LinearLayout llAddCredit;
    @BindView(R.id.ll_credit)
    LinearLayout llCredit;
    @BindView(R.id.iv_crad_logo)
    ImageView ivCradLogo;
    @BindView(R.id.tv_crad_name)
    TextView tvCradName;
    @BindView(R.id.tv_crad_number)
    TextView tvCradNumber;
    @BindView(R.id.ll_crad_info)
    LinearLayout llCradInfo;
    @BindView(R.id.ll_add_crad)
    LinearLayout llAddCrad;
    @BindView(R.id.ll_crad)
    LinearLayout llCrad;
    private String type;
    private ImageView ivWx, ivali, ivAccount;
    private static final int SDK_PAY_FLAG = 200;

    private QuickReceiptChannelAdapter mAdapter = new QuickReceiptChannelAdapter(R.layout.item_receipt_channel);
        //储蓄卡
    DebitcardListBean debitcardListBean;

    private String mCreditId;
    private String mDebitId;
    private List<CreditcardListBean.ListBean> mCreditcardList;    // 信用卡列表
    private List<DebitcardListBean> mDebitcardList;  // 储蓄卡列表
    private QuickReceiptSelectCreditDialogAdapter mCreditDialogAdapter;
    private QuickReceiptSelectDebitDialogAdapter mDebitDialogAdapter;

    private QuickChannelBean.ChannelBean mItemSelectedChannel;

    @Override
    public int intiLayout() {
        return R.layout.activity_quick_receipt;
    }

    @Override
    public void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initTitle("快捷收款", "收款记录");
        mRecycler.setNestedScrollingEnabled(false);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.layout_empty, mRecycler);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            //查看支持银行
            String type = mAdapter.getData().get(position).type;
            if (type.contains("yb") || type.contains("Yb")) {    // 易宝
                startAct(YbBankListActivity.class,type);
            }else if (type.contains("Jft") || type.contains("jft")) {
                //||TextUtils.equals("Jft",bean.type)
                startAct(YbBankListActivity.class,"jft");
            } else {            // 畅捷
                startAct(CjBankListActivity.class,type);

            }

        });

        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String money = etMoney.getText().toString();
                if (TextUtils.isEmpty(money)) {
                    money = "0";
                }
                mAdapter.setMoney(money);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        etMoney.setHint("0.00");
    }

    @Override
    public void doBusiness() {
        requestQuickChanne();
    }


    @Override
    public void rightClick() {
        // 收款记录
        startAct(QuickReceiptLogActivity.class);
    }


    @OnClick({R.id.ll_credit, R.id.ll_crad, R.id.ll_add_crad, R.id.ll_add_credit})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.ll_credit:
                if (null != mCreditcardList) {
                    showCreditCardDialog();
                }
                break;

            case R.id.ll_crad:
                if (null != mDebitcardList) {
                    showDebitcardDialog();
                }
                break;

            case R.id.ll_add_crad:  // 添加储蓄卡
                startAct(AddDepositCardActivity.class);
                break;

            case R.id.ll_add_credit:  // 添加信用卡
                checkBindCredit();
                break;
        }
    }


    /**
     * 渠道通道列表
     */
    private void requestQuickChanne() {
        HttpFactory.getInstance().quickChannelList(getUserToken())
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(QuickReceiptActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<QuickChannelBean>>() {
                    @Override
                    public void success(HttpResponseData<QuickChannelBean> data) {
                        if ("9999".equals(data.status)) {
                            setViewData(data);
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
     * 设置 view 数据
     *
     * @param data
     */
    private void setViewData(HttpResponseData<QuickChannelBean> data) {
        List<QuickChannelBean.ChannelBean> channelBeanList = new ArrayList<>();
        QuickChannelBean channelBean = data.getData();
     List<QuickChannelBean.ChannelBean> channel = channelBean.channel;
/*       QuickChannelBean.ChannelBean b=new QuickChannelBean.ChannelBean();
        b.name="花呗";
        b.content="光大、招商、交通、邮政、兴业、浦发";
        b.rate="6";
        b.time="8:00-20:00";
        b.type="hb";
        b.status="1";
        b.minMoney="1";
        b.maxMoney="10000";
        b.cost="2";

        channel.add(b);*/
        // 过滤出已开启通道
        for (int i = 0; i < channel.size(); i++) {
            QuickChannelBean.ChannelBean bean = channel.get(i);
            if ("1".equals(bean.status)) {
                //||TextUtils.equals("Jft",bean.type)
                channelBeanList.add(bean);
            }
        }

        if (channelBeanList.size() > 0) {    // 默认选中第一条通道
            channelBeanList.get(0).isCheck = true;
            mItemSelectedChannel = channelBeanList.get(0);
        }
        mAdapter.setNewData(channelBeanList);

        //信用卡
        Object credit_card = channelBean.credit_card;
        if (null != credit_card) {
            try {
                llCreditInfo.setVisibility(View.VISIBLE);
                llAddCredit.setVisibility(View.GONE);
                Map<String, Object> creditMap = (Map<String, Object>) credit_card;
                mCreditId = creditMap.get("id") + "";
                String logo = (String) creditMap.get("logo");
                String creditBankNumber = (String) creditMap.get("bank_num");
                ivCreditName.setText((String) creditMap.get("bank_name"));
                ivCreditNumber.setText("(" + creditBankNumber.substring(creditBankNumber.length() - 4) + ")");
                GlideUtils.setImage(ivCreditLogo, logo);
                requestCreditList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            llCreditInfo.setVisibility(View.GONE);
            llAddCredit.setVisibility(View.VISIBLE);
        }

        // 储蓄卡
        Object debit_card = channelBean.debit_card;
        if (null != debit_card) {
            try {
                llCradInfo.setVisibility(View.VISIBLE);
                llAddCrad.setVisibility(View.GONE);
                Map<String, Object> debitMap = (Map<String, Object>) debit_card;
                mDebitId = debitMap.get("id") + "";
                String logo = (String) debitMap.get("logo");
                String creditBankNumber = (String) debitMap.get("bank_num");
                tvCradName.setText((String) debitMap.get("bank_name"));

                tvCradNumber.setText("(" + creditBankNumber.substring(creditBankNumber.length() - 4) + ")");
                debitcardListBean=new DebitcardListBean();
                debitcardListBean.bank_name=(String)debitMap.get("bank_name");
                debitcardListBean.bank_num=creditBankNumber;
                debitcardListBean.bank_number=(String)debitMap.get("bank_number");

                GlideUtils.setImage(ivCradLogo, logo);
                requestDebitCardList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            llCradInfo.setVisibility(View.GONE);
            llAddCrad.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 请求信用卡列表
     */
    private void requestCreditList() {
        HttpFactory.getInstance().creditcardList(getUserToken(), "2", "1", "20")
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<CreditcardListBean>>() {
                    @Override
                    public void success(HttpResponseData<CreditcardListBean> data) {
                        if ("9999".equals(data.status)) {
                            CreditcardListBean data1 = data.getData();
                            if (null != data1) {
                                mCreditcardList = data1.list;
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

    /***
     * 请求储蓄卡列表
     */
    private void requestDebitCardList() {
        HttpFactory.getInstance().debitcardList(getUserToken(), "2", "1")
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<List<DebitcardListBean>>>() {
                    @Override
                    public void success(HttpResponseData<List<DebitcardListBean>> data) {
                        if ("9999".equals(data.status)) {
                            mDebitcardList = data.getData();
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
     * 信用卡列表 弹框
     */
    private void showCreditCardDialog() {
        DialogUtils.getInstance()
                .with(this)
                .setlayoutPosition(Gravity.BOTTOM)
                .setlayoutPading(0, 0, 0, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FADE_IN)
                .setlayoutId(R.layout.dialog_select_card)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    view.findViewById(R.id.view).setOnClickListener(v -> DialogUtils.dismiss());
                    TextView tvTitle = view.findViewById(R.id.tv_title);
                    tvTitle.setText("选择信用卡");
                    RecyclerView mDialogRecycler = view.findViewById(R.id.mRecyclerView);
                    mCreditDialogAdapter = new QuickReceiptSelectCreditDialogAdapter(R.layout.item_gather_dialog);
                    mDialogRecycler.setAdapter(mCreditDialogAdapter);
                    mCreditDialogAdapter.setNewData(mCreditcardList);
                })
                .show();

        if (null != mCreditDialogAdapter) {
            mCreditDialogAdapter.setOnItemClickListener((adapter, view, position) -> {
                CreditcardListBean.ListBean listBean = mCreditDialogAdapter.getData().get(position);
                mCreditId = listBean.id + "";
                ivCreditName.setText(listBean.bank_name);
                ivCreditNumber.setText("(" + listBean.bank_num + ")");
                GlideUtils.setImage(ivCreditLogo, listBean.logo);
                DialogUtils.dismiss();
            });
        }
    }


    /**
     * 储蓄卡列表 弹框
     */
    private void showDebitcardDialog() {
        DialogUtils.getInstance()
                .with(this)
                .setlayoutPosition(Gravity.BOTTOM)
                .setlayoutPading(0, 0, 0, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FADE_IN)
                .setlayoutId(R.layout.dialog_select_card)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    view.findViewById(R.id.view).setOnClickListener(v -> DialogUtils.dismiss());
                    TextView tvTitle = view.findViewById(R.id.tv_title);
                    tvTitle.setText("选择储蓄卡");
                    RecyclerView mDialogRecycler = view.findViewById(R.id.mRecyclerView);
                    mDebitDialogAdapter = new QuickReceiptSelectDebitDialogAdapter(R.layout.item_gather_dialog);
                    mDialogRecycler.setAdapter(mDebitDialogAdapter);
                    mDebitDialogAdapter.setNewData(mDebitcardList);
                })
                .show();

        if (null != mDebitDialogAdapter) {
            mDebitDialogAdapter.setOnItemClickListener((adapter, view, position) -> {
                 DebitcardListBean debitcardListBeans = mDebitDialogAdapter.getData().get(position);
                 debitcardListBean=debitcardListBeans;
                String bank_num = debitcardListBean.bank_num;
                mDebitId = debitcardListBean.id + "";
                tvCradName.setText(debitcardListBean.bank_name);
                tvCradNumber.setText("(" + bank_num.substring(bank_num.length() - 4) + ")");
                GlideUtils.setImage(ivCradLogo, debitcardListBean.logo);
                DialogUtils.dismiss();
            });
        }
    }

    /**
     * 检测是否支付首次绑信用卡
     */
    private void checkBindCredit() {
        startAct(this, AddCreditCardActivity.class);
    }


    @Subscribe
    public void onEvent(String onEvent) {
        if (TextUtils.isEmpty(onEvent)) {
            return;
        }
        if (AppCacheInfo.mRefreshDebitcardList.equals(onEvent)) {
            requestQuickChanne();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<QuickChannelBean.ChannelBean> channelList = mAdapter.getData();
        for (int i = 0; i < channelList.size(); i++) {
            channelList.get(i).isCheck = false;
        }
        mItemSelectedChannel = channelList.get(position);
        mItemSelectedChannel.isCheck = true;
        mAdapter.setNewData(channelList);

    }


    /**
     * 立即收款
     */
    @OnClick(R.id.tv_receipt)
    public void onClickview() {
        QuickChannelBean.ChannelBean bean= mAdapter.getChanne();

        if(bean.type.equals("hb")){

            payHabei(bean);
        }else {
            String mMoney = etMoney.getText().toString().trim();
            if (TextUtils.isEmpty(mMoney)) {
                DyToast.getInstance().warning("请输入收款金额");
                return;
            }
            if (Float.parseFloat(mMoney) < Float.parseFloat(mItemSelectedChannel.minMoney)) {
                DyToast.getInstance().warning("收款金额不能低于通道最低消费!");
                return;
            }

            if (Float.parseFloat(mMoney) > Float.parseFloat(mItemSelectedChannel.maxMoney)) {
                DyToast.getInstance().warning("收款金额不能大于通道最高消费!");
                return;
            }

            if (null == mCreditcardList) {     // 信用卡列表
                showAddCrad("系统检测到您暂无添加信用卡", "credit");
                return;
            }

            if (null == mDebitcardList) {
                showAddCrad("系统检测到您暂无添加储蓄卡", "bank");
                return;
            }

            String mChannelType = mItemSelectedChannel.type;
            if (TextUtils.isEmpty(mChannelType)) {
                return;
            }

            if (mChannelType.contains("cj") || mChannelType.contains("Cj")) {   // 判断畅捷是否进件
                checkCj(mChannelType);
            } else if (mChannelType.contains("yb") || mChannelType.contains("Yb")) {   // 易宝收款
                checkBy(mChannelType);
            } else if (mChannelType.contains("Jft") || mChannelType.contains("jft")) {   //
                //20200310佳付通收款
                jftIsJoin("3004");
            } else if (mChannelType.contains("kjt") || mChannelType.contains("Kjt")) {
                // 快捷通
//            checkKjtIsCard();
                kjtGoConfirmInfo();
            }
        }
    }


    /**
     * 花呗收款
     * @param bean
     */
    private void payHabei( QuickChannelBean.ChannelBean bean){
        String mMoney = etMoney.getText().toString().trim();
        if (TextUtils.isEmpty(mMoney)) {
            DyToast.getInstance().warning("请输入收款金额");
            return;
        }
        if (Float.parseFloat(mMoney) < Float.parseFloat(mItemSelectedChannel.minMoney)) {
            DyToast.getInstance().warning("收款金额不能低于通道最低消费!");
            return;
        }

        if (Float.parseFloat(mMoney) > Float.parseFloat(mItemSelectedChannel.maxMoney)) {
            DyToast.getInstance().warning("收款金额不能大于通道最高消费!");
            return;
        }
        if (null == mDebitcardList) {
            showAddCrad("系统检测到您暂无添加储蓄卡", "bank");
            return;
        }

        Intent intent=new Intent(QuickReceiptActivity.this,HabeiPayActivity.class);
        intent.putExtra("data",mDebitId);
        intent.putExtra("money",mMoney);
        intent.putExtra("data2",  mAdapter.getChanne());
        startActivity(intent);


    }


    /**
     * 检测易宝 是否鉴权
     *
     * @param mChannelType
     */
    private void checkBy(String mChannelType) {
        HttpFactory.getInstance().ybCheck(getUserToken())
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            ybSumbit();
                        } else if ("1002".equals(data.status)) {
                            YbIntentData ybIntentData = new YbIntentData();
                            ybIntentData.crediteid = mCreditId;
                            ybIntentData.debitid = mDebitId;
                            ybIntentData.money = etMoney.getText().toString().trim();
                            startAct(YbShopInfoActivity.class, ybIntentData);
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
     * 易宝收款
     */
    private void ybSumbit() {
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        map.put("token", getUserToken());
        map.put("crediteid", mCreditId);
        map.put("debitid", mDebitId);
        map.put("amount", etMoney.getText().toString().trim());
        HttpFactory.getInstance().ybSkSumbit(map)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<YbSkSubmitBean>>() {
                    @Override
                    public void success(HttpResponseData<YbSkSubmitBean> data) {
                        if ("9999".equals(data.status)) {
                            BaseWebViewData webViewData = new BaseWebViewData();
                            webViewData.title = "消费申请";
                            webViewData.content = data.getData().url;
                            webViewData.isOut = true;
                            startAct(WebViewActivity.class, webViewData);
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
     * 检测畅捷状态
     *
     * @param mChannelType
     */
    private void checkCj(String mChannelType) {
        String code = MMKV.defaultMMKV().decodeString(AppCacheInfo.merchantCode, "");
        if ("0".equals(code)) {   //   未进件~    畅捷进件 0.未进件 其他为已进件
            ShopInputBean bean = new ShopInputBean();
            bean.type = "replaceRepay";
            bean.channle = mChannelType;
            startAct(ShopInputActivity.class, bean);
        } else {  // 已进件,判断开卡状态
            checkCjCard(mChannelType);
        }
    }


    /**
     * 畅捷 大小额是否绑卡
     *
     * @param mChannelType
     */
    private void checkCjCard(String mChannelType) {
        ConcurrentHashMap<String, Object> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put("token", getUserToken());
        concurrentHashMap.put("cardId", mCreditId + "");   // 卡片Id
        concurrentHashMap.put("channel", mChannelType);
        HttpFactory.getInstance().changjieCard(concurrentHashMap)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<CardpaymentBean>>() {
                    @Override
                    public void success(HttpResponseData<CardpaymentBean> data) {
                        if ("9999".equals(data.status)) {
                            // 开卡成功
                            cjApply(mChannelType);
                        } else if ("8888".equals(data.status)) {
                            CardpaymentBean data1 = data.getData();
                            showBindSmsDialog2(data1, mChannelType);
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
    private void showBindSmsDialog2(CardpaymentBean cardpaymentBean, String mChannelType) {
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
                            cjConfirm2(cardpaymentBean.orderNo, smsCode, mChannelType);
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
    private void cjConfirm2(String orderNo, String smsCode, String mChannelType) {
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        map.put("token", getUserToken());
        map.put("cardId", mCreditId);
        map.put("orderNo", orderNo);
        map.put("smsCode", smsCode);
        map.put("channel", mChannelType);
        HttpFactory.getInstance().changjiecardconfirm(map)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<SmsBean>>() {
                    @Override
                    public void success(HttpResponseData<SmsBean> data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            String signStatus = data.getData().signStatus;
                            if ("2".equals(signStatus)) {
                                cjApply(mChannelType);
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


    /***
     * 畅捷 收款提交
     * @param mChannelType
     */
    private void cjApply(String mChannelType) {
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        map.put("token", getUserToken());
        map.put("crediteid", mCreditId.replace(".0", ""));
        map.put("debitid", mDebitId.replace(".0", ""));
        map.put("money", etMoney.getText().toString());
        map.put("channel", mChannelType);
        HttpFactory.getInstance().cjSkPlan(map)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<CjSkInfoBean>>() {
                    @Override
                    public void success(HttpResponseData<CjSkInfoBean> data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            CjSkInfoBean cjSkInfoBean = data.getData();
                            cjSkInfoBean.channelType = mChannelType;
                            cjSkInfoBean.crediteid = mCreditId.replace(".0", "");
                            cjSkInfoBean.debitid = mDebitId.replace(".0", "");
                            startAct(QuickReceiptInfoActivity.class, cjSkInfoBean);
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
     * 暂无卡片  提示添加卡片dialog
     */
    private void showAddCrad(final String title, final String mtype) {
        DialogUtils.getInstance()
                .with(this)
                .setlayoutPosition(Gravity.CENTER)
                .setlayoutPading(140, 0, 140, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FADE_IN)
                .setlayoutId(R.layout.dialog_add_card)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    TextView tvtitle = view.findViewById(R.id.tv_tile);
                    tvtitle.setText(title);
                    view.findViewById(R.id.tv_cancel).setOnClickListener(v -> DialogUtils.dismiss());
                    TextView tvQd = view.findViewById(R.id.tv_safety_quit);
                    tvQd.setText("添加一张?");
                    tvQd.setOnClickListener(v -> {
                        DialogUtils.dismiss();
                        if (mtype.equals("credit")) {
                            checkBindCredit();
                        } else if (mtype.equals("bank")) {
                            startAct(AddDepositCardActivity.class);
                        }
                    });
                })
                .show();
    }

    //*****************************jft********************************start*************************************

    /***
     * @param code 佳付通  是否进件
     */
    private void jftIsJoin(final String code) {
        HttpFactory.getInstance()
                .jftIsJoin(getUserToken(), code)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("8888".equals(data.status)) {
                            //用户未进件
                            startAct(getAty(), ShopInputJftActivity.class, code);
                        } else if ("9999".equals(data.getStatus())) {
                            //用户已进件   判断卡的状态
                           // checkJftCardStatus(code);
                            jftGoConfirmInfo();
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

    private void checkJftCardStatus(String code) {
        String cardId=mCreditId.replace(".0", "");
        String token = getUserToken();
        HttpFactory.getInstance().jftIsBindCard(token, cardId, code)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("8888".equals(data.getStatus())) {
                            //用户未绑卡
                            jftApplySms(code, false);
                        } else if ("9999".equals(data.getStatus())) {
                            //用户已绑卡 制定计划 收款
                            jftGoConfirmInfo();
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
    private void jftGoConfirmInfo(){
        JftpayCardBean bean =new JftpayCardBean();
        bean.channelType ="Jft";
        bean.crediteid = mCreditId.replace(".0", "");
        bean.debitid = mDebitId.replace(".0", "");
        bean.money=etMoney.getText().toString();
        startAct(QuickReceiptInfoJftActivity.class, bean);

//        etMoney.setText("");
    }
    private CountDownUtil mCount;
    private String tradeNo = "";
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
    private String getUserToken(){
        return MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
    }
    private void jftApplySms(String code, boolean reGet) {
//        ConcurrentHashMap<String, Object> params=new ConcurrentHashMap<>();
//        params.put("userid",token);
//        params.put("token",token);
//        params.put("cardid",mCreditId);
//        params.put("code","2005");
        //5. 绑卡短信申请 （针对 2003，2005无需绑卡）
    /*userid	是	string	用户id
        cardid	是	string	银行卡id
        code	是	string	类型（2005 大额商旅收款 2003 大额落地快捷）*/
//        HttpFactory.getJftSk().jftApplySms(params)
        HttpFactory.getInstance().jftApplySms(getUserToken(), mCreditId, code)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<JftSmsBean>() {
                    @Override
                    public void success(JftSmsBean data) {
                        if ("8888".equals(data.status)) {
                            tradeNo = data.info.tradeno;
                            //用户未绑卡
                            if (reGet && mCount != null) {
                                mCount.startCount();
                            } else {
                                showJftSmsDialog(code, data.info);
                            }
                        } else if ("9999".equals(data.status)) {
                            //用户已绑卡 制定计划 收款
                            jftGoConfirmInfo();
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
    private void jftConfirm(String code, String sms) {
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>(4);
        map.put("token", getUserToken());
        map.put("cardid", mCreditId);
        map.put("tradeno", tradeNo);
        map.put("smscode", sms);
        map.put("code", code);
        HttpFactory.getInstance().jftApplyConfirm(map)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.getStatus())) {
                            //用户已绑卡 制定计划 收款
                            jftGoConfirmInfo();
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

    //*****************************jft********************************end*************************************




    ////////////////////////////////////////////////////// 快捷通 /////////////////////////////

    /**
     * 快捷通 是否绑卡
     */
    private void checkKjtIsCard() {
        HttpFactory.getInstance().kjtIsBindCard(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mCreditId + "")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("8888".equals(data.getStatus())) {
                            //用户未绑卡
                            kjtBankCard(true);
                        } else if ("9999".equals(data.getStatus())) {
                            //用户已绑卡 收款
                            kjtGoConfirmInfo();
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
        HttpFactory.getInstance().kjtSendSms(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mCreditId + "")
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
                            //用户已绑卡 收款
                            kjtGoConfirmInfo();
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
                    tvGetCode.setOnClickListener(v -> kjtBankCard(true));
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
        map.put("cardid",mCreditId+ "");
        map.put("smscode", smsCode);
        map.put("tradeno", tradeno);
        HttpFactory.getInstance().kjtBindCard(map)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            //用户已绑卡 收款
                            kjtGoConfirmInfo();
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


    private void kjtGoConfirmInfo(){
        JftpayCardBean bean =new JftpayCardBean();
        bean.channelType ="kjt";
        bean.crediteid = mCreditId.replace(".0", "");
        bean.debitid = mDebitId.replace(".0", "");
        bean.money=etMoney.getText().toString();
        startAct(QuickReceiptInfoKjtActivity.class, bean);

//        etMoney.setText("");
    }


    ////////////////////////////////////////// 快捷通  end /////////////////





}
