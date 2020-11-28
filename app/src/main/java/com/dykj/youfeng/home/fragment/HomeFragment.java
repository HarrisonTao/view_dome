package com.dykj.youfeng.home.fragment;

import android.Manifest;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager2.widget.ViewPager2;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dykj.module.base.BaseFragment;
import com.dykj.module.base.BaseWebViewActivity;
import com.dykj.module.entity.BaseWebViewData;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.BaseToolsUtil;
import com.dykj.module.util.GlideImageLoader;
import com.dykj.module.util.GlideUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.dykj.module.view.dialog.ConfirmDialog;
import com.dykj.youfeng.R;
import com.dykj.youfeng.home.base.Planlist;
import com.dykj.youfeng.home.kf.HelpBookActivity;
import com.dykj.youfeng.home.kf.KfActivity;
import com.dykj.youfeng.home.rank.act.RankActivity;
import com.dykj.youfeng.home.rank.adpter.TackRankAdapter;
import com.dykj.youfeng.home.receipt.activity.QuickReceiptActivity;
import com.dykj.youfeng.home.repayment.activity.PlanDetailActivity;
import com.dykj.youfeng.home.repayment.activity.RepaymentChannelActivity;
import com.dykj.youfeng.home.repayment.activity.RepaymentOneMoreActivity;
import com.dykj.youfeng.home.repayment.activity.RepaymentPlanActivity;
import com.dykj.youfeng.home.sign.act.SignActivity;
import com.dykj.youfeng.mine.authentication.AuthenticationActivity;
import com.dykj.youfeng.mine.bank.BankMsgActivity;
import com.dykj.youfeng.mine.integral.IntegralActivity;
import com.dykj.youfeng.mine.msg.MessageActivity;
import com.dykj.youfeng.mine.yue.BalanceActivity;
import com.dykj.youfeng.mode.HomeBean;
import com.dykj.youfeng.mode.PlanDetailBean;
import com.dykj.youfeng.mode.RepaymentPlanListBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.start.LoginActivity;
import com.dykj.youfeng.tools.AmapLocationUils;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.vip.activity.VipActivity;
import com.google.gson.Gson;
import com.sunfusheng.marqueeview.MarqueeView;
import com.tencent.mmkv.MMKV;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lcjingon 2019/10/22.
 * description:
 */

public class HomeFragment extends BaseFragment {

