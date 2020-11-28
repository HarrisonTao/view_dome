package com.dykj.module.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author WZ
 * @date 2019/5/16.
 * 严格要求自己，对每一行代码负责
 * description：
 */
public class BaseDateUtil {
    public static final String dateFormatYMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String dateFormatYMD = "yyyy-MM-dd";
    public static final String dateFormatYM = "yyyy-MM";
    public static final String dateFormatYMDHM = "yyyy-MM-dd HH:mm";
    public static final String dateFormatMD = "MM/dd";
    public static final String dateFormatHMS = "HH:mm:ss";
    public static final String dateFormatHM = "HH:mm";
    public static final String AM = "AM";
    public static final String PM = "PM";
    public static final int TIME_SECOND = 1000;
    public static final int TIME_MINUTE = 60000;
    public static final int TIME_HOUR = 3600000;
    public static final int TIME_DAY = 86400000;

    public BaseDateUtil() {
    }

    public static Date getDateByFormat(String strDate, String format) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = mSimpleDateFormat.parse(strDate);
        } catch (ParseException var5) {
            var5.printStackTrace();
        }
        return date;
    }

    public static String getStringByOffset(String strDate, String format, int calendarField, int offset) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.setTime(mSimpleDateFormat.parse(strDate));
            c.add(calendarField, offset);
            mDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (ParseException var7) {
            var7.printStackTrace();
        }
        return mDateTime;
    }

    public static String getStringByOffset(Date date, String format, int calendarField, int offset) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.setTime(date);
            c.add(calendarField, offset);
            strDate = mSimpleDateFormat.format(c.getTime());
        } catch (Exception var7) {
            var7.printStackTrace();
        }
        return strDate;
    }

    public static String getStringByFormat(Date date, String format) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        String strDate = null;
        try {
            strDate = mSimpleDateFormat.format(date);
        } catch (Exception var5) {
            var5.printStackTrace();
        }
        return strDate;
    }

    public static String getStringByFormat(String strDate, String format) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            c.setTime(mSimpleDateFormat.parse(strDate));
            SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(format);
            mDateTime = mSimpleDateFormat2.format(c.getTime());
        } catch (Exception var6) {
            var6.printStackTrace();
        }
        return mDateTime;
    }

    public static String getStringByFormat(long milliseconds, String format) {
        String thisDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            thisDateTime = mSimpleDateFormat.format(milliseconds * 1000L);
        } catch (Exception var5) {
            var5.printStackTrace();
        }
        return thisDateTime;
    }

    public static String getCurrentDate(String format) {
        MyLogger.dLog().d("getCurrentDate:" + format);
        String curDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            Calendar c = new GregorianCalendar();
            curDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (Exception var4) {
            var4.printStackTrace();
        }
        return curDateTime;
    }

    public static String getCurrentDateByOffset(String format, int calendarField, int offset) {
        String mDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            Calendar c = new GregorianCalendar();
            c.add(calendarField, offset);
            mDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (Exception var6) {
            var6.printStackTrace();
        }
        return mDateTime;
    }

    public static String getFirstDayOfWeek(String format) {
        return getDayOfWeek(format, 2);
    }

//    public static int getOffectDay(long milliseconds1, long milliseconds2) {
//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.setTimeInMillis(milliseconds1);
//        Calendar calendar2 = Calendar.getInstance();
//        calendar2.setTimeInMillis(milliseconds2);
//        int y1 = calendar1.get(1);
//        int y2 = calendar2.get(1);
//        int d1 = calendar1.get(6);
//        int d2 = calendar2.get(6);
//        int maxDays = false;
//        int day = false;
//        int maxDays;
//        int day;
//        if (y1 - y2 > 0) {
//            maxDays = calendar2.getActualMaximum(6);
//            day = d1 - d2 + maxDays;
//        } else if (y1 - y2 < 0) {
//            maxDays = calendar1.getActualMaximum(6);
//            day = d1 - d2 - maxDays;
//        } else {
//            day = d1 - d2;
//        }
//
//        return day;
//    }

