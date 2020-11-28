package com.dykj.youfeng.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.GoodsInfoBean;
import com.dykj.youfeng.mode.SpecResultBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.view.adapter.GoodsSpecAdapter;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.GlideUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lcjing
 * 商品类型选择
 */

public class GoodSpecDialog implements LifecycleObserver, GoodsSpecAdapter.SelectCall {
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.tv_spec)
    TextView tvSpec;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.iv_goods)
    ImageView ivGoods;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.iv_jian)
    ImageView ivJian;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.ll_recy)
    LinearLayout llRecy;
    private Dialog dialog;
    private Context context;


    private GoodsInfoBean.GoodInfoBean goodInfo;
    //    private Map<String, GoodsInfoBean.GoodInfoBean.GoodsSpecBean> specMap;
    private List<GoodsInfoBean.GoodInfoBean.GoodsSpeccBean> spList;

    public GoodSpecDialog(Context context, ViewInterface viewInterface, GoodsInfoBean.GoodInfoBean goodInfo) {
        this.context = context;
        this.goodInfo = goodInfo;
        this.viewInterface = viewInterface;
        if (goodInfo.goods_spec != null && goodInfo.goods_spec.toString().length() > 10) {
//            try {
//                String json = new Gson().toJson(goodInfo.goods_spec);
//                specMap = new Gson().fromJson(json, new TypeToken<Map<String, GoodsInfoBean.GoodInfoBean.GoodsSpecBean>>() {
//                }.getType());
//            }catch (Exception e){
//                MyLogger.dLog().e(e);
//                dismiss();
//                DyToast.getInstance().error("获取商品参数失败！");
//            }
            spList = goodInfo.goods_specc;
        }
    }

    public GoodSpecDialog(Context context, ViewInterface viewInterface) {
        this.context = context;
        this.viewInterface = viewInterface;
    }

    public GoodSpecDialog setLifecycle(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        return this;
    }


    private int goodsStorage = 0;

    public void show() {
        if (dialog != null) {
            dialog.show();
            return;
        }
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_select_spec, null);
        ButterKnife.bind(this, dialogView);

        GlideUtils.setImage(ivGoods, goodInfo.goods_img);
        if (StringUtils.isEmpty(goodInfo.goods_price)) {
            tvPrice.setText("￥");
        } else {
            tvPrice.setText("￥" + goodInfo.goods_price);
        }

        tvCount.setText("库存" + goodInfo.goods_storage);
        if (!StringUtils.isEmpty(goodInfo.goods_storage)) {
            goodsStorage = Integer.parseInt(goodInfo.goods_storage);
        } else {
            goodsStorage = 1;
        }

        tvSpec.setText("");
        if (spList != null && spList.size() > 0) {
            GoodsSpecAdapter adapter = new GoodsSpecAdapter(spList, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        }
        dialog = new Dialog(context, R.style.dialog_bottom);
        dialog.setCancelable(true);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        dialog.show();
    }

    private ViewInterface viewInterface;

    @OnClick({R.id.iv_close, R.id.rl_bg, R.id.iv_jian, R.id.iv_add, R.id.tv_add_car, R.id.tv_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
            case R.id.rl_bg:
                dismiss();
                break;
            case R.id.iv_jian:
                if (count > 1) {
                    tvNum.setText(--count + "");
                }
                break;
            case R.id.iv_add:
                if (count < goodsStorage) {
                    tvNum.setText(++count + "");
                } else {
                    DyToast.getInstance().warning("商品数量不能超过库存！");
                }
                break;
            case R.id.tv_buy:
                if (spList != null) {
                    for (GoodsInfoBean.GoodInfoBean.GoodsSpeccBean spBean : spList) {
                        if (StringUtils.isEmpty(spBean.sp_child_name)) {
                            DyToast.getInstance().warning("请选择商品" + spBean.sp_name);
                            return;
                        }
                    }
                }

                if (viewInterface != null) {
                    viewInterface.commit(count, true);
                }
                dismiss();
                break;
            case R.id.tv_add_car:
                if (spList != null) {
                    for (GoodsInfoBean.GoodInfoBean.GoodsSpeccBean spBean : spList) {
                        if (StringUtils.isEmpty(spBean.sp_child_name)) {
                            DyToast.getInstance().warning("请选择商品" + spBean.sp_name);
                            return;
                        }
                    }
                }
                if (viewInterface != null) {
                    viewInterface.commit(count, false);
                }
                dismiss();
                break;
            default:
                break;
        }
    }


    private int count = 1;

    //选择属性
    @Override
    public void call() {
        StringBuilder type = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < spList.size(); i++) {
            if (StringUtils.isEmpty(spList.get(i).sp_child_name)) {
                return;
            }
            type.append(spList.get(i).sp_child_name).append("");
        }
        tvSpec.setText("已选：" + type.toString());
        for (int i = 0; i < spList.size(); i++) {
            if (StringUtils.isEmpty(spList.get(i).sp_child_id)) {
                return;
            }
            sb.append(spList.get(i).sp_id).append("_").append(spList.get(i).sp_child_id);
            if (i != spList.size() - 1) {
                sb.append("|");
            }
        }
        String key = sb.toString();
        getStorage(key);
//        GoodsSpecBean specBean = specMap.get(key);
//        if (specBean != null) {
//            tvPrice.setText("￥" + specBean.price);
//            tvCount.setText("库存" + specBean.kucun);
//            goodsStorage = Integer.parseInt(specBean.kucun);
//        }
        if (viewInterface != null) {
            viewInterface.select(type.toString());
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void destroy() {
        // disconnect if connected
        dismiss();
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private LifecycleOwner lifecycleOwner;

    public GoodSpecDialog setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
        return this;
    }

    private void getStorage(String spec) {
        HttpFactory.getMallInstance().getGoodsSpec(spec, goodInfo.goods_id)
                .compose(HttpObserver.schedulers(context))
                .as(HttpObserver.life(lifecycleOwner))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<SpecResultBean>>() {
                    @Override
                    public void success(HttpResponseData<SpecResultBean> data) {
                        if ("9999".equals(data.status)) {
                            tvPrice.setText("￥" + data.getData().getPrice());
                            tvCount.setText("库存" + data.getData().getKucun());
                            goodsStorage = Integer.parseInt(data.getData().getKucun());
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

    public interface ViewInterface {
        void commit(int count, boolean commit);

        void select(String type);

    }


}

