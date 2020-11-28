package com.dykj.youfeng.home.receipt.activity;

import com.dykj.youfeng.R;
import com.dykj.youfeng.home.receipt.adapter.QuickReceiptLogAdapter;
import com.dykj.youfeng.mode.QuickListBean;
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
 * 收款记录
 */
public class QuickReceiptLogActivity extends BaseActivity {

    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.mSmartRefresh)
    SmartRefreshLayout mSmartRefresh;
    private QuickReceiptLogAdapter mAdapter = new QuickReceiptLogAdapter(R.layout.item_receipt_log);
    private int mPage = 1;

    @Override
    public int intiLayout() {
        return R.layout.activity_recycler;
    }

    @Override
    public void initData() {
        initTitle("收款记录");
        mRecycler.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.layout_empty,mRecycler);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            String infoId = mAdapter.getData().get(position).id +"";
            startAct(QuickReceiptLogDetailActivity.class,infoId );
        });
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

        HttpFactory.getInstance().quickList(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mPage + "")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<QuickListBean>>() {
                    @Override
                    public void success(HttpResponseData<QuickListBean> data) {
                        if ("9999".equals(data.status)) {
                            QuickListBean quickListBean = data.getData();
                            if (null != quickListBean) {
                                SmartRefreshUtils.loadMore(mAdapter, mPage, quickListBean.all_page, quickListBean.list, mSmartRefresh);
                            } else {
                                mSmartRefresh.finishRefresh().finishLoadMore();
                            }
                        } else {
                            DyToast.getInstance().error(data.message);
                            mSmartRefresh.finishRefresh().finishLoadMore();
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
