package com.dykj.youfeng.mine.yue;

import android.widget.TextView;

import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.SmartRefreshUtils;
import com.dykj.module.util.TabLayoutVPAdapter;
import com.dykj.module.util.toast.DyToast;
import com.dykj.module.view.dialog.ConfirmDialog;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mine.authentication.AuthenticationActivity;
import com.dykj.youfeng.mine.yue.cash.TakeOutActivity;
import com.dykj.youfeng.mine.yue.transfer.TransferActivity;
import com.dykj.youfeng.mode.BalanceBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.google.android.material.tabs.TabLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 余额
 */
public class BalanceActivity extends BaseActivity {

    @BindView(R.id.tv_accountMoney)
    TextView tvAccountMoney;
    @BindView(R.id.tv_takeout)
    TextView tvTakeout;
    @BindView(R.id.tv_todayMoney)
    TextView tvTodayMoney;
    @BindView(R.id.tv_all_money)
    TextView tvAllMoney;
    //@BindView(R.id.tv_type)
//    TextView tvType;
//    @BindView(R.id.recycler)
//    RecyclerView mRecycler;
    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.mSmartRefresh)
    SmartRefreshLayout mSmartRefresh;

    @BindView(R.id.tv_transfer)
    TextView tvTransfer;

    private int mPage = 1;
    private BalanceAdapter mAdapter = new BalanceAdapter(R.layout.item_integral);
    private int mType;

    @Override
    public int intiLayout() {
        return R.layout.activity_balance;
    }

    @Override
    public void initData() {

        mSmartRefresh.setEnableRefresh(false).setEnableLoadMore(false);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.layout_empty, mRecycler);

    }

    @Override
    public void doBusiness() {
        requestListMoney(true);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        requestListMoney(true);
    }

    @OnClick(R.id.tv_takeout)        // 提现
    public void onClickView() {
        int realnameStatus1 = MMKV.defaultMMKV().decodeInt(AppCacheInfo.mRealnameStatus, 0);
        if (0 == realnameStatus1) {
            new ConfirmDialog(this, () ->
                    startAct(this, AuthenticationActivity.class)
                    , "您还未实名", "去实名").setLifecycle(getLifecycle()).show();
        } else {
            startAct(TakeOutActivity.class);
        }
    }


    @OnClick(R.id.tv_transfer)
    public void transfer() {
        // 转账
        startAct(TransferActivity.class);
    }

    private void requestListMoney(boolean isResh) {
        if (isResh) {
            mPage = 1;
        } else {
            mPage++;
        }

        HttpFactory.getInstance().listMoney(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), mPage + "")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(BalanceActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<BalanceBean>>() {
                    @Override
                    public void success(HttpResponseData<BalanceBean> data) {
                        if ("9999".equals(data.status)) {
                            BalanceBean balanceBean = data.getData();
                            if (null != balanceBean) {
                                setViewData(balanceBean);
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

    /**
     * 设置view 数据
     *
     * @param balanceBean
     */
    private void setViewData(BalanceBean balanceBean) {
        tvAccountMoney.setText(balanceBean.accountMoney);
        tvAllMoney.setText(balanceBean.all);
        tvTodayMoney.setText(balanceBean.today);

        SmartRefreshUtils.loadMore(mAdapter, 1, balanceBean.page, balanceBean.list, mSmartRefresh);

    }

}
