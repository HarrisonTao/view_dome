package com.dykj.module.util.update;

import com.cretin.www.cretinautoupdatelibrary.model.DownloadInfo;
import com.cretin.www.cretinautoupdatelibrary.model.TypeConfig;
import com.cretin.www.cretinautoupdatelibrary.model.UpdateConfig;
import com.cretin.www.cretinautoupdatelibrary.utils.AppUpdateUtils;
import com.dykj.module.base.BaseApplication;


/**
 * Created by lcjingon 2019/10/22.
 * description:
 *权衡代码灵活性、这里没有进行自动网络请求
 * 自定义弹窗请参照下方连接修改
 * https://github.com/MZCretin/AutoUpdateProject
 *
 * @author lcj
 */

public class UpdateUtils {

    //绿色主题弹框
    public static final int THEME_GREEN_1=TypeConfig.UI_THEME_A;
    public static final int THEME_GREEN_2=TypeConfig.UI_THEME_J;
    //橙色主题弹框
    public static final int THEME_ORANGE_1=TypeConfig.UI_THEME_B;
    public static final int THEME_ORANGE_2=TypeConfig.UI_THEME_C;
    public static final int THEME_ORANGE_3=TypeConfig.UI_THEME_E;
    public static final int THEME_ORANGE_4=TypeConfig.UI_THEME_L;
    //蓝色主题弹框
    public static final int THEME_BLUE_1=TypeConfig.UI_THEME_F;
    public static final int THEME_BLUE_2=TypeConfig.UI_THEME_G;
    public static final int THEME_BLUE_3=TypeConfig.UI_THEME_I;
    //红色主题弹框
    public static final int THEME_RED_1=TypeConfig.UI_THEME_H;
    public static final int THEME_RED_2=TypeConfig.UI_THEME_K;
    //深色主题弹框
    public static final int THEME_DARK=TypeConfig.UI_THEME_D;


    /**
     * 检查更新 初始化
     * @param logoRes 应用logo（知栏显示的图标）
     */
    public static void init(int logoRes) {
        //更新库配置
        UpdateConfig updateConfig = new UpdateConfig()
                .setDebug(true)
                //设置获取更新信息的方式      DATA_SOURCE_TYPE_MODEL需要自己写网络请求
                .setDataSourceType(TypeConfig.DATA_SOURCE_TYPE_MODEL)
                //配置更新的过程中是否在通知栏显示进度
                .setShowNotification(true)
                //配置通知栏显示的图标
                .setNotificationIconRes(logoRes)
                //配置UI的样式，一种有12种样式可供选择
                .setUiThemeType(TypeConfig.UI_THEME_I);
        AppUpdateUtils.init(BaseApplication.getInstance(), updateConfig);
    }



    /**
     *    没有兼容到AndroidX！没有兼容到AndroidX！没有兼容到AndroidX！没有兼容到AndroidX！
     * 检查更新 初始化  自定义弹窗
     * 弹窗样式在com.dykj.module.util.update.CustomActivity 的布局文件中修改
     * @param logoRes 应用logo（知栏显示的图标）
     *
     */
    public static void initCustom(int logoRes) {
        //更新库配置
        UpdateConfig updateConfig = new UpdateConfig()
                .setDebug(true)
                //设置获取更新信息的方式      DATA_SOURCE_TYPE_MODEL需要自己写网络请求
                .setDataSourceType(TypeConfig.DATA_SOURCE_TYPE_MODEL)
                //配置更新的过程中是否在通知栏显示进度
                .setShowNotification(true)
                //配置通知栏显示的图标
                .setNotificationIconRes(logoRes)
                //自定义弹窗页面
                .setCustomActivityClass(CustomActivity.class)
                //配置UI的样式，一种有12种样式可供选择
                .setUiThemeType(TypeConfig.UI_THEME_G);
        AppUpdateUtils.init(BaseApplication.getInstance(), updateConfig);
    }

    /**
     * 显示下载更新弹窗
     * @param downloadUrl  apk下载地址
     * @param versionName 版本名称
     * @param isForce 是否强制下载
     * @param msg 更新内容
     */
    public static void showDownloadDialog(String downloadUrl, String versionName, boolean isForce, String msg) {
        DownloadInfo info = new DownloadInfo().setApkUrl(downloadUrl)
                .setProdVersionName(versionName)
                .setForceUpdateFlag(isForce ? 1 : 0)
                .setUpdateLog(msg);
        AppUpdateUtils.getInstance().checkUpdate(info);
    }
}
