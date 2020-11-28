package com.dykj.module.view.dialog;

import android.app.Dialog;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;


import com.dykj.module.util.MyLogger;

/**
 * Created by lcjingon 2019/10/15.
 * description:
 *
 * @author lcjing
 */
public abstract class BaseDialog implements LifecycleObserver {
    public Dialog dialog;


    /**
     * 设置所在Activity活Fragment的生命周期观察者
     * 销毁时自动关闭弹窗
     * 防止异常退出时产生内存泄漏
     *
     * @param lifecycle
     * @return
     */
    public BaseDialog setLifecycle(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        return this;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void destroy() {
        MyLogger.dLog().e("Lifecycle.Event：" + "ON_DESTROY");
        dismiss();
    }


    /**
     * dialog初始化 并显示
     */
    public abstract void show();

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }


}
