package com.dykj.youfeng.home.receipt.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import com.dykj.module.base.BaseActivity;
import com.dykj.youfeng.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageActivity extends BaseActivity {
    @BindView(R.id.image)
    ImageView image;

    private Intent intent;

    @Override
    public int intiLayout() {
        return R.layout.activity_hb_image_layout;
    }

    @Override
    public void initData() {
        initTitle("扫码支付");
        intent=getIntent();
        String data=intent.getStringExtra("data");
        Bitmap bitmap=stringtoBitmap(data);
        image.setImageBitmap(bitmap);
    }

    @Override
    public void doBusiness() {

    }
    public Bitmap stringtoBitmap(String string) {
        //将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
