package com.dykj.module.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * @author LewChich
 * @date: 2019-10-09
 * @describe: 通用工具类
 */
public class UniversalTools {

    private UniversalTools() {

    }

    public static UniversalTools getInstance() {
        return new UniversalTools();
    }

    /**
     * 判断当前应用是否是debug状态
     */
    public boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

}
