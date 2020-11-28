package com.dykj.youfeng.mine.fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dykj.module.base.BaseFragment;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.GlideUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.dykj.module.view.dialog.ConfirmDialog;
import com.dykj.youfeng.R;
import com.dykj.youfeng.home.kf.HelpBookActivity;
import com.dykj.youfeng.home.kf.KfActivity;
import com.dykj.youfeng.home.repayment.activity.RepaymentPlanActivity;
import com.dykj.youfeng.mine.authentication.AuthenticationActivity;
import com.dykj.youfeng.mine.authentication.AuthenticationStep3Activity;
import com.dykj.youfeng.mine.bank.BankMsgActivity;
import com.dykj.youfeng.mine.group.MeGroupActivity;
import com.dykj.youfeng.mine.integral.IntegralActivity;
import com.dykj.youfeng.mine.msg.MessageActivity;
import com.dykj.youfeng.mine.set.PersonageMsgActivity;
import com.dykj.youfeng.mine.set.SetingActivity;
import com.dykj.youfeng.mine.yue.BalanceActivity;
import com.dykj.youfeng.mode.UserInfoBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.share.activity.ShareActivity;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.tools.MmvkUtlis;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的
 */
public class MeFragment extends BaseFragment {

    @BindView(R.id.iv_user_header)
    RoundedImageView ivUserHeader;

    @BindView(R.id.tv_sign)
    TextView tvSign;

    @BindView(R.id.tv_user_name)
    TextView tvUserName;

    @BindView(R.id.tv_vip_grade)
    ImageView tvVipGrade;


    @BindView(R.id.mSmartRefresh)
    SmartRefreshLayout mSmartRefresh;

    @BindView(R.id.todayMoney)
    TextView todayMoney;
    @BindView(R.id.accountMoney)
    TextView accountMoney;


    @BindView(R.id.tv_fx)
    TextView tvFx;
    @BindView(R.id.tv_jf)
    TextView tvJf;
    @BindView(R.id.tv_authentication)
    LinearLayout tvAuthentication;
    @BindView(R.id.tv_seting)
    LinearLayout tvSeting;
    @BindView(R.id.tv_me_item)
    LinearLayout tvMeItem;
    @BindView(R.id.kefuLinear)
    LinearLayout kefuLinear;
    @BindView(R.id.czshoucheLinear)
    LinearLayout czshoucheLinear;

    int level = 0;
    @BindView(R.id.directText)
    TextView directText;
    @BindView(R.id.vipText)
    TextView vipText;
    @BindView(R.id.vipLinear)
    LinearLayout vipLinear;


    @Override
    protected int intiLayout() {
        return R.layout.fragment_me;
    }

