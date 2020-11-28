package com.dykj.youfeng.mall.activity;


import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mall.adapter.OrderInfoDetailAdapter;
import com.dykj.youfeng.mode.RefundInfoBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.AppConstant;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.DialogUtils;
import com.dykj.module.util.GlideUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.date.DateFormatUtil;
import com.dykj.module.util.toast.DyToast;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author lcjing
 * 退款详情
 */
public class RefundInfoActivity extends BaseActivity {


    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_reject)
    TextView tvReject;
    @BindView(R.id.ll_reject)
    LinearLayout llReject;
    @BindView(R.id.tv_refund_money)
    TextView tvRefundMoney;
    @BindView(R.id.tv_seller_name)
    TextView tvSellerName;
    @BindView(R.id.tv_seller_phone)
    TextView tvSellerPhone;
    @BindView(R.id.tv_seller_location)
    TextView tvSellerLocation;
    @BindView(R.id.tv_edit_express)
    TextView tvEditExpress;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.ll_seller_info)
    LinearLayout llSellerInfo;
    @BindView(R.id.tv_express)
    TextView tvExpress;
    @BindView(R.id.ll_express)
    LinearLayout llExpress;
    @BindView(R.id.iv_goods_img)
    ImageView ivGoodsImg;
    @BindView(R.id.tv_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.tv_goods_type)
    TextView tvGoodsType;
    @BindView(R.id.tv_goods_price)
    TextView tvGoodsPrice;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.rv_infos)
    RecyclerView rvInfos;
    private OrderInfoDetailAdapter adapter;
    private List<OrderInfoDetailAdapter.Value> infos;

    @Override
    public int intiLayout() {
        return R.layout.activity_refund_info;
    }

    @Override
    public void initData() {
        initTitle("退款详情");
        infos = new ArrayList<>();
        adapter = new OrderInfoDetailAdapter(R.layout.item_order_info_detail, infos);
        rvInfos.setAdapter(adapter);
    }

    @Override
    public void doBusiness() {
        getData();
    }

    private Map<String, String> map;

    private void getData() {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        map = (Map<String, String>) getIntentData();
        map.put("token", mToken);
        HttpFactory.getMallInstance().refundOrderInfo(map)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<RefundInfoBean>>() {
                    @Override
                    public void success(HttpResponseData<RefundInfoBean> data) {
                        if (data.status.equals(AppConstant.SUCCESS)) {
//                            map.put("invoice_no", data.getData().invoice_no);
                            tvGoodsName.setText(data.getData().goods_name);
                            GlideUtils.setMallImage(ivGoodsImg, data.getData().goods_image);
                            tvCount.setText(String.format("x%s", data.getData().goods_num));
                            tvGoodsPrice.setText(String.format("￥%s", data.getData().goods_price));
                            tvGoodsType.setText(data.getData().goods_spec_name);
                            tvRefundMoney.setText(String.format("￥%s", data.getData().refund_amount));
                            tvTime.setText(DateFormatUtil.getTime(data.getData().add_time));
                            infos.clear();
                            infos.add(new OrderInfoDetailAdapter.Value("退款原因", data.getData().reason_info));
                            infos.add(new OrderInfoDetailAdapter.Value("退款金额", "￥" + data.getData().refund_amount));
                            infos.add(new OrderInfoDetailAdapter.Value("申请时间", DateFormatUtil.getTime(data.getData().add_time)));
                            infos.add(new OrderInfoDetailAdapter.Value("退款编号", data.getData().refund_sn));
                            adapter.notifyDataSetChanged();
                            if ("1".equals(data.getData().seller_state)) {
//                                等待商家同意申请
                                tvStatus.setText("等待商家确认");
                            } else if ("2".equals(data.getData().seller_state) && "3".equals(data.getData().refund_state)) {
//                                平台同意申请
//                                tvStatus.setText("平台已同意退款申请");
                                tvStatus.setText("退款成功");
                                infos.add(new OrderInfoDetailAdapter.Value("退款时间", DateFormatUtil.getTime(data.getData().seller_time)));
                                adapter.notifyDataSetChanged();
                            }else if ("2".equals(data.getData().seller_state) && "0".equals(data.getData().buyer_is_submit)) {
//                                商家同意申请  等待用户提交
                                tvStatus.setText("商家已同意退款申请");
                                showShouhuDialog(data.getData());
                            } else if ("2".equals(data.getData().seller_state) && "1".equals(data.getData().refund_state)) {
//                                等待平台同意
                                tvStatus.setText("等待平台确认");
                                if (!StringUtils.isEmpty(data.getData().express_name)) {
                                    llSellerInfo.setVisibility(View.VISIBLE);
                                    tvSellerLocation.setText(String.format("收货地址： %s", data.getData().address));
                                    tvSellerName.setText(String.format("收货人：%s", data.getData().seller_name));
                                    tvSellerPhone.setText(data.getData().seller_phone);

                                    tvSend.setVisibility(View.VISIBLE);
                                    tvEditExpress.setVisibility(View.GONE);

                                    llExpress.setVisibility(View.VISIBLE);
                                    tvExpress.setText(String.format("%s %s", data.getData().express_name, data.getData().invoice_no));

                                }
                            }  else if ("3".equals(data.getData().seller_state) && "3".equals(data.getData().refund_state)) {
//                                不同意申请
                                tvStatus.setText("平台驳回了您的退款申请");
                                llReject.setVisibility(View.VISIBLE);
                                tvReject.setText(data.getData().seller_message);
                            } else {
                                DyToast.getInstance().info(data.getMessage());
                                finish();
                            }

                        } else {
                            DyToast.getInstance().warning(data.getMessage());
                            finish();
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


    private void showShouhuDialog(RefundInfoBean infoBean) {
        DialogUtils.getInstance()
                .with(this)
                .setlayoutPosition(Gravity.CENTER)
                .setlayoutPading(150, 0, 150, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FROM_BOTTOM)
                .setlayoutId(R.layout.dialog_shouhou_type)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DialogUtils.dismiss();
                        }
                    });
                    view.findViewById(R.id.fl_th).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //退货 退款
                            DialogUtils.dismiss();
                            llSellerInfo.setVisibility(View.VISIBLE);
                            tvSellerLocation.setText(String.format("收货地址： %s", infoBean.address));
                            tvSellerName.setText(String.format("收货人：%s", infoBean.seller_name));
                            tvSellerPhone.setText(infoBean.seller_phone);
                            tvSend.setVisibility(View.GONE);
                            tvEditExpress.setVisibility(View.VISIBLE);

                        }
                    });
                    view.findViewById(R.id.fl_tk).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //仅退款
                            DialogUtils.dismiss();
                            commit();

                        }
                    });
                })
                .show();
    }

    private void commit() {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        map.put("token", mToken);
        map.put("refund_type", "1");
        HttpFactory.getMallInstance().returnsOk(map)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if (data.status.equals(AppConstant.SUCCESS)) {
                            DyToast.getInstance().success(data.message);
                            getData();
                        } else {
                            DyToast.getInstance().warning(data.getMessage());
                            finish();
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


    @OnClick({R.id.tv_edit_express, R.id.tv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_edit_express:
                startAct(EditExpressActivity.class, map, 1);
                break;
            case R.id.tv_send:
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getData();
    }

}
