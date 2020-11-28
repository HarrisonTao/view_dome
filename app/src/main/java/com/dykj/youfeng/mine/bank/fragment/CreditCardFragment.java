package com.dykj.youfeng.mine.bank.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dykj.module.base.BaseFragment;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.SmartRefreshUtils;
import com.dykj.module.util.toast.DyToast;
import com.dykj.module.view.dialog.ConfirmDialog;
import com.dykj.youfeng.R;
import com.dykj.youfeng.home.repayment.activity.AddCreditCardActivity;
import com.dykj.youfeng.home.repayment.activity.EditCreditCardActivity;
import com.dykj.youfeng.mine.bank.adpter.CreditCardAdapter;
import com.dykj.youfeng.mode.CreditcardListBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 信用卡
 */
public class CreditCardFragment extends BaseFragment {

    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.tv_card_type)
    TextView tvCardType;
    int mPage=1;

        private ArrayList<CreditcardListBean.ListBean> list;
    private CreditCardAdapter mAdapter ;

    @Override
    protected int intiLayout() {
        return R.layout.fragment_add_card;
    }

    @Override
    protected void onViewReallyCreated(View view) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        list=new ArrayList<CreditcardListBean.ListBean>();

        mAdapter= new CreditCardAdapter(getActivity(),list);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnClickMannageListene(new CreditCardAdapter.OnClickMannageListene() {
            @Override
            public void onCommit(CreditcardListBean.ListBean b) {
                new ConfirmDialog(getActivity(), () -> deleteDepositCard(b.id), "确定删除该卡片?").show();
            }
        });
        mAdapter.setOnEditCardListener(new CreditCardAdapter.OnEditCardListener() {
            @Override
            public void onEditCard(CreditcardListBean.ListBean b) {

                startAct( EditCreditCardActivity.class,b);
            }
        });
        mAdapter.setOnItemClickListener(new CreditCardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                LinearLayout viewLinear=v.findViewById(R.id.viewLinear);
                if(viewLinear.getVisibility()==View.GONE){
                    viewLinear.setVisibility(View.VISIBLE);
                }else{
                    viewLinear.setVisibility(View.GONE);
                }

            }
        });


    }


    @Override
    public void doBusiness() {
        requestCreditCardList(true);

    }


    /**
     * 列表
     */
    private void requestCreditCardList(boolean isRefresh) {
        if (isRefresh) {
            mPage = 1;
            if(list!=null){
                list.clear();
            }
        } else {
            mPage++;
        }

        HttpFactory.getInstance().creditcardList(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), "2", mPage + "", "10")
                .compose(HttpObserver.schedulers(getActivity()))
                .as(HttpObserver.life(CreditCardFragment.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<CreditcardListBean>>() {
                    @Override
                    public void success(HttpResponseData<CreditcardListBean> data) {
                        if ("9999".equals(data.status)) {
                            CreditcardListBean data1 = data.getData();
                            if (null != data1) {

                                if(data1.list!=null && data1.list.size()>0){
                                    list.addAll(data1.list);
                                    mAdapter.notifyDataSetChanged();
                                }
                               // SmartRefreshUtils.loadMore(mAdapter, mPage, data1.page, data1.list, mSmartRefresh);
                            } else {
                              //  mSmartRefresh.finishRefresh().finishLoadMore();
                            }
                        } else {
                            DyToast.getInstance().error(data.message);
                           // mSmartRefresh.finishRefresh().finishLoadMore();
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
     * 删除信用卡
     *
     * @param id
     */
    private void deleteDepositCard(int id) {
        HttpFactory.getInstance().deleteCreditcard(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), id + "")
                .compose(HttpObserver.schedulers(getActivity()))
                .as(HttpObserver.life(CreditCardFragment.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            DyToast.getInstance().success(data.message);
                            requestCreditCardList(true);
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
    public void onEvent(String event) {
        if (!TextUtils.isEmpty(event) && event.equals(AppCacheInfo.mRefreshCreditCardList)) {
            requestCreditCardList(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.tv_card_type)
    public void onViewClicked() {
        startAct(this, AddCreditCardActivity.class);
    }
}