    @Override
    protected void onViewReallyCreated(View view) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mSmartRefresh.setOnRefreshListener(refreshLayout -> {
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSmartRefresh.finishRefresh();
                }
            }, 500);

            initNetDataUpUserInfo();

        });

        mSmartRefresh.setEnableLoadMore(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        initNetDataUpUserInfo();
        setViewData(new MmvkUtlis().getUserInfo());
    }

    @Override
    public void doBusiness() {
        setViewData(new MmvkUtlis().getUserInfo());
    }

    /**
     * 设置view 数据
     */
    private void setViewData(UserInfoBean userInfo) {
        GlideUtils.setImage(ivUserHeader, R.mipmap.moren_tx, userInfo.image);
        tvUserName.setText(userInfo.nickName);

        todayMoney.setText(userInfo.todayMoney + "");
        accountMoney.setText(userInfo.accountMoney + " ▶ ");
        //大额费率0.69%·小额费率0.59%·直推返佣0.13%
        StringBuilder bu=new StringBuilder();
        bu.append("大额费率0.");
        bu.append(userInfo.repayRateJft3004Level);
        bu.append("%·小额费率0.");
        bu.append(userInfo.repayRateJft3013Level);
        bu.append("%·直推返佣0.");
        bu.append(userInfo.directRepayProfitLevel);
        bu.append("%还款");
        bu.append(userInfo.repayCostLevel);
        bu.append("元/次");
        directText.setText(bu.toString());
        vipText.setText("开通会员 "+userInfo.vipprice+"元");
        //tvPoints.setText(userInfo.points + "");

        level = userInfo.level;  // 1.普通会员，2.VIP 3.黄金VIP 4.钻石VIP
        switch (level) {
            case 1:
                tvVipGrade.setImageResource(R.mipmap.ic_pt);
                break;
            case 2:
                tvVipGrade.setImageResource(R.mipmap.ic_vip);
                break;
            case 3:
                tvVipGrade.setImageResource(R.mipmap.ic_hj);
                break;
            case 4:
                tvVipGrade.setImageResource(R.mipmap.ic_zs);
                break;

        }


    }

    @OnClick({R.id.todayMoney, R.id.accountMoney,  R.id.tv_group, R.id.tv_repayment_plan,R.id.vipLinear,
            R.id.tv_card_msg, R.id.tv_authentication, R.id.iv_user_header,
            R.id.tv_sign, R.id.tv_fx, R.id.tv_jf, R.id.tv_seting, R.id.czshoucheLinear, R.id.tv_me_item, R.id.kefuLinear})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.todayMoney:
            case R.id.accountMoney:
                int realnameStatus1 = MMKV.defaultMMKV().decodeInt(AppCacheInfo.mRealnameStatus, 0);
                if (0 == realnameStatus1) {
                    new ConfirmDialog(getContext(), () ->
                            startAct(this, AuthenticationActivity.class)
                            , "您还未实名", "去实名").setLifecycle(getLifecycle()).show();
                } else {
                    // 余额
                    startAct(this, BalanceActivity.class);
                }
                break;
            case R.id.vipLinear:
                DyToast.getInstance().info("请联系客服开通");
                /*if(level==2) {
                    DyToast.getInstance().info("您已经是VIP");
                }else{
                    goAct(VipActivity.class);
                }*/
                break;
            case R.id.tv_group:
                // 我的团队
                goAct(MeGroupActivity.class);
                break;
            case R.id.tv_repayment_plan:
                // 还款计划
                int realnameStatus2 = MMKV.defaultMMKV().decodeInt(AppCacheInfo.mRealnameStatus, 0);
                if (0 == realnameStatus2) {
                    new ConfirmDialog(getContext(), () ->
                            startAct(this, AuthenticationActivity.class)
                            , "您还未实名", "去实名").setLifecycle(getLifecycle()).show();
                } else {
                    startAct(RepaymentPlanActivity.class, "Repaymentplan");
                }
                break;
            case R.id.tv_card_msg:
                // 卡片管理
                goAct(BankMsgActivity.class);
                break;
            case R.id.tv_authentication:
                // 实名认证
                int realnameStatus = MMKV.defaultMMKV().decodeInt(AppCacheInfo.mRealnameStatus, 0);
                if (0 == realnameStatus) {
                    startAct(this, AuthenticationActivity.class);
                } else {
                    startAct(this, AuthenticationStep3Activity.class);
                    //  DyToast.getInstance().info("您已实名!");
                }
                break;
            case R.id.iv_user_header:
                startAct(this, PersonageMsgActivity.class);
                break;
            case R.id.tv_fx:
                //分享
                startAct(this, ShareActivity.class);
                break;
            case R.id.tv_jf:
                // 积分
                startAct(this, IntegralActivity.class);
                break;
            case R.id.tv_seting:
                // 设置
                startAct(this, SetingActivity.class);
                break;
            case R.id.czshoucheLinear:
                getHelpBook();
                break;
            case R.id.tv_me_item:
                // 消息
                startAct(this, MessageActivity.class);
                break;
            case R.id.kefuLinear:
                startAct(this, KfActivity.class);
                break;

