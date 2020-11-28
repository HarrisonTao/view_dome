package com.dykj.youfeng.mall.activity;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mall.adapter.AddressListAdapter;
import com.dykj.youfeng.mode.AddressBean;
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
import com.dykj.module.view.dialog.ConfirmDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lcjing on 2019/8/20.
 */
public class AddressListActivity extends BaseActivity {

    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private List<AddressBean> list = new ArrayList<>();
    private AddressListAdapter adapter;
    boolean isSelect=false;


    @Override
    public int intiLayout() {
        return R.layout.activity_address_list;
    }

    @Override
    public void initData() {

        initTitle("收货地址");
        if (getIntentData()!=null) {
            isSelect = (Boolean) getIntentData();
        }
        adapter = new AddressListAdapter(R.layout.item_address_list, list);
        adapter.setEmptyView(EmptyView.getEmptyView(EmptyView.NORM,this));
        rvList.setAdapter(adapter);
        adapter.setSelect(isSelect);
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.tv_edit:
                    startAct(AddAddressActivity.class,list.get(position));
                    break;
                case R.id.tv_del:
                    showRemind(position);
                    break;
                default:
                    break;
            }
        });
        adapter.setOnItemClickListener((adapter, view, position) -> {
            if (isSelect) {
                Intent intent = new Intent();
                intent.putExtra("data", list.get(position));
                setResult(1, intent);
                finish();
            }

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

    @Override
    public void doBusiness() {

    }


    private void showRemind(int position) {
        new ConfirmDialog(this, () ->
                delAddress(position), "删除该地址？", "删除")
                .setLifecycle(getLifecycle()).show();
    }

    private void delAddress(int position) {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().delArea(mToken, list.get(position).address_id)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if (data.status.equals(AppConstant.SUCCESS)) {
                            //设置数据
                            DyToast.getInstance().success(data.getMessage());
                            list.remove(position);
                            adapter.notifyDataSetChanged();
                        } else {
                            DyToast.getInstance().error(data.getMessage());
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));

    }


    private int page = 1;

    private void getData() {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().getAreaList(mToken, page)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<List<AddressBean>>>() {
                    @Override
                    public void success(HttpResponseData<List<AddressBean>> data) {
                        if (data.status.equals(AppConstant.SUCCESS)) {
                            //设置数据
                            if (page == 1) {
                                list.clear();
                                adapter.notifyDataSetChanged();
                            }
                            refreshLayout.finishRefresh();
                            list.addAll(data.getData());
                            adapter.notifyDataSetChanged();
                            refreshLayout.finishLoadMoreWithNoMoreData();
                        } else {
                            DyToast.getInstance().warning(data.getMessage());
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        page = 1;
        getData();
    }


    @OnClick(R.id.tv_add)
    public void onViewClicked() {
        startAct(AddAddressActivity.class, null, 1);
    }
}
