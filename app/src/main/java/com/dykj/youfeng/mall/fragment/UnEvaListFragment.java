package com.dykj.youfeng.mall.fragment;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mall.activity.CommitEvaActivity;
import com.dykj.youfeng.mall.adapter.UnEvaListAdapter;
import com.dykj.youfeng.mode.EvaListBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.view.EmptyView;
import com.dykj.module.AppConstant;
import com.dykj.module.base.BaseFragment;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by lcjing on 2019/8/15.
 * 未评价
 */
public class UnEvaListFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private List<EvaListBean.ListBean> list;
    private UnEvaListAdapter adapter;

    @Override
    protected int intiLayout() {
        return R.layout.activity_refund_list;
    }

    @Override
    protected void onViewReallyCreated(View view) {
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getData();
            }
        });
    }

    @Override
    public void doBusiness() {
        list = new ArrayList<>();
        adapter = new UnEvaListAdapter(R.layout.item_un_eva, list);
        adapter.setEmptyView(EmptyView.getEmptyView(EmptyView.NORM,getContext()));
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                Bundle bundle = new Bundle();
//                bundle.putString("order_id", list.get(position).getOrder_id());
//                jumpToPage(CommitEvaActivity.class, bundle, true, 1);
                startAct(CommitEvaActivity.class,list.get(position).order_id, 1);
            }
        });
        recyclerView.setAdapter(adapter);
        getData();
    }

    private int page = 1;

    private void getData() {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().unEvaluateIndex(mToken, "0", page)
                .compose(HttpObserver.schedulers(getContext()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<EvaListBean>>() {
                    @Override
                    public void success(HttpResponseData<EvaListBean> data) {
                        if (data.status.equals(AppConstant.SUCCESS)) {
                            //设置数据
                            if (page == 1) {
                                list.clear();
                                adapter.notifyDataSetChanged();
                            }
                            refreshLayout.finishRefresh();
                            if (data.getData().all_page > page) {
                                refreshLayout.finishLoadMore();
                            } else {
                                refreshLayout.finishLoadMoreWithNoMoreData();
                            }

                            list.addAll(data.getData().list);
                            adapter.notifyDataSetChanged();
                        } else {
                            DyToast.getInstance().warning(data.getMessage());
                            refreshLayout.finishRefresh().finishLoadMoreWithNoMoreData();
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        if (!"数据解析异常".equals(data.getMessage())) {
                            DyToast.getInstance().error(data.getMessage());
                        }
                        refreshLayout.finishRefresh().finishLoadMoreWithNoMoreData();
                    }
                }));

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        page=1;
        getData();
    }


}
