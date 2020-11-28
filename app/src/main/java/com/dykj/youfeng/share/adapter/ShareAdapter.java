package com.dykj.youfeng.share.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.ShareListBean;
import com.dykj.youfeng.tools.QRCodeUtil;
import com.dykj.module.util.BaseToolsUtil;
import com.dykj.module.util.GlideUtils;

import java.io.ByteArrayOutputStream;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ShareAdapter extends PagerAdapter {

    private List<ShareListBean.ListBean> mList;
    private String url;
    private Context mCt;
    private View currentView = null;
    private String mPhone;


    public ShareAdapter(List<ShareListBean.ListBean> listBean, String url, Context mct) {
        this.mList = listBean;
        this.url = url;
        this.mCt = mct;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        currentView = (View) object;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mCt).inflate(R.layout.item_img, container, false);
        TextView tvSharePhone = view.findViewById(R.id.tv_share_phone);
        tvSharePhone.setText(TextUtils.isEmpty(mPhone) ? "" : BaseToolsUtil.hintPhone(mPhone));
        ImageView img = view.findViewById(R.id.img_item_img);
        GlideUtils.setImage(img, R.mipmap.jiazai_bg, mList.get(position).image);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ImageView mIvCode = view.findViewById(R.id.iv_code);
        Bitmap bitmap = QRCodeUtil.createQRCodeBitmap(url, 300, 300, "UTF-8",
                "H", "1", Color.BLACK, Color.WHITE, null, 0.2f, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        GlideUtils.setImage(mIvCode, bytes);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public View getCurrentItemView() {
        return currentView;
    }

    public void setSharePhone(String decodeString) {
        this.mPhone = decodeString;
    }
}