package com.dykj.youfeng.mall.activity;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.StringUtils;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mall.fragment.EvaListFragment;
import com.dykj.youfeng.mode.GoodsInfoBean;
import com.dykj.youfeng.mode.UnlineGoodsBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.view.GoodSpecDialog;
import com.dykj.module.activity.JBrowseImgActivity;
import com.dykj.module.activity.WebViewFragment;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.GlideUtils;
import com.dykj.module.util.ImgPagerAdapter;
import com.dykj.module.util.MessageWrap;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.dykj.module.view.dialog.RemindDialog;
import com.dykj.module.view.imgbrowser.util.JMatrixUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class GoodsInfoActivity extends BaseActivity {


    @BindView(R.id.vp)
    ViewPager banner;
    @BindView(R.id.tv_banner_count)
    TextView tvBannerCount;
    @BindView(R.id.tv_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_collect_num)
    TextView tvCollectNum;
    @BindView(R.id.tv_sel_num)
    TextView tvSelNum;
    @BindView(R.id.tv_stock)
    TextView tvStock;
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.tv_eva_num)
    TextView tvEvaNum;
    @BindView(R.id.riv_eva_head)
    RoundedImageView rivEvaHead;
    @BindView(R.id.ll_eva_content)
    LinearLayout llEvaContent;
    @BindView(R.id.tv_eva_name)
    TextView tvEvaName;
    @BindView(R.id.tv_eva_content)
    TextView tvEvaContent;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.tv_collect)
    TextView tvCollect;
    @BindView(R.id.fl_info)
    FrameLayout flInfo;
    @BindView(R.id.fl_eva)
    FrameLayout flEva;
    @BindView(R.id.ll_select)
    LinearLayout llSelect;
    @BindView(R.id.tv_yh)
    TextView tvYh;
    @BindView(R.id.iv_img)
    ImageView ivImg;

    private String goodsId = "";

    @Override
    public int intiLayout() {
        return R.layout.activity_goods_info;
    }

    @Override
    public void initData() {
        goodsId = getIntentData().toString();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void doBusiness() {

        getData();
    }

    private void getData() {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().goodsInfoObj(mToken, goodsId)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<JsonObject>>() {
                    @Override
                    public void success(HttpResponseData<JsonObject> data) {
                        if ("9999".equals(data.status)) {

//                            String json=data.getData().toString();
//                            UnlineGoodsBean bean= new Gson().fromJson(data.getData(),UnlineGoodsBean.class);
//                            bean.getGood_info();

                            infoBean = new Gson().fromJson(data.getData(), GoodsInfoBean.class);
                            initBanner(infoBean.good_info.goods_image);
                            tvGoodsName.setText(infoBean.good_info.goods_name);
                            tvMoney.setText(infoBean.good_info.goods_price);
                            tvCollectNum.setText("原价" + infoBean.good_info.prime_price);
                            tvSelNum.setText("月销" + infoBean.good_info.goods_salenum);
                            tvStock.setText("库存" + infoBean.good_info.goods_storage);
                            if ("0".equals(infoBean.total_evaluate)) {
                                tvEvaNum.setText("商品评价");
                                llEvaContent.setVisibility(View.GONE);
                            } else {
                                tvEvaNum.setText("商品评价（" + infoBean.total_evaluate + "）");
                                llEvaContent.setVisibility(View.VISIBLE);
                                if (infoBean.evaluate_goods != null && infoBean.evaluate_goods.size() > 0) {
                                    tvEvaName.setText(infoBean.evaluate_goods.get(0).nickname);
                                    tvEvaContent.setText(infoBean.evaluate_goods.get(0).geval_content);
                                    GlideUtils.setMallImage(rivEvaHead, infoBean.evaluate_goods.get(0).avatar);
                                }
                            }
                            if (infoBean.good_info.goods_specc == null || infoBean.good_info.goods_specc.size() < 1) {
                                llSelect.setVisibility(View.GONE);
                            }
                            if ("1".equals(infoBean.good_info.keep_goods)) {
                                tvCollect.setCompoundDrawablesWithIntrinsicBounds(null,
                                        getResources().getDrawable(R.mipmap.btn_collect_pre), null, null);
                            } else {
                                tvCollect.setCompoundDrawablesWithIntrinsicBounds(null,
                                        getResources().getDrawable(R.mipmap.btn_collect), null, null);
                            }
                            tvYh.setText("积分最高可抵" + infoBean.good_info.goods_points + "%");
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fl_info, WebViewFragment.getInstance(Html.fromHtml(infoBean.good_info.goods_body).toString()))
                                    .commit();

                        } else if ("1009".equals(data.status)) {
                            UnlineGoodsBean unlineGoodsBean = new Gson().fromJson(data.getData(), UnlineGoodsBean.class);
                            ArrayList<String> listBanner = new ArrayList<>();
                            listBanner.add(unlineGoodsBean.getGoods_img());
                            initBanner(listBanner);
                            tvGoodsName.setText(unlineGoodsBean.getGoods_name());
                            tvMoney.setText(unlineGoodsBean.getGoods_price());
                            tvCollectNum.setText("原价" + unlineGoodsBean.getPrime_price());
                            tvSelNum.setText("月销" + unlineGoodsBean.getGoods_salenum());
                            tvStock.setText("库存" + unlineGoodsBean.getGoods_storage());
                            llEvaContent.setVisibility(View.GONE);
                            llSelect.setVisibility(View.GONE);

                            new RemindDialog(GoodsInfoActivity.this, data.message)
                                    .setLifecycle(getLifecycle()).setCallBack(() -> finish()).setCancel(false).show();
                        } else if ("1008".equals(data.status)) {
                            //                            1008商品不存在！
                            new RemindDialog(GoodsInfoActivity.this, data.message)
                                    .setLifecycle(getLifecycle()).setCallBack(() -> finish()).setCancel(false).show();
                        } else {
                            DyToast.getInstance().error(data.message);
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
//                        DyToast.getInstance().error(data.getMessage());
                        DyToast.getInstance().error("获取数据失败");
                        finish();
                    }
                }));
    }

