package com.dykj.module.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dykj.module.AppConstant;
import com.dykj.module.R;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by lcjing on 2019/1/4.
 */

public class GlideUtils {

    public static void setImage(ImageView imageView, String url) {

        setImage(imageView, R.mipmap.load_fail, url);
    }
    public static void setImage(ImageView imageView, byte[] bytes) {
        Glide.with(imageView.getContext())
                .load(bytes)
                .into(imageView);
    }

    public static void setMallImage(ImageView imageView, String url){
        if (!StringUtils.isEmpty(url)&&!url.startsWith("http")) {
            url= AppConstant.MALL_HOST_IMG+url;
        }
        setImage(imageView, R.mipmap.load_fail, url);
    }

    public static void setMallImage(ImageView imageView,  int defaultImg, String url){
        if (!StringUtils.isEmpty(url)&&!url.startsWith("http")) {
            url= AppConstant.MALL_HOST_IMG+url;
        }
        setImage(imageView,defaultImg, url);
    }

    public static void setImageRound(RoundedImageView imageView, String url) {
        setImageRound(imageView, R.mipmap.load_fail, url);
    }

    public static void setImageFile(ImageView imageView, int defaultImg, String url) {
        if (StringUtils.isEmpty(url)) {
            imageView.setImageResource(defaultImg);
            return;
        }
//        if (!url.startsWith("http")) {
//            url=DyConstant.HOST_IMAGE+url;
//        }

        RequestOptions options = new RequestOptions()
                .placeholder(defaultImg)                //加载成功之前占位图
                .error(defaultImg)//加载错误之后的错误图
                .fallback(defaultImg) ;//url为空的时候,显示的图片
        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .into(imageView);

    }

    public static void setImage(ImageView imageView, int defaultImg, String url) {
        if (StringUtils.isEmpty(url)) {
            imageView.setImageResource(defaultImg);
            return;
        }
        String m="\\";
        while (url.contains(m)){
            url=url.replace(m,"/");
        }
        if (!url.startsWith("http")) {
            url = AppConstant.HOST_IMAGE + url;
        }

        RequestOptions options = new RequestOptions()
                .placeholder(defaultImg)                //加载成功之前占位图
                .error(defaultImg)//加载错误之后的错误图
                .fallback(defaultImg) ;//url为空的时候,显示的图片
        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .into(imageView);

    }



    public static void setImageNoCache(ImageView imageView, int defaultImg, String url) {
        if (StringUtils.isEmpty(url)) {
            imageView.setImageResource(defaultImg);
            return;
        }
        if (!url.startsWith("http")) {
            url = AppConstant.HOST_IMAGE + url;
        }

        RequestOptions options = new RequestOptions()
                .placeholder(defaultImg)                //加载成功之前占位图
                .error(defaultImg)//加载错误之后的错误图
                .fallback(defaultImg) ;//url为空的时候,显示的图片
        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .into(imageView);

    }

    public static void setImageFileRound(ImageView imageView, int defaultImg, String url) {
        if (StringUtils.isEmpty(url)) {
            imageView.setImageResource(defaultImg);
            return;
        }
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(defaultImg)                //加载成功之前占位图
                .error(defaultImg)//加载错误之后的错误图
                .fallback(defaultImg);//url为空的时候,显示的图片

        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setImageDrawable(resource);
                    }
                });

    }

    public static void setImageRound(ImageView imageView, int defaultImg, String url) {
        if (StringUtils.isEmpty(url)) {
            imageView.setImageResource(defaultImg);
            return;
        }
        if (!url.startsWith("http")) {
            url = AppConstant.HOST_IMAGE + url;
        }

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(defaultImg)                //加载成功之前占位图
                .error(defaultImg)//加载错误之后的错误图
                .fallback(defaultImg);//url为空的时候,显示的图片

        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setImageDrawable(resource);
                    }
                });

    }


    public static void setImage(Context context, ImageView imageView, int defaultImg, String url) {
        RequestOptions options = new RequestOptions()
                .placeholder(defaultImg)                //加载成功之前占位图
                .error(defaultImg)//加载错误之后的错误图
                .fallback(defaultImg) ;//url为空的时候,显示的图片
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);


    }
}
