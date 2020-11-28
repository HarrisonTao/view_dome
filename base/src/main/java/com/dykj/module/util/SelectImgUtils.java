package com.dykj.module.util;

import android.content.Intent;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.dykj.module.activity.SelectImgActivity;
import com.dykj.module.base.BaseApplication;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.Serializable;
import java.util.List;


/**
 * Created by lcjingon 2019/10/17.
 * description:
 */

public class SelectImgUtils implements LifecycleObserver {

    private static volatile SelectImgUtils singletonLazy;

    private SelectImgUtils() {

    }

    public static SelectImgUtils getInstance() {
        if (null == singletonLazy) {
            synchronized (SelectImgUtils.class) {
                if (null == singletonLazy) {
                    singletonLazy = new SelectImgUtils();
                }
            }
        }

        return singletonLazy;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void destroy() {
        MyLogger.dLog().e("Lifecycle.Event：" + "ON_DESTROY");
        onDestroy = true;
    }

    private boolean onDestroy = false;

    private SelectCallBack callBack;

    public void select(Lifecycle lifecycle, SelectCallBack callBack) {
        select(1, lifecycle, callBack);
    }

    /**
     * 图片选择
     * @param count     图片数量
     * @param lifecycle  监听生命周期
     * @param callBack 回调
     */
    public void select(int count, Lifecycle lifecycle, SelectCallBack callBack) {
        this.callBack = callBack;
        lifecycle.addObserver(this);
        onDestroy = false;
        Intent intent = new Intent(BaseApplication.getInstance(), SelectImgActivity.class);
        intent.putExtra("count", count);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BaseApplication.getInstance().startActivity(intent);
    }

    /**
     * 设置 图片选择回调
     * @param selectList 选中的图片数据
     */
    public void setBack(List<LocalMedia> selectList) {
        if (!onDestroy && callBack != null) {
            callBack.onSelectResult(selectList);
        }
    }

    public interface SelectCallBack extends Serializable {

        /**
         * @param selectList   选中的图片数据
         */
        void onSelectResult(List<LocalMedia> selectList);
    }

    public static final String BACK_TAG = "imgSelectCallBack";


}