//    public static int getOffectHour(long date1, long date2) {
//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.setTimeInMillis(date1);
//        Calendar calendar2 = Calendar.getInstance();
//        calendar2.setTimeInMillis(date2);
//        int h1 = calendar1.get(11);
//        int h2 = calendar2.get(11);
//        int h = false;
//        int day = getOffectDay(date1, date2);
//        int h = h1 - h2 + day * 24;
//        return h;
//    }
//
//    public static int getOffectMinutes(long date1, long date2) {
//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.setTimeInMillis(date1);
//        Calendar calendar2 = Calendar.getInstance();
//        calendar2.setTimeInMillis(date2);
//        int m1 = calendar1.get(12);
//        int m2 = calendar2.get(12);
//        int h = getOffectHour(date1, date2);
//        int m = false;
//        int m = m1 - m2 + h * 60;
//        return m;
//    }

    public static String getLastDayOfWeek(String format) {
        return getDayOfWeek(format, 1);
    }

    private static String getDayOfWeek(String format, int calendarField) {
        String strDate = null;

        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            int week = c.get(7);
            if (week == calendarField) {
                strDate = mSimpleDateFormat.format(c.getTime());
            } else {
                int offectDay = calendarField - week;
                if (calendarField == 1) {
                    offectDay = 7 - Math.abs(offectDay);
                }

                c.add(5, offectDay);
                strDate = mSimpleDateFormat.format(c.getTime());
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return strDate;
    }

    public static String getFirstDayOfMonth(String format) {
        String strDate = null;

        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.set(5, 1);
            strDate = mSimpleDateFormat.format(c.getTime());
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return strDate;
    }

    public static String getLastDayOfMonth(String format) {
        String strDate = null;

        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.set(5, 1);
            c.roll(5, -1);
            strDate = mSimpleDateFormat.format(c.getTime());
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return strDate;
    }

    public static long getFirstTimeOfDay() {
        Date date = null;

        try {
            String currentDate = getCurrentDate("yyyy-MM-dd");
            date = getDateByFormat(currentDate + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
            return date.getTime();
        } catch (Exception var2) {
            return -1L;
        }
    }

    public static long getLastTimeOfDay() {
        Date date = null;

        try {
            String currentDate = getCurrentDate("yyyy-MM-dd");
            date = getDateByFormat(currentDate + " 24:00:00", "yyyy-MM-dd HH:mm:ss");
            return date.getTime();
        } catch (Exception var2) {
            return -1L;
        }
    }

    public static boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 400 != 0 || year % 400 == 0;
    }

    public static String getWeekNumber(String strDate, String inFormat) {
        String week = "星期日";
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat df = new SimpleDateFormat(inFormat);

        try {
            calendar.setTime(df.parse(strDate));
        } catch (Exception var6) {
            return "错误";
        }

        int intTemp = calendar.get(7) - 1;
        switch (intTemp) {
            case 0:
                week = "星期日";
                break;
            case 1:
                week = "星期一";
                break;
            case 2:
                week = "星期二";
                break;
            case 3:
                week = "星期三";
                break;
            case 4:
                week = "星期四";
                break;
            case 5:
                week = "星期五";
                break;
            case 6:
                week = "星期六";
            default:
                break;
        }

        return week;
    }

//    public static String formatDateStr2Desc(String strDate, String outFormat) {
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Calendar c1 = Calendar.getInstance();
//        Calendar c2 = Calendar.getInstance();
//
//        try {
//            c2.setTime(df.parse(strDate));
//            c1.setTime(new Date());
//            int d = getOffectDay(c1.getTimeInMillis(), c2.getTimeInMillis());
//            if (d == 0) {
//                int h = getOffectHour(c1.getTimeInMillis(), c2.getTimeInMillis());
//                if (h > 0) {
//                    return "今天" + getStringByFormat(strDate, "HH:mm");
//                }
//
//                if (h >= 0 && h == 0) {
//                    int m = getOffectMinutes(c1.getTimeInMillis(), c2.getTimeInMillis());
//                    if (m > 0) {
//                        return m + "分钟前";
//                    }
//
//                    if (m >= 0) {
//                        return "刚刚";
//                    }
//                }
//            } else if (d > 0) {
//                if (d != 1 && d == 2) {
//                    ;
//                }
//            } else if (d < 0 && d != -1 && d == -2) {
//                ;
//            }
//
//            String out = getStringByFormat(strDate, outFormat);
//            if (!BaseStringUtil.isEmpty(out)) {
//                return out;
//            }
//        } catch (Exception var8) {
//            ;
//        }
//
//        return strDate;
//    }

    public static String getTimeQuantum(String strDate, String format) {
        Date mDate = getDateByFormat(strDate, format);
        int hour = mDate.getHours();
        return hour >= 12 ? "PM" : "AM";
    }

    public static String getTimeDescription(long milliseconds) {
        if (milliseconds > 1000L) {
            if (milliseconds / 1000L / 60L > 1L) {
                long minute = milliseconds / 1000L / 60L;
                long second = milliseconds / 1000L % 60L;
                return minute + "分" + second + "秒";
            } else {
                return milliseconds / 1000L + "秒";
            }
        } else {
            return milliseconds + "毫秒";
        }
    }

    public static boolean compareTime(String date1, String date2) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date dt1 = df.parse(String.valueOf(date1));
            Date dt2 = df.parse(String.valueOf(date2));
            if (dt1.getTime() > dt2.getTime()) {
                return true;
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }
        return false;
    }

    public static boolean compareTime2Date(Date date1, String date2) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date dt2 = df.parse(String.valueOf(date2));
            if (date1.getTime() > dt2.getTime()) {
                return true;
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }
        return false;
    }

    public static boolean xiangdeng(String date1, String date2) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(String.valueOf(date1));
            Date dt2 = df.parse(String.valueOf(date2));
            if (dt1.getTime() == dt2.getTime()) {
                return true;
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }
        return false;
    }

    public static boolean compareTimee(String date1, String date2) {
        int date1H = Integer.valueOf(date1.substring(0, 2));
        int date1m = Integer.valueOf(date1.substring(3, 5));
        int date2H = Integer.valueOf(date2.substring(0, 2));
        int date2m = Integer.valueOf(date2.substring(3, 5));
        if (date1H > date2H) {
            return true;
        } else if (date1H == date2H) {
            return date1m >= date2m;
        } else {
            return false;
        }
    }

    public static boolean compareTimeee(String date1, String date2) {
        int date1H = Integer.valueOf(date1.substring(0, 2));
        int date1m = Integer.valueOf(date1.substring(3, 5));
        int date2H = Integer.valueOf(date2.substring(0, 2));
        int date2m = Integer.valueOf(date2.substring(3, 5));
        if (date1H > date2H) {
            return true;
        } else if (date1H == date2H) {
            return date1m > date2m;
        } else {
            return false;
        }
    }

    public static String dateFormat(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(datetime);
        } catch (ParseException var4) {
            var4.printStackTrace();
        }
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(2, 1);
        date = cl.getTime();
        return sdf.format(date);
    }

    public static String getNextMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(5, mondayPlus + 7);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    public static String getNextMonday(int d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(5, mondayPlus + 7 + d);
        Date monday = currentDate.getTime();
        return sdf.format(monday);
    }

    private static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        int dayOfWeek = cd.get(7) - 1;
        return dayOfWeek == 1 ? 0 : 1 - dayOfWeek;
    }

    public Date getDateByOffset(Date date, int calendarField, int offset) {
        GregorianCalendar c = new GregorianCalendar();
        try {
            c.setTime(date);
            c.add(calendarField, offset);
        } catch (Exception var6) {
            var6.printStackTrace();
        }
        return c.getTime();
    }
}
