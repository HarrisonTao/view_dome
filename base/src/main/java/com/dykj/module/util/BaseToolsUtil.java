package com.dykj.module.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.dykj.module.util.toast.DyToast;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

import androidx.fragment.app.FragmentActivity;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Intent.ACTION_DEFAULT;
import static android.content.Intent.CATEGORY_DEFAULT;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * @author WZ
 * @date 2019/5/16.
 * 严格要求自己，对每一行代码负责
 * description：
 */
public class BaseToolsUtil {
    private static final String[] zodiacArr = new String[]{"猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"};
    private static final String[] constellationArr = new String[]{"水瓶座", "双鱼座", "牡羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};
    private static final int[] constellationEdgeDay = new int[]{20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};

    public BaseToolsUtil() {
    }

    public static String getTime() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH);
        int day = ca.get(Calendar.DAY_OF_MONTH);
        int minute = ca.get(Calendar.MINUTE);
        int hour = ca.get(Calendar.HOUR);
        int second = ca.get(Calendar.SECOND);
        return String.format("%d%d%d%d%d%d", year, month, day, minute, hour, second);
    }

    public static boolean isCellphone(String phone) {
        String telRegex = "^((13[0-9])|(14[4,5,6,7,8,9])|(15[0-9])|(16[2,5,6,7])|(18[0-9])|(17[^9])|(19[1,8,9]))\\d{8}$";
        if (TextUtils.isEmpty(phone))
            return false;
        else
            return phone.matches(telRegex);
    }

    private String phoneFormat(String mobile) {
        if (BaseStringUtil.isMobile(mobile) && mobile.length() == 11) {
            mobile = mobile.substring(0, 3) + "****" + mobile.substring(7, 11);
        }

        return mobile;
    }

    public static String MD5(String content) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(content.getBytes("UTF-8"));
        } catch (Exception var5) {
            return null;
        }

        byte[] byteArray = messageDigest.digest();
        StringBuilder md5StrBuff = new StringBuilder();

        for(int i = 0; i < byteArray.length; ++i) {
            if (Integer.toHexString(255 & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(255 & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(255 & byteArray[i]));
            }
        }

        return md5StrBuff.toString().toLowerCase();
    }

    public static int createAge(String birthday) {
        if (birthday == null) {
            return 0;
        } else {
            long d = Long.parseLong(birthday);
            Date data = new Date(d);
            Calendar c = Calendar.getInstance();
            c.setTime(data);
            int dy = c.get(Calendar.YEAR);
            int dm = c.get(Calendar.MONTH) + 1;
            Calendar now = Calendar.getInstance();
            int nowY = now.get(Calendar.YEAR);
            int nowM = now.get(Calendar.MONTH) + 1;
            float m = (float)(nowY - dy) + (float)(nowM - dm) / 12.0F;
            return Integer.parseInt(String.valueOf((new BigDecimal((double)m)).setScale(0, 4)));
        }
    }

    public static int getAgeByBirthday(String strBirthday) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthday = null;

        try {
            birthday = sdf.parse(strBirthday);
        } catch (ParseException var11) {
            return 0;
        }

        Calendar cal = Calendar.getInstance();
        if (cal.before(birthday)) {
            throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
        } else {
            int yearNow = cal.get(Calendar.YEAR);
            int monthNow = cal.get(Calendar.MONTH) + 1;
            int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
            cal.setTime(birthday);
            int yearBirth = cal.get(Calendar.YEAR);
            int monthBirth = cal.get(Calendar.MONTH) + 1;
            int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
            int age = yearNow - yearBirth;
            if (monthNow <= monthBirth) {
                if (monthNow == monthBirth) {
                    if (dayOfMonthNow < dayOfMonthBirth) {
                        --age;
                    }
                } else {
                    --age;
                }
            }

            return age;
        }
    }

    public static String getZodica(String birthday) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;

        try {
            d = sdf.parse(birthday);
        } catch (ParseException var4) {
            return "";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        return zodiacArr[calendar.get(Calendar.YEAR) % 12];
    }

    public static String getConstellation(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;

        try {
            d = sdf.parse(date);
        } catch (ParseException var6) {
            return "";
        }

        Calendar time = Calendar.getInstance();
        time.setTime(d);
        int month = time.get(Calendar.MONTH);
        int day = time.get(Calendar.DAY_OF_MONTH);
        if (day < constellationEdgeDay[month]) {
            --month;
        }

        return month >= 0 ? constellationArr[month] : constellationArr[11];
    }

    public static void hideKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, 2);
    }

    public static void keepDialog(DialogInterface dialog) {
        try {
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, false);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static void distoryDialog(DialogInterface dialog) {
        try {
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, true);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static void openKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, 2);
        imm.toggleSoftInput(2, 1);
    }

    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    public static void showSoftInput(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, 2);
    }

    public static void closeSoftInput(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && ((Activity)context).getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(((Activity)context).getCurrentFocus().getWindowToken(), 2);
        }

    }

    public static void call(Activity activity, String number) {
        Intent intent = new Intent("android.intent.action.DIAL", Uri.parse("tel:" + number));
        activity.startActivity(intent);
    }

    public static void sendMessage(Activity activity, String number) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent it = new Intent("android.intent.action.SENDTO", uri);
        it.putExtra("sms_body", "");
        activity.startActivity(it);
    }

    public static String getLocalIpAddress() {
        try {
            Enumeration en = NetworkInterface.getNetworkInterfaces();

            while(en.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface)en.nextElement();
                Enumeration enumIpAddress = networkInterface.getInetAddresses();

                while(enumIpAddress.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress)enumIpAddress.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException var4) {
            ;
        }

        return null;
    }

    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();

        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (NameNotFoundException var3) {
            return "";
        }
    }

    public static String getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();

        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return String.valueOf(packInfo.versionCode);
        } catch (NameNotFoundException var3) {
            return "";
        }
    }

    public static String getCacheSize(Context context) {
        long cacheSize = 0L;

        try {
            cacheSize = getFolderSize(context.getCacheDir());
            if (Environment.getExternalStorageState().equals("mounted")) {
                cacheSize += getFolderSize(context.getExternalCacheDir());
            }
        } catch (Exception var4) {
            ;
        }

        return getFormatSize((double)cacheSize);
    }

    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals("mounted")) {
            deleteDir(context.getExternalCacheDir());
        }

    }

    public static void clearAllCache(Context context, String tips) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals("mounted")) {
            deleteDir(context.getExternalCacheDir());
        }

        DyToast.getInstance().info(tips);
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();

            for(int i = 0; i < children.length; ++i) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        assert dir != null;

        return dir.delete();
    }

    private static long getFolderSize(File file) throws Exception {
        long size = 0L;

        try {
            File[] fileList = file.listFiles();

            for(int i = 0; i < fileList.length; ++i) {
                if (fileList[i].isDirectory()) {
                    size += getFolderSize(fileList[i]);
                } else {
                    size += fileList[i].length();
                }
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return size;
    }

    public static String getFormatSize(double size) {
        double kiloByte = size / 1024.0D;
        if (kiloByte < 1.0D) {
            return "0K";
        } else {
            double megaByte = kiloByte / 1024.0D;
            if (megaByte < 1.0D) {
                BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
                return result1.setScale(2, 4).toPlainString() + "KB";
            } else {
                double gigaByte = megaByte / 1024.0D;
                if (gigaByte < 1.0D) {
                    BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
                    return result2.setScale(2, 4).toPlainString() + "MB";
                } else {
                    double teraBytes = gigaByte / 1024.0D;
                    BigDecimal result4;
                    if (teraBytes < 1.0D) {
                        result4 = new BigDecimal(Double.toString(gigaByte));
                        return result4.setScale(2, 4).toPlainString() + "GB";
                    } else {
                        result4 = new BigDecimal(teraBytes);
                        return result4.setScale(2, 4).toPlainString() + "TB";
                    }
                }
            }
        }
    }

    public static String getCurrentTime() {
        String str = "";
        Calendar c = Calendar.getInstance();
        str = str + c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH)
                + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        return str;
    }

    public static String HourAddMinus(int h1, int m1, int h2, int m2) {
        if (h2 > 0) {
            h1 += h2;
        }

        if (m2 > 0) {
            m1 += m2;
        }

        if (m1 >= 60) {
            h1 += m1 / 60;
            m1 %= 60;
        }

        if (h1 >= 24) {
            h1 = 0;
        }

        return (h1 < 10 ? "0" + h1 : h1) + ":" + (m1 < 10 ? "0" + m1 : m1);
    }

    public static String numberFormat(double number, String code) {
        DecimalFormat df = new DecimalFormat(code);
        return df.format(number);
    }

    public static boolean checkNull(Context context, String string, int msgId) {
        if (TextUtils.isEmpty(string)) {
            Toast.makeText(context, context.getString(msgId), Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkNull(Context context, String string, String message) {
        if (TextUtils.isEmpty(string)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkPhone(Context context, String phone, int msgId) {
        if (!BaseStringUtil.isMobile(phone)) {
            Toast.makeText(context, context.getString(msgId), Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkPhone(Context context, String phone, String message) {
        if (!BaseStringUtil.isMobile(phone)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    public static Intent getOpenFileIntent(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        } else {
            String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
            if (!end.equals("m4a") && !end.equals("mp3") && !end.equals("mid") && !end.equals("xmf") && !end.equals("ogg") && !end.equals("wav")) {
                if (!end.equals("3gp") && !end.equals("mp4")) {
                    if (!end.equals("jpg") && !end.equals("gif") && !end.equals("png") && !end.equals("jpeg") && !end.equals("bmp")) {
                        if (end.equals("apk")) {
                            return getApkFileIntent(filePath);
                        } else if (end.equals("ppt")) {
                            return getPptFileIntent(filePath);
                        } else if (end.equals("xls")) {
                            return getExcelFileIntent(filePath);
                        } else if (end.equals("doc")) {
                            return getWordFileIntent(filePath);
                        } else if (end.equals("pdf")) {
                            return getPdfFileIntent(filePath);
                        } else if (end.equals("chm")) {
                            return getChmFileIntent(filePath);
                        } else {
                            return end.equals("txt") ? getTextFileIntent(filePath, false) : getAllIntent(filePath);
                        }
                    } else {
                        return getImageFileIntent(filePath);
                    }
                } else {
                    return getAudioFileIntent(filePath);
                }
            } else {
                return getAudioFileIntent(filePath);
            }
        }
    }

    public static Intent getAllIntent(String filePath) {
        Intent intent = new Intent();
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(ACTION_DEFAULT);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    public static Intent getApkFileIntent(String filePath) {
        Intent intent = new Intent();
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(ACTION_DEFAULT);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    public static Intent getVideoFileIntent(String filePath) {
        Intent intent = new Intent(ACTION_DEFAULT);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    public static Intent getAudioFileIntent(String filePath) {
        Intent intent = new Intent(ACTION_DEFAULT);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    public static Intent getHtmlFileIntent(String filePath) {
        Uri uri = Uri.parse(filePath).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(filePath).build();
        Intent intent = new Intent(ACTION_DEFAULT);
        intent.setDataAndType(uri, "rank_label/html");
        return intent;
    }

    public static Intent getImageFileIntent(String filePath) {
        Intent intent = new Intent(ACTION_DEFAULT);
        intent.addCategory(CATEGORY_DEFAULT);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    public static Intent getPptFileIntent(String filePath) {
        Intent intent = new Intent(ACTION_DEFAULT);
        intent.addCategory(CATEGORY_DEFAULT);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    public static Intent getExcelFileIntent(String filePaht) {
        Intent intent = new Intent(ACTION_DEFAULT);
        intent.addCategory(CATEGORY_DEFAULT);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePaht));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    public static Intent getWordFileIntent(String filePath) {
        Intent intent = new Intent(ACTION_DEFAULT);
        intent.addCategory(CATEGORY_DEFAULT);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    public static Intent getChmFileIntent(String filePath) {
        Intent intent = new Intent(ACTION_DEFAULT);
        intent.addCategory(CATEGORY_DEFAULT);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    public static Intent getTextFileIntent(String filePath, boolean paramBoolean) {
        Intent intent = new Intent(ACTION_DEFAULT);
        intent.addCategory(CATEGORY_DEFAULT);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        Uri uri1;
        if (paramBoolean) {
            uri1 = Uri.parse(filePath);
            intent.setDataAndType(uri1, "rank_label/plain");
        } else {
            uri1 = Uri.fromFile(new File(filePath));
            intent.setDataAndType(uri1, "rank_label/plain");
        }

        return intent;
    }

    public static Intent getPdfFileIntent(String filePath) {
        Intent intent = new Intent(ACTION_DEFAULT);
        intent.addCategory(CATEGORY_DEFAULT);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    public static String getSuffix(String path) {
        return path.substring(path.lastIndexOf(".") + 1, path.length()).toLowerCase();
    }


    public static void askPermissions(final FragmentActivity context, final PermissionInterface permissionInterface, final String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(context);
        Disposable disposable = rxPermissions.request(permissions).subscribe(aBoolean -> {
            if (aBoolean) {
                permissionInterface.ok();
            } else {
                permissionInterface.error();
            }
        });
        disposable.dispose();
    }

    public interface PermissionInterface {
        void ok();

        void error();
    }


    //从html中提取纯文本
    public static String StripHT(String strHtml) {

        String txtcontent = strHtml.replaceAll("</?[^>]+>", ""); //剔出<html>的标签

        txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>", "");//去除字符串中的空格,回车,换行符,制表符

        return txtcontent;

    }

    /**
     * 截取手机号为123****4234的形式
     */
    public static String hintPhone(String phone) {
        if (phone.length() == 11) {
            String str1 = phone.substring(0, 3);
            String str2 = phone.substring(7, 11);
            return str1 + "****" + str2;
        } else {
            return phone;
        }
    }


    /***
     * 隐藏软键盘
     */
    public static void hiddenKeyboard(Activity act) {
        View view = act.getCurrentFocus();
        if (view == null) view = new View(act);
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
