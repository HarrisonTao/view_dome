package com.dykj.module.base;


import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.blankj.utilcode.util.Utils;
import com.dykj.module.R;
import com.dykj.module.util.ACache;
import com.dykj.module.util.MyLogger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.mmkv.MMKV;

import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author WZ
 * @date 2019/5/14.
 * 严格要求自己，对每一行代码负责
 * description：
 */
public class BaseApplication extends Application {
    public static ACache aCacheAPP;
    private static BaseApplication instance;

    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.transparent, R.color.font_ff66);
            return new ClassicsHeader(context).setEnableLastTime(false);
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) ->
                new ClassicsFooter(context).setDrawableSize(20));
    }

    public static BaseApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        aCacheAPP = ACache.get(this);
        instance = this;
        MMKV.initialize(this);
        Utils.init(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //7.0
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
        RxJavaPlugins.setErrorHandler(throwable ->
                MyLogger.dLog().e("onRxJavaErrorHandler ---->: $throwable", throwable));




    }


}
