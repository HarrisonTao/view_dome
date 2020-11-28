package com.dykj.module.util.date;


import com.dykj.module.view.bean.DateBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${LCJ} on 2016/11/29.
 */

public class MyCalendar {

    //static MC[][] calendars=new MC[6][7];
    private static LunarCalendar lunarCalendar = new LunarCalendar();

    public static MC[][] getCalendars(int year, int month) {
        MC[][] calendars = new MC[6][7];
        int lastMonth, lastMonthYear, nextMonth, nextYear;
        if (month == 0) {
            lastMonth = 11;
            lastMonthYear = year - 1;
        } else {
            lastMonth = month - 1;
            lastMonthYear = year;
        }
        if (month == 11) {
            nextMonth = 0;
            nextYear = year + 1;
        } else {
            nextMonth = month + 1;
            nextYear = year;
        }
        //判断上个月有多少天
        int lastMonthDays = DateUtil.getMonthDays(lastMonthYear, lastMonth);
        //判断这个月有多少天
        int mMonthDays = DateUtil.getMonthDays(year, month);
        //判断该月第一天是周几
        int weekNumber = DateUtil.getFirstDayWeek(year, month);
        MC mc;
        int j = 0;
        int pWN = pauseWN(weekNumber);
        for (int i = pWN - 1; i >= 0; i--) {
            mc = new MC();
            mc.setMonth(-1);
            mc.setThisMonth(false);
            mc.setDay(lastMonthDays - j);
            mc.setLunar(lunarCalendar.getLunarDate(lastMonthYear, lastMonth, mc.getDay()));
            calendars[0][i] = mc;
            j++;
        }

        for (int i = 1; i <= mMonthDays; i++) {
            mc = new MC();
            mc.setDay(i);
            mc.setLunar(lunarCalendar.getLunarDate(year, month, mc.getDay()));
            mc.setThisMonth(true);
            mc.setPass(DateUtil.isPassed(year, month, mc.getDay()));
            mc.setMonth(0);
            int column = (i + pWN - 1) % 7;
            int row = (i + pWN - 1) / 7;
            calendars[row][column] = mc;
        }

        for (int i = 1; i <= 42 - pWN - mMonthDays; i++) {
            mc = new MC();
            mc.setDay(i);
            mc.setThisMonth(false);
            mc.setLunar(lunarCalendar.getLunarDate(nextYear, nextMonth, mc.getDay()));
            mc.setMonth(1);
            int column = (i + pWN + mMonthDays - 1) % 7;
            int row = (i + pWN + mMonthDays - 1) / 7;
            calendars[row][column] = mc;
        }
        return calendars;
    }

    public static List<DateBean> getCalendarList(int year, int month) {
        List<DateBean> calendars = new ArrayList<>();
        int lastMonth, lastMonthYear, nextMonth, nextYear;
        if (month == 0) {
            lastMonth = 11;
            lastMonthYear = year - 1;
        } else {
            lastMonth = month - 1;
            lastMonthYear = year;
        }
        if (month == 11) {
            nextMonth = 0;
            nextYear = year + 1;
        } else {
            nextMonth = month + 1;
            nextYear = year;
        }
        //判断上个月有多少天
        int lastMonthDays = DateUtil.getMonthDays(lastMonthYear, lastMonth);
        //判断这个月有多少天
        int mMonthDays = DateUtil.getMonthDays(year, month);
        //判断该月第一天是周几
        int weekNumber = DateUtil.getFirstDayWeek(year, month);
        DateBean mc;

        int pWN = pauseWN(weekNumber);
        while (pWN > 0) {
            pWN--;
            mc = new DateBean();
            int date = lastMonthDays - pWN;
            mc.setDate(DateFormatUtil.getDate(lastMonthYear, lastMonth + 1, date));
            mc.setDateTime(DateFormatUtil.getDateTime(mc.getDate()));
            mc.setDay(date + "");
            mc.setShow(false);
            calendars.add(mc);
        }


        for (int i = 1; i <= mMonthDays; i++) {
            mc = new DateBean();
            mc.setDay(i + "");
            mc.setDate(DateFormatUtil.getDate(year, month + 1, i));
            mc.setDateTime(DateFormatUtil.getDateTime(mc.getDate()));
            mc.setShow(true);
            calendars.add(mc);
        }
        int nextMonthDays;
        if (calendars.size() < 35) {
            nextMonthDays = 35 - calendars.size();
        } else {
            nextMonthDays = 42 - calendars.size();
        }
        for (int i = 1; i <= nextMonthDays; i++) {
            mc = new DateBean();
            mc.setDay(i + "");
            mc.setDate(DateFormatUtil.getDate(nextYear, nextMonth + 1, i));
            mc.setDateTime(DateFormatUtil.getDateTime(mc.getDate()));
            mc.setShow(false);
            calendars.add(mc);
        }
        return calendars;
    }

    //不选下个月的日期
    public static List<DateBean> getCalendarLists(int year, int month) {
        List<DateBean> calendars = new ArrayList<>();
        int lastMonth, lastMonthYear, nextMonth, nextYear;
        if (month == 0) {
            lastMonth = 11;
            lastMonthYear = year - 1;
        } else {
            lastMonth = month - 1;
            lastMonthYear = year;
        }
        if (month == 11) {
            nextMonth = 0;
            nextYear = year + 1;
        } else {
            nextMonth = month + 1;
            nextYear = year;
        }
        //判断上个月有多少天
        int lastMonthDays = DateUtil.getMonthDays(lastMonthYear, lastMonth);
        //判断这个月有多少天
        int mMonthDays = DateUtil.getMonthDays(year, month);
        //判断该月第一天是周几
        int weekNumber = DateUtil.getFirstDayWeek(year, month);
        DateBean mc;

        int pWN = pauseWN(weekNumber);
        while (pWN > 0) {
            pWN--;
            mc = new DateBean();
            int date = lastMonthDays - pWN;
            mc.setDate(DateFormatUtil.getDate(lastMonthYear, lastMonth + 1, date));
            mc.setDateTime(DateFormatUtil.getDateTime(mc.getDate()));
            mc.setDay(date + "");
            mc.setShow(false);
            calendars.add(mc);
        }


        for (int i = 1; i <= mMonthDays; i++) {
            mc = new DateBean();
            mc.setDay(i + "");
            mc.setDate(DateFormatUtil.getDate(year, month + 1, i));
            mc.setDateTime(DateFormatUtil.getDateTime(mc.getDate()));
            mc.setShow(true);
            calendars.add(mc);
        }
        return calendars;
    }

    private static int pauseWN(int weekNumber) {
        if (weekNumber == 7) {
            return 0;
        } else {
            return weekNumber;
        }
    }

    public static class MC {
        private int day;
        private String lunar;
        private String festival;
        //0 休息 1 上班
        private boolean DayOff;
        private boolean isThisMonth = false;
        private boolean pass = true;
        private int month;

        public boolean isPass() {
            return pass;
        }

        public void setPass(boolean pass) {
            this.pass = pass;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public boolean isDayOff() {
            return DayOff;
        }

        public void setDayOff(boolean dayOff) {
            DayOff = dayOff;
        }

        public boolean isThisMonth() {
            return isThisMonth;
        }

        public void setThisMonth(boolean thisMonth) {
            isThisMonth = thisMonth;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public String getLunar() {
            return lunar;
        }

        public void setLunar(String lunar) {
            this.lunar = lunar;
        }

        public String getFestival() {
            return festival;
        }

        public void setFestival(String festival) {
            this.festival = festival;
        }


    }
}