    protected List<String> bannerTop, strNoticeList, bannerFoot;
    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.tv_consultation)
    MarqueeView marqueeView;
    @BindView(R.id.foot_banner)
    Banner mFootBanner;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_msg)
    ImageView ivMsg;
    @BindView(R.id.qiandao_msg)
    ImageView qiandao_msg;
    @BindView(R.id.taskSizeText)
    TextView taskSizeText;
    @BindView(R.id.pagerVeiwLinear)
    LinearLayout pager2Veiw;

    @BindView(R.id.viewPagerGuper)
    ViewPager2 viewPagerGuper;
    @BindView(R.id.bankImage1)
    ImageView bankImage1;
    @BindView(R.id.bankImage2)
    ImageView bankImage2;
    @BindView(R.id.bankImage3)
    ImageView bankImage3;
    @BindView(R.id.repaymentRelatice)
    RelativeLayout repaymentRelatice;

    TackRankAdapter tackRankAdapter;

    private List<String> bannerTitle, bannerFootTitle;

    HomeBean.FootBannerBean footBannerBean = null;

    @Override
    protected int intiLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onViewReallyCreated(View view) {

    }

    @Override
    public void doBusiness() {
        BaseToolsUtil.askPermissions(getActivity(), new BaseToolsUtil.PermissionInterface() {
                    @Override
                    public void ok() {
                        initLocation();
                    }

                    @Override
                    public void error() {
                        DyToast.getInstance().warning("您已拒绝定位相关权限，无法使用该功能");
                    }
                }, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //获取还款计划
        planlist();

        requestHomeData();

        setMsgStaus();

        //禁止滚动true为可以滑动false为禁止
        viewPagerGuper.setUserInputEnabled(true);
        //设置垂直滚动ORIENTATION_VERTICAL，横向的为
        //  viewPagerGuper.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        //切换到指定页，是否展示过渡中间页
        viewPagerGuper.setCurrentItem(1, true);

    }

    @OnClick({R.id.tv_repayment, R.id.tv_collection,
            R.id.tv_oneMore, R.id.tv_ranking,
            R.id.rl_qd, R.id.rl_up, R.id.rl_notebook,
            R.id.qiandao_msg, R.id.rl_points,
            R.id.iv_msg,R.id.repaymentRelatice})
    public void onClickView(View view) {
        switch (view.getId()) {
            case  R.id.repaymentRelatice:
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
            case R.id.tv_repayment:
                // planlist();
                // 还款
                goAct(RepaymentChannelActivity.class);
                break;
            case R.id.tv_collection:
                //DyToast.getInstance().info("暂未开通次服务");
                // 收款
                goAct(QuickReceiptActivity.class);
                break;
            case R.id.tv_oneMore:
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
            case R.id.tv_ranking:
                // 排行榜
                // goAct(RankActivity.class);
                break;
            case R.id.rl_qd:
                goAct(BankMsgActivity.class);
                break;
            case R.id.rl_up:
                DyToast.getInstance().info("请联系客服开通");
                // 会员升级
               // goAct(VipActivity.class);

              /*  String uid = MMKV.defaultMMKV().decodeString(AppCacheInfo.mUid, "");
                if (!TextUtils.isEmpty(uid)) {
                    if (footBannerBean != null) {
                        BaseWebViewData baseWebViewData = new BaseWebViewData();
                        baseWebViewData.title = footBannerBean.title;
                        baseWebViewData.content = footBannerBean.url + "&uid=" + uid;
                        LogUtils.e(baseWebViewData.content);
                        startAct(getFragment(), BaseWebViewActivity.class, baseWebViewData);
                    }
                } else {
                    new ConfirmDialog(getContext(), () ->
                            startAct(this, LoginActivity.class)
                            , "您还未登录", "去登录").setLifecycle(getLifecycle()).show();
                }*/
                break;
            case R.id.rl_notebook:
                // 操作手册
                // getHelpBook();

                // 一卡多还
                //goAct(RepaymentOneMoreActivity.class);
                startAct(this, KfActivity.class);
                break;
            case R.id.qiandao_msg:
                // 签到
                String token = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
                if (TextUtils.isEmpty(token)) {
                    new ConfirmDialog(getContext(), () ->
                            startAct(this, LoginActivity.class)
                            , "您还未登录", "去登录").setLifecycle(getLifecycle()).show();
                } else {
                    startAct(SignActivity.class);
                }

                break;
            case R.id.iv_msg:

                // 消息
                goAct(MessageActivity.class);

                break;
            case R.id.rl_points:

                // 积分
                goAct(IntegralActivity.class);

                break;

        }
    }

    /**
     * 获取帮助文档
     */
    private void getHelpBook() {
        HttpFactory.getInstance().helpBook()
                .compose(HttpObserver.schedulers(getActivity()))
                .as(HttpObserver.life(HomeFragment.this))
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

    private void goAct(Class c) {
        String token = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        int realnameStatus = MMKV.defaultMMKV().decodeInt(AppCacheInfo.mRealnameStatus, 0);
        if (TextUtils.isEmpty(token)) {
            new ConfirmDialog(getContext(), () ->
                    startAct(this, LoginActivity.class)
                    , "您还未登录", "去登录").setLifecycle(getLifecycle()).show();
        } else {
            if (0 == realnameStatus) {
                new ConfirmDialog(getContext(), () ->
                        startAct(this, AuthenticationActivity.class)
                        , "您还未实名", "去实名").setLifecycle(getLifecycle()).show();
            } else {
                startAct(this, c);
            }
        }
    }

    /**
     * 请求首页数据
     */
    public void requestHomeData() {
        String token = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getInstance().homeIndex(token)
                .compose(HttpObserver.schedulers(getActivity()))
                .as(HttpObserver.life(HomeFragment.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<HomeBean>>() {
                    @Override
                    public void success(HttpResponseData<HomeBean> data) {
                        if ("9999".equals(data.status)) {
                            Log.d("wtf", new Gson().toJson(data));
                            setViewData(data.getData());
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
     * 获取还款计划
     */
    public void planlist() {

        String token = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");

     //   Log.d("wtf", "http://yfapi.xunyunsoft.com/api/user/planlist?token=" + token + "&page=1&limit=5");
        HttpFactory.getInstance().planlist(token, 1, 5)
                .compose(HttpObserver.schedulers(getActivity()))
                .as(HttpObserver.life(HomeFragment.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<Planlist>>() {
                    @Override
                    public void success(HttpResponseData<Planlist> data) {

                        Log.d("wtf", "@#" + new Gson().toJson(data) );
                        if ("9999".equals(data.status)) {
                            Planlist planlist=data.getData();
                            if(planlist.list!=null && planlist.list.size()>0) {
                                repaymentRelatice.setVisibility(View.VISIBLE);
                                pager2Veiw.setVisibility(View.VISIBLE);
                                if(tackRankAdapter==null) {
                                    tackRankAdapter=new TackRankAdapter(getActivity(), planlist.list);
                                    tackRankAdapter.setOnItemClickListener(new TackRankAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(Planlist.ListBean beans) {
                                            /*PlanDetailBean bean = new PlanDetailBean();
                                            if(beans.card!=null) {
                                                bean.planType = 1;    // 1 是正常还款 2是宜卡多换
                                                bean.type = "repId";
                                                bean.repId = beans.card.id + "";
                                                startAct(PlanDetailActivity.class, bean);
                                            }*/

                                            startAct(RepaymentPlanActivity.class, "Repaymentplan");
                                        }
                                    });
                                    viewPagerGuper.setAdapter(tackRankAdapter);
                                }else{
                                    tackRankAdapter.notifyDataSetChanged();
                                }
                                int size=planlist.list.size();
                                    taskSizeText.setText("执行中还款计划"+size+"个");
                                for (int i=0;size>i;i++) {
                                    switch (i) {
                                        case  0:
                                            bankImage1.setVisibility(View.VISIBLE);
                                            GlideUtils.setImage(bankImage1, planlist.list.get(i).card_logo);
                                            break;
                                        case  1:
                                            bankImage2.setVisibility(View.VISIBLE);
                                            GlideUtils.setImage(bankImage2, planlist.list.get(i).card_logo);
                                            break;
                                        case  2:
                                            bankImage3.setVisibility(View.VISIBLE);
                                            GlideUtils.setImage(bankImage3, planlist.list.get(i).card_logo);
                                            break;
                                    }
                                }
                            }else{
                                repaymentRelatice.setVisibility(View.GONE);
                                pager2Veiw.setVisibility(View.GONE);
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
     * @param data
     */
    private void setViewData(HomeBean data) {

        /////////////banner//////////
        List<HomeBean.BannerBean> bannerTopList = data.banner;
        bannerTop = new ArrayList<>();
        bannerTitle = new ArrayList<>();
        strNoticeList = new ArrayList<>();
        if (null != bannerTopList && bannerTopList.size() > 0) {
            for (int i = 0; i < bannerTopList.size(); i++) {
                bannerTop.add(bannerTopList.get(i).image);
                bannerTitle.add(bannerTopList.get(i).title);
            }
            mBanner.setBannerTitles(bannerTitle);
            mBanner.setOnBannerListener(position -> {
                HomeBean.BannerBean bannerBean = bannerTopList.get(position);
                String url = bannerBean.url;
                String title = bannerBean.title;
                if (!StringUtils.isEmpty(url)) {
                    BaseWebViewData baseWebViewData = new BaseWebViewData();
                    baseWebViewData.title = title;
                    baseWebViewData.content = url;
                    startAct(getFragment(), BaseWebViewActivity.class, baseWebViewData);
                }
            });
            mBanner.setImages(bannerTop).setImageLoader(new GlideImageLoader()).start();
        }

        ////////////资讯///////
        // 跑马灯
        List<HomeBean.NoticeBean> noticeList = data.notice;
        strNoticeList.clear();
        if (null != noticeList && noticeList.size() > 0) {
            for (int i = 0; i < noticeList.size(); i++) {
                strNoticeList.add(noticeList.get(i).info);
            }
            marqueeView.startWithList(strNoticeList, R.anim.anim_bottom_in, R.anim.anim_top_out);
            marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
                @Override
                public void onItemClick(int position, TextView textView) {
                    HomeBean.NoticeBean b = noticeList.get(position);
                    BaseWebViewData baseWebViewData = new BaseWebViewData();
                    baseWebViewData.title = "资讯详情";
                    //baseWebViewData.content =b. url;
                    startAct(getFragment(), BaseWebViewActivity.class, baseWebViewData);
                }
            });
        }


        //////////////底部banner/////f
        bannerFoot = new ArrayList<>();
        bannerFootTitle = new ArrayList<>();
        List<HomeBean.FootBannerBean> foot_banner = data.foot_banner;
        Log.d("wtf", new Gson().toJson(data.foot_banner));
        for (int i = 0; i < foot_banner.size(); i++) {
            bannerFoot.add(foot_banner.get(i).image);
            bannerFootTitle.add(foot_banner.get(i).title);
            if (foot_banner.get(i).title.equals("优丰申卡")) {
                footBannerBean = foot_banner.get(i);
            }
        }

        mFootBanner.setBannerTitles(bannerFootTitle);
        mFootBanner.setOnBannerListener(position -> {
            HomeBean.FootBannerBean footBannerBean = foot_banner.get(position);
            String url = footBannerBean.url;
            String title = footBannerBean.title;
            if (!url.isEmpty()) {
                String uid = MMKV.defaultMMKV().decodeString(AppCacheInfo.mUid, "");
                if (!TextUtils.isEmpty(uid)) {
                    BaseWebViewData baseWebViewData = new BaseWebViewData();
                    baseWebViewData.title = title;
                    baseWebViewData.content = url + "&uid=" + uid;
                    LogUtils.e(baseWebViewData.content);
                    startAct(getFragment(), BaseWebViewActivity.class, baseWebViewData);
                } else {
                    new ConfirmDialog(getContext(), () ->
                            startAct(this, LoginActivity.class)
                            , "您还未登录", "去登录").setLifecycle(getLifecycle()).show();
                }
            }
        });
        mFootBanner.setImages(bannerFoot).setImageLoader(new GlideImageLoader()).start();
    }


    public void setMsgStaus() {
        /* LogUtils.e("Home", "--------homeFragment---刷新消息");
         int messageCount = MMKV.defaultMMKV().decodeInt(AppCacheInfo.messageCount, 0);
         ivMsg.setImageResource(messageCount > 0 ? R.mipmap.nav_gongg_s : R.mipmap.nav_gongg);*/
    }


    /**
     * 定位
     */
    public void initLocation() {
        AmapLocationUils.initLocation(getContext(), aMapLocation -> {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    showLog("onLocationChanged: " + aMapLocation.getProvince());
                    showLog("onLocationChanged: " + aMapLocation.getCity());
                    showLog("onLocationChanged: " + aMapLocation.getDistrict());
                    //城市信息
                    tvAddress.setText(aMapLocation.getCity());
                } else {
                    DyToast.getInstance().error(aMapLocation.getErrorInfo());
                    showLog("location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();
        AmapLocationUils.stopLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AmapLocationUils.destroyLocation();
    }

}
