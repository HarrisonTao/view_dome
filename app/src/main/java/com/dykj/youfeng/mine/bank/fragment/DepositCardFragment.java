package com.dykj.youfeng.mine.bank.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.home.repayment.activity.AddDepositCardActivity;
import com.dykj.youfeng.mine.bank.adpter.DepositCardAdapter;
import com.dykj.youfeng.mode.DebitcardListBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.base.BaseFragment;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.SmartRefreshUtils;
import com.dykj.module.util.toast.DyToast;
import com.dykj.module.view.dialog.ConfirmDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 储蓄卡
 */
public class DepositCardFragment extends BaseFragment {

    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;

    @BindView(R.id.tv_card_type)
    TextView tvCardType;

    private int mPage = 1;
    private DepositCardAdapter mAdapter = new DepositCardAdapter(R.layout.item_deposit_card);

    @Override
    protected int intiLayout() {
        return R.layout.fragment_add_card;
    }

    @Override
    protected void onViewReallyCreated(View view) {
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        tvCardType.setText("添加储蓄卡");
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener((adapter, view1, position) -> {
            new ConfirmDialog(getActivity(), () -> deleteDepositCard(mAdapter.getData().get(position).id), "确定删除该卡片?").show();
        });
    }


    @Override
    public void doBusiness() {
        requestDepositCardList(true);

    }

   @OnClick(R.id.tv_card_type)
    public void onClickView() {
        startAct(this, AddDepositCardActivity.class);
    }


    private void requestDepositCardList(boolean isRefresh) {
        if (isRefresh) {
            mPage = 1;
        } else {
            mPage++;
        }

        HttpFactory.getInstance().debitcardList(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), "2", mPage + "")
                .compose(HttpObserver.schedulers(getActivity()))
                .as(HttpObserver.life(DepositCardFragment.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<List<DebitcardListBean>>>() {
                    @Override
                    public void success(HttpResponseData<List<DebitcardListBean>> data) {
                        if ("9999".equals(data.status)) {
                            List<DebitcardListBean> debitcardListBeanList = data.getData();
                            if (null != debitcardListBeanList) {
                              //  SmartRefreshUtils.loadMore(mAdapter, debitcardListBeanList, mSmartRefresh);
                            }else {
                                if(1 == mPage){
                                    mAdapter.setNewData(null);
                                    mAdapter.setEmptyView(R.layout.layout_empty,mRecycler);
                                }
                               // mSmartRefresh.finishRefresh().finishLoadMore();
                            }
                        } else {
                          //  mSmartRefresh.finishRefresh().finishLoadMore();
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
     * 删除储蓄卡
     *
     * @param id
     */
    private void deleteDepositCard(int id) {
        HttpFactory.getInstance().deleteDebitcard(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), id +"")
                .compose(HttpObserver.schedulers(getActivity()))
                .as(HttpObserver.life(DepositCardFragment.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            DyToast.getInstance().success(data.message);
                            requestDepositCardList(true);
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


    @Subscribe
    public void onEvent(String event){
        if (!TextUtils.isEmpty(event) && event.equals(AppCacheInfo.mRefreshDebitcardList)){
            requestDepositCardList(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
