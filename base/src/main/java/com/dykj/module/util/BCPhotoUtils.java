package com.dykj.module.util;

import android.app.Activity;
import android.content.Context;

import com.dykj.module.R;
import com.dykj.module.util.PhotoGallery.GlideEngine;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;

/**
 * Created by 小智
 * on 2017/12/19
 * 描述：
 */

public class BCPhotoUtils {
    /**
     * 圆形图片裁剪
     *
     * @param context
     */
    public static void initCircle(Context context) {
        PictureSelector.create((Activity) context)
                // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .openGallery(PictureMimeType.ofImage())
                // 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .theme(R.style.picture_default_style)
                // 最大图片选择数量
                .maxSelectNum(1)
                // 最小选择数量
                .minSelectNum(1)
                // 每行显示个数
                .imageSpanCount(4)
                // 多选 or 单选
                .selectionMode(PictureConfig.SINGLE)
                .loadImageEngine(GlideEngine.createGlideEngine())
                // 是否可预览图片
                .previewImage(true)
                // 是否可预览视频
                .previewVideo(false)
                // 是否可播放音频
                .enablePreviewAudio(false)
                // 是否显示拍照按钮
                .isCamera(true)
                // 图片列表点击 缩放效果 默认true
                .isZoomAnim(true)
                // 拍照保存图片格式后缀,默认jpeg
                .imageFormat(PictureMimeType.PNG)
                // 是否裁剪
                .enableCrop(true)
                // 是否压缩
                .compress(true)
                //同步true或异步false 压缩 默认同步
                .synOrAsy(true)
                // glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .glideOverride(160, 160)
                // 是否显示gif图片
                .isGif(false)
                // 裁剪框是否可拖拽
                .freeStyleCropEnabled(true)
                // 是否圆形裁剪
                .circleDimmedLayer(true)
                // 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropFrame(false)
                // 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .showCropGrid(false)
                // 是否开启点击声音
                .openClickSound(false)
                // 小于100kb的图片不压缩
                .minimumCompressSize(100)
                //结果回调onActivityResult code
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 非圆形裁剪
     *
     * @param context
     * @param maxNum
     */
    public static void initRectangle(Context context, int maxNum) {
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create((Activity) context)
                //全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .openGallery(PictureMimeType.ofImage())
                //主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .theme(R.style.picture_default_style)
                // 最大图片选择数量 int
                .maxSelectNum(maxNum)
                // 最小选择数量 int
                .minSelectNum(1)
                .loadImageEngine(GlideEngine.createGlideEngine())
                // 每行显示个数 int
                .imageSpanCount(4)
                .openClickSound(false)
                // 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .selectionMode(PictureConfig.SINGLE)
                // 是否可预览图片 true or false
                .previewImage(true)
                // 是否可预览视频 true or false
                .previewVideo(true)
                // 是否可播放音频 true or false
                .enablePreviewAudio(false)
                // 是否显示拍照按钮 true or false
                .isCamera(true)
                // 拍照保存图片格式后缀,默认jpeg
                .imageFormat(PictureMimeType.PNG)
                // 图片列表点击 缩放效果 默认true
                .isZoomAnim(true)
                // glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .sizeMultiplier(0.5f)
                // 自定义拍照保存路径,可不填
                .setOutputCameraPath("/CustomPath")
                // 是否裁剪 true or false
                .enableCrop(true)
                // 是否压缩 true or false
                .compress(true)
                // int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .glideOverride(400, 400)
                // 是否显示uCrop工具栏，默认不显示 true or false
                .hideBottomControls(false)
                // 是否显示gif图片 true or false
                .isGif(false)
                // 裁剪框是否可拖拽 true or false
                .freeStyleCropEnabled(false)
                // 是否圆形裁剪 true or false
                .circleDimmedLayer(false)
                // 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropFrame(true)
                // 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .showCropGrid(true)
                // 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .previewEggs(true)
                // 小于100kb的图片不压缩
                .minimumCompressSize(100)
                //同步true或异步false 压缩 默认同步
                .synOrAsy(true)
                // 裁剪是否可旋转图片 true or false
                .rotateEnabled(false)
                // 裁剪是否可放大缩小图片 true or false
                .scaleEnabled(true)
                //结果回调onActivityResult code 
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
}
