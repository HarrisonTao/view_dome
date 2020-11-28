package com.dykj.youfeng.mall.adapter;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.EvaInfoBean;
import com.dykj.youfeng.mode.ImageInfoBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.activity.JBrowseImgActivity;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.GlideUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.SelectImgUtils;
import com.dykj.module.util.toast.DyToast;
import com.tencent.mmkv.MMKV;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by lcjingon 2019/11/11.
 * description:
 */

public class EvaInfoAdapter extends BaseQuickAdapter<EvaInfoBean, BaseViewHolder> {

    public EvaInfoAdapter(BaseActivity activity, int layoutResId, List<EvaInfoBean> data) {
        super(layoutResId, data);
        this.activity = activity;
    }


    private BaseActivity activity;


    @Override
    protected void convert(BaseViewHolder helper, final EvaInfoBean item) {


        //    @BindView(R.id.iv_goods)
//    ImageView ivGoods;
//    @BindView(R.id.tv_goods_name)
//    TextView tvGoodsName;
//    @BindView(R.id.tv_goods_type)
//    TextView tvGoodsType;
//    @BindView(R.id.tv_goods_count)
//    TextView tvGoodsCount;
//    @BindView(R.id.et_content)
//    EditText etContent;

        GlideUtils.setMallImage(helper.getView(R.id.iv_goods), item.goods_image);
        helper.setText(R.id.tv_goods_name, item.goods_name)
                .setText(R.id.tv_goods_type, item.goods_spec_name)
                .setText(R.id.tv_goods_count, "x" + item.goods_num);
        item.setEtContent(helper.getView(R.id.et_content));
        EvaImgAdapter imgAdapter;
        imgAdapter = new EvaImgAdapter(R.layout.item_eva_img, item.imgs);
        imgAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.iv_c) {
                //删除图片
                item.imgs.remove(position);
                if (!StringUtils.isEmpty(item.imgs.get(item.imgs.size() - 1))) {
                    item.imgs.add("");
                }
                imgAdapter.refreshData();
            } else if (StringUtils.isEmpty(item.imgs.get(position))) {
                //添加图片
                SelectImgUtils.getInstance().select(4 - item.imgs.size(), activity.getLifecycle(), (SelectImgUtils.SelectCallBack) selectList -> {
                    //回调
                    for (int i = 0; i < selectList.size(); i++) {
                        upImg(selectList.get(i).getCompressPath(), item, imgAdapter);
                    }

                });
            } else {
                //查看图片
                ArrayList<String> imgList = new ArrayList<>(item.imgs);
                if (StringUtils.isEmpty(imgList.get(imgList.size() - 1))) {
                    imgList.remove(imgList.size() - 1);
                }
                JBrowseImgActivity.start(mContext, imgList, position, imgAdapter.getImgList());
            }

        });
        RecyclerView rvImg = helper.getView(R.id.rv_img);
        rvImg.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        rvImg.setAdapter(imgAdapter);

    }

    private void upImg(String mPhotoPath, EvaInfoBean item, EvaImgAdapter imgAdapter) {
        String token = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        File file = new File(mPhotoPath);
        MultipartBody build = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .addFormDataPart("token", token)
                .build();
        HttpFactory.getMallInstance().upImg(build)
                .compose(HttpObserver.schedulers(mContext))
                .as(HttpObserver.life(activity))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<ImageInfoBean>>() {
                    @Override
                    public void success(HttpResponseData<ImageInfoBean> data) {
                        if ("9999".equals(data.status)) {
                            if (item.imgs.size() > 0 && StringUtils.isEmpty(item.imgs.get(item.imgs.size() - 1))) {
                                item.imgs.remove(item.imgs.size() - 1);
                            }
                            item.imgs.add(data.getData().images);
                            if (item.imgs.size() < 3) {
                                item.imgs.add("");
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
