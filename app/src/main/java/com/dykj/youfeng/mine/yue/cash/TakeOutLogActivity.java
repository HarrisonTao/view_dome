package com.dykj.youfeng.mine.yue.cash;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.CashListBean;
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

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 提现记录
 */
public class TakeOutLogActivity extends BaseActivity {

    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.mSmartRefresh)
    SmartRefreshLayout mSmartRefresh;
    private TakeOutLogAdapter mAdapter = new TakeOutLogAdapter(R.layout.item_plan_log);
    private int mPage = 1;

    @Override
    public int intiLayout() {
        return R.layout.activity_recycler;
    }

    @Override
    public void initData() {
        initTitle("提现记录");
        mRecycler.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.layout_empty,mRecycler);
    }

    @Override
    public void doBusiness() {

        requestList(true);
        mSmartRefresh.setOnRefreshListener(refreshLayout -> requestList(true));

        mSmartRefresh.setOnLoadMoreListener(refreshLayout -> requestList(false));

    }


    private void requestList(boolean isRefresh) {
        if (isRefresh) {
            mPage = 1;
        } else {
            mPage++;
        }

        HttpFactory.getInstance().cashList(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mPage + "")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(TakeOutLogActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<CashListBean>>() {
                    @Override
                    public void success(HttpResponseData<CashListBean> data) {
                        if ("9999".equals(data.status)) {
                            CashListBean cashListBean = data.getData();
                            if (null != cashListBean) {
                                SmartRefreshUtils.loadMore(mAdapter, mPage, cashListBean.page, cashListBean.list, mSmartRefresh);
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
