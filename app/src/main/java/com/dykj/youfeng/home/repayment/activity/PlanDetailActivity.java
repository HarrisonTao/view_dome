package com.dykj.youfeng.home.repayment.activity;


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.GlideUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.dykj.module.view.dialog.ConfirmDialog;
import com.dykj.youfeng.R;
import com.dykj.youfeng.home.repayment.adapter.PlanDetailAdapter;
import com.dykj.youfeng.mode.CommonRepayBean;
import com.dykj.youfeng.mode.PlanDetailBean;
import com.dykj.youfeng.mode.PlanInfoBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 计划详情
 */
public class PlanDetailActivity extends BaseActivity {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.tv_money_1)
    TextView tvMoney1;
    @BindView(R.id.tv_count_repayment_money)
    TextView tvCountRepaymentMoney;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_bank_num)
    TextView tvBankNum;
    @BindView(R.id.iv_oneCard_logo)
    ImageView ivOneCardLogo;
    @BindView(R.id.iv_oneCard_name)
    TextView ivOneCardName;
    @BindView(R.id.iv_oneCard_bank_num)
    TextView ivOneCardBankNum;
    @BindView(R.id.tv_repayment_money)
    TextView tvRepaymentMoney;
    @BindView(R.id.tv_fee_money)
    TextView tvFeeMoney;
    @BindView(R.id.tv_repayment_count_money)
    TextView tvRepaymentCountMoney;
    @BindView(R.id.tv_count_fee_money)
    TextView tvCountFeeMoney;
    @BindView(R.id.tv_yet_money)
    TextView tvYetMoney;
    @BindView(R.id.tv_not_money)
    TextView tvNotMoney;
    @BindView(R.id.tv_yet_num)
    TextView tvYetNum;
    @BindView(R.id.tv_yet_fee_money)
    TextView tvYetFeeMoney;
    @BindView(R.id.ll_crad_info)
    LinearLayout mLlcradInfo;
    @BindView(R.id.iv_bank_logo)
    ImageView ivBankLogo;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.bank_text)
    TextView bankText;
    @BindView(R.id.credRelative)
    RelativeLayout credRelative;
    @BindView(R.id.tv_card_number)
    TextView tvCardNumber;
    @BindView(R.id.tv_card_edu)
    TextView tvCardEdu;
    @BindView(R.id.tv_card_riqi)
    TextView tvCardRiqi;
    @BindView(R.id.tv_card_haikri)
    TextView tvCardHaikri;
    @BindView(R.id.bankLinear)
    LinearLayout bankLinear;
    @BindView(R.id.tv_1)
    TextView tv1;



    private PlanDetailAdapter mAdapter = new PlanDetailAdapter(R.layout.item_plan_detail);
    private int mRepId;
    private String mPlanType;    // 一卡多还,普通还款

    @Override
    public int intiLayout() {
        return R.layout.activity_plan_detail;
    }

    @Override
    public void initData() {
        initTitle("计划详情");
        tvRight.setText("取消计划");

        PlanDetailBean bean = (PlanDetailBean) getIntentData();
        String type = bean.type;
        int planType = bean.planType;
        if (2 == planType) {                      // 一卡多还
            mPlanType = "oneMoreRepayment";
            if ("credit".equals(type)) {
                String creditId = bean.creditId;
                requestCreditIdInfo(creditId);
            } else if ("repId".equals(type)) {
                String repId = bean.repId;
                requestrepIdInfo(repId);
            }
        } else if (1 == planType) {    // 普通还款
            mPlanType = "commonRepayment";
            mLlcradInfo.setVisibility(View.GONE);
            if ("credit".equals(type)) {
                String creditId = bean.creditId;
                requestRepaymentCreditIdInfo(bean.creditId);
            } else {
                String repId = bean.repayment_id;
                requestRepaymentrepIdInfo(repId);
            }
        }

        mRecycler.setAdapter(mAdapter);
    }


    @Override
    public void doBusiness() {
        tvRight.setOnClickListener(v -> {
            new ConfirmDialog(this, () -> {
                // 取消计划
                if ("oneMoreRepayment".equals(mPlanType)) {
                    planCancel();
                } else if ("commonRepayment".equals(mPlanType)) {
                    if (channel.contains("Jft") || channel.contains("jft")) {
                        String code = channel.replace("jft", "");
                        code = code.replace("Jft", "");
                        jftCancelPlan(code);
                    } else if (TextUtils.indexOf(channel, "Kjt") > -1) {
                        kjtCancelPlan();
                    } else {
                        planCommonRepaymentPlan();
                    }

                }
            }, "确定取消该计划?").show();
        });
    }

    /**
     * 快捷通取消计划
     */
    private void kjtCancelPlan() {
        HttpFactory.getInstance().kjtRepayCancel(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), cardId)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            DyToast.getInstance().success(data.message);
                            EventBus.getDefault().post(AppCacheInfo.mRefreshCreditCardList);
                            EventBus.getDefault().post(AppCacheInfo.mCancelPlan);
                            finish();
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
     * 普通还款 取消计划
     */
    private void planCommonRepaymentPlan() {
        HttpFactory.getInstance().commonCancelPlan(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mRepId + "")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(PlanDetailActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            DyToast.getInstance().success(data.message);
                            EventBus.getDefault().post(AppCacheInfo.mRefreshCreditCardList);
                            EventBus.getDefault().post(AppCacheInfo.mCancelPlan);
                            finish();
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));
    }

    private String cardId = "";
    private String channel = "";

    /**
     * 普通还款 取消计划
     */
    private void jftCancelPlan(String code) {
        HttpFactory.getInstance().jftCancel(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), cardId, code)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(PlanDetailActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            DyToast.getInstance().success(data.message);
                            EventBus.getDefault().post(AppCacheInfo.mRefreshCreditCardList);
                            EventBus.getDefault().post(AppCacheInfo.mCancelPlan);
                            finish();
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
     * 取消计划 一卡多还
     */
    private void planCancel() {
        HttpFactory.getInstance().cancelPlan(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mRepId + "")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(PlanDetailActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            DyToast.getInstance().success(data.message);
                            EventBus.getDefault().post(AppCacheInfo.mRefreshCreditCardList);
                            EventBus.getDefault().post(AppCacheInfo.mCancelPlan);
                            finish();
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
     * 请求卡计划详情
     *
     * @param creditId
     */
    private void requestCreditIdInfo(String creditId) {
        HttpFactory.getInstance().repaymentInfoCard(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), creditId)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(PlanDetailActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<PlanInfoBean>>() {
                    @Override
                    public void success(HttpResponseData<PlanInfoBean> data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            PlanInfoBean infoBean = data.getData();
                            setViewData(infoBean);
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
     * 请求 计划id 详情
     *
     * @param repId
     */
    private void requestrepIdInfo(String repId) {
        HttpFactory.getInstance().repaymentInfo(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), repId)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(PlanDetailActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<PlanInfoBean>>() {
                    @Override
                    public void success(HttpResponseData<PlanInfoBean> data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            PlanInfoBean infoBean = data.getData();
                            setViewData(infoBean);
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
     * 普通还款计划id
     * @param repId
     */
    private void requestRepaymentrepIdInfo(String repId) {
        HttpFactory.getInstance().commonRepaymentInfo(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), repId)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(PlanDetailActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<CommonRepayBean>>() {
                    @Override
                    public void success(HttpResponseData<CommonRepayBean> data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            CommonRepayBean data1 = data.getData();
                            setRepaymentPlanViewData(data1);
                            setCradView(data1);
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
     * 设置卡片信息
     * @param data
     */
    private  void setCradView(CommonRepayBean data){
        if(data.bank!=null) {
            GlideUtils.setImage(ivBankLogo, data.bank.logo);
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(Color.parseColor(data.bank.color));
            gd.setCornerRadii(getCornerRadii(8,8,8,8));
            bankLinear.setBackground(gd);
           // bankLinear.setBackgroundColor(Color.parseColor(data.bank.color));
        }
        if(data.card!=null){
            tvBankName.setText(data.card.bank_name);

            if(data.card.bank_number!=null && data.card.bank_number.length()>4){
                tvCardNumber.setText(data.card.bank_number.substring(data.card.bank_number.length()-4,data.card.bank_number.length()));
            }
            tvCardEdu.setText(data.card.money);
            tvCardRiqi.setText(data.card.statement_date);
            tvCardHaikri.setText(data.card.repayment_date);
        }

    }

    /**
     * 普通还款 卡id
     *
     * @param creditId
     */
    private void requestRepaymentCreditIdInfo(String creditId) {
        HttpFactory.getInstance().commonRepaymentCardInfo(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), creditId)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(PlanDetailActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<CommonRepayBean>>() {
                    @Override
                    public void success(HttpResponseData<CommonRepayBean> data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            CommonRepayBean data1 = data.getData();
                            setRepaymentPlanViewData(data1);
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
     * 普通还款计划 设置view 数据
     *
     * @param commonRepayBean
     */
    private void setRepaymentPlanViewData(CommonRepayBean commonRepayBean) {
        CommonRepayBean.InfoBean info = commonRepayBean.info;
        mRepId = info.id;
        cardId = info.cardId + "";
        channel = info.channel_type;

        int status = info.status;
        switch (status) {
            case 0:
                tvRight.setVisibility(View.INVISIBLE);
                break;
            case 1:
                tvRight.setVisibility(View.VISIBLE);
                break;
            case 2:
                tvRight.setVisibility(View.INVISIBLE);
                break;
            case 3:
                tvRight.setVisibility(View.INVISIBLE);
                break;
            case 4:
                tvRight.setVisibility(View.VISIBLE);
                break;
        }
        tvCountRepaymentMoney.setText((Double.parseDouble(info.repayment_money) + info.fee_money) + "");

        // 计划明细
        tvRepaymentMoney.setText("￥" + info.repayment_money);

        double v1 = info.fee_money - (Double.parseDouble(info.frequency_money));

        tvFeeMoney.setText("￥"+String.format("%.2f", v1));
        tvRepaymentCountMoney.setText( "￥"+info.frequency_money);
              tvCountFeeMoney.setText("￥"+String.format("%.2f", info.fee_money));


        // 还款金额
        Double hasRMoney = 0.0;
        String has_repayment_money = info.has_repayment_money;
        if (TextUtils.isEmpty(has_repayment_money)) {
            hasRMoney = 0.0;
        } else {
            Double.parseDouble(has_repayment_money);
        }
        tvYetMoney.setText("￥" + has_repayment_money);

        double v = Double.parseDouble(info.repayment_money) - hasRMoney;
        tvNotMoney.setText("￥" + String.format("%.2f", v));
        tvYetNum.setText(info.has_repayment_num + "");
        tvYetFeeMoney.setText("￥" + info.has_fee_money);

        List<PlanInfoBean.PlanBean> plan = commonRepayBean.plan;
        mAdapter.setType("还款");
        mAdapter.setNewData(plan);

    }


    /**
     * 设置view 数据
     *
     * @param infoBean
     */
    private void setViewData(PlanInfoBean infoBean) {
        PlanInfoBean.InfoBean info = infoBean.info;
        mRepId = info.id;
        cardId = info.cardId + "";
        channel = info.channel_type;

        int status = info.status;
        switch (status) {
            case 0:

                tvRight.setVisibility(View.INVISIBLE);
                break;

            case 1:

                tvRight.setVisibility(View.VISIBLE);
                break;

            case 2:

                tvRight.setVisibility(View.INVISIBLE);
                break;

            case 3:

                tvRight.setVisibility(View.INVISIBLE);
                break;
            case 4:

                tvRight.setVisibility(View.VISIBLE);
                break;
        }
        tvCountRepaymentMoney.setText((Double.parseDouble(info.repayment_money) + info.fee_money) + "");
//
        PlanInfoBean.CardBean card = infoBean.card;
        String bank_num = card.bank_num;
        GlideUtils.setImage(ivLogo, card.logo);
        tvName.setText(card.name);
        tvBankNum.setText("(" + bank_num.substring(bank_num.length() - 4) + ")");


        //消费信用卡-----
        Map<String, Object> oneCard = (Map<String, Object>) infoBean.oneCard;
        try {
            String logo = (String) oneCard.get("logo");
            GlideUtils.setImage(ivOneCardLogo, logo);
            String name = (String) oneCard.get("name");
            String rpBank_nums = (String) oneCard.get("bank_num");
            ivOneCardName.setText(name);
            ivOneCardBankNum.setText("(" + rpBank_nums.substring(rpBank_nums.length() - 4) + ")");
        } catch (Exception e) {
            ivOneCardName.setText("暂无查询到消费卡片信息");
        }


        // 计划明细
        tvRepaymentMoney.setText("￥" + info.repayment_money);
        double v1 = info.fee_money - (Double.parseDouble(info.frequency_money));
        tvFeeMoney.setText("￥" + String.format("%.2f", v1)); // 保留两位数
        tvRepaymentCountMoney.setText("￥" + info.frequency_money);
        tvCountFeeMoney.setText("￥" + info.fee_money);


        // 还款金额
        Double hasRMoney = 0.0;
        String has_repayment_money = info.has_repayment_money;
        if (TextUtils.isEmpty(has_repayment_money)) {
            hasRMoney = 0.0;
        } else {
            Double.parseDouble(has_repayment_money);
        }
        tvYetMoney.setText("￥" + has_repayment_money);
        double v = Double.parseDouble(info.repayment_money) - hasRMoney;
        tvNotMoney.setText("￥" + String.format("%.2f", v));   // 保留两位数
        tvYetNum.setText(""+info.has_repayment_num + "");
        tvYetFeeMoney.setText("￥" + info.has_fee_money);

        List<PlanInfoBean.PlanBean> plan = infoBean.plan;
        mAdapter.setNewData(plan);

    }
    private float [] getCornerRadii(float leftTop,float rightTop, float leftBottom,float rightBottom){
        //这里返回的一个浮点型的数组，一定要有8个元素，不然会报错
        return floatArrayOf(dp2px(leftTop), dp2px(leftTop), dp2px(rightTop),
                dp2px(rightTop),dp2px(rightBottom), dp2px(rightBottom),dp2px(leftBottom),dp2px(leftBottom));
    }

    private float[] floatArrayOf(float dp2px, float dp2px1, float dp2px2, float dp2px3, float dp2px4, float dp2px5, float dp2px6, float dp2px7) {
        float array[]={dp2px,dp2px1,dp2px2,dp2px3,dp2px4,dp2px5,dp2px6,dp2px7};
        return  array;
    }

    private float dp2px(float dpVal){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, this.getResources().getDisplayMetrics());
    }


}