/*            case R.id.tv_info:
                // 个人信息
            case R.id.iv_user_header:
                startAct(this, PersonageMsgActivity.class);
                break;
           *//* case R.id.ll_earnings:
                // 收益
            case R.id.ll_yue:

                int realnameStatus1 = MMKV.defaultMMKV().decodeInt(AppCacheInfo.mRealnameStatus, 0);
                if (0 == realnameStatus1) {
                    new ConfirmDialog(getContext(), () ->
                            startAct(this, AuthenticationActivity.class)
                            , "您还未实名", "去实名").setLifecycle(getLifecycle()).show();
                } else {
                    // 余额
                    startAct(this, BalanceActivity.class);
                }

                break;

            case R.id.ll_integral:
                // 积分
                startAct(this, IntegralActivity.class);
                break;
*//*
            case R.id.tv_group:
                // 我的团队
                goAct(MeGroupActivity.class);
                break;

            case R.id.tv_repayment_plan:
                // 还款计划
                int realnameStatus2 = MMKV.defaultMMKV().decodeInt(AppCacheInfo.mRealnameStatus, 0);
                if (0 == realnameStatus2) {
                    new ConfirmDialog(getContext(), () ->
                            startAct(this, AuthenticationActivity.class)
                            , "您还未实名", "去实名").setLifecycle(getLifecycle()).show();
                } else {
                    startAct(RepaymentPlanActivity.class, "Repaymentplan");
                }
                break;

            case R.id.tv_card_msg:
                // 卡片管理
                goAct(BankMsgActivity.class);
                break;

            case R.id.tv_authentication:
                // 实名认证
                int realnameStatus = MMKV.defaultMMKV().decodeInt(AppCacheInfo.mRealnameStatus, 0);
                if (0 == realnameStatus) {
                    startAct(this, AuthenticationStep2Activity.class);
                } else {
                    startAct(this, AuthenticationStep3Activity.class);
                    //  DyToast.getInstance().info("您已实名!");
                }
                break;

         *//*   case R.id.tv_deal:
                // 交易记录  deal
                goAct(DealRecordActivity.class);
                break;*//*

            case R.id.tv_kf:
                // 客服
                startAct(this, KfActivity.class);
                break;

            case R.id.tv_seting:
                // 设置
                startAct(this, SetingActivity.class);
                break;

            case R.id.tv_msg:
                // 消息
                startAct(this, MessageActivity.class);
                break;
*//*
            case R.id.tv_server:
                // 系统公告
                startAct(this, SystemMessageActivity.class);
                break;*//*

            case R.id.tv_sign:
                // 签到
                startAct(this, SignActivity.class);
                break;*/
           /* case R.id.lx_kf:
                Intent intent = new MQIntentBuilder(getActivity(), MQConversationActivity.class).build();
                startActivity(intent);
                break;*/
        }
    }

    /**
     * 获取帮助文档
     */
    private void getHelpBook() {
        HttpFactory.getInstance().helpBook()
                .compose(HttpObserver.schedulers(getActivity()))
                .as(HttpObserver.life(MeFragment.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<String>>() {

                    @Override
                    public void success(HttpResponseData<String> data) {
                        if ("9999".equals(data.status)) {
                            startAct(HelpBookActivity.class, data.getData());
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
     * 请求用户信息
     */
    private void initNetDataUpUserInfo() {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        if (TextUtils.isEmpty(mToken)) {
            return;
        }
        HttpFactory.getInstance().getUserInfo(mToken)
                .compose(HttpObserver.schedulers(getActivity()))
                .as(HttpObserver.life(MeFragment.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<UserInfoBean>>() {
                    @Override
                    public void success(HttpResponseData<UserInfoBean> data) {

                        if ("9999".equals(data.status)) {
                            setViewData(data.getData());
                            new MmvkUtlis().saveUserInfo(data.getData());
                        } else {
                            DyToast.getInstance().info(data.message);
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));
    }


    @Subscribe(sticky = true)
    public void onEvent(String event) {
        if (!TextUtils.isEmpty(event) && event.equals(AppCacheInfo.mRefreshUserInfo)) {
            initNetDataUpUserInfo();
        }
    }


    private void goAct(Class c) {
        int realnameStatus = MMKV.defaultMMKV().decodeInt(AppCacheInfo.mRealnameStatus, 0);
        if (0 == realnameStatus) {
            new ConfirmDialog(getContext(), () ->
                    startAct(this, AuthenticationActivity.class)
                    , "您还未实名", "去实名").setLifecycle(getLifecycle()).show();
        } else {
            startAct(this, c);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
