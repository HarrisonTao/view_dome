package com.dykj.module.activity;

import android.content.Intent;

import com.dykj.module.R;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.PhotoGallery.GlideEngine;
import com.dykj.module.util.SelectImgUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;


public class SelectImgActivity extends BaseActivity {


    @Override
    public int intiLayout() {
        return R.layout.activity_select_img;
    }

    @Override
    public void initData() {

    }

    @Override
    public void doBusiness() {
        int count=getIntent().getIntExtra("count",1);
        overridePendingTransition(R.anim.pop_bottom_to_top, R.anim.pop_top_to_bottom);
        //相机
        findViewById(R.id.tv_camera).setOnClickListener(view ->
                PictureSelector.create(this)
                        .openCamera(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .maxSelectNum(count)// 最大图片选择数量 int
                        .imageSpanCount(4)// 每行显示个数 int
                        .previewImage(true)// 是否可预览图片 true or false
                        .isCamera(false)// 是否显示拍照按钮 true or false
                        .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                        .loadImageEngine(GlideEngine.createGlideEngine())
                        .compress(true)// 是否压缩 true or false
                        .minimumCompressSize(200)
                        .forResult(PictureConfig.CHOOSE_REQUEST));
        //图库
        findViewById(R.id.tv_photo).setOnClickListener(view -> {
            PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .maxSelectNum(count)// 最大图片选择数量 int
                    .imageSpanCount(4)// 每行显示个数 int
                    .previewImage(true)// 是否可预览图片 true or false
                    .isCamera(false)// 是否显示拍照按钮 true or false
                    .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                    .loadImageEngine(GlideEngine.createGlideEngine())
                    .compress(true)// 是否压缩 true or false
                    .minimumCompressSize(200)
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        });
        findViewById(R.id.tv_cancel).setOnClickListener(view -> back(findViewById(R.id.tv_cancel)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList.size() > 0) {
                        LocalMedia localMedia = selectList.get(0);
                        String mPhotoPath = "";
                        if (localMedia.isCompressed()) {
                            mPhotoPath = localMedia.getCompressPath();
                            MyLogger.dLog().e("图片路径 " + 0 + " == " + mPhotoPath);
                        }

                    }
                    SelectImgUtils.getInstance().setBack(selectList);
//                    HashMap<String, Object> map = new HashMap<>();
//                    map.put("data", selectList);
//                    MessageWrap messageWrap = MessageWrap.getInstance(BACK_TAG, map);
//                    EventBus.getDefault().post(messageWrap);
                    finish();
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.pop_top_to_bottom, R.anim.pop_top_to_bottom);
    }
}
