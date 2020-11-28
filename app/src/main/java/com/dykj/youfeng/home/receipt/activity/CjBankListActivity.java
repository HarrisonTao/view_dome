package com.dykj.youfeng.home.receipt.activity;

import com.dykj.youfeng.R;
import com.dykj.youfeng.home.receipt.adapter.CjBankListAdapter;
import com.dykj.youfeng.mode.CjBankBean;
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

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/***
 * 畅捷 银行
 */
public class CjBankListActivity extends BaseActivity {
    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.mSmartRefresh)
    SmartRefreshLayout mSmartRefresh;

    private CjBankListAdapter mAdapter = new CjBankListAdapter(R.layout.item_yb_list);
    private String mChannel;

    @Override
    public int intiLayout() {
        return R.layout.activity_recycler;
    }

    @Override
    public void initData() {
        initTitle("支持银行列表");
        mChannel = (String) getIntentData();
        mSmartRefresh.setEnableRefresh(false).setEnableLoadMore(false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecycler.setLayoutManager(gridLayoutManager);
        mRecycler.setAdapter(mAdapter);

        getData();

    }

    private void getData() {
        HttpFactory.getInstance().cjBankList(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""),mChannel)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<List<CjBankBean>>>() {
                    @Override
                    public void success(HttpResponseData<List<CjBankBean>> data) {
                        if ("9999".equals(data.status)) {
                            SmartRefreshUtils.loadMore(mAdapter,data.getData(),mSmartRefresh);
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

    @Override
    public void doBusiness() {

    }
}