//    private void getData() {
//        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
//        HttpFactory.getMallInstance().goodsInfo(mToken, goodsId)
//                .compose(HttpObserver.schedulers(this))
//                .as(HttpObserver.life(this))
//                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<GoodsInfoBean>>() {
//                    @Override
//                    public void success(HttpResponseData<GoodsInfoBean> data) {
//                        if ("9999".equals(data.status)) {
//                            infoBean = data.getData();
//                            initBanner(data.getData().good_info.goods_image);
//                            tvGoodsName.setText(data.getData().good_info.goods_name);
//                            tvMoney.setText(data.getData().good_info.goods_price);
//                            tvCollectNum.setText("收藏" + data.getData().good_info.goods_collect);
//                            tvSelNum.setText("月销" + data.getData().good_info.goods_salenum);
//                            tvStock.setText("库存" + data.getData().good_info.goods_storage);
//                            if ("0".equals(data.getData().total_evaluate)) {
//                                tvEvaNum.setText("商品评价");
//                                llEvaContent.setVisibility(View.GONE);
//                            } else {
//                                tvEvaNum.setText("商品评价（" + data.getData().total_evaluate + "）");
//                                llEvaContent.setVisibility(View.VISIBLE);
//                                if (data.getData().evaluate_goods != null && data.getData().evaluate_goods.size() > 0) {
//                                    tvEvaName.setText(data.getData().evaluate_goods.get(0).nickname);
//                                    tvEvaContent.setText(data.getData().evaluate_goods.get(0).geval_content);
//                                    GlideUtils.setMallImage(rivEvaHead, data.getData().evaluate_goods.get(0).avatar);
//                                }
//                            }
//                            if (data.getData().good_info.goods_specc == null || data.getData().good_info.goods_specc.size() < 1) {
//                                llSelect.setVisibility(View.GONE);
//                            }
//                            if ("1".equals(data.getData().good_info.keep_goods)) {
//                                tvCollect.setCompoundDrawablesWithIntrinsicBounds(null,
//                                        getResources().getDrawable(R.mipmap.btn_collect_pre), null, null);
//                            } else {
//                                tvCollect.setCompoundDrawablesWithIntrinsicBounds(null,
//                                        getResources().getDrawable(R.mipmap.btn_collect), null, null);
//                            }
//                            tvYh.setText("积分最高可抵" + data.getData().good_info.goods_points + "%");
//                            getSupportFragmentManager().beginTransaction()
//                                    .replace(R.id.fl_info, WebViewFragment.getInstance(Html.fromHtml(data.getData().good_info.goods_body).toString()))
//                                    .commit();
//
//                        } else {
//                            DyToast.getInstance().error(data.message);
//                        }
//                    }
//
//                    @Override
//                    public void error(Throwable data) {
//                        MyLogger.dLog().e(data);
//                        DyToast.getInstance().error(data.getMessage());
//                    }
//                }));
//    }

    private void initBanner(ArrayList<String> listBanner) {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(listBanner);
        if (listBanner.size() > 0) {
            tvBannerCount.setText(1 + "/" + listBanner.size());
            if (listBanner.size() > 1) {
                list.add(0, listBanner.get(listBanner.size() - 1));
                list.add(listBanner.get(0));
//                list.add(listBanner.get(listBanner.size() - 1));
            }

        } else {
            tvBannerCount.setText(0 + "/" + listBanner.size());

        }
        ImgPagerAdapter adapter = new ImgPagerAdapter(this, list);
        banner.setAdapter(adapter);
        tvBannerCount.setVisibility(View.VISIBLE);

        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (!isShow) {
                    return;
                }
                handler.removeMessages(1);
                if (position == 0 && listBanner.size() > 1) {
                    banner.setCurrentItem(list.size() - 2, false);
                } else if (position == list.size() - 1 && listBanner.size() > 1) {
                    banner.setCurrentItem(1, false);
                } else {
                    tvBannerCount.setText(position + "/" + listBanner.size());
                    handler.sendEmptyMessageDelayed(1, 5000);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        banner.setCurrentItem(1);
        adapter.setOnItemClick(position -> {
            if (position > 0) {
                position = position - 1;
            }
            List<Rect> rects = new ArrayList<>();
            for (int i = 0; i < listBanner.size(); i++) {
                rects.add(JMatrixUtil.getDrawableBoundsInView(ivImg));
            }
            JBrowseImgActivity.start(this, listBanner, position, rects);
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            handler.removeMessages(1);
            if (isShow) {
                banner.setCurrentItem(banner.getCurrentItem() + 1);
            }
        }
    };

    private boolean isShow = true;
    private EvaListFragment evaListFragment;

    private void toEva(boolean eva) {
        if (eva) {
            flEva.setVisibility(View.VISIBLE);
            if (evaListFragment == null) {
                evaListFragment = EvaListFragment.getInstance(goodsId);
                evaListFragment.setBack(() -> toEva(false));
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_eva, evaListFragment).commit();
            }
        } else {
            flEva.setVisibility(View.GONE);
        }
    }

    private GoodsInfoBean infoBean;

    @OnClick({R.id.ll_select, R.id.ll_eva, R.id.ll_eva_content, R.id.fl_info, R.id.fl_eva, R.id.fl_kf, R.id.fl_collect, R.id.tv_add, R.id.tv_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_select:
                selectGoodsType();
                break;
            case R.id.ll_eva:
                toEva(true);
                break;
            case R.id.ll_eva_content:
                break;
            case R.id.fl_info:
                break;
            case R.id.fl_eva:
                break;
            case R.id.fl_kf:
                if (!openIsQQClientAvailable(infoBean.serviceQQ)) {
                    DyToast.getInstance().warning("请安装QQ");
                }
                break;
            case R.id.fl_collect:
                collectGoods();
                break;
            case R.id.tv_add:
                if (llSelect.getVisibility() == View.VISIBLE) {
                    selectGoodsType();
                } else {
                    Map<String, String> bundle = new HashMap<>();
                    bundle.put("goods_id", infoBean.good_info.goods_id);
                    bundle.put("sp_value", "");
                    bundle.put("sp_name", "");
                    bundle.put("num", "1");
                    addCar(bundle);
                }

                break;
            case R.id.tv_buy:
                selectGoodsType();
                break;
            default:
                break;
        }
    }

    private GoodSpecDialog goodSpecDialog;

    private void selectGoodsType() {
        if (goodSpecDialog == null) {
            goodSpecDialog = new GoodSpecDialog(this, new GoodSpecDialog.ViewInterface() {
                @Override
                public void commit(int count, boolean commit) {
                    String sp_value = "";
                    String sp_name = "";
                    if (llSelect.getVisibility() != View.GONE) {
                        List<GoodsInfoBean.GoodInfoBean.GoodsSpeccBean> spList = infoBean.good_info.goods_specc;
                        StringBuilder sp = new StringBuilder();
                        StringBuilder type = new StringBuilder();
                        for (GoodsInfoBean.GoodInfoBean.GoodsSpeccBean spBean : spList) {
                            if (StringUtils.isEmpty(spBean.sp_child_name)) {
                                DyToast.getInstance().warning("请选择商品" + spBean.sp_name);
                                selectGoodsType();
                                return;
                            }
                            type.append(spBean.sp_name).append(":").append(spBean.sp_child_name).append(",");
                        }
                        sp_name = type.toString();
                        if (sp_name.length() > 0) {
                            sp_name = sp_name.substring(0, sp_name.length() - 1);
                        }
                        for (int i = 0; i < spList.size(); i++) {
                            sp.append(spList.get(i).sp_id).append("_").append(spList.get(i).sp_child_id);
                            if (i != spList.size() - 1) {
                                sp.append("|");
                            }
                        }
                        sp_value = sp.toString();
                    }
                    Map<String, String> bundle = new HashMap<>();
                    bundle.put("goods_id", infoBean.good_info.goods_id);
                    bundle.put("num", count + "");
                    bundle.put("sp_value", sp_value);
                    bundle.put("sp_name", sp_name);
                    bundle.put("goods_spec", sp_value);
                    bundle.put("goods_spec_name", sp_name);
                    if (commit) {
                        startAct(ConfirmOrderActivity.class, bundle, 1);
                    } else {
                        addCar(bundle);
                    }

                }

                @Override
                public void select(String type) {
                    tvSelect.setText(type);
                }
            }, infoBean.good_info).setLifecycle(getLifecycle()).setLifecycleOwner(this);
        }
        goodSpecDialog.show();
    }


    private void addCar(Map<String, String> params) {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        params.put("token", mToken);
        HttpFactory.getMallInstance().addCar(params)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            DyToast.getInstance().success(data.message);
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


    private void collectGoods() {
        if (infoBean == null) {
            DyToast.getInstance().warning("获取商品信息失败");
            return;
        }
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        String collect = "1".equals(infoBean.good_info.keep_goods) ? "0" : "1";
        HttpFactory.getMallInstance().collectGood(mToken, goodsId, collect)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            infoBean.good_info.keep_goods = collect;
                            DyToast.getInstance().success(data.message);
                            if ("1".equals(infoBean.good_info.keep_goods)) {
                                tvCollect.setCompoundDrawablesWithIntrinsicBounds(null,
                                        getResources().getDrawable(R.mipmap.btn_collect_pre), null, null);
                            } else {
                                tvCollect.setCompoundDrawablesWithIntrinsicBounds(null,
                                        getResources().getDrawable(R.mipmap.btn_collect), null, null);
                            }
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


    //判断是否安装Qq
    private boolean openIsQQClientAvailable(String dataQq) {
        final PackageManager packageManager = getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if ("com.tencent.tim".equalsIgnoreCase(pn) || "com.tencent.qqlite".equalsIgnoreCase(pn) || "com.tencent.mobileqq".equalsIgnoreCase(pn)) {
                    //                 注意：   1.此处传入的QQ号,需开通QQ推广功能,不然向此QQ号发送临时消息,会不成功.
//                    2.开通QQ推广方法:1.打开QQ推广网址http://shang.qq.com并用QQ登录  2.点击顶部导航栏:推广工具
//         3.在弹出菜单中点击'立即免费开通' 即可
                    String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=" + dataQq + "&version=1";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl));
//                            intent.putExtra("title","heehehheheh");
                    startActivity(intent);
                    //指定的QQ号只需要修改uin后的值即可。
//                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
//                    startActivity(intent);
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            setResult(1);
            finish();
            startAct(OrderListActivity.class);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(MessageWrap messageWrap) {
        if ("JBrowseImgPageSelected".equals(messageWrap.message)) {
            int position = (int) messageWrap.map.get("position");
            if (position < banner.getAdapter().getCount() - 1) {
                position = position + 1;
            }
//            position<
            banner.setCurrentItem(position, false);
        }
    }

    @Override
    protected void onDestroy() {
        isShow = false;
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


}
