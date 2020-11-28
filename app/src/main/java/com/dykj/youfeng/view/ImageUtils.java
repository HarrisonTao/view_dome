package com.dykj.youfeng.view;

import android.text.TextUtils;
import android.widget.ImageView;


import com.dykj.module.util.GlideUtils;

/***
 * 加载图片
 */
public class ImageUtils {

    /**
     * 实名认证上传头像
     *
     * @param resource
     * @param url
     * @param imageView
     */
    public static void loadShopImage(int resource, String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(resource);
            return;
        }
        GlideUtils.setImageFile(imageView,resource,url);
    }

}
