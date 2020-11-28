package com.dykj.module.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/11/30 0030.
 */

public class ImgPagerAdapter extends PagerAdapter {
    private Context context;
    private List<String> list;
    private View[] views;

    public ImgPagerAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        views = new View[list.size()];
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == (view);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (views[position] == null) {
            views[position] = new ImageView(context);
            ((ImageView) views[position]).setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
//        ((ImageView) views[position]).setImageResource(list.get())]);
        GlideUtils.setImage((ImageView) views[position], list.get(position));
        container.addView(views[position]);
        views[position].setOnClickListener(v -> {
            if (itemClick!=null) {
                itemClick.click(position);
            }
        });

        return views[position];
    }

    ItemClick itemClick;

    public void setOnItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //        super.destroyItem(container, position, object);
        container.removeView(views[position]);
    }

    public interface ItemClick {
        void click(int position);
    }
}
