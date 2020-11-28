package com.dykj.youfeng.mine.yue;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.dykj.module.base.BaseFragment;
import com.dykj.module.util.SmartRefreshUtils;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.BalanceBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class BalanceFragment extends BaseFragment {

    private static String mTAG_ID = "ID";
    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.mSmartRefresh)
    SmartRefreshLayout mSmartRefresh;

    private BalanceAdapter mAdapter = new BalanceAdapter(R.layout.item_integral);
    private int mType;


    public static BalanceFragment newInstance(int mType) {

        Bundle args = new Bundle();
        BalanceFragment fragment = new BalanceFragment();
        args.putSerializable(mTAG_ID, mType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int intiLayout() {
        return R.layout.activity_recycler;
    }

    @Override
    protected void onViewReallyCreated(View view) {
        mSmartRefresh.setEnableRefresh(false).setEnableLoadMore(false);
        mType = (int) getArguments().getSerializable(mTAG_ID);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.layout_empty, mRecycler);
    }

    @Override
    public void doBusiness() {

    }


    public void setData(BalanceBean balanceBean) {

        LogUtils.e("---------");
        SmartRefreshUtils.loadMore(mAdapter, 1, balanceBean.page, balanceBean.list, mSmartRefresh);
    }
}
