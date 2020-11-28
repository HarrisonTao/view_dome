package com.dykj.youfeng.home.repayment.activity;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.view.WheelView;
import com.dykj.youfeng.R;
import com.dykj.youfeng.dialog.RepaymentDialog;
import com.dykj.youfeng.mode.AreaBean;
import com.dykj.youfeng.mode.CreditcardListBean;
import com.dykj.youfeng.mode.RepaymentPreviewBean;
import com.dykj.youfeng.mode.SmsBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.base.BaseWebViewActivity;
import com.dykj.module.entity.BaseWebViewData;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.DialogUtils;
import com.dykj.module.util.GlideUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.WheelPickerBean;
import com.dykj.module.util.WheelPickerUtils;
import com.dykj.module.util.toast.DyToast;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 还款 制定计划
 */
public class EnactPlanActivity extends BaseActivity {

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
    @BindView(R.id.rl_1)
    RelativeLayout rl1;
    @BindView(R.id.tv_repayment_date)
    TextView tvRepaymentDate;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_consume_card)
    TextView tvConsumeCard;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_repayment_mode)
    TextView tvRepaymentMode;
    @BindView(R.id.tv_repayment_day_num)
    TextView tvRepaymentNum;
    @BindView(R.id.tv_repayment_num_count)
    TextView tvRepaymentNumCount;
    @BindView(R.id.tv_plan_submit)
    TextView tvPlanSubmit;
    @BindView(R.id.tv_agree)
    TextView tvAgree;
    @BindView(R.id.ll_agree)
    LinearLayout llAgree;
    @BindView(R.id.ll_statement_card)
    LinearLayout llStatementCard;
    private CreditcardListBean.ListBean mCreditCarsBean;
    private String mStatementDate, mRepaymentDate, mCreditcardId;
    private ArrayList<String> mDayList;
    private ArrayList<String> mNumberList;  // 每笔消费次数
    private String mProvince, mCity;
    private String mProvinceId, mCityId;
    private String dayNum, repaymentType;
    private String mChannel;

    @Override
    public int intiLayout() {
        return R.layout.activity_enact_plan;
    }

    @Override
    public void initData() {
        initTitle("制定计划");
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void doBusiness() {
        llAgree.setVisibility(View.GONE);
        mCreditCarsBean = (CreditcardListBean.ListBean) getIntentData();
        if (null != mCreditCarsBean) {
            GlideUtils.setImage(ivBankLogo, mCreditCarsBean.logo);
            String idcard = mCreditCarsBean.idcard;
            tvBankName.setText(mCreditCarsBean.bank_name);
            tvEndNumber.setText("(" + idcard.substring(idcard.length() - 4) + ")");
            tvMoney.setText(mCreditCarsBean.money);
            mStatementDate = mCreditCarsBean.statement_date;
            mRepaymentDate = mCreditCarsBean.repayment_date;
            tvStatementDate.setText("账单日 " + mStatementDate + "号");
            tvRepaymentDate.setText("还款日 " + mRepaymentDate + "号");
            mCreditcardId = mCreditCarsBean.id + "";
            mChannel = mCreditCarsBean.channel;

            llStatementCard.setVisibility(View.GONE);
        }
        addDayList();
    }

    /**
     * 每日还款次数 添加list
     */
    private void addDayList() {
        mDayList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            mDayList.add(i + "");
        }

        mNumberList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            mNumberList.add(i + "");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.tv_city, R.id.tv_plan_submit,
            R.id.tv_time, R.id.tv_repayment_day_num,
            R.id.tv_repayment_num_count, R.id.ll_agree})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.tv_time:
                // 设置还款日期
                new RepaymentDialog(Integer.parseInt(mStatementDate), Integer.parseInt(mRepaymentDate), this, tvTime).showDialog();
                break;
            case R.id.tv_city:
                // 选择地区
                getArea("", true);
                break;
            case R.id.tv_repayment_day_num:
                // 设置每天笔数
                showMoneyDayDialog(true);
                break;
            case R.id.tv_repayment_num_count:
                // 每笔消费次数
                showMoneyDayDialog(false);
                break;
            case R.id.tv_plan_submit:
                // 预览提交
                planSubmit();
                break;
            case R.id.ll_agree:
                // 操作协议
                getAgeree();
                break;
            default:
                break;
        }
    }

    /**
     * 提交计划
     */
    private void planSubmit() {
        String mMoney = etMoney.getText().toString();
        String mTime = tvTime.getText().toString();

        if (TextUtils.isEmpty(mMoney)) {
            DyToast.getInstance().warning("请输入还款金额!");
            return;
        }

        if (TextUtils.isEmpty(mTime)) {
            DyToast.getInstance().warning("请选择还款日期!");
            return;
        }

        if (TextUtils.isEmpty(mProvince)) {
            DyToast.getInstance().warning("请选择消费省份!");
            return;
        }

        if (TextUtils.isEmpty(mCity)) {
            DyToast.getInstance().warning("请选择消费城市!");
            return;
        }

        if (TextUtils.isEmpty(dayNum)) {
            DyToast.getInstance().warning("请选择每日还款次数");
            return;
        }


        if (TextUtils.isEmpty(repaymentType)) {
            DyToast.getInstance().warning("请选择每笔消费次数");
            return;
        }


        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>(10);
        map.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        map.put("cardId", mCreditcardId);
        map.put("money", mMoney);
        map.put("pay_num", repaymentType);
        map.put("repay_num", dayNum);
        map.put("time", mTime);
        if (mChannel.contains("jft") || mChannel.contains("Jft")) {
            map.put("province_name", mProvinceId);
            map.put("city_name", mCityId);
        } else {
            map.put("province_name", mProvince);
            map.put("city_name", mCity);
        }
        map.put("channel", mChannel);
        HttpFactory.getInstance().repaymentPreview(map)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(EnactPlanActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<RepaymentPreviewBean>>() {
                    @Override
                    public void success(HttpResponseData<RepaymentPreviewBean> data) {
                        if ("9999".equals(data.status)) {
                            startAct(PreviewPlanActivity.class, mCreditCarsBean);    // 提交计划成功
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
     * 选择省份
     *
     * @param id
     * @param isProvince
     */
    private void getArea(String id, boolean isProvince) {
        if (mChannel.contains("jft") || mChannel.contains("Jft")) {
            HttpFactory.getInstance().getJftArea(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), id)
                    .compose(HttpObserver.schedulers(getAty()))
                    .as(HttpObserver.life(EnactPlanActivity.this))
                    .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<List<AreaBean>>>() {
                        @Override
                        public void success(HttpResponseData<List<AreaBean>> data) {
                            if ("9999".equals(data.status)) {
                                List<AreaBean> areaBeanList = data.getData();
                                if (null != areaBeanList) {
                                    showArea(areaBeanList, isProvince);
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
        } else {
            HttpFactory.getInstance().areaList(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), id)
                    .compose(HttpObserver.schedulers(getAty()))
                    .as(HttpObserver.life(EnactPlanActivity.this))
                    .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<List<AreaBean>>>() {
                        @Override
                        public void success(HttpResponseData<List<AreaBean>> data) {
                            if ("9999".equals(data.status)) {
                                List<AreaBean> areaBeanList = data.getData();
                                if (null != areaBeanList) {
                                    showArea(areaBeanList, isProvince);
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

    }

    /**
     * 显示省市区弹框
     *
     * @param data
     * @param isProvince
     */
    private void showArea(List<AreaBean> data, boolean isProvince) {
        final ArrayList<WheelPickerBean> beans = new ArrayList<>();
        if (data != null && !data.isEmpty()) {
            for (int i = 0; i < data.size(); i++) {
                beans.add(new WheelPickerBean(data.get(i).id + "", data.get(i).name));
            }
            if (isProvince) {
                WheelPickerUtils.getInstance().initCustomOptionPicker(getAty(), "请选择省份", beans, current -> {
                    mProvince = beans.get(current).getContext();
                    mProvinceId = beans.get(current).getId();
                    String mPid = beans.get(current).getId();
                    getArea(mPid, false);
                });
            } else {
                WheelPickerUtils.getInstance().initCustomOptionPicker(getAty(), "请选择城市", beans, current -> {
                    mCity = beans.get(current).getContext();
                    mCityId = beans.get(current).getId();
                    tvCity.setText(mProvince + mCity);
                });
            }
        }
    }

    /**
     * 每天笔数  / 每笔次数
     */
    private void showMoneyDayDialog(final boolean isDay) {
        DialogUtils.getInstance()
                .with(this)
                .setlayoutPosition(Gravity.BOTTOM)
                .setlayoutPading(0, 0, 0, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FROM_BOTTOM)
                .setlayoutId(R.layout.dialog_recyclerview)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    TextView tvTitle = view.findViewById(R.id.tv_tile);
                    tvTitle.setText(isDay ? "选择每天笔数" : "每笔消费次数");
                    WheelView wheelView = view.findViewById(R.id.wheelview);
                    setMoeyWheelView(wheelView, isDay);
                    view.findViewById(R.id.tv_ok).setOnClickListener(v -> {
                        if (isDay) {   // 还款日
                            dayNum = mDayList.get(wheelView.getCurrentItem());
                            tvRepaymentNum.setText("每天" + mDayList.get(wheelView.getCurrentItem()) + "笔");
                        } else {
                            repaymentType = mNumberList.get(wheelView.getCurrentItem());
                            tvRepaymentNumCount.setText("每笔消费" + mNumberList.get(wheelView.getCurrentItem()) + "次");
                        }
                        DialogUtils.dismiss();
                    });
                    view.findViewById(R.id.tv_cancel).setOnClickListener(v -> DialogUtils.dismiss());
                })
                .show();
    }

    private void setMoeyWheelView(WheelView wheelView, final boolean is) {
        wheelView.setCyclic(false);
        if (is) {
            wheelView.setAdapter(new ArrayWheelAdapter(mDayList));
        } else {
            wheelView.setAdapter(new ArrayWheelAdapter(mNumberList));
        }
    }

    /**
     * 操作协议
     */
    private void getAgeree() {
        HttpFactory.getInstance().getCardrepaymentInfo(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""))
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(EnactPlanActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<SmsBean>>() {
                    @Override
                    public void success(HttpResponseData<SmsBean> data) {
                        if ("9999".equals(data.status)) {
                            BaseWebViewData webViewData = new BaseWebViewData();
                            webViewData.title = "操作协议";
                            webViewData.content = data.getData().cardrepayment;
                            startAct(EnactPlanActivity.this, BaseWebViewActivity.class, webViewData);
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
    public void onEvent(String onEvent) {
        if (!TextUtils.isEmpty(onEvent) && onEvent.equals(AppCacheInfo.mSubmitRepayMent)) {
            finish();
        }
    }
}
