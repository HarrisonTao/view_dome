package com.dykj.module.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;

import com.blankj.utilcode.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tangji
 * @date 2017/12/29 15:12
 */

public class DateUtils {

    public static String getCurrentDate() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = sf.format(curDate);
        return str;
    }


    /*时间戳转换成字符窜*/
    public static String getTime(String timeSrc) {
        if (StringUtils.isEmpty(timeSrc)||"0".equals(timeSrc)) {
            return "0";
        }
        long time = Long.parseLong(timeSrc) * 1000;
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(d);
    }
    /*时间戳转换成字符窜*/
    public static String getDateToString(long timeSrc) {
        long time = timeSrc * 1000;
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(d);
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString2(long timeSrc) {
        long time = timeSrc;
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(d);
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString3(long timeSrc) {
        long time = timeSrc;
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
        return sf.format(d);
    }

    /*时间戳转换成完整时间*/
    public static String getCompleteTime(long timeSrc) {
        long time = timeSrc * 1000;
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(d);
    }

    /*时间戳转换成完整时间*/
    public static String getCompleteTime2(long timeSrc) {
        long time = timeSrc * 1000;
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sf.format(d);
    }

    /*时间戳转换成完整时间*/
    public static String getCompleteTime3(long timeSrc) {
        long time = timeSrc * 1000;
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("MM-dd HH:mm");
        return sf.format(d);
    }

    /*将字符串转为时间戳*/
    public static long getStringToDate(String time) {
        if (TextUtils.isEmpty(time)) {
            return 0L;
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /*将字符串转为时间戳*/
    public static long getStringToDateTwo(String time) {
        if (TextUtils.isEmpty(time)) {
            return 0L;
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /*将字符串转为时间戳*/
    public static long getStringToDate2(String time) {
        if (TextUtils.isEmpty(time)) {
            return 0L;
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
    /*将字符串转为时间戳*/
    public static long getStringToStamp(String time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /*将字符串转为时间戳*/
    public static long getStringToStamp2(String time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 时间戳格式转年月日时分秒
     */
    public static String TimeStamp2Date(Long ltime) {
        Long timetamp = ltime * 1000;
        Date d = new Date(timetamp);
        SimpleDateFormat sf = new SimpleDateFormat("dd天HH时mm分ss秒");
        String date = sf.format(d);
        return date;
    }

    /*
     * 时间差值解析
     * @param str
     * @return
     */
    public static String TimeStamp2Remain(Long ltime) {
        StringBuffer remain_time = new StringBuffer();

        int days = (int) (ltime / 86400);
        if (days > 0) {
            remain_time.append(days + "天");
        }

        long remain1 = ltime - days * 86400;
        int hours = (int) (remain1 / 3600);
        if (hours > 0) {
            remain_time.append(hours + "时");
        }

        long remain2 = remain1 - hours * 3600;
        int mumites = (int) (remain2 / 60);
        if (mumites > 0) {
            remain_time.append(mumites + "分");
        }

        int seconds = (int) (remain2 - mumites * 60);
        if (seconds > 0) {
            remain_time.append(seconds + "秒");
        }

        return remain_time.toString();
    }

    /*
     * 时间差值解析
     * @param str
     * @return
     */
    public static String TimeStamp2HMS(Long ltime) {
        StringBuffer remain_time = new StringBuffer();

        int days = (int) (ltime / 86400);
        if (days > 0) {
            remain_time.append(days + ":");
        }

        long remain1 = ltime - days * 86400;
        int hours = (int) (remain1 / 3600);
        if (hours < 10) {
            remain_time.append("0" + hours + ":");
        } else {
            remain_time.append(hours + ":");
        }

        long remain2 = remain1 - hours * 3600;
        int mumites = (int) (remain2 / 60);
        if (mumites < 10) {
            remain_time.append("0" + mumites + ":");
        } else {
            remain_time.append(mumites + ":");
        }

        int seconds = (int) (remain2 - mumites * 60);
        if (seconds < 10) {
            remain_time.append("0" + seconds);
        } else {
            remain_time.append(seconds);
        }

        return remain_time.toString();
    }

    /*
     * 时间差值解析
     * @param str
     * @return
     */
    public static String TimeStamp2DHMS(Long ltime) {
        StringBuffer remain_time = new StringBuffer();

        int days = (int) (ltime / 86400);
        if (days < 10) {
            remain_time.append("0" + days + ":");
        } else {
            remain_time.append(days + ":");
        }

        long remain1 = ltime - days * 86400;
        int hours = (int) (remain1 / 3600);
        if (hours < 10) {
            remain_time.append("0" + hours + ":");
        } else {
            remain_time.append(hours + ":");
        }

        long remain2 = remain1 - hours * 3600;
        int mumites = (int) (remain2 / 60);
        if (mumites < 10) {
            remain_time.append("0" + mumites + ":");
        } else {
            remain_time.append(mumites + ":");
        }

        int seconds = (int) (remain2 - mumites * 60);
        if (seconds < 10) {
            remain_time.append("0" + seconds);
        } else {
            remain_time.append(seconds);
        }

        return remain_time.toString();
    }
    /*
     * 时间差值解析
     * @param str
     * @return
     */
    public static String TimeStamp2HMS2(Long ltime) {
        StringBuffer remain_time = new StringBuffer();

        int days = (int) (ltime / 86400);
        if (days > 0) {
            remain_time.append(days + "天");
        }

        long remain1 = ltime - days * 86400;
        int hours = (int) (remain1 / 3600);
        if (hours < 10) {
            remain_time.append("0" + hours + "时");
        } else {
            remain_time.append(hours + "时");
        }

        long remain2 = remain1 - hours * 3600;
        int mumites = (int) (remain2 / 60);
        if (mumites < 10) {
            remain_time.append("0" + mumites + "分");
        } else {
            remain_time.append(mumites + "分");
        }

        int seconds = (int) (remain2 - mumites * 60);
        if (seconds < 10) {
            remain_time.append("0" + seconds+ "秒");
        } else {
            remain_time.append(seconds+ "秒");
        }

        return remain_time.toString();
    }
    /**
     * 时间戳格式转年月日时分秒
     */
    public static String TimeStamp2DateLicai(Long ltime) {
        Long timetamp = ltime * 1000;
        String date = new SimpleDateFormat("dd HH:mm:ss").format(new Date(timetamp));
        return date;
    }

    /**
     * 时间戳格式转年月日时分秒
     */
    public static String TimeStamp2DateCharge(Long ltime) {
        Long timetamp = ltime * 1000;
        String date = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date(timetamp));
        return date;
    }

    /**
     * 时间戳格式转年月日
     */
    public static String TimeStamp2Time(Long ltime) {
        Long timetamp = ltime * 1000;
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(timetamp));
        return date;
    }

    /**
     * 时间戳格式转年月日
     */
    public static String TimeStamp2Time2(Long ltime) {
        Long timetamp = ltime * 1000;
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(timetamp));
        return date;
    }

    /**
     * 时间戳格式转年月日
     */
    public static String TimeStamp2Time3(Long ltime) {
        String date = new SimpleDateFormat("yyyy年MM月").format(new Date(ltime));
        return date;
    }

    /**
     * 时间戳格式转年月日时分秒
     */
    public SpannableString TimeStamp2DateSpannable(String str) {
        Long timetemp = Long.parseLong(str) * 1000;
        String date = new SimpleDateFormat("HH时mm分ss秒").format(new Date(timetemp));
        //设置字体大小
        SpannableString dateText = new SpannableString(date);
        dateText.setSpan(new AbsoluteSizeSpan(30, true), 0, date.indexOf("."), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return dateText;
    }

    /**
     * 时间戳格式转年月日
     */
    public static String MessageTime2Time(Long ltime) {
        Long timetamp = ltime * 1000;
        String date = new SimpleDateFormat("MM-dd hh:mm").format(new Date(timetamp));
        return date;
    }
}
