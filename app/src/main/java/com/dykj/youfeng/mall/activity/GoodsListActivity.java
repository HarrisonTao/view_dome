package com.dykj.youfeng.mall.activity;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mall.adapter.RecommGoodsAdapter;
import com.dykj.youfeng.mode.GoodsBean;
import com.dykj.youfeng.mode.GoodsListBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.view.EmptyView;
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
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class GoodsListActivity extends BaseActivity {


    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.iv_count)
    ImageView ivCount;
    @BindView(R.id.iv_price)
    ImageView ivPrice;
    private List<GoodsBean> goods;
    private RecommGoodsAdapter goodsAdapter;
    private String gcId = "";

    @Override
    public int intiLayout() {
        return R.layout.activity_goods_list;
    }

    @Override
    public void initData() {
        Map<String, String> params = (Map<String, String>) getIntentData();
        gcId = params.get("id");
        initTitle(params.get("title"));
        hindLine();
    }

    @Override
    public void doBusiness() {
        tvCount.setTextColor(ContextCompat.getColor(this, R.color.font_ff99));
        tvPrice.setTextColor(ContextCompat.getColor(this, R.color.font_ff99));
        tvAll.setTextColor(ContextCompat.getColor(this, R.color.main));
        goods = new ArrayList<>();
        goodsAdapter = new RecommGoodsAdapter(R.layout.item_goods_search, goods);
        goodsAdapter.setOnItemClickListener((adapter, view, position)
                -> startAct(GoodsInfoActivity.class, goods.get(position).goods_id));
        goodsAdapter.setEmptyView(EmptyView.getEmptyView(EmptyView.NORM, this));
        recyclerView.setAdapter(goodsAdapter);
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


    private boolean selNumUp = false, priceUp = false;


    @OnClick({R.id.tv_all, R.id.ll_count, R.id.ll_price})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_all:
                tvCount.setTextColor(ContextCompat.getColor(this, R.color.font_ff99));
                tvPrice.setTextColor(ContextCompat.getColor(this, R.color.font_ff99));
                tvAll.setTextColor(ContextCompat.getColor(this, R.color.main));
                type = "max";
                break;
            case R.id.ll_count:
                tvCount.setTextColor(ContextCompat.getColor(this, R.color.main));
                tvPrice.setTextColor(ContextCompat.getColor(this, R.color.font_ff99));
                tvAll.setTextColor(ContextCompat.getColor(this, R.color.font_ff99));
                selNumUp = !selNumUp;
                if (selNumUp) {
                    ivCount.setImageResource(R.mipmap.ic_rise);
                } else {
                    ivCount.setImageResource(R.mipmap.ic_rise_x);
                }
                if (selNumUp) {
                    sort = "asc";
                } else {
                    sort = "desc";
                }
                type = "finish";
                break;
            case R.id.ll_price:
                priceUp = !priceUp;
                tvPrice.setTextColor(ContextCompat.getColor(this, R.color.main));
                tvCount.setTextColor(ContextCompat.getColor(this, R.color.font_ff99));
                tvAll.setTextColor(ContextCompat.getColor(this, R.color.font_ff99));
                if (priceUp) {
                    ivPrice.setImageResource(R.mipmap.ic_rise);
                } else {
                    ivPrice.setImageResource(R.mipmap.ic_rise_x);
                }
                if (priceUp) {
                    sort = "asc";
                } else {
                    sort = "desc";
                }
                type = "price";
                break;
            default:
                break;
        }
        getData();
    }

    private int page = 1;
    //    max：综合 finish：销量 price：价格
    private String type = "max", sort = "asc";

    private void getData() {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().categoryGoodsList(mToken, gcId, type, sort, page)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<GoodsListBean>>() {
                    @Override
                    public void success(HttpResponseData<GoodsListBean> data) {
                        if (page == 1) {
                            goods.clear();
                            goodsAdapter.notifyDataSetChanged();
                        }
                        if ("9999".equals(data.status)) {
                            goods.addAll(data.getData().list);
                            goodsAdapter.notifyDataSetChanged();
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

}
