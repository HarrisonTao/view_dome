package com.dykj.module.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

/**
 * Created by Administrator on 2016/11/30 0030.
 */

public class GuideBannerLocationAdapter extends PagerAdapter {
    private Context context;
    private int[] list;
    private View[] views;

    public GuideBannerLocationAdapter(Context context, int[] list) {
        this.context = context;
        this.list = list;
        views = new View[list.length];
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == (view);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (views[position] == null) {
            views[position] = new ImageView(context);
            ((ImageView) views[position]).setScaleType(ImageView.ScaleType.FIT_XY);
        }
        ((ImageView) views[position]).setImageResource(list[position]);
        container.addView(views[position]);
        return views[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //        super.destroyItem(container, position, object);
        container.removeView(views[position]);
    }
}
