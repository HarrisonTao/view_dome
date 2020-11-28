package com.dykj.youfeng.mall;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mall.activity.CarActivity;
import com.dykj.youfeng.mall.activity.GoodsInfoActivity;
import com.dykj.youfeng.mall.activity.GoodsListActivity;
import com.dykj.youfeng.mall.activity.MallPersonActivity;
import com.dykj.youfeng.mall.activity.MallSearchActivity;
import com.dykj.youfeng.mall.adapter.MallCateAdapter;
import com.dykj.youfeng.mall.adapter.RecommGoodsAdapter;
import com.dykj.youfeng.mall.adapter.SpecialGoodsAdapter;
import com.dykj.youfeng.mode.GoodsBean;
import com.dykj.youfeng.mode.GoodsCateBean;
import com.dykj.youfeng.mode.MallHomeBean;
import com.dykj.youfeng.mode.MallUserInfo;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.start.LoginActivity;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.base.BaseFragment;
import com.dykj.module.base.BaseWebViewActivity;
import com.dykj.module.entity.BaseWebViewData;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.GlideImageLoader;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.dykj.module.view.dialog.ConfirmDialog;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mmkv.MMKV;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lcjingon 2019/10/22.
 * description:
 */

public class MallFragment extends BaseFragment implements OnRefreshListener {
    @BindView(R.id.tv_car_num)
    TextView tvCarNum;

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.rv_type)
    RecyclerView rvType;
    @BindView(R.id.rv_special)
    RecyclerView rvSpecial;
    @BindView(R.id.rv_recommend)
    RecyclerView rvRecommend;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private MallCateAdapter cateAdapter;
    private SpecialGoodsAdapter specialGoodsAdapter;
    private RecommGoodsAdapter recommGoodsAdapter;

    private List<GoodsCateBean.GoodsClassBean> list = new ArrayList<>();
    private List<GoodsBean> specialList = new ArrayList<>();
    private List<GoodsBean> recommList = new ArrayList<>();

    @Override
    protected int intiLayout() {
        return R.layout.fragment_mall;
    }

    @Override
    protected void onViewReallyCreated(View view) {
        cateAdapter = new MallCateAdapter(R.layout.item_mall_cate, list);
        cateAdapter.setOnItemClickListener((adapter, view2, position) -> {
            String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
            if (TextUtils.isEmpty(mToken)) {
                new ConfirmDialog(getContext(), () ->
                        startAct(this, LoginActivity.class)
                        , "您还未登录", "去登录").setLifecycle(getLifecycle()).show();
                return;
            }
            Map<String, String> map = new HashMap<>();
            map.put("title", list.get(position).getGc_name());
            map.put("id", list.get(position).getGc_id());
            startAct(GoodsListActivity.class, map);
        });
        rvType.setAdapter(cateAdapter);

        specialGoodsAdapter = new SpecialGoodsAdapter(R.layout.item_special, specialList);
        specialGoodsAdapter.setOnItemClickListener((adapter, view1, position)
                -> {
            String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
            if (TextUtils.isEmpty(mToken)) {
                new ConfirmDialog(getContext(), () ->
                        startAct(this, LoginActivity.class)
                        , "您还未登录", "去登录").setLifecycle(getLifecycle()).show();
                return;
            }
            startAct(GoodsInfoActivity.class, specialList.get(position).goods_id);
        });
        rvSpecial.setAdapter(specialGoodsAdapter);

        recommGoodsAdapter = new RecommGoodsAdapter(R.layout.item_mall_recommendl, recommList);
        recommGoodsAdapter.setOnItemClickListener((adapter, view12, position)
                -> {
            String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
            if (TextUtils.isEmpty(mToken)) {
                new ConfirmDialog(getContext(), () ->
                        startAct(this, LoginActivity.class)
                        , "您还未登录", "去登录").setLifecycle(getLifecycle()).show();
                return;
            }
            startAct(GoodsInfoActivity.class, recommList.get(position).goods_id);
        });
        rvRecommend.setAdapter(recommGoodsAdapter);

        refreshLayout.setOnRefreshListener(this);
    }


    @Override
    public void doBusiness() {
        requestHomeData();
    }

    /**
     * 请求首页数据
     */
    private void requestHomeData() {
        HttpFactory.getMallInstance().homeIndex()
                .compose(HttpObserver.schedulers(getContext()))
                .as(HttpObserver.life(MallFragment.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<MallHomeBean>>() {
                    @Override
                    public void success(HttpResponseData<MallHomeBean> data) {
                        if ("9999".equals(data.status)) {
//                            list.clear();
//                            list.addAll(data.getData().category);
//                            cateAdapter.notifyDataSetChanged();
                            specialList.clear();
                            specialList.addAll(data.getData().goods_salenum);
                            specialGoodsAdapter.notifyDataSetChanged();
                            recommList.clear();
                            recommList.addAll(data.getData().goods_commend);
                            recommGoodsAdapter.notifyDataSetChanged();
                            initBanner(data.getData().banner);
                        } else {
                            DyToast.getInstance().error(data.message);
                        }

                        refreshLayout.finishRefresh().finishLoadMoreWithNoMoreData();
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                        refreshLayout.finishRefresh().finishLoadMoreWithNoMoreData();
                    }
                }));

        HttpFactory.getMallInstance().homeGoodsCate()
                .compose(HttpObserver.schedulers(getContext()))
                .as(HttpObserver.life(MallFragment.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<GoodsCateBean>>() {
                    @Override
                    public void success(HttpResponseData<GoodsCateBean> data) {
                        if ("9999".equals(data.status)) {
                            list.clear();
                            list.addAll(data.getData().getGoods_class());
                            cateAdapter.notifyDataSetChanged();
                        } else {
                            DyToast.getInstance().error(data.message);
                        }
                        refreshLayout.finishRefresh().finishLoadMoreWithNoMoreData();
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                        refreshLayout.finishRefresh().finishLoadMoreWithNoMoreData();
                    }
                }));
    }

    private void initBanner(List<MallHomeBean.BannerBean> bannerBeans) {
        List<String> listBanner = new ArrayList<>();
        for (int i = 0; i < bannerBeans.size(); i++) {
            listBanner.add(bannerBeans.get(i).image);
        }
        banner.setImageLoader(new GlideImageLoader()).setImages(listBanner).start();
        banner.setOnBannerListener(position -> {
            MallHomeBean.BannerBean bannerBean = bannerBeans.get(position);
            String url = bannerBean.url;
            String title = bannerBean.title;
            if (!StringUtils.isEmpty(url)) {
                BaseWebViewData baseWebViewData = new BaseWebViewData();
                baseWebViewData.title = title;
                baseWebViewData.content = url;
                startAct(getFragment(), BaseWebViewActivity.class, baseWebViewData);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserData();
    }

    private void getUserData() {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        if (TextUtils.isEmpty(mToken)) {
            return;
        }
        Log.d("wtf","*****************"+mToken);
        HttpFactory.getMallInstance().userIndex(mToken)
                .compose(HttpObserver.schedulers(getContext()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<MallUserInfo>>() {
                    @Override
                    public void success(HttpResponseData<MallUserInfo> data) {
                        Log.d("wtf",new Gson().toJson(data));
                        if ("9999".equals(data.status)) {
                            if (StringUtils.isEmpty(data.getData().carcount) || "0".equals(data.getData().carcount)) {
                                tvCarNum.setVisibility(View.GONE);
                            } else {
                                tvCarNum.setText(data.getData().carcount);
                                tvCarNum.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        Log.d("wtf",data.getMessage());
                        MyLogger.dLog().e(data);
                    }
                }));
    }


    @OnClick({R.id.iv_car, R.id.iv_person, R.id.fl_search})
    public void onViewClicked(View view) {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        if (TextUtils.isEmpty(mToken)) {
            new ConfirmDialog(getContext(), () ->
                    startAct(this, LoginActivity.class)
                    , "您还未登录", "去登录").setLifecycle(getLifecycle()).show();
            return;
        }
        switch (view.getId()) {
            case R.id.iv_car:
                startAct(CarActivity.class);
                break;
            case R.id.iv_person:
                startAct(MallPersonActivity.class);
                break;
            case R.id.fl_search:
                startAct(MallSearchActivity.class);
                break;
            default:
                break;
        }
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        requestHomeData();
//        DyToast.getInstance().info("下拉刷新");
    }
}
