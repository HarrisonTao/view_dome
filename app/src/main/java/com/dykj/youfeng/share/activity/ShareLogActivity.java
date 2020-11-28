package com.dykj.youfeng.share.activity;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.ShareBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.share.adapter.ShareLogAdapter;
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

public class ShareLogActivity extends BaseActivity {
    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.mSmartRefresh)
    SmartRefreshLayout mSmartRefresh;

    private int mPage = 1;
    private ShareLogAdapter mAdapter = new ShareLogAdapter(R.layout.item_share_log);


    @Override
    public int intiLayout() {
        return R.layout.activity_recycler;
    }

    @Override
    public void initData() {
        initTitle("分享记录");

        mRecycler.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.layout_empty, mRecycler);

        requestList(true);
        mSmartRefresh.setOnRefreshListener(refreshLayout -> requestList(true));

        mSmartRefresh.setOnLoadMoreListener(refreshLayout -> requestList(false));
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

        HttpFactory.getInstance().shareList(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mPage + "")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(ShareLogActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<ShareBean>>() {
                    @Override
                    public void success(HttpResponseData<ShareBean> data) {
                        if ("9999".equals(data.status)) {
                            ShareBean shareBean = data.getData();
                            if (null != shareBean) {
                                SmartRefreshUtils.loadMore(mAdapter, mPage, shareBean.page, shareBean.list, mSmartRefresh);
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
