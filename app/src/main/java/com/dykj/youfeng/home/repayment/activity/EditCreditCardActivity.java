package com.dykj.youfeng.home.repayment.activity;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.view.WheelView;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.CreditcardListBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.tools.ArithUtil;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
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

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 编辑信用卡
 */
public class EditCreditCardActivity extends BaseActivity {
    @BindView(R.id.tv_bank_card_num)
    TextView tvBankCardNum;
    @BindView(R.id.tv_credit_card_num)
    TextView tvCreditCardNum;
    @BindView(R.id.et_cvn)
    EditText etCvn;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_validity)
    TextView tvValidity;
    @BindView(R.id.tv_statement_date)
    TextView tvStatementDate;
    @BindView(R.id.tv_repayment_date)
    TextView tvRepaymentDate;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.rl_money)
    RelativeLayout rlMoney;

    private ArrayList<String> dayList;
    private String mRepamentDate, mStatementDate;
    private String mCreditcardId;


    @Override
    public int intiLayout() {
        return R.layout.activity_edit_credit;
    }

    @Override
    public void initData() {
        initTitle("编辑信用卡");
        initdayList();
    }

    @Override
    public void doBusiness() {
        CreditcardListBean.ListBean creditBean = (CreditcardListBean.ListBean) getIntentData();
        if (null != creditBean) {
            tvCreditCardNum.setText(ArithUtil.hintBank(creditBean.bank_num));
            etCvn.setText(creditBean.cvn);
            tvValidity.setText(creditBean.validity);
            mRepamentDate = creditBean.repayment_date;
            mStatementDate = creditBean.statement_date;

            tvStatementDate.setText("每月" + mStatementDate + "日");
            tvRepaymentDate.setText("每月" + mRepamentDate + "日");
            etMoney.setText(creditBean.money);

            mCreditcardId = creditBean.id + "";
        }

    }


    @OnClick({R.id.rl_versions, R.id.rl_repayment, R.id.rl_statement, R.id.tv_submit})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.rl_versions:
                showTimeDialog();
                break;

            case R.id.rl_repayment:
                showMoneyDayDialog(true);
                break;

            case R.id.rl_statement:
                showMoneyDayDialog(false);
                break;


            case R.id.tv_submit:
                submit();
                break;
        }
    }

    /**
     * 提交
     */
    private void submit() {
        String mCvn = etCvn.getText().toString();
        String mMoney = etMoney.getText().toString();
        if (TextUtils.isEmpty(mCvn)) {
            DyToast.getInstance().warning("请输入信用卡背面三位安全码");
            return;
        }
        if (TextUtils.isEmpty(mMoney)) {
            DyToast.getInstance().warning("请输入信用卡金额");
            return;
        }

        HashMap<String, Object> parmas = new HashMap<>();
        parmas.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        parmas.put("creditcardId", mCreditcardId);
        parmas.put("validity", tvValidity.getText().toString());
        parmas.put("cvn", mCvn);
        parmas.put("statement_date", mStatementDate);
        parmas.put("repayment_date", mRepamentDate);
        parmas.put("money", mMoney);

        HttpFactory.getInstance().editCreditcard(parmas)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(EditCreditCardActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            DyToast.getInstance().success(data.message);
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
                    view.findViewById(R.id.tv_ok).setOnClickListener(v -> DialogUtils.dismiss());
                    view.findViewById(R.id.tv_cancel).setOnClickListener(v -> DialogUtils.dismiss());
                })
                .show();
    }

    private void setMoeyWheelView(WheelView wheelView, final boolean is) {
        wheelView.setCyclic(false);
        wheelView.setAdapter(new ArrayWheelAdapter(dayList));
        wheelView.setOnItemSelectedListener(index -> {
            if (is) {   // 还款日
                tvRepaymentDate.setText("每月" + dayList.get(index) + "日");
                mRepamentDate = dayList.get(index);

            } else {
                tvStatementDate.setText("每月" + dayList.get(index) + "日");
                mStatementDate = dayList.get(index);
            }
        });
    }

    private void initdayList() {
        dayList = new ArrayList<>();
        dayList.clear();
        for (int i = 1; i < 31; i++) {
            dayList.add(i + "");
        }
    }


}
