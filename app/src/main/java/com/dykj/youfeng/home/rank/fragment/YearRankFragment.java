package com.dykj.youfeng.home.rank.fragment;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.blankj.utilcode.util.LogUtils;
import com.contrarywind.view.WheelView;
import com.dykj.youfeng.R;
import com.dykj.youfeng.home.rank.adpter.RankAdapter;
import com.dykj.youfeng.mode.AgentListBean;
import com.dykj.youfeng.mode.BonusIndexBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.base.BaseFragment;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.DateUtils;
import com.dykj.module.util.DialogUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.tencent.mmkv.MMKV;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;

/**
 * 半年榜
 */
public class YearRankFragment extends BaseFragment {
    @BindView(R.id.tv_bonus)
    TextView tvBonus;
    @BindView(R.id.tv_periods)
    TextView tvPeriods;
    @BindView(R.id.count_down_view)
    CountdownView mCountDownView;
    @BindView(R.id.tv_select_time)
    TextView tvSelectTime;
    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.mSmartRefresh)
    SmartRefreshLayout mSmartRefresh;
    private int mCurMonth;
    private int mType;
    private List<String> mPeriods_list;
    private RankAdapter mAdapter = new RankAdapter(R.layout.item_rank);
    private int mPager = 1;
    private String mCurPeriods;

    @Override
    protected int intiLayout() {
        return R.layout.fragment_rank;
    }

    @Override
    protected void onViewReallyCreated(View view) {
        mRecycler.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.layout_empty, mRecycler);
        mCurMonth = Calendar.getInstance(Locale.CHINA).getActualMaximum(Calendar.MONTH);
        mType = (mCurMonth >= 1 && mCurMonth <= 6) ? 1 : ((mCurMonth >= 7 && mCurMonth <= 12) ? 0 : -1);   //1:上半年,0:下半年

        mSmartRefresh.setEnableRefresh(false);
        mSmartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (!TextUtils.isEmpty(mCurPeriods)){
                    mPager ++;
                    requestMonth(mCurPeriods);
                }
            }
        });
    }

    @Override
    public void doBusiness() {
        requestData();
    }


    private void requestData() {
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        map.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        map.put("type", mType);
        HttpFactory.getInstance().bonusIndex(map)
                .compose(HttpObserver.schedulers(getActivity()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<BonusIndexBean>>() {
                    @Override
                    public void success(HttpResponseData<BonusIndexBean> data) {
                        if ("9999".equals(data.status)) {
                            BonusIndexBean bonusIndexBean = data.getData();
                            setViewData(bonusIndexBean);
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
     * 设置view数据
     *
     * @param bonusIndexBean
     */
    private void setViewData(BonusIndexBean bonusIndexBean) {
        BonusIndexBean.InfoBean info = bonusIndexBean.info;
        tvBonus.setText("￥" + info.value);
        String day_last = info.day_end;
        String day = info.day_now;
        long endTime = DateUtils.getStringToDateTwo(day_last);
        long curTime = DateUtils.getStringToDateTwo(day);
        mCountDownView.start(endTime - curTime);

        mCurPeriods = bonusIndexBean.periods;
        tvSelectTime.setText(mCurPeriods + "期");
        mPeriods_list = bonusIndexBean.periods_list;
        if (null != mPeriods_list) {
            for (int i = 0; i < mPeriods_list.size(); i++) {
                String s = mPeriods_list.get(i);
                if (!TextUtils.isEmpty(mCurPeriods) && mCurPeriods.equals(s)) {
                    tvPeriods.setText((i + 1) + "/");
                    continue;
                }
            }
        }
        requestMonth(mCurPeriods);
    }


    @OnClick(R.id.tv_select_time)
    public void onClickView() {
        if (null != mPeriods_list) {
            showMonthDayDialog();
        }
    }

    /**
     * 请求排行列表
     *
     * @param mCurPeriods
     */
    private void requestMonth(String mCurPeriods) {
        LogUtils.e(mCurPeriods);
        try {
            String[] split = mCurPeriods.split("-");
            String year = split[0];    // 2019/07
            String[] strYear = year.split("/");      // 2019   07
            String month = strYear[1];
            String newMonth = "";
            if (month.startsWith("0")) {
                newMonth = month.replace("0", "");
            }
            int type = (Integer.parseInt(newMonth) >= 1 && Integer.parseInt(newMonth) <= 6) ? 1 : 0;   // 1:上半年,0:下半年
            HttpFactory.getInstance().bonusAgentYearList(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), strYear[0], type + "",mPager +"")
                    .compose(HttpObserver.schedulers(getActivity()))
                    .as(HttpObserver.life(this))
                    .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<List<AgentListBean>>>() {
                        @Override
                        public void success(HttpResponseData<List<AgentListBean>> data) {
                            if ("9999".equals(data.status)) {
                                List<AgentListBean> listBean = data.getData();
                                if (null != listBean) {
                                    mAdapter.setNewData(listBean);
                                }else {
                                    mAdapter.setNewData(null);
                                    mAdapter.setEmptyView(R.layout.layout_empty,mRecycler);
                                }
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
        } catch (Exception e) {

        }

    }

    private void showMonthDayDialog() {
        DialogUtils.getInstance()
                .with(getContext())
                .setlayoutPosition(Gravity.BOTTOM)
                .setlayoutPading(0, 0, 0, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FROM_BOTTOM)
                .setlayoutId(R.layout.dialog_recyclerview)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    TextView tvTitle = view.findViewById(R.id.tv_tile);
                    tvTitle.setText("选择期数");
                    WheelView wheelView = view.findViewById(R.id.wheelview);
                    wheelView.setCyclic(false);
                    wheelView.setAdapter(new ArrayWheelAdapter(mPeriods_list));
                    view.findViewById(R.id.tv_ok).setOnClickListener(v -> {
                        String pitch = mPeriods_list.get(wheelView.getCurrentItem());
                        mCurPeriods = pitch;
                        mPager = 1;
                        tvSelectTime.setText(pitch + "期");
                        requestMonth(mCurPeriods);
                        DialogUtils.dismiss();
                    });
                    view.findViewById(R.id.tv_cancel).setOnClickListener(v -> DialogUtils.dismiss());
                })
                .show();
    }
}
