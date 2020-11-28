package com.dykj.youfeng.home.repayment.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.view.WheelView;
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
import com.dykj.youfeng.R;
import com.dykj.youfeng.dialog.RepaymentDialog;
import com.dykj.youfeng.mode.AreaBean;
import com.dykj.youfeng.mode.CreditcardListBean;
import com.dykj.youfeng.mode.OneMoreRepaymetBean;
import com.dykj.youfeng.mode.SelectCreditBean;
import com.dykj.youfeng.mode.SmsBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 一卡还多卡  制定计划
 */
public class OneMoreEnactPlanActivity extends BaseActivity {
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
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_consume_card)
    TextView mTvSelectCard;
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

    private String mStatementDate = "";
    private String mRepaymentDate = "";
    private ArrayList<String> mDayList;
    private ArrayList<String> mNumberList;  // 每笔消费次数
    private String mProvince, mCity;
    private int mCreditcardId;
    private String oneCardId = "";  // 消费信用卡id
    private String dayNum, repaymentType;
    private String mCityCode;
    private String mAreaCode;


    @Override
    public int intiLayout() {
        return R.layout.activity_enact_plan;
    }

    @Override
    public void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initTitle("制定计划");
        addDayList();
    }


    @Override
    public void doBusiness() {
        CreditcardListBean.ListBean creditcardBean = (CreditcardListBean.ListBean) getIntentData();
        if (null != creditcardBean) {
            GlideUtils.setImage(ivBankLogo, creditcardBean.logo);
            String idcard = creditcardBean.idcard;
            tvBankName.setText(creditcardBean.bank_name);
            tvEndNumber.setText("(" + idcard.substring(idcard.length() - 4) + ")");
            tvMoney.setText(creditcardBean.money);
            mStatementDate = creditcardBean.statement_date;
            mRepaymentDate = creditcardBean.repayment_date;
            tvStatementDate.setText("账单日 " + mStatementDate + "号");
            tvRepaymentDate.setText("还款日 " + mRepaymentDate + "号");
            mCreditcardId = creditcardBean.id;
        }
    }


    @OnClick({R.id.tv_city, R.id.tv_plan_submit, R.id.tv_consume_card,
            R.id.tv_time, R.id.tv_repayment_day_num,
            R.id.tv_repayment_num_count, R.id.ll_agree})
    public void onClickView(View view) {
        switch (view.getId()) {

            case R.id.tv_time:
                // 设置还款日期
                new RepaymentDialog(Integer.parseInt(mStatementDate), Integer.parseInt(mRepaymentDate), this, tvTime).showDialog();
                break;

            case R.id.tv_consume_card:
                // 选择消费信用卡
                SelectCreditBean bean = new SelectCreditBean();
                bean.type = "oneMore";
                bean.creditId = mCreditcardId + "";
                startAct(CreditCardMsgActivity.class, bean, 1);
                break;

            case R.id.tv_city:
                // 选择地区
                getArea("", 1);
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
     * 预览提交
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

        if (TextUtils.isEmpty(oneCardId)) {
            DyToast.getInstance().warning("请选择消费信用卡!");
            return;
        }

        if (TextUtils.isEmpty(mProvince)) {
            DyToast.getInstance().warning("请选择消费省份!");
            return;
        }

        if (TextUtils.isEmpty(mCityCode)) {
            DyToast.getInstance().warning("请选择消费城市!");
            return;
        }


        if (TextUtils.isEmpty(mAreaCode)) {
            DyToast.getInstance().warning("请选择消费地区!");
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
//        map.put("channel", "cjmin");
        map.put("cardId", mCreditcardId);
        map.put("oneCardId", oneCardId);
        map.put("repaymentNum", dayNum);
        map.put("repaymentType", repaymentType);
        map.put("money", mMoney);
        map.put("dates", mTime);
        map.put("province", mCityCode);
        map.put("city", mAreaCode);

        HttpFactory.getInstance().oneMoreRepaymetPreview(map)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(OneMoreEnactPlanActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<OneMoreRepaymetBean>>() {
                    @Override
                    public void success(HttpResponseData<OneMoreRepaymetBean> data) {
                        if ("9999".equals(data.status)) {
                            OneMoreRepaymetBean data1 = data.getData();
                            if (null != data1) {
                                data1.card.mCreditcardId = mCreditcardId + "";
                                startAct(OneMorePreviewPlanActivity.class, data1);
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
     * 选择省份
     *
     * @param id
     * @param areaType 1 省  2 市  3 区
     */
    private void getArea(String id, int areaType) {
        HttpFactory.getInstance().gftGetAreas(id, areaType)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(OneMoreEnactPlanActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<List<AreaBean>>>() {
                    @Override
                    public void success(HttpResponseData<List<AreaBean>> data) {
                        if ("9999".equals(data.status)) {
                            List<AreaBean> areaBeanList = data.getData();
                            if (null != areaBeanList) {
                                showArea(areaBeanList, areaType);
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
     * 显示省市区弹框
     *
     * @param data
     * @param areaType
     */
    private void showArea(List<AreaBean> data, int areaType) {
        final ArrayList<WheelPickerBean> beans = new ArrayList<>();
        if (data != null && !data.isEmpty()) {
            for (int i = 0; i < data.size(); i++) {
                AreaBean areaBean = data.get(i);
                beans.add(new WheelPickerBean(areaBean.id + "",areaBean.name,areaBean.code));
            }
            if (areaType == 1) {
                WheelPickerUtils.getInstance().initCustomOptionPicker(getAty(), "请选择省份", beans, current -> {
                    mProvince = beans.get(current).getContext();
                    String mPid = beans.get(current).getId();
                    getArea(mPid, 2);
                });
            } else if (areaType == 2) {
                WheelPickerUtils.getInstance().initCustomOptionPicker(getAty(), "请选择城市", beans, current -> {

                    WheelPickerBean wheelPickerBean = beans.get(current);
                    mCity = wheelPickerBean.getContext();
                    String mPid = wheelPickerBean.getId();

                    mCityCode = wheelPickerBean.code;
                    getArea(mPid, 3);
                });
            } else {
                WheelPickerUtils.getInstance().initCustomOptionPicker(getAty(), "请选择地区", beans, current -> {
                    WheelPickerBean wheelPickerBean = beans.get(current);
                    String area =wheelPickerBean.getContext();
                    mAreaCode = wheelPickerBean.code;
                    tvCity.setText(mProvince + mCity + area);
                });
            }
        }
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //此处可以根据两个Code进行判断，本页面和结果页面跳过来的值
        if (requestCode == 1 && resultCode == 3) {
            oneCardId = data.getStringExtra("cardId");
            String cardInfo = data.getStringExtra("cardInfo");
            mTvSelectCard.setText(cardInfo);
        }
        if (requestCode == -1 && resultCode == 0) {
            finish();
        }
    }

    /**
     * 操作协议
     */
    private void getAgeree() {
        HttpFactory.getInstance().getCardrepaymentInfo(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""))
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(OneMoreEnactPlanActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<SmsBean>>() {
                    @Override
                    public void success(HttpResponseData<SmsBean> data) {
                        if ("9999".equals(data.status)) {
                            BaseWebViewData webViewData = new BaseWebViewData();
                            webViewData.title = "操作协议";
                            webViewData.content = data.getData().cardrepayment;
                            startAct(OneMoreEnactPlanActivity.this, BaseWebViewActivity.class, webViewData);
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
        if (!TextUtils.isEmpty(onEvent) && onEvent.equals(AppCacheInfo.mSubmitOneMoreRepayMent)) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
