package com.dykj.youfeng.mine.yue.cash;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.dialog.PayPassDialog;
import com.dykj.youfeng.home.repayment.activity.AddDepositCardActivity;
import com.dykj.youfeng.mode.CashApplyBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.view.PayPassView;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.DialogUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 提现
 */
public class TakeOutActivity extends BaseActivity {
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tv_add_card)
    TextView tvAddCard;
    @BindView(R.id.tv_fee_moey)
    TextView tvFeeMoey;
    @BindView(R.id.tv_all_cash_money)
    TextView tvAllCashMoney;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.dot_horizontal)
    LinearLayout dotHorizontal;
    @BindView(R.id.rl_card)
    RelativeLayout rlCard;
    @BindView(R.id.tv_info)
    TextView tvInfo;


    private boolean isExistCrad;  // 是否有储蓄卡
    private String memberCashFee;
    private String memberCashTax;

    private TakeOutAdapter mAdapter;
    private List<CashApplyBean.DebitCardBean> debitCardList;
    private String mCashMinMoney;

    @Override
    public int intiLayout() {
        return R.layout.activity_take_out;
    }

    @Override
    public void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initTitle("提现", "提现记录");
    }

    @Override
    public void doBusiness() {
        requestCashApply();
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
                double skMoney = Float.parseFloat(money);
                if (null != memberCashFee && null != memberCashTax && skMoney > 0) {
                    tvFeeMoey.setText("¥" + (skMoney * (Double.parseDouble(memberCashTax) / 100) + Double.parseDouble(memberCashFee)));
                } else {
                    tvFeeMoey.setText("¥0.00");
                }
            }
        });
    }


    @Override
    public void rightClick() {
        startAct(TakeOutLogActivity.class);
    }


    @OnClick({R.id.tv_add_card, R.id.tv_submit})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.tv_add_card:   // 添加储蓄卡
                startAct(AddDepositCardActivity.class);
                break;


            case R.id.tv_submit:  // 提现
                if (!isExistCrad) {
                    showAddDepositCardDialog();
                    return;
                }
                String cashMoney = etMoney.getText().toString().trim();
                double cashMinMoey = TextUtils.isEmpty(mCashMinMoney) ? 0.0 : Double.parseDouble(mCashMinMoney);
                if (!TextUtils.isEmpty(cashMoney)) {
                    double money = Double.parseDouble(cashMoney);
                    if (money < cashMinMoey) {
                        DyToast.getInstance().warning("提现金额过低,请重新输入!");
                        return;
                    }
                    final PayPassDialog dialog = new PayPassDialog(this);
                    dialog.getPayViewPass()
                            .setPayClickListener(new PayPassView.OnPayClickListener() {
                                @Override
                                public void onPassFinish(String passContent) {
                                    //6位输入完成回调
                                    submitCash(cashMoney, passContent);
                                }

                                @Override
                                public void onPayClose() {
                                    dialog.dismiss();
                                    //关闭回调
                                }
                            });
                } else {
                    DyToast.getInstance().warning("请输入提现金额!");
                }
                break;
        }
    }

    /**
     * 添加储蓄卡 dialog
     */
    private void showAddDepositCardDialog() {
        DialogUtils.getInstance()
                .with(this)
                .setlayoutPosition(Gravity.CENTER)
                .setlayoutPading(140, 0, 140, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FADE_IN)
                .setlayoutId(R.layout.dialog_add_card)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    TextView tvtitle = view.findViewById(R.id.tv_tile);
                    tvtitle.setText("系统检测到您暂无添加储蓄卡");
                    view.findViewById(R.id.tv_cancel).setOnClickListener(v -> DialogUtils.dismiss());
                    TextView tvQd = view.findViewById(R.id.tv_safety_quit);
                    tvQd.setText("添加一张?");
                    tvQd.setOnClickListener(v -> {
                        DialogUtils.dismiss();
                        startAct(AddDepositCardActivity.class);
                    });
                })
                .show();

    }

    /**
     * 申请提现
     *
     * @param cashMoney
     */
    private void submitCash(String cashMoney, String psw) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        map.put("money", cashMoney);
        map.put("pass", psw);
        map.put("debitid", debitCardList.get(mAdapter.getCurrentItemPotision()).id);
        HttpFactory.getInstance().addCash(map)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(TakeOutActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            DyToast.getInstance().success(data.message);
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
     * 提现申请
     */
    private void requestCashApply() {
        HttpFactory.getInstance().cashApply(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""))
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(TakeOutActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<CashApplyBean>>() {
                    @Override
                    public void success(HttpResponseData<CashApplyBean> data) {
                        if ("9999".equals(data.status)) {
                            tvAddCard.setVisibility(View.GONE);
                            rlCard.setVisibility(View.VISIBLE);
                            isExistCrad = true;

                            CashApplyBean cashApplyBean = data.getData();
                            setViewData(cashApplyBean);

                        } else {
                            // 未绑定储蓄卡
                            String message = data.message;
                            if (message.contains("未绑定储蓄卡")) {
                                tvAddCard.setVisibility(View.VISIBLE);
                                rlCard.setVisibility(View.GONE);
                                isExistCrad = false;
                            }
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
     * 设置view 数据
     *
     * @param cashApplyBean
     */
    private void setViewData(CashApplyBean cashApplyBean) {

        mCashMinMoney = cashApplyBean.cashMinMoney;
        tvAllCashMoney.setText(cashApplyBean.accountMoney);
        tvInfo.setText("温馨提示：" + cashApplyBean.info);
        memberCashFee = cashApplyBean.memberCashFee;
        memberCashTax = cashApplyBean.memberCashTax;

        debitCardList = cashApplyBean.debit_card;
        mAdapter = new TakeOutAdapter(debitCardList, this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new PageIndicator(this, dotHorizontal, debitCardList.size()));
    }

    @Subscribe
    public void onEvent(String event) {
        if (!TextUtils.isEmpty(event) && event.equals(AppCacheInfo.mRefreshDebitcardList)) {
            requestCashApply();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
