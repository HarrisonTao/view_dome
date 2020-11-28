package com.dykj.module.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * Created by lcjingon 2019/11/22.
 * description:输入框被遮挡工具类
 */

public class MyWorkGround {
    public static void assistActivity(Activity activity) {
        new MyWorkGround(activity);
    }
    private View mChildOfContent;
    /*保存改变高度前的高度*/
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;
    private Activity activity;
    private MyWorkGround(final Activity activity) {
        this.activity = activity;
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        //整体布局监听：当软键盘打开，关闭，虚拟键盘显示，隐藏时都会调用该方法
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                possiblyResizeChildOfContent();
            }
        });
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent() {
        //activity有效高度
        int usableHeightNow = computeUsableHeight();
        //有效高度与之前的有效高度不等时，则重新设置高度
        if (usableHeightNow != usableHeightPrevious) {
            //可能是因为用了沉浸式，所以在有效高度上需要加上状态栏的高度
            //如果你发现这样写高度不对，可以去掉状态栏高度试试
            frameLayoutParams.height = usableHeightNow + getStatusHeight(activity);
            mChildOfContent.requestLayout();
            //保存本次有效高度
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return r.bottom - r.top;// 全屏模式下： return r.bottom
    }
    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context)
    {

        int statusHeight = -1;
        try
        {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return statusHeight;
    }
//————————————————
//    版权声明：本文为CSDN博主「Motejia」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
//    原文链接：https://blog.csdn.net/Motejia/article/details/93481204
}
