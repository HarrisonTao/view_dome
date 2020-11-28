package com.dykj.youfeng.mall.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mall.adapter.EvaGoodsAdapter;
import com.dykj.youfeng.mode.GoodsEvaBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.view.EmptyView;
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
import butterknife.OnClick;


/**
 * Created by lcjing on 2019/8/14.
 */
public class EvaListFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private List<GoodsEvaBean.ListBean> list;

    private String goodsId = "";
    private EvaGoodsAdapter adapter;

    public static EvaListFragment getInstance(String goodsId) {
        EvaListFragment fragment = new EvaListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("goodsId", goodsId);
        fragment.setArguments(bundle);
        return fragment;
    }


    private int page = 1;


    @Override
    protected int intiLayout() {
        return R.layout.fragment_eva_list;
    }

    @Override
    protected void onViewReallyCreated(View view) {
        tvTitle.setText("商品评价");
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
        goodsId = getArguments().getString("goodsId");
        list = new ArrayList<>();
        adapter = new EvaGoodsAdapter(R.layout.item_eva_goods, list);
        adapter.setEmptyView(R.layout.layout_empty,recycler);
        recycler.setAdapter(adapter);
        getData();
    }

    private void getData() {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().allGoodsEva(mToken, goodsId, page)
                .compose(HttpObserver.schedulers(getContext()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<GoodsEvaBean>>() {
                    @Override
                    public void success(HttpResponseData<GoodsEvaBean> data) {
                        if (page == 1) {
                            list.clear();
                            adapter.notifyDataSetChanged();
                        }
                        if ("9999".equals(data.status)) {
                            list.addAll(data.getData().list);
                            adapter.notifyDataSetChanged();
                            refreshLayout.finishRefresh();
                            if (data.getData().all_page <= page) {
                                refreshLayout.finishLoadMoreWithNoMoreData();
                            } else {
                                refreshLayout.finishLoadMore();
                            }
                        } else {
                            if (!"1015".equals(data.status)) {
                                DyToast.getInstance().error(data.message);
                            }
                            refreshLayout.finishRefresh().finishLoadMoreWithNoMoreData();
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                        refreshLayout.finishRefresh().finishLoadMoreWithNoMoreData();
                    }
                }));
    }

    @OnClick(R.id.iv_left)
    public void onViewClicked() {
        if (back!=null) {
            back.back();
        }
    }
    private Back back;

    public void setBack(Back back) {
        this.back = back;
    }

    public interface  Back{
        void back();
    }


}
