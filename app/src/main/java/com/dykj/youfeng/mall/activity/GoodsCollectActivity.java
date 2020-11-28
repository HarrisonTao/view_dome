package com.dykj.youfeng.mall.activity;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mall.adapter.CollectionGoodsAdapter;
import com.dykj.youfeng.mode.CollectListBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.view.EmptyView;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.dykj.module.view.dialog.ConfirmDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lcjingon 2019/11/1.
 * description:
 */

public class GoodsCollectActivity extends BaseActivity implements OnRefreshLoadMoreListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    private CollectionGoodsAdapter adapter;
    private List<CollectListBean.ListBean> list;

    @Override
    public int intiLayout() {
        return R.layout.activity_refund_list;
    }

    @Override
    public void initData() {
        initTitle("我的收藏");
    }

    @Override
    public void doBusiness() {
        list = new ArrayList<>();
        adapter = new CollectionGoodsAdapter(R.layout.item_collet_goods, list);

        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.tv_add_car:
//                    page=1;
//                    handler.sendEmptyMessage(0);
                    break;
                case R.id.tv_remove:
                    new ConfirmDialog(this, () ->
                            delCollect(position),
                            "确认将此商品移出收藏？").setLifecycle(getLifecycle()).show();
                    break;
                case R.id.ll_content:
                    //商品详情
                    startAct(GoodsInfoActivity.class, list.get(position).goods_id);
                    break;
                default:
                    break;
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.setEmptyView(EmptyView.getEmptyView(EmptyView.NORM, this));
        refreshLayout.setOnRefreshLoadMoreListener(this);
        getData();
    }

    private int page = 1;

    private void getData() {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().collectIndex(mToken, page)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<CollectListBean>>() {
                    @Override
                    public void success(HttpResponseData<CollectListBean> data) {
                        if ("9999".equals(data.status)) {
                            if (page == 1) {
                                list.clear();
                                adapter.notifyDataSetChanged();
                            }
                            list.addAll(data.getData().list);
                            adapter.notifyDataSetChanged();
                            refreshLayout.finishRefresh();
                            if (data.getData().all_page > page) {
                                refreshLayout.finishLoadMore();
                            }
                            refreshLayout.finishLoadMoreWithNoMoreData();
                        } else {
                            DyToast.getInstance().error(data.message);
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

    private void delCollect(int position) {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().delCollect(mToken, list.get(position).id)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            list.remove(position);
                            adapter.notifyDataSetChanged();
                            getData();
                        } else {
                            DyToast.getInstance().error(data.message);
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

}
