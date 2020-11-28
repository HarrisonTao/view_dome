package com.dykj.youfeng.mall.activity;


import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mall.adapter.RefundImgAdapter;
import com.dykj.youfeng.mode.ExpBean;
import com.dykj.youfeng.mode.ImageInfoBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.AppConstant;
import com.dykj.module.activity.JBrowseImgActivity;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.SelectImgUtils;
import com.dykj.module.util.toast.DyToast;
import com.dykj.module.view.dialog.SelectDialog;
import com.tencent.mmkv.MMKV;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author lcjing
 * 填写物流
 */
public class EditExpressActivity extends BaseActivity {


    @BindView(R.id.tv_exp_name)
    TextView tvExpName;
    @BindView(R.id.et_exp_no)
    EditText etExpNo;
    @BindView(R.id.tv_phone)
    EditText tvPhone;
    @BindView(R.id.rv_img)
    RecyclerView rvImg;


    private RefundImgAdapter imgAdapter;
    private ArrayList<String> imgs;

    @Override
    public int intiLayout() {
        return R.layout.activity_edit_express;
    }

    @Override
    public void initData() {
        initTitle("填写物流");
        tvExpName.requestFocus();
    }

    @Override
    public void doBusiness() {
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
                    imgs.remove(imgs.size() - 1);
                    //回调
                    for (int i = 0; i < selectList.size(); i++) {
//                        imgs.add(selectList.get(i).getCompressPath());
                        upImg(selectList.get(i).getCompressPath());
                    }
//                    if (imgs.size() < 3) {
//                        imgs.add("");
//                    }
//                    imgAdapter.refreshData();
                });
            } else {
                //查看图片
                ArrayList<String> imgList = new ArrayList<>(imgs);
                if (StringUtils.isEmpty(imgList.get(imgList.size() - 1))) {
                    imgList.remove(imgList.size() - 1);
                }
                JBrowseImgActivity.start(EditExpressActivity.this, imgList, position, imgAdapter.getImgList());
            }
        });
    }


    @OnClick({R.id.tv_exp_name, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_exp_name:
                getExpList();
                break;
            case R.id.tv_confirm:
                commit();
                break;
            default:
                break;
        }
    }


    private void getExpList() {
        HttpFactory.getMallInstance().getExpress()
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<List<ExpBean>>>() {
                    @Override
                    public void success(HttpResponseData<List<ExpBean>> data) {
                        if (data.status.equals(AppConstant.SUCCESS)) {
                            List<String> list = new ArrayList<>();
                            for (int i = 0; i < data.getData().size(); i++) {
                                list.add(data.getData().get(i).e_name);
                            }
                            new SelectDialog(EditExpressActivity.this,
                                    (position, value) -> {
                                        tvExpName.setText(data.getData().get(position).e_name);
                                    },
                                    "选择物流公司", list).setLifecycle(getLifecycle()).show();
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


    private void commit() {
        String invoiceNo = etExpNo.getText().toString();
        if (StringUtils.isEmpty(invoiceNo)) {
            DyToast.getInstance().warning("请输入物流单号");
            return;
        }
        String expressName = tvExpName.getText().toString();
        if (StringUtils.isEmpty(expressName)) {
            DyToast.getInstance().warning("请选择物流公司");
            return;
        }
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        Map<String, String> map = (Map<String, String>) getIntentData();
        map.put("token", mToken);
        map.put("refund_type", "2");
        map.put("invoice_no", invoiceNo);
        map.put("express_name", expressName);
        HttpFactory.getMallInstance().returnsOk(map)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if (data.status.equals(AppConstant.SUCCESS)) {
                            DyToast.getInstance().success(data.getMessage());
                            setResult(1);
                            finish();
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


    private void upImg(String mPhotoPath) {
        String token = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        File file = new File(mPhotoPath);
        MultipartBody build = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .addFormDataPart("token", token)
                .build();
        HttpFactory.getMallInstance().upImg(build)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<ImageInfoBean>>() {
                    @Override
                    public void success(HttpResponseData<ImageInfoBean> data) {
                        if ("9999".equals(data.status)) {
                            if (imgs.size() > 0 && StringUtils.isEmpty(imgs.get(imgs.size() - 1))) {
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

}
