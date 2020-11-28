package com.dykj.module.util.toast;

import android.text.TextUtils;

import com.dykj.module.base.BaseApplication;
import com.dykj.module.util.MyLogger;


/**
 * @author LewChich
 * @date 2019/8/20
 * description：DyToast
 */
public class DyToast {
    private static DyToast instance = new DyToast();

    public static DyToast getInstance() {
        return instance;
    }

    public void success(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Toasty.success(BaseApplication.getInstance(), msg);
    }

    public void error(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if ("登录状态异常".equals(msg)) {

        }else {
            Toasty.error(BaseApplication.getInstance(), msg);
        }

    }

    public void info(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        try {
            Toasty.info(BaseApplication.getInstance(), msg);
        } catch (Exception e) {
            MyLogger.dLog().e(e);
        }

    }

    public void warning(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Toasty.warning(BaseApplication.getInstance(), msg);
    }
}
