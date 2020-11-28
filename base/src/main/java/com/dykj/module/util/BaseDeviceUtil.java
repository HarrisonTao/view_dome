package com.dykj.module.util;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * @author WZ
 * @date 2019/5/16.
 * 严格要求自己，对每一行代码负责
 * description：
 */
public class BaseDeviceUtil {
    public static final int SCREEN_240P = 240;
    public static final int SCREEN_360P = 360;
    public static final int SCREEN_480P = 480;
    public static final int SCREEN_720P = 720;
    public static final int SCREEN_1080P = 1080;
    public static final int SCREEN_1280P = 1280;

    private BaseDeviceUtil() {
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = null;
        dm = context.getResources().getDisplayMetrics();
        return dm == null ? -1 : dm.heightPixels;
    }

    public static int getDeviceScreen(Context context) {
        DisplayMetrics dm = null;
        dm = context.getResources().getDisplayMetrics();
        if (dm == null) {
            return -1;
        } else {
            int screenWidth = dm.widthPixels;
            if (screenWidth <= 240) {
                return 240;
            } else if (screenWidth > 240 && screenWidth <= 360) {
                return 360;
            } else if (screenWidth > 360 && screenWidth <= 480) {
                return 480;
            } else if (screenWidth > 480 && screenWidth <= 720) {
                return 720;
            } else {
                return screenWidth > 720 && screenWidth <= 1080 ? 1080 : 1280;
            }
        }
    }

    public static String getNetworkType(Context context) {
        NetworkInfo info = ((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null) {
            return "无网络";
        } else if (info.getType() == 1) {
            return "WIFI";
        } else {
            switch(info.getSubtype()) {
                case 1:
                case 2:
                case 4:
                    return "2G";
                case 3:
                case 5:
                case 6:
                case 8:
                case 12:
                    return "3G";
                case 7:
                case 9:
                case 10:
                case 11:
                default:
                    return "";
                case 13:
                    return "4G";
            }
        }
    }

    public static String getSimType(Context context) {
        String IMSI = ((TelephonyManager)context.getSystemService("phone")).getSubscriberId();
        if (TextUtils.isEmpty(IMSI)) {
            return "无服务";
        } else if (!IMSI.startsWith("46000") && !IMSI.startsWith("46002")) {
            if (IMSI.startsWith("46001")) {
                return "中国联通";
            } else {
                return IMSI.startsWith("46003") ? "中国电信" : "未知运营商";
            }
        } else {
            return "中国移动";
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return info != null ? info.isAvailable() : false;
    }

    public static boolean isWifi(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return info != null && info.getType() == 1;
    }

    public static boolean isMobile(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return info != null && info.getType() == 0;
    }

    private static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService("connectivity");
        return cm.getActiveNetworkInfo();
    }

    public static boolean checkSdCard() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static String getDeviceIEMI(Context context) {
        return String.valueOf(((TelephonyManager)context.getSystemService("phone")).getDeviceId());
    }

    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static String getOSVersionName() {
        return VERSION.RELEASE;
    }

    public static int getOSVersionCode() {
        return VERSION.SDK_INT;
    }

    public static String getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;

        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException var4) {
            var4.printStackTrace();
        }

        String version = packInfo.versionCode + "";
        return version;
    }

    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;

        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException var4) {
            var4.printStackTrace();
        }

        String version = packInfo.versionName + "";
        return version;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager)context.getSystemService("window");
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getStatusHeight(Context context) {
        int statusHeight = -1;

        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return statusHeight;
    }

    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
    }

    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }
}
