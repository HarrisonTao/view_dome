package com.dykj.youfeng.mine.msg;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.SystemMegBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.base.BaseWebViewActivity;
import com.dykj.module.entity.BaseWebViewData;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.SmartRefreshUtils;
import com.dykj.module.util.toast.DyToast;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.mmkv.MMKV;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 系统公告
 */
public class SystemMessageActivity extends BaseActivity {

    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.mSmartRefresh)
    SmartRefreshLayout mSmartRefresh;
    private SystemMessageAdapter mAdapter = new SystemMessageAdapter(R.layout.item_system_msg);
    private int mPage = 1;

    @Override
    public int intiLayout() {
        return R.layout.activity_recycler;
    }

    @Override
    public void initData() {
        initTitle("系统公告");
        mRecycler.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.layout_empty, mRecycler);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {

            List<SystemMegBean.ListBean> data = mAdapter.getData();
            SystemMegBean.ListBean listBean = data.get(position);

            BaseWebViewData baseWebViewData = new BaseWebViewData();
            baseWebViewData.title = listBean.title;
            baseWebViewData.content = listBean.url;
            startAct(BaseWebViewActivity.class, baseWebViewData);
        });

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

        HttpFactory.getInstance().articleList(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mPage + "")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(SystemMessageActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<SystemMegBean>>() {
                    @Override
                    public void success(HttpResponseData<SystemMegBean> data) {
                        if ("9999".equals(data.status)) {
                            SystemMegBean systemMegBean = data.getData();
                            if (null != systemMegBean) {
                                SmartRefreshUtils.loadMore(mAdapter, mPage, systemMegBean.page, systemMegBean.list, mSmartRefresh);
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
