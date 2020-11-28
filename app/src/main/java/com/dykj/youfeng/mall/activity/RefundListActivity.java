package com.dykj.youfeng.mall.activity;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mall.adapter.RefundListAdapter;
import com.dykj.youfeng.mode.RefundListBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.view.EmptyView;
import com.dykj.module.AppConstant;
import com.dykj.module.base.BaseActivity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * @author lcjing
 * 售后
 */
public class RefundListActivity extends BaseActivity {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private List<RefundListBean.ListBean> list;
    private RefundListAdapter adapter;

    @Override
    public int intiLayout() {
        return R.layout.activity_refund_list;
    }

    @Override
    public void initData() {
        initTitle("售后");

    }

    @Override
    public void doBusiness() {
        list = new ArrayList<>();
        adapter = new RefundListAdapter(R.layout.item_refund, list);
        adapter.setEmptyView(EmptyView.getEmptyView(EmptyView.NORM,this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            Map<String, String> map = new HashMap<>();
            map.put("order_goods_id", list.get(position).order_goods.rec_id);
            map.put("order_id", list.get(position).order_id);

            startAct(RefundInfoActivity.class, map, 1);
        });
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
        getData();
    }


    private int page = 1;

    private void getData() {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().refundOrderList(mToken, page)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<RefundListBean>>() {
                    @Override
                    public void success(HttpResponseData<RefundListBean> data) {
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        page = 1;
        getData();
    }
}
