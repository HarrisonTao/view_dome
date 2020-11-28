package com.dykj.youfeng.home.repayment.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.home.repayment.adapter.RepaymentPreviewPlanAdapter2;
import com.dykj.youfeng.mode.CreditcardListBean;
import com.dykj.youfeng.mode.RepaymentPreviewPlanBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.GlideUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.dykj.module.view.dialog.RemindDialog;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 计划预览
 */
public class PreviewPlanActivity extends BaseActivity {
    
    
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.iv_bank_logo)
    ImageView ivBankLogo;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.tv_end_number)
    TextView tvEndNumber;
    @BindView(R.id.ll_crad_info)
    LinearLayout llCradInfo;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_statement_date)
    TextView tvStatementDate;
    @BindView(R.id.tv_repayment_date)
    TextView tvRepaymentDate;
    @BindView(R.id.tv_repayment_money)
    TextView tvRepaymentMoney;
    @BindView(R.id.tv_repayment_fee_money)
    TextView tvRepaymentFeeMoney;
    @BindView(R.id.frequency_money)
    TextView tvFrequencyMoney;
    @BindView(R.id.tv_count_money)
    TextView tvCountMoney;
    @BindView(R.id.tv_min_money)
    TextView tvMinMoney;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    
    private RepaymentPreviewPlanAdapter2 mAdapter = new RepaymentPreviewPlanAdapter2(R.layout.item_repayment_preview_plan);
    private String mCreditcardId;
    private StringBuffer SB;
    private CreditcardListBean.ListBean mCreditCarsBean;
    
    @Override
    public int intiLayout() {
        return R.layout.activity_preview_plan;
    }
    
    @Override
    public void initData() {
        initTitle("计划预览");
        mRecycler.setNestedScrollingEnabled(false);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.layout_empty, mRecycler);
    }
    
    @Override
    public void doBusiness() {
        mCreditCarsBean = (CreditcardListBean.ListBean) getIntentData();
        mCreditcardId = mCreditCarsBean.id + "";
        if (!TextUtils.isEmpty(mCreditcardId)) {
            requestPreviewPlan(mCreditcardId);
        }
    }
    
    /**
     * 请求计划预览
     *
     * @param mCreditcardId
     */
    private void requestPreviewPlan(String mCreditcardId) {
        HttpFactory.getInstance().repaymentPreviewPlan(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mCreditcardId)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(PreviewPlanActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<RepaymentPreviewPlanBean>>() {
                    @Override
                    public void success(HttpResponseData<RepaymentPreviewPlanBean> data) {
                        if ("9999".equals(data.status)) {
                            RepaymentPreviewPlanBean repaymentPreviewPlanBean = data.getData();
                            setView(repaymentPreviewPlanBean);
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
     * 设置view 数据
     * @param repaymentPreviewPlanBean
     */
    private void setView(RepaymentPreviewPlanBean repaymentPreviewPlanBean) {
        RepaymentPreviewPlanBean.CardBean card = repaymentPreviewPlanBean.card;
        String bank_num = card.bank_num;
        GlideUtils.setImage(ivBankLogo, card.logo);
        tvBankName.setText(card.bank_name);
        tvEndNumber.setText("(" + bank_num.substring(bank_num.length() - 4) + ")");
        tvMoney.setText(card.money);
        tvRepaymentDate.setText("还款日:" + card.repayment_date + "号");
        tvStatementDate.setText("账单日:" + card.statement_date + "号");
        tvMinMoney.setText("¥" + repaymentPreviewPlanBean.min_money);
        
        tvRepaymentMoney.setText("¥" + repaymentPreviewPlanBean.repayment_money);
        tvRepaymentFeeMoney.setText("还款手续费:¥" + repaymentPreviewPlanBean.fee_money);
        tvFrequencyMoney.setText("¥" + repaymentPreviewPlanBean.frequency_money);
//        tvCountMoney.setText("总手续费:¥" + (Double.parseDouble(TextUtils.isEmpty(repaymentPreviewPlanBean.fee_money) ? "0" : repaymentPreviewPlanBean.fee_money) + repaymentPreviewPlanBean.frequency_money));
        tvCountMoney.setText("总手续费:¥" + repaymentPreviewPlanBean.all_fee_money);
        
        List<RepaymentPreviewPlanBean.PlanBean> plan = repaymentPreviewPlanBean.plan;
        mAdapter.setNewData(plan);
        SB = new StringBuffer();
        for (int i = 0; i < plan.size(); i++) {
            RepaymentPreviewPlanBean.PlanBean planBean = plan.get(i);
            if (i != plan.size() - 1) {
                SB.append(planBean.mcc_class_id + ",");
            } else {
                SB.append(planBean.mcc_class_id);
            }
        }
    }
    
    @OnClick({R.id.tv_commit})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.tv_commit:
                // 提交预览计划
                if (null != SB) {
                    if (mCreditCarsBean.channel.contains("jft") || mCreditCarsBean.channel.contains("Jft")) {
                        String code = mCreditCarsBean.channel.replace("jft", "");
                        code = code.replace("Jft", "");
                        commitJftPlan(code);
                    } else if (TextUtils.indexOf(mCreditCarsBean.channel, "Ds") > -1) {
                        commitDsPlan();
                    } else if (TextUtils.indexOf(mCreditCarsBean.channel, "Kjt") > -1) {
                        commitKjtPlan();
                    } else {
                        commitPlan();
                    }
                }
                break;
            default:
                break;
        }
    }
    
    private void commitJftPlan(String code) {
        HttpFactory.getInstance().jftSubmit(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mCreditcardId, code)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(PreviewPlanActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            EventBus.getDefault().post(AppCacheInfo.mSubmitRepayMent);
                            DyToast.getInstance().success(data.message);
                            finish();
                        } else {
                            new RemindDialog(PreviewPlanActivity.this, data.message)
                                    .setLifecycle(getLifecycle()).show();
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
     * 得仕  提交计划
     */
    private void commitDsPlan() {
        HttpFactory.getInstance().dsSubmit(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mCreditcardId)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(PreviewPlanActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            EventBus.getDefault().post(AppCacheInfo.mSubmitRepayMent);
                            DyToast.getInstance().success(data.message);
                            finish();
                        }else{
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
     * 快捷通  提交计划
     */
    private void commitKjtPlan(){
        HttpFactory.getInstance().kjtRepaySubmit(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mCreditcardId)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(PreviewPlanActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            EventBus.getDefault().post(AppCacheInfo.mSubmitRepayMent);
                            DyToast.getInstance().success(data.message);
                            finish();
                        }else{
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
     * 提交计划
     */
    private void commitPlan() {
        HttpFactory.getInstance().repaymentPlanSubmit(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mCreditcardId, SB.toString())
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(PreviewPlanActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            EventBus.getDefault().post(AppCacheInfo.mSubmitRepayMent);
                            DyToast.getInstance().success(data.message);
                            finish();
                        }else{
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
    
    
}
