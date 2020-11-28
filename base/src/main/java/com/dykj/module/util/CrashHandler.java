package com.dykj.module.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.pm.PackageManager.GET_ACTIVITIES;

/**
 * @date 2019/4/20.
 * description：异常日志打印到手机
 */
public class CrashHandler implements UncaughtExceptionHandler {
    private static final String TAG = CrashHandler.class.getSimpleName();
    private static CrashHandler INSTANCE = new CrashHandler();
    File _crashLogDirFile = null;
    private UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;
    private Map<String, String> infos = new HashMap();
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private String errorMsg;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        this.mContext = context;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            Thread.sleep(1500L);
        } catch (InterruptedException var4) {
            Log.e(TAG, "error : ", var4);
        }

        this.handleException(ex);
        Intent i = this.mContext.getPackageManager().getLaunchIntentForPackage(this.mContext.getPackageName());
        i.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("data", "1");
        this.mContext.startActivity(i);
        Process.killProcess(Process.myPid());
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        } else {
            this.collectDeviceInfo();
            this.saveCrashInfo2File(ex);
            ex.printStackTrace();
            return true;
        }
    }

    public void collectDeviceInfo() {
        try {
            PackageManager pm = this.mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(this.mContext.getPackageName(), GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                this.infos.put("versionName", versionName);
                this.infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException var8) {
            Log.e(TAG, "an error occured when collect package info", var8);
        }

        Field[] fields = Build.class.getDeclaredFields();
        Field[] var10 = fields;
        int var11 = fields.length;

        for (int var12 = 0; var12 < var11; ++var12) {
            Field field = var10[var12];

            try {
                field.setAccessible(true);
                this.infos.put(field.getName(), field.get((Object) null).toString());
                Log.d(TAG, field.getName() + " : " + field.get((Object) null));
            } catch (Exception var7) {
                Log.e(TAG, "an error occured when collect crash info", var7);
            }
        }

    }

    private File getCrashLogFolder() {
        PackageManager manager = this.mContext.getPackageManager();
        ApplicationInfo info = null;

        try {
            info = manager.getApplicationInfo(this.mContext.getPackageName(), 0);
        } catch (NameNotFoundException var5) {
            var5.printStackTrace();
        }

        String appName = (String) manager.getApplicationLabel(info);
        if (this._crashLogDirFile == null) {
            File SDCardRoot = this.mContext.getExternalFilesDir(appName + "异常日志/");
            this._crashLogDirFile = SDCardRoot;
            this._crashLogDirFile.mkdirs();
        }

        return this._crashLogDirFile;
    }

    private String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        Iterator var3 = this.infos.entrySet().iterator();

        String result;
        while (var3.hasNext()) {
            Entry<String, String> entry = (Entry) var3.next();
            String key = (String) entry.getKey();
            result = (String) entry.getValue();
            sb.append(key + "=" + result + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);

        for (Throwable cause = ex.getCause(); cause != null; cause = cause.getCause()) {
            cause.printStackTrace(printWriter);
        }

        printWriter.close();
        result = writer.toString();
        sb.append(result);

        try {
            long timestamp = System.currentTimeMillis();
            String time = this.formatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".txt";
            if (Environment.getExternalStorageState().equals("mounted")) {
                Log.d(TAG, String.format("crash log dir is %s", this.getCrashLogFolder().getAbsolutePath()));
                File file = new File(this.getCrashLogFolder(), fileName);
                Log.d(TAG, String.format("carsh log file is %s", file.getAbsolutePath()));
                FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
                fos.write(sb.toString().getBytes());
                fos.close();
            }

            this.errorMsg = sb.toString();
            return fileName;
        } catch (Exception var13) {
            Log.e(TAG, "an error occured while writing file...", var13);
            return null;
        }
    }
}
