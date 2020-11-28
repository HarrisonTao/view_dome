package com.dykj.module.util.PhotoGallery;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * @date 2019/4/8.
 * description：
 */
public class PhotoGalleryViewPager extends ViewPager {
    public PhotoGalleryViewPager(Context context) {
        super(context);
    }

    public PhotoGalleryViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException var3) {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException var3) {
            return false;
        }
    }
}