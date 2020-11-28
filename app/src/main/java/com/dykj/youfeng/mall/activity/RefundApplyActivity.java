package com.dykj.youfeng.mall.activity;


import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mall.adapter.RefundImgAdapter;
import com.dykj.youfeng.mode.ImageInfoBean;
import com.dykj.youfeng.mode.OrderInfoBean;
import com.dykj.youfeng.mode.RefundReasonBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.AppConstant;
import com.dykj.module.activity.JBrowseImgActivity;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.GlideUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.SelectImgUtils;
import com.dykj.module.util.toast.DyToast;
import com.dykj.module.view.dialog.SelectDialog;
import com.tencent.mmkv.MMKV;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author lcjing
 * 申请退款
 */
public class RefundApplyActivity extends BaseActivity {

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
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_reason)
    TextView tvReason;
    @BindView(R.id.tv_return_money)
    TextView tvReturnMoney;
    @BindView(R.id.et_des)
    EditText etDes;
    @BindView(R.id.rv_img)
    RecyclerView rvImg;
    private RefundImgAdapter imgAdapter;
    private ArrayList<String> imgs;
    private OrderInfoBean.GoodsListBean bean;

    @Override
    public int intiLayout() {
        return R.layout.activity_refund_apply;
    }

    @Override
    public void initData() {
        bean = (OrderInfoBean.GoodsListBean) getIntentData();
        initTitle("申请退款");
        imgs = new ArrayList<>();
        imgs.add("");
        imgAdapter = new RefundImgAdapter(R.layout.item_refund_img, imgs);
        rvImg.setAdapter(imgAdapter);
        imgAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.iv_delete) {
                //删除图片
                imgs.remove(position);
                if (!StringUtils.isEmpty(imgs.get(imgs.size() - 1))) {
                    imgs.add("");
                }
                imgAdapter.refreshData();
            } else if (StringUtils.isEmpty(imgs.get(position))) {
                //添加图片
                SelectImgUtils.getInstance().select(4 - imgs.size(), getLifecycle(), (SelectImgUtils.SelectCallBack) selectList -> {
                    //回调
                    for (int i = 0; i < selectList.size(); i++) {
                        upImg(selectList.get(i).getCompressPath());
                    }
                });
            } else {
                //查看图片
                ArrayList<String> imgList = new ArrayList<>(imgs);
                if (StringUtils.isEmpty(imgList.get(imgList.size() - 1))) {
                    imgList.remove(imgList.size() - 1);
                }
                JBrowseImgActivity.start(RefundApplyActivity.this, imgList, position, imgAdapter.getImgList());
            }
        });
    }

    @Override
    public void doBusiness() {
        tvReason.requestFocus();
        tvGoodsName.setText(bean.goods_name);
        tvCount.setText("x" + bean.goods_num);
        GlideUtils.setMallImage(ivGoodsImg, bean.goods_image);
        tvGoodsPrice.setText("￥" + bean.goods_price);
        tvGoodsType.setText(bean.goods_spec_name);
        tvReturnMoney.setText("￥"+bean.refund_money);
    }

    @OnClick({R.id.ll_status, R.id.ll_reason, R.id.tv_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_status:
//                List<String> statusList = new ArrayList<>();
//                statusList.add("未收到货");
//                statusList.add("已收到货");
//                new SelectDialog(this,
//                        (position, value) -> tvStatus.setText(value),
//                        "货物状态", statusList).setLifecycle(getLifecycle()).show();
                break;
            case R.id.ll_reason:
                getRefundReason();
                break;
            case R.id.tv_commit:
                if (StringUtils.isEmpty(reasonId)) {
                    ToastUtils.showShort("请选择退款原因");
                    return;
                }
                String token = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
                Map<String, String> map = new HashMap<>();
                map.put("token", token);
                map.put("order_goods_id", bean.rec_id);
                map.put("order_id", bean.order_id);
                map.put("reason_id", reasonId);
                map.put("buyer_message", etDes.getText().toString());
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < imgs.size(); i++) {
                    if (StringUtils.isEmpty(imgs.get(i))) {
                        break;
                    }
                    stringBuilder.append(imgs.get(i)).append(",");
                }
                String picInfo ="";
                if (stringBuilder.length()>1) {
                     stringBuilder.substring(0, stringBuilder.length() - 1);
                }
                map.put("pic_info", picInfo);
                applyReturns(map);
                break;
            default:
                break;
        }
    }

    private String reasonId = "";

    private void getRefundReason() {
        HttpFactory.getMallInstance().refundReason()
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<List<RefundReasonBean>>>() {
                    @Override
                    public void success(HttpResponseData<List<RefundReasonBean>> data) {
                        if (data.status.equals(AppConstant.SUCCESS)) {
                            List<String> list = new ArrayList<>();
                            for (int i = 0; i < data.getData().size(); i++) {
                                list.add(data.getData().get(i).reason_info);
                            }
                            new SelectDialog(RefundApplyActivity.this,
                                    (position, value) -> {
                                        reasonId = data.getData().get(position).reason_id;
                                        tvReason.setText(data.getData().get(position).reason_info);
                                    },

                                    "退款原因", list).setLifecycle(getLifecycle()).show();
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


    private void upImg(String mPhotoPath) {
        String token = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        File file = new File(mPhotoPath);
        MultipartBody build = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .addFormDataPart("token", token)
                .build();


        HttpFactory.getMallInstance().upImg(build)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<ImageInfoBean>>() {
                    @Override
                    public void success(HttpResponseData<ImageInfoBean> data) {
                        if ("9999".equals(data.status)) {
                            if (imgs.size()>0&& StringUtils.isEmpty( imgs.get(imgs.size() - 1))) {
                                imgs.remove(imgs.size() - 1);
                            }
                            imgs.add(data.getData().images);
                            if (imgs.size() < 3) {
                                imgs.add("");
                            }
                            imgAdapter.refreshData();
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


    private void applyReturns(Map<String, String> map) {
        HttpFactory.getMallInstance().applyReturns(map)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if (data.status.equals(AppConstant.SUCCESS)) {
                            DyToast.getInstance().success(data.message);
                            setResult(1);
                            finish();
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
}
