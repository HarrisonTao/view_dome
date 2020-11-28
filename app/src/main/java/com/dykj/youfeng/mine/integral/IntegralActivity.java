package com.dykj.youfeng.mine.integral;

import android.widget.TextView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.IntegralBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.SmartRefreshUtils;
import com.dykj.module.util.toast.DyToast;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 积分
 */
public class IntegralActivity extends BaseActivity {
    @BindView(R.id.tv_points)
    TextView tvPoints;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.mSmartRefresh)
    SmartRefreshLayout mSmartRefresh;
    private int mPage = 1;

    private IntegralAdapter mAdapter = new IntegralAdapter(R.layout.item_integral);

    @Override
    public int intiLayout() {
        return R.layout.activity_integral;
    }

    @Override
    public void initData() {

        requestList(true);
        mSmartRefresh.setEnableRefresh(false);
        mSmartRefresh.setOnLoadMoreListener(refreshLayout -> requestList(false));
        mRecycler.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.layout_empty, mRecycler);

    }

    @Override
    public void doBusiness() {

    }


    private void requestList(boolean isRefresh) {
        if (isRefresh) {
            mPage = 1;
        } else {
            mPage++;
        }

        Map<String, Object> parms = new HashMap<>();
        parms.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        parms.put("page", mPage);
        parms.put("limit", "10");
        HttpFactory.getInstance().mePointsList(parms)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(IntegralActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<IntegralBean>>() {
                    @Override
                    public void success(HttpResponseData<IntegralBean> data) {
                        if ("9999".equals(data.status)) {
                            IntegralBean data1 = data.getData();
                            tvPoints.setText(data1.points + "");
                            SmartRefreshUtils.loadMore(mAdapter, mPage, data1.page, data1.list, mSmartRefresh);
                        } else {
                            DyToast.getInstance().error(data.message);
                            if (isRefresh) {
                                mSmartRefresh.finishRefresh();
                            } else {
                                mSmartRefresh.finishLoadMore();
                            }
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
