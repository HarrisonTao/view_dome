package com.dykj.youfeng.home.repayment.activity;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.home.repayment.adapter.RepaymentPreviewPlanAdapter;
import com.dykj.youfeng.mode.OneMoreRepaymetBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.GlideUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 一卡还多卡 计划预览
 */
public class OneMorePreviewPlanActivity extends BaseActivity {
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.tv_bank_num)
    TextView tvBankNum;
    @BindView(R.id.iv_repayment_logo)
    ImageView ivRepaymentLogo;
    @BindView(R.id.iv_repayment_name)
    TextView tvRepaymentName;
    @BindView(R.id.iv_repayment_num)
    TextView tvRepaymentNum;
    @BindView(R.id.tv_repayment_money)
    TextView tvRepaymentMoney;
    @BindView(R.id.tv_fee_money)
    TextView tvFeeMoney;
    @BindView(R.id.tv_repayment_count_money)
    TextView tvRepaymentCountMoney;
    @BindView(R.id.tv_count_money)
    TextView tvCountMoney;
    @BindView(R.id.tv_count_fee)
    TextView tvCountFee;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;


    private RepaymentPreviewPlanAdapter mAdapter = new RepaymentPreviewPlanAdapter(R.layout.item_repayment_preview_plan);
    private String mCreditcardId;


    @Override
    public int intiLayout() {
        return R.layout.activity_one_more_preview_plan;
    }

    @Override
    public void initData() {
        initTitle("计划预览");
        mRecycler.setNestedScrollingEnabled(false);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.layout_empty, mRecycler);

        OneMoreRepaymetBean oneMoreRepaymetBean = (OneMoreRepaymetBean) getIntentData();
        if (null != oneMoreRepaymetBean)
            setView(oneMoreRepaymetBean);
    }


    private void setView(OneMoreRepaymetBean oneMoreRepaymetBean) {
        OneMoreRepaymetBean.CardBean card = oneMoreRepaymetBean.card;
        String bank_num = card.bank_num;
        GlideUtils.setImage(ivLogo, card.logo);
        tvBankName.setText(card.name);
        tvBankNum.setText("(" + bank_num.substring(bank_num.length() - 4) + ")");
        mCreditcardId = card.mCreditcardId;

        OneMoreRepaymetBean.RepaymentBean repayment = oneMoreRepaymetBean.repayment;
        tvRepaymentMoney.setText("￥" + repayment.repaymentMoney);
        tvFeeMoney.setText("￥" + repayment.paymentFee);
        tvCountMoney.setText("￥" + repayment.feeMoney);
        tvRepaymentCountMoney.setText("￥" + repayment.repaymentFee);

        tvCountFee.setText("￥" + repayment.minMoney);  // 预留额度
        mAdapter.setNewData(repayment.plan);

        // 消费卡片
        try {
            Map<String, Object> map = (Map<String, Object>) oneMoreRepaymetBean.oneCard;
            String logo = (String) map.get("logo");
            String num = (String) map.get("bank_num");
            GlideUtils.setImage(ivRepaymentLogo, logo);
            tvRepaymentName.setText((String) map.get("name"));
            tvRepaymentNum.setText("(" + num.substring(num.length() - 4) + ")");
        } catch (Exception e) {
            tvRepaymentName.setText("暂无消费卡片信息");
        }
    }

    @Override
    public void doBusiness() {
        tvCommit.setOnClickListener(v -> {   // 提交
            ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>(10);
            map.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
            map.put("channel", "cjmin");
            map.put("cardId", mCreditcardId);
            HttpFactory.getInstance().oneMoreDosubmit(map)
                    .compose(HttpObserver.schedulers(getAty()))
                    .as(HttpObserver.life(OneMorePreviewPlanActivity.this))
                    .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                        @Override
                        public void success(HttpResponseData data) {
                            if ("9999".equals(data.status)) {
                                DyToast.getInstance().success(data.message);
                                EventBus.getDefault().post(AppCacheInfo.mSubmitOneMoreRepayMent);
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
        });
    }

}
