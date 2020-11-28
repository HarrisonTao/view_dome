package com.dykj.module.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    /**
     * @param year
     * @param month
     * @param day
     * @param last
     * @return
     */
    public static Date getDateLast(int year, int month, int day, int last) {
        Date targetDate = new Date(year - 1900, month - 1, day);
        long addTime = (((long) last) * 24 * 3600 * 1000);
        long currentTime = targetDate.getTime();
        long lastTime = currentTime + addTime;
        Date date = new Date(lastTime);
        return date;
    }

    /**
     * @param year
     * @param month
     * @param day
     * @param before
     * @return
     */
    public static Date getDateBefore(int year, int month, int day, int before) {
        Date targetDate = new Date(year - 1900, month - 1, day);
        long delTime = (((long) before) * 24 * 3600 * 1000);
        long beforeTime = targetDate.getTime() - delTime;
        return new Date(beforeTime);
    }

    public static long getDateInternal(Date date1, Date date2) {
        long interalTime = Math.abs(date2.getTime() - date1.getTime());
        return interalTime / (1000 * 3600 * 24);
    }

    public static boolean isDate(int year, int month, int day) {
        int maxDay = CalendarUtil.getMonthMaxDay(year, month);
        return day <= maxDay;
    }

    public static int getMaxDay(int year, int month) {
        return CalendarUtil.getMonthMaxDay(year, month);
    }

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    //判断 某年某月有多少天
    public static int getMonthDays(int years, int month) {
        int days = 0;
        switch (month + 1) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 2:
                if (isLeapYears(years))
                    days = 29;
                else
                    days = 28;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
        }
        return days;
    }

    //判断某天是否已经过去
    // month 0-11
    public static boolean isPassed(int years, int month, int day) {
        String str;
        month++;
        if (month < 10) {
            str = years + "-0" + month + "-" + day;
        } else {
            str = years + "-" + month + "-" + day;
        }
        try {
            long time = sdf.parse(str).getTime();
            if (time >= System.currentTimeMillis()) {
                return false;
            } else if (System.currentTimeMillis() - time < 24 * 60 * 60 * 1000) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    //判断 某月第一天是周几
    public static int getFirstDayWeek(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month);    //设置月份
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.YEAR, year);
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return 7;
        } else {
            return c.get(Calendar.DAY_OF_WEEK) - 1;
        }
    }

    public static final boolean isLeapYears(int year) {
        if (year % 100 == 0) {
            if (year % 400 == 0) {
                return true;
            } else {
                return false;
            }
        } else if (year % 4 == 0) {
            return true;
        } else {
            return false;
        }
    }

    //判断某年某月有几个周
    public static final int getWeekNum(int year, int month) {
        int days = getMonthDays(year, month);
        int fdw = getFirstDayWeek(year, month);
        if (days == 28 && fdw == 1) {
            return 4;
        } else if (days == 29) {
            return 5;
        } else if (days == 30 && fdw < 5 || fdw == 7) {
            return 5;
        } else if (days == 31 && fdw < 4 || fdw == 7) {
            return 5;
        } else {
            return 6;
        }
    }


    private static final String[] WEEK_CN_NAME = {"周一", "周二", "周三", "周四",
            "周五", "周六", "周日"};


    public static final Date getDate(int y, int m, int d) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String pas = y + "-" + getDouble(m) + "-" + getDouble(d);
        try {
            return simpleDateFormat.parse(pas);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final String getDouble(int i) {
        if (i < 10 && i > 0) {
            return "0" + i;
        }
        return i + "";
    }

    public static String getDateWeekCnName(Date date) {//通过传入date对象,获取对应的星期数

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = WEEK_CN_NAME[6];
        } else if ("2".equals(mWay)) {
            mWay = WEEK_CN_NAME[0];
        } else if ("3".equals(mWay)) {
            mWay = WEEK_CN_NAME[1];
        } else if ("4".equals(mWay)) {
            mWay = WEEK_CN_NAME[2];
        } else if ("5".equals(mWay)) {
            mWay = WEEK_CN_NAME[3];
        } else if ("6".equals(mWay)) {
            mWay = WEEK_CN_NAME[4];
        } else if ("7".equals(mWay)) {
            mWay = WEEK_CN_NAME[5];
        }
        return mWay;
    }

    public static int getWeekOfYear(Date date) {
        long Time1 = date.getTime();
        date = new Date(Time1 - 24 * 60 * 60 * 1000);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        return c.get(Calendar.WEEK_OF_YEAR) - 1;
    }

    public static int getWeekCountInMonth(int year, int month) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String pas1 = year + "-" + getDouble(month - 1) + "-01";
        String pas2 = year + "-" + getDouble(month - 1) + "-" + getMonthDays(year, month);
        Date date1, date2;
        try {
            date1 = simpleDateFormat.parse(pas1);
            date2 = simpleDateFormat.parse(pas2);
            int rows = getWeekOfYear(date2) - getWeekOfYear(date1) + 3;
            return rows;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
