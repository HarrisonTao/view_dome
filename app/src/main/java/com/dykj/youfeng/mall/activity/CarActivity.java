package com.dykj.youfeng.mall.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mall.adapter.CarAdapter;
import com.dykj.youfeng.mode.CarListBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.tools.ArithUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

//购物车
public class CarActivity extends BaseActivity {


    @BindView(R.id.rv_car)
    RecyclerView rvCar;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.iv_select)
    ImageView ivSelect;
    @BindView(R.id.ll_select)
    LinearLayout llSelect;
    @BindView(R.id.tv_tot_money)
    TextView tvTotMoney;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    private CarAdapter adapter;
    private List<CarListBean.GoodsInfoBean> list;

    @Override
    public int intiLayout() {
        return R.layout.activity_car;
    }

    @Override
    public void initData() {
        initTitle("购物车");
        list = new ArrayList<>();
        adapter = new CarAdapter(R.layout.item_mall_car, list);
        adapter.setEmptyView(EmptyView.getEmptyView(EmptyView.NORM, this));
        rvCar.setLayoutManager(new LinearLayoutManager(this));
        rvCar.setAdapter(adapter);
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
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
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.iv_check:
                    //选择或取消选择
                    list.get(position).select = !list.get(position).select;
                    adapter.notifyDataSetChanged();
                    int selectNum = 0;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).select) {
                            selectNum++;
                        }
                    }
                    selectAll = selectNum == list.size();
                    ivSelect.setImageResource(selectAll ? R.mipmap.btn_xuanz_c : R.mipmap.btn_xuanz_big);
                    countMoney();
                    break;
                case R.id.iv_cut:
                    //减少商品数量
                    int num = Integer.parseInt(list.get(position).goods_num);
                    if (num < 2) {
                        return;
                    }
                    changeCarNum(position, --num);
                    break;
                case R.id.iv_add:
                    //增加商品数量
                    //todo 更新列表数据
                    int num2 = Integer.parseInt(list.get(position).goods_num);
                    changeCarNum(position, ++num2);
                    break;
                case R.id.iv_delete:
                    new ConfirmDialog(this, () -> deleteGoods(position),
                            "确定要从购物车中删除该商品吗？").setLifecycle(getLifecycle()).show();
                    break;
                default:
                    break;
            }
        });
        adapter.setOnItemClickListener((adapter, view, position)
                -> startAct(GoodsInfoActivity.class, list.get(position).goods_id));
    }

    @Override
    public void doBusiness() {
        getData();
    }

    private void countMoney() {
        float money = 0;
        int num = 0;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).select) {
                num = num + Integer.parseInt(list.get(i).goods_num);
                money = money + Float.parseFloat(list.get(i).goods_price) * Float.parseFloat(list.get(i).goods_num);
            }
        }
        tvTotMoney.setText("￥" + ArithUtil.formatNum(money));
        tvPay.setText("结算(" + num + ")");
        if (list.size() == 0) {
            selectAll = false;
            ivSelect.setImageResource(R.mipmap.btn_xuanz_big);
        }
    }

    private int page = 1;

    private void getData() {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().carList(mToken, page)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<CarListBean>>() {
                    @Override
                    public void success(HttpResponseData<CarListBean> data) {
                        if (page == 1) {
                            list.clear();
                            adapter.notifyDataSetChanged();
                        }
                        mRefreshLayout.finishRefresh();
                        if ("9999".equals(data.status)) {
//                            for (int i = 0; i < data.getData().list.size(); i++) {
//                                list.addAll(data.getData().list.get(i).goods_info);
//                            }
                            if (data.getData().list.size()>0) {
                                list.addAll(data.getData().list.get(0).goods_info);
                            }
                            selectAll = false;
                            ivSelect.setImageResource(R.mipmap.btn_xuanz_big);
                            adapter.notifyDataSetChanged();
                            if (data.getData().all_page > page) {
                                mRefreshLayout.finishLoadMore();
                            } else {
                                mRefreshLayout.finishLoadMoreWithNoMoreData();
                            }
                            countMoney();
                        } else {
                            DyToast.getInstance().error(data.message);
                            mRefreshLayout.finishLoadMoreWithNoMoreData();
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                        mRefreshLayout.finishRefresh().finishLoadMoreWithNoMoreData();
                    }
                }));

    }

    private void deleteGoods(int position) {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().delCar(mToken, list.get(position).id)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            list.remove(position);
                            adapter.notifyDataSetChanged();
                            countMoney();
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

    private void changeCarNum(int position, int num) {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().changecarNum(mToken, list.get(position).id, num)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            list.get(position).goods_num = String.valueOf(num);
                            adapter.notifyDataSetChanged();
                            countMoney();
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

    private boolean selectAll = false;

    @OnClick({R.id.ll_select, R.id.tv_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_select:
                //全选
                selectAll = !selectAll;
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).select = selectAll;
                }
                ivSelect.setImageResource(selectAll ? R.mipmap.btn_xuanz_c : R.mipmap.btn_xuanz_big);
                adapter.notifyDataSetChanged();
                countMoney();
                break;
            case R.id.tv_pay:
                //结算
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).select) {
                        stringBuilder.append(list.get(i).id).append(",");
                    }
                }
                String cardId = stringBuilder.toString();
                if (StringUtils.isEmpty(cardId)) {
                    DyToast.getInstance().warning("请选择要结算的商品");
                    return;
                }
                Map<String, String> params = new HashMap<>();
                params.put("cardid", cardId.substring(0, cardId.length() - 1));
                startAct(ConfirmOrderCarActivity.class, params, 1);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            startAct(OrderListActivity.class);
            finish();
        } else {
            page = 1;
            getData();
        }
    }
}
