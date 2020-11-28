package com.dykj.youfeng.mall.activity;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mall.adapter.RecommGoodsAdapter;
import com.dykj.youfeng.mode.GoodsBean;
import com.dykj.youfeng.mode.GoodsListBean;
import com.dykj.youfeng.mode.SearchLogBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.view.EmptyView;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.DrawableUtil;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.TextChangedListener;
import com.dykj.module.util.toast.DyToast;
import com.example.library.AutoFlowLayout;
import com.example.library.FlowAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MallSearchActivity extends BaseActivity {

    @BindView(R.id.et_key)
    EditText etKey;
    @BindView(R.id.flow_layout)
    AutoFlowLayout flowLayout;
    @BindView(R.id.layout_hot_search)
    LinearLayout layoutHotSearch;
    @BindView(R.id.tv_colligate)
    TextView tvColligate;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.layout_search_result)
    LinearLayout layoutSearchResult;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    private List<GoodsBean> goods;
    private RecommGoodsAdapter goodsAdapter;

    @Override
    public int intiLayout() {
        return R.layout.activity_mall_search;
    }

    private List<String> logBeans;

    @Override
    public void initData() {
        initTitle("搜索");
        hindLine();
        logBeans = new ArrayList<>();
        goods = new ArrayList<>();
        flowLayout.setMaxLines(Integer.MAX_VALUE);
        flowLayout.setOnItemClickListener((i, view) -> {
            etKey.setText(logBeans.get(i));
            etKey.setSelection(logBeans.get(i).length());
            page = 1;
            getData();
            layoutHotSearch.setVisibility(View.GONE);
            layoutSearchResult.setVisibility(View.VISIBLE);
        });
        etKey.addTextChangedListener(new TextChangedListener() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (etKey.getText().toString().length() > 0) {
                    ivClose.setVisibility(View.VISIBLE);
                } else {
                    ivClose.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void doBusiness() {
        tvCount.setTextColor(ContextCompat.getColor(this, R.color.font_ff99));
        tvPrice.setTextColor(ContextCompat.getColor(this, R.color.font_ff99));
        tvColligate.setTextColor(ContextCompat.getColor(this, R.color.main));
        goodsAdapter = new RecommGoodsAdapter(R.layout.item_goods_search, goods);
        goodsAdapter.setOnItemClickListener((adapter, view, position) -> startAct(GoodsInfoActivity.class, goods.get(position).goods_id));
        goodsAdapter.setEmptyView(EmptyView.getEmptyView(EmptyView.SEARCH, this));
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
        getSearchLog();
    }


    @OnClick({R.id.iv_search, R.id.iv_close, R.id.tv_colligate, R.id.ll_count, R.id.ll_price})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                page = 1;
                getData();
                layoutHotSearch.setVisibility(View.GONE);
                layoutSearchResult.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_close:
                etKey.setText("");
                goods.clear();
                goodsAdapter.notifyDataSetChanged();
                layoutHotSearch.setVisibility(View.VISIBLE);
                layoutSearchResult.setVisibility(View.GONE);
                ivClose.setVisibility(View.GONE);
                break;
            case R.id.tv_colligate:
                type = "max";
                sort = "desc";
                tvCount.setTextColor(ContextCompat.getColor(this, R.color.font_ff99));
                tvPrice.setTextColor(ContextCompat.getColor(this, R.color.font_ff99));
                tvColligate.setTextColor(ContextCompat.getColor(this, R.color.main));
                getData();
                break;
            case R.id.ll_count:
//                if ("finish".equals(type)) {
//                    if ("asc".equals(sort)) {
//                        sort="desc";
//                    }else {
//
//                    }
//                }
                type = "finish";
                sort = "desc";
                tvCount.setTextColor(ContextCompat.getColor(this, R.color.main));
                tvPrice.setTextColor(ContextCompat.getColor(this, R.color.font_ff99));
                tvColligate.setTextColor(ContextCompat.getColor(this, R.color.font_ff99));

                getData();
                break;
            case R.id.ll_price:
                type = "price";
                sort = "asc";
                tvPrice.setTextColor(ContextCompat.getColor(this, R.color.main));
                tvCount.setTextColor(ContextCompat.getColor(this, R.color.font_ff99));
                tvColligate.setTextColor(ContextCompat.getColor(this, R.color.font_ff99));

                getData();
                break;
            default:
                break;
        }
    }

    private int page = 1;

    private String type = "max", sort = "asc";

    private void getData() {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().searchInfo(mToken, etKey.getText().toString(), type, sort, page)
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
                            DyToast.getInstance().error(data.message);
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

    private void getSearchLog() {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().searchLog(mToken, "2")
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<List<SearchLogBean>>>() {
                    @Override
                    public void success(HttpResponseData<List<SearchLogBean>> data) {
                        if ("9999".equals(data.status)) {
                            for (int i = 0; i < data.getData().size(); i++) {
                                logBeans.add(data.getData().get(i).keyword);
                            }
                            flowLayout.setAdapter(new FlowAdapter(logBeans) {
                                @Override
                                public View getView(int i) {
                                    View view = LayoutInflater.from(MallSearchActivity.this).inflate(R.layout.item_key_flow, null);
                                    TextView tv = view.findViewById(R.id.tv_name);
                                    DrawableUtil.setTextStrokeTheme(tv, 0, ConvertUtils.dp2px(10),
                                            ContextCompat.getColor(MallSearchActivity.this, R.color.font_88),
                                            ContextCompat.getColor(MallSearchActivity.this, R.color.font_ed));
                                    tv.setText(logBeans.get(i));
                                    return view;
                                }
                            });
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

}
