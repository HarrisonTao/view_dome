package com.dykj.youfeng.home.sign.act;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.home.sign.adpter.SignLogAdapter;
import com.dykj.youfeng.mode.SignListBean;
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

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class SignLogActivity extends BaseActivity {

    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.mSmartRefresh)
    SmartRefreshLayout mSmartRefresh;
    private SignLogAdapter mAdapter = new SignLogAdapter(R.layout.item_sign_log);
    private int mPage;
    private TextView tvDayCount;
    private TextView tvIntegral;

    @Override
    public int intiLayout() {
        return R.layout.activity_recycler;
    }

    @Override
    public void initData() {
        initTitle("签到记录");
        mAdapter.setEmptyView(R.layout.layout_empty, mRecycler);
        mRecycler.setAdapter(mAdapter);
        View headerView = LayoutInflater.from(this).inflate(R.layout.header_sign, null);
        tvDayCount = headerView.findViewById(R.id.tv_day);
        tvIntegral = headerView.findViewById(R.id.tv_integral);

        mAdapter.addHeaderView(headerView);
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

        HttpFactory.getInstance().signLog(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mPage + "")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<SignListBean>>() {
                    @Override
                    public void success(HttpResponseData<SignListBean> data) {
                        if ("9999".equals(data.status)) {
                            SignListBean signListBean = data.getData();
                            if (null != signListBean) {
                                List<SignListBean.InfoBean> info = signListBean.info;
                                tvDayCount.setText(signListBean.count + "天");
                                if (isRefresh && info.size() > 0) {
                                    tvIntegral.setText(info.get(0).sumPoints);
                                }
                                SmartRefreshUtils.loadMore(mAdapter, info, mSmartRefresh);
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
