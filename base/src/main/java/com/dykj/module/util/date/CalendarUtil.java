package com.dykj.module.util.date;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class CalendarUtil {

    public static int getTheFirstDayOfWeek(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, 1);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }

    /**
     * 日期转换成秒数
     */
    public static long getMilliSecondsFromDate(String expireDate) {
        if (expireDate == null || expireDate.trim().equals("")) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(expireDate);
            return (long) (date.getTime() / 1000) * 1000;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public static int getYear() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat yearSdf = new SimpleDateFormat("yyyy");
        String yearString = yearSdf.format(calendar.getTime());
        return Integer.parseInt(yearString);
    }

    public static int getMonth() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat monthSdf = new SimpleDateFormat("MM");
        String monthString = monthSdf.format(calendar.getTime());
        return Integer.parseInt(monthString);
    }

    public static int getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateSdf = new SimpleDateFormat("dd");
        String dateString = dateSdf.format(calendar.getTime());
        return Integer.parseInt(dateString);
    }

    public static int getWeek(int year, int month, int day) {
        Date date = new Date(year - 1900, month - 1, day);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }

    public static int getMonthMaxDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.DATE, -1);
        String str = sdf.format(calendar.getTime());
        int maxDate = Integer.parseInt(str);
        return maxDate;
    }

}
