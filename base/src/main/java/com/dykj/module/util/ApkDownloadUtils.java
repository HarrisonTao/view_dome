package com.dykj.module.util;


import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.pkg.helper.DialogHelper;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.service.DownloadService;

import java.io.File;
import java.util.List;

/**
 * Created by lcjing on 2019/10/14.
 * 检查更新 下载并安装
 */
public class ApkDownloadUtils {

    public static String TAG = "ApkDownloadUtils";
    private String downloadUrl;
    private Context context;

    private ApkDownloadUtils() {
    }

    public static ApkDownloadUtils getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void downLoad(final Context context, String downloadUrl) {
        this.context = context;
        this.downloadUrl = downloadUrl;
        downLoad(context, downloadUrl, false);
    }

    public void downLoad(final Context context, String downloadUrl, boolean isGranted) {
        if (!isGranted) {
            if (!checkPermission()) {
                return;
            }
        }

        String path = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {
            try {
                path = context.getExternalCacheDir().getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(path)) {
                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            }
        } else {
            path = context.getCacheDir().getAbsolutePath();
        }
        path = path + "/downloadApk";
        File dir = new File(path);
        FileUtils.createOrExistsDir(dir);
        path = path + "/" + AppUtils.getAppName() + ".apk";


        UpdateAppBean updateAppBean = new UpdateAppBean();

        //设置 apk 的下载地址
        updateAppBean.setApkFileUrl(downloadUrl);
        updateAppBean.setTargetPath(path);
        //实现网络接口，只实现下载就可以
        updateAppBean.setHttpManager(new UpdateAppHttpUtil());

        UpdateAppManager.download(context, updateAppBean, new DownloadService.DownloadCallback() {
            @Override
            public void onStart() {
                HProgressDialogUtils.showHorizontalProgressDialog(context, "下载进度", false);
                Log.d(TAG, "onStart() called");
            }

            @Override
            public void onProgress(float progress, long totalSize) {
                HProgressDialogUtils.setProgress(Math.round(progress * 100));
                Log.d(TAG, "onProgress() called with: progress = [" + progress + "], totalSize = [" + totalSize + "]");

            }

            @Override
            public void setMax(long totalSize) {
                Log.d(TAG, "setMax() called with: totalSize = [" + totalSize + "]");
            }

            @Override
            public boolean onFinish(File file) {
                HProgressDialogUtils.cancel();
                AppUtils.installApp(file);
                Log.d(TAG, "onFinish() called with: file = [" + file.getAbsolutePath() + "]");
                return true;
            }

            @Override
            public void onError(String msg) {
                HProgressDialogUtils.cancel();
                Log.e(TAG, "onError() called with: msg = [" + msg + "]");
            }

            @Override
            public boolean onInstallAppAndAppOnForeground(File file) {
                Log.d(TAG, "onInstallAppAndAppOnForeground() called with: file = [" + file + "]");
                return true;
            }
        });
    }

    private boolean checkPermission() {
        boolean isGranted;
        isGranted = PermissionUtils.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // 设置全屏
//        if (!isGranted) {
        PermissionUtils.permission(PermissionConstants.STORAGE)
                .rationale(shouldRequest -> {
                    DialogHelper dialogHelper = new DialogHelper();
                    dialogHelper.showRationaleDialog(shouldRequest);
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
//                           int a= permissionsGranted.size();
                        downLoad(context, downloadUrl, true);
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            DialogHelper dialogHelper = new DialogHelper();
                            dialogHelper.showOpenAppSettingDialog();
                        }
                        LogUtils.d(permissionsDeniedForever, permissionsDenied);
                    }
                })
                .theme(ScreenUtils::setFullScreen)
                .request();
//        }

        return isGranted;
    }

    private static class LazyHolder {
        private static final ApkDownloadUtils INSTANCE = new ApkDownloadUtils();
    }


}
