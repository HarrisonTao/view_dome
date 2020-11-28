package com.dykj.module.util.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dykj.module.AppConstant;
import com.dykj.module.activity.JBrowseImgActivity;
import com.dykj.module.view.imgbrowser.util.JMatrixUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * h5图片加载工具
 */

public class URLImageParser implements Html.ImageGetter {
    TextView mTextView;
    Context context;

    public URLImageParser(TextView textView, Context context) {
        this.mTextView = textView;
        this.context=context;
        mTextView.setOnClickListener(v -> {
            List<Rect> rects = new ArrayList<>();
            for (int i = 0; i < imgs.size(); i++) {
                rects.add(JMatrixUtil.getDrawableBoundsInView(new ImageView(context)));
            }
            JBrowseImgActivity.start(context, imgs, 0, rects);
        });
    }

    private ArrayList<String> imgs=new ArrayList<>();



    @Override
    public Drawable getDrawable(String source) {
        final URLDrawable urlDrawable = new URLDrawable();
        if (!source.startsWith("http")) {
            source = AppConstant.MALL_HOST_IMG + source;
        }
        imgs.add(source);
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .centerCrop()
                .dontAnimate();
        Glide.with(mTextView.getContext())
                .load(source)
                .apply(options)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        Bitmap newBitmap;
                        BitmapDrawable bd = (BitmapDrawable) resource;
                        Bitmap loadedImg = bd.getBitmap();
                        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                        DisplayMetrics dm = new DisplayMetrics();
                        wm.getDefaultDisplay().getMetrics(dm);
                        int width = dm.widthPixels;         // 屏幕宽度（像素）
                        float scale = (float) ((float) width / (float) loadedImg.getWidth());
                        newBitmap = setImgScale(loadedImg, scale);
                        urlDrawable.bitmap = newBitmap;
                        urlDrawable.setBounds(0, 0, newBitmap.getWidth(), newBitmap.getHeight());
                        mTextView.invalidate();
                        mTextView.setText(mTextView.getText());
                    }
                });
        return urlDrawable;
    }

    private Bitmap setImgScale(Bitmap bm, float scale) {
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // 得到新的图片.
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }
}

