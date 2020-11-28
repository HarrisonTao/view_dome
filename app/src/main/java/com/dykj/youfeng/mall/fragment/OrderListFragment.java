package com.dykj.youfeng.mall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mall.activity.CommitEvaActivity;
import com.dykj.youfeng.mall.activity.ExpressInfoActivity;
import com.dykj.youfeng.mall.activity.OrderInfoActivity;
import com.dykj.youfeng.mall.adapter.OrderListAdapter;
import com.dykj.youfeng.mode.CancelReasonBean;
import com.dykj.youfeng.mode.OrderListBean;
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
import com.dykj.module.view.dialog.ConfirmDialog;
import com.dykj.module.view.dialog.SelectDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.tencent.mmkv.MMKV;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by lcjingon 2019/10/29.
 * description:
 */

public class OrderListFragment extends BaseFragment {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private List<OrderListBean.OrderInfoBean> list;
    private OrderListAdapter adapter;

    public static OrderListFragment getInstance(String type) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    private String type;


    @Override
    protected int intiLayout() {
        return R.layout.fragment_order_list;
    }

    @Override
    protected void onViewReallyCreated(View view) {
        type = getArguments().getString("type");
        list = new ArrayList<>();
        adapter = new OrderListAdapter(R.layout.item_order_list, list);
        adapter.setOnItemChildClickListener((adapter, view1, position) -> {
            switch (view1.getId()) {
                case R.id.tv_cancel:
                    getCancelReason(list.get(position).order_sn);
                    break;
                case R.id.tv_delete:
                    new ConfirmDialog(getContext(), () -> delete(position),
                            "删除该订单？", "删除").setLifecycle(getLifecycle()).show();
                    break;
                case R.id.v_order_goods:
                    startAct(OrderInfoActivity.class, list.get(position).order_id);
                    break;
                case R.id.tv_receipt:
                    new ConfirmDialog(getContext(), () -> receive(position),
                            "确认收到该商品？", "确认").setLifecycle(getLifecycle()).show();
                    break;
                case R.id.tv_go_pay:
                    Intent intent = new Intent(getContext(), OrderInfoActivity.class);
                    intent.putExtra("Data", (Serializable) list.get(position).order_id);
                    intent.putExtra("goPay", true);
                    startActivityForResult(intent, 1);
                    break;
                case R.id.tv_exp:
                    Map<String,String> map=new HashMap<>();
                    map.put("orderId",list.get(position).order_id);
                    if (list.get(position).order_goods!=null&&list.get(position).order_goods.size()>0) {
                        map.put("goodsImg",list.get(position).order_goods.get(0).goods_image);
                    }
                    startAct(ExpressInfoActivity.class,map);
                    break;
                case R.id.tv_remark:
                    startAct(CommitEvaActivity.class,list.get(position).order_id, 1);
                    break;
                default:
                    break;
            }
        });
        adapter.setOnItemClickListener((adapter, view12, position) -> {
            startAct(OrderInfoActivity.class, list.get(position).order_id,1);
        });
        recycler.setAdapter(adapter);
        adapter.setEmptyView(EmptyView.getEmptyView(EmptyView.NORM, getContext()));
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
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private int page = 1;

    private void getData() {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().getOrderList(mToken, type, page)
                .compose(HttpObserver.schedulers(getContext()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<OrderListBean>>() {
                    @Override
                    public void success(HttpResponseData<OrderListBean> data) {
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

                            list.addAll(data.getData().list.order_detail.order_list.order_info);
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


    private void getCancelReason(String orderSn) {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().getCancelReason(mToken)
                .compose(HttpObserver.schedulers(getContext()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<CancelReasonBean>>() {
                    @Override
                    public void success(HttpResponseData<CancelReasonBean> data) {
                        if (data.status.equals(AppConstant.SUCCESS)) {
                            List<String> list = new ArrayList<>();
                            for (int i = 0; i < data.getData().list.size(); i++) {
                                list.add(data.getData().list.get(i).msg);
                            }
                            new SelectDialog(getContext(),
                                    (position, value) -> {
                                        cancel(orderSn, data.getData().list.get(position).reason);
                                    },
                                    "请选择取消原因", list).setLifecycle(getLifecycle()).show();

                        } else {
                            DyToast.getInstance().warning(data.getMessage());
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        if (!"数据解析异常".equals(data.getMessage())) {
                            DyToast.getInstance().error(data.getMessage());
                        }
                    }
                }));

    }


    private void cancel(String orderSn, String reason) {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().getCancel(mToken, orderSn, reason)
                .compose(HttpObserver.schedulers(getContext()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if (data.status.equals(AppConstant.SUCCESS)) {
                            DyToast.getInstance().success(data.message);
                            page = 1;
                            getData();
                        } else {
                            DyToast.getInstance().warning(data.getMessage());
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        if (!"数据解析异常".equals(data.getMessage())) {
                            DyToast.getInstance().error(data.getMessage());
                        }
                    }
                }));
    }

    private void delete(int position) {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().deleteOrder(mToken, list.get(position).order_id)
                .compose(HttpObserver.schedulers(getContext()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if (data.status.equals(AppConstant.SUCCESS)) {
                            DyToast.getInstance().success(data.message);
                            list.remove(position);
                            adapter.notifyDataSetChanged();
                        } else {
                            DyToast.getInstance().warning(data.getMessage());
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        if (!"数据解析异常".equals(data.getMessage())) {
                            DyToast.getInstance().error(data.getMessage());
                        }
                    }
                }));
    }

    private void receive(int position) {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().confirmReceive(mToken, list.get(position).order_id)
                .compose(HttpObserver.schedulers(getContext()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if (data.status.equals(AppConstant.SUCCESS)) {
                            DyToast.getInstance().success(data.message);
                            page = 1;
                            getData();
                        } else {
                            DyToast.getInstance().warning(data.getMessage());
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        if (!"数据解析异常".equals(data.getMessage())) {
                            DyToast.getInstance().error(data.getMessage());
                        }
                    }
                }));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        page = 1;
        getData();
    }


}
