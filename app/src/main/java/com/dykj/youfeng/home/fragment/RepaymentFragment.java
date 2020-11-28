package com.dykj.youfeng.home.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.dykj.youfeng.R;
import com.dykj.youfeng.home.repayment.activity.PlanDetailActivity;
import com.dykj.youfeng.home.repayment.adapter.RepaymentAdapter;
import com.dykj.youfeng.mode.PlanDetailBean;
import com.dykj.youfeng.mode.RepaymentPlanListBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.base.BaseFragment;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.SmartRefreshUtils;
import com.dykj.module.util.toast.DyToast;
import com.dykj.module.view.dialog.ConfirmDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.ConcurrentHashMap;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class RepaymentFragment extends BaseFragment {


    private static final String ARG_IDS = "id";
    private static final String ARG_type = "type";
    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.mSmartRefresh)
    SmartRefreshLayout mSmartRefresh;
    private RepaymentAdapter mAdapter = new RepaymentAdapter(R.layout.item_repayment);
    private int mPage = 1;
    private String mStatus, mType;
    private int mRepId;

    public static RepaymentFragment newInstance(String status, String type) {
        RepaymentFragment fragment = new RepaymentFragment();
        Bundle arg = new Bundle();
        arg.putString(ARG_IDS, status);
        arg.putString(ARG_type, type);
        fragment.setArguments(arg);
        return fragment;
    }


    @Override
    protected int intiLayout() {
        return R.layout.activity_recycler;
    }

    @Override
    protected void onViewReallyCreated(View view) {
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        mSmartRefresh.setBackgroundColor(getResources().getColor(R.color.bg_color));
        mStatus = getArguments().getString(ARG_IDS);
        mType = getArguments().getString(ARG_type);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.layout_empty, mRecycler);
        mAdapter.setOnItemClickListener((adapter, view1, position) -> {
            // 查看计划
            PlanDetailBean bean = new PlanDetailBean();
            bean.planType = "oneMoreRepayment".equals(mType) ? 2 : 1;    // 1 是正常还款 2是宜卡多换
            bean.type = "repId";
            bean.repayment_id = mAdapter.getData().get(position).id + "";
            startAct(PlanDetailActivity.class, bean);
        });

        mAdapter.setOnItemChildClickListener((adapter, view12, position) -> {
            mRepId = mAdapter.getData().get(position).id;
            new ConfirmDialog(getContext(), () -> {
                // 取消计划

                if ("oneMoreRepayment".equals(mType)) {
                    planCancel();
                } else if ("Repaymentplan".equals(mType)) {
                    String channelType =mAdapter.getData().get(position).channel_type;
                    if (channelType .contains("jft")||  channelType .contains("Jft")) {
                        String code=channelType .replace("jft","");
                        code=code.replace("Jft","");
                        jftCancelPlan(mAdapter.getData().get(position).cardId+"",code);
                    }else if (TextUtils.indexOf(channelType, "Kjt") > -1) {
                      kjtCancelPlan(mAdapter.getData().get(position).cardId+"");
                    }else {
                        planCommonRepaymentPlan();
                    }

                }
            }, "确定取消该计划?").show();
        });
    }

    /**
     * 快捷通取消计划
     * @param cardid
     */
    private void kjtCancelPlan(String cardid) {
        HttpFactory.getInstance().kjtRepayCancel(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), cardid)
                .compose(HttpObserver.schedulers(getContext()))
                .as(HttpObserver.life(RepaymentFragment.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            EventBus.getDefault().post(AppCacheInfo.mCancelPlan);
                            DyToast.getInstance().success(data.message);
                            doBusiness();
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));
    }


    @Override
    public void doBusiness() {
        requestCreditCardList(true);
        mSmartRefresh.setOnRefreshListener(refreshLayout -> requestCreditCardList(true));

        mSmartRefresh.setOnLoadMoreListener(refreshLayout -> requestCreditCardList(false));
    }


    /**
     * 列表
     */
    private void requestCreditCardList(boolean isRefresh) {
        if ("oneMoreRepayment".equals(mType)) {     // 一卡多还
            requestOneMorePlanList(isRefresh);
        } else if ("Repaymentplan".equals(mType)) {   // 普通还款列表
            requestRepaymentPlanList(isRefresh);
        }
    }

    /**
     * 请求普通还款列表
     */
    private void requestRepaymentPlanList(boolean isRefresh) {
        if (isRefresh) {
            mPage = 1;
        } else {
            mPage++;
        }
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap();
        map.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        map.put("status", mStatus);
        map.put("cardId", "0");   // 全部
        map.put("page", mPage);
        HttpFactory.getInstance().oneRepaymentList(map)
                .compose(HttpObserver.schedulers(getActivity()))
                .as(HttpObserver.life(RepaymentFragment.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<RepaymentPlanListBean>>() {
                    @Override
                    public void success(HttpResponseData<RepaymentPlanListBean> data) {
                        if ("9999".equals(data.status)) {
                            RepaymentPlanListBean data1 = data.getData();
                            if (null != data1) {
                                SmartRefreshUtils.loadMore(mAdapter, mPage, data1.page, data1.list, mSmartRefresh);
                            }
                            finishRefrsh(isRefresh);
                        } else {
                            DyToast.getInstance().error(data.message);
                            finishRefrsh(isRefresh);
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
     * 请求一卡还多卡 列表
     *
     * @param isRefresh
     */
    private void requestOneMorePlanList(boolean isRefresh) {
        if (isRefresh) {
            mPage = 1;
        } else {
            mPage++;
        }

        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap();
        map.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        map.put("status", mStatus);
        map.put("page", mPage);
        HttpFactory.getInstance().oneMoreRepaymentList(map)
                .compose(HttpObserver.schedulers(getActivity()))
                .as(HttpObserver.life(RepaymentFragment.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<RepaymentPlanListBean>>() {
                    @Override
                    public void success(HttpResponseData<RepaymentPlanListBean> data) {
                        if ("9999".equals(data.status)) {
                            RepaymentPlanListBean data1 = data.getData();
                            if (null != data1) {
                                SmartRefreshUtils.loadMore(mAdapter, mPage, data1.page, data1.list, mSmartRefresh);
                            }
                            finishRefrsh(isRefresh);
                        } else {
                            DyToast.getInstance().error(data.message);
                            finishRefrsh(isRefresh);
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));
    }


    private void finishRefrsh(boolean isRefresh) {
        if (isRefresh) {
            mSmartRefresh.finishRefresh();
        } else {
            mSmartRefresh.finishLoadMore();
        }
    }

    /**
     * 取消计划 一卡多还
     */
    private void planCancel() {
        HttpFactory.getInstance().cancelPlan(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mRepId + "")
                .compose(HttpObserver.schedulers(getContext()))
                .as(HttpObserver.life(RepaymentFragment.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            DyToast.getInstance().success(data.message);
                            EventBus.getDefault().post(AppCacheInfo.mCancelPlan);
                            doBusiness();
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
                .compose(HttpObserver.schedulers(getContext()))
                .as(HttpObserver.life(RepaymentFragment.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            EventBus.getDefault().post(AppCacheInfo.mCancelPlan);
                            DyToast.getInstance().success(data.message);
                            doBusiness();
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
    private void jftCancelPlan(String cardId,String code) {
        HttpFactory.getInstance().jftCancel(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), cardId,code)
                .compose(HttpObserver.schedulers(getContext()))
                .as(HttpObserver.life(RepaymentFragment.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            EventBus.getDefault().post(AppCacheInfo.mCancelPlan);
                            DyToast.getInstance().success(data.message);
                            doBusiness();
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
    public void onEvent(String event) {
        if (!TextUtils.isEmpty(event) && event.equals(AppCacheInfo.mCancelPlan)){
            doBusiness();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
