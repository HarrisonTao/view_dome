package com.dykj.module.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Administrator on 2016/8/25.
 */
public class DateFormatUtil {
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
    public static SimpleDateFormat sdf3 = new SimpleDateFormat("昨天 HH:mm:ss");
    public static final SimpleDateFormat yMd = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat hm = new SimpleDateFormat("HH:mm");

    public static final String getDate() {
        return yMd.format(new Date());
    }

    public static final String getDateTime() {
        return sdf1.format(new Date());
    }

    public static final String getHm(long data) {
        return hm.format(data);
    }


    public static String getDateYSM(long date) {
        return sdf.format(date);
    }

    public static String dateFormat(long date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm", Locale.CHINA);
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy", Locale.CHINA);
        long now = System.currentTimeMillis();
        if (now < date) {
            return sdf2.format(date);
        }
        String day = sdf.format(now);
        long nowDay = sdf.parse(day).getTime();
        long ch = (nowDay - date) / 1000 / 60 / 60;
        if (date >= nowDay) {//今天
            return sdf2.format(date);
        } else if (ch < 24) {//昨天
            return "昨天 " + sdf2.format(date);
        } else {
            String year = day.substring(0, 4);
            String dateYear = sdf3.format(now);
            if (year.equals(dateYear)) {//今年
                return sdf1.format(date);
            } else {//
                return sdf.format(date);
            }
        }
    }


    public static String getTime(String dateMill){
//        pointBean.getType_name()+"000"

        try {
            long date= Long.parseLong(dateMill)*1000;
            return sdf.format(date);
        }catch (Exception e){
            return dateMill;
        }
    }

    public static String getTimeToS(String dateMill){
//        pointBean.getType_name()+"000"

        try {
            long date= Long.parseLong(dateMill)*1000;
            return sdf1.format(date);
        }catch (Exception e){
            return dateMill;
        }
    }

    public static String getDate(int year, int month, int day){
        String sMonth=month>9?""+month:"0"+month;
        String sDay=day>9?""+day:"0"+day;
        return year+"-"+sMonth+"-"+sDay;
    }
    public static long getDateTime(String date){
        try {
            return   yMd.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getDateTime(int year,int month,int day){
        String sMonth=month>9?""+month:"0"+month;
        String sDay=day>9?""+day:"0"+day;
        String date=year+"-"+sMonth+"-"+sDay;
        try {
          return   yMd.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getDay(long time){
//        pointBean.getType_name()+"000"
        time=time*1000;
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        try {
            return Integer.parseInt(sdf.format(time));
        }catch (Exception e){
            return 0;
        }
    }


    public static String chatDateFormat(long date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM月dd日", Locale.CHINA);
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm", Locale.CHINA);
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy", Locale.CHINA);
        long now = System.currentTimeMillis();
        if (now < date) {
            return sdf2.format(date);
        }
        String day = sdf.format(now);
        long nowDay = sdf.parse(day).getTime();
        long ch = (nowDay - date) / 1000 / 60 / 60;
        if (date >= nowDay) {//今天
            return sdf2.format(date);
        } else if (ch < 24) {//昨天
            return "昨天";
        } else {
            String year = day.substring(0, 4);
            String dateYear = sdf3.format(now);
            if (year.equals(dateYear)) {//今年
                String md = sdf1.format(date);
                if (md.charAt(0) == '0') {
                    md = md.substring(1);
                }
                return md;
            } else {//
                return sdf.format(date);
            }
        }
    }


    public static String banjianDateFormat(long date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM月dd日", Locale.CHINA);
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm", Locale.CHINA);
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy", Locale.CHINA);
        long now = System.currentTimeMillis();
        if (now < date) {
            return sdf2.format(date);
        }
        String day = sdf.format(now);
        String year = day.substring(0, 4);
        String dateYear = sdf3.format(now);
        if (year.equals(dateYear)) {//今年
            String md = sdf1.format(date);
            if (md.charAt(0) == '0') {
                md = md.substring(1);
            }
            return md;
        } else {//
            return sdf.format(date);
        }
    }

    public static String getToMin(long date) {
        return sdf.format(date);
    }


    public static String getToS(long date) {
        return sdf1.format(date);
    }

    public static long timeToLong(String time) {
        long date = 0;
        try {
            date = sdf.parse(time).getDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static long DateToLong(String date) {
        long date1 = 0;
        try {
            date1 = yMd.parse(date).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date1;
    }

    /**
     * 格式化日期时间
     *
     * @param date
     * @param pattern 格式化模式，详见{@link SimpleDateFormat}构造器
     *                <code>SimpleDateFormat(String pattern)</code>
     * @return
     */
    public static String formatDatetime(Date date, String pattern) {
        SimpleDateFormat customFormat = (SimpleDateFormat) datetimeFormat
                .clone();
        customFormat.applyPattern(pattern);
        return customFormat.format(date);
    }

    private static SimpleDateFormat datetimeFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat(
            "HH:mm:ss");

    /**
     * 获得当前日期时间
     * <p>
     * 日期时间格式yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String currentDatetime() {
        return datetimeFormat.format(now());
    }

    /**
     * 格式化日期时间
     * <p>
     * 日期时间格式yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String formatDatetime(Date date) {
        return datetimeFormat.format(date);
    }


    /**
     * 获得当前日期
     * <p>
     * 日期格式yyyy-MM-dd
     *
     * @return
     */
    public static String currentDate() {
        return dateFormat.format(now());
    }

    /**
     * 格式化日期
     * <p>
     * 日期格式yyyy-MM-dd
     *
     * @return
     */
    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }

    /**
     * 格式化日期
     * <p>
     * 日期格式yyyy-MM-dd
     *
     * @return
     */
    public static String formatDate(long date) {
        return dateFormat.format(date);
    }

    /**
     * 获得当前时间
     * <p>
     * 时间格式HH:mm:ss
     *
     * @return
     */
    public static String currentTime() {
        return timeFormat.format(now());
    }

    /**
     * 格式化时间
     * <p>
     * 时间格式HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String formatTime(long date) {
        return timeFormat.format(date);
    }

    /**
     * 获得当前时间的<code>java.util.Date</code>对象
     *
     * @return
     */
    public static Date now() {
        return new Date();
    }

    public static Calendar calendar() {
        Calendar cal = GregorianCalendar.getInstance(Locale.CHINESE);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        return cal;
    }

    /**
     * 获得当前时间的毫秒数
     * <p>
     * 详见{@link System#currentTimeMillis()}
     *
     * @return
     */
    public static long millis() {
        return System.currentTimeMillis();
    }


    /**
     * 获得当前Chinese月份
     *
     * @return
     */
    public static int month() {
        return calendar().get(Calendar.MONTH) + 1;
    }

    /**
     * 获得当前Chinese年份
     *
     * @return
     */
    public static int year() {
        return calendar().get(Calendar.YEAR);
    }

    /**
     * 获得月份中的第几天
     *
     * @return
     */
    public static int dayOfMonth() {
        return calendar().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 今天是星期的第几天
     *
     * @return
     */
    public static int dayOfWeek() {
        return calendar().get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 今天是年中的第几天
     *
     * @return
     */
    public static int dayOfYear() {
        return calendar().get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 判断原日期是否在目标日期之前
     *
     * @param src
     * @param dst
     * @return
     */
    public static boolean isBefore(Date src, Date dst) {
        return src.before(dst);
    }

    /**
     * 判断原日期是否在目标日期之后
     *
     * @param src
     * @param dst
     * @return
     */
    public static boolean isAfter(Date src, Date dst) {
        return src.after(dst);
    }

    /**
     * 判断两日期是否相同
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isEqual(Date date1, Date date2) {
        return date1.compareTo(date2) == 0;
    }

    /**
     * 判断某个日期是否在某个日期范围
     *
     * @param beginDate 日期范围开始
     * @param endDate   日期范围结束
     * @param src       需要判断的日期
     * @return
     */
    public static boolean between(Date beginDate, Date endDate, Date src) {
        return beginDate.before(src) && endDate.after(src);
    }

    /**
     * 获得当前月的最后一天
     * <p>
     * HH:mm:ss为0，毫秒为999
     *
     * @return
     */
    public static Date lastDayOfMonth() {
        Calendar cal = calendar();
        cal.set(Calendar.DAY_OF_MONTH, 0); // M月置零
        cal.set(Calendar.HOUR_OF_DAY, 0);// H置零
        cal.set(Calendar.MINUTE, 0);// m置零
        cal.set(Calendar.SECOND, 0);// s置零
        cal.set(Calendar.MILLISECOND, 0);// S置零
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);// 月份+1
        cal.set(Calendar.MILLISECOND, -1);// 毫秒-1
        return cal.getTime();
    }

    /**
     * 获得当前月的第一天
     * <p>
     * HH:mm:ss SS为零
     *
     * @return
     */
    public static Date firstDayOfMonth() {
        Calendar cal = calendar();
        cal.set(Calendar.DAY_OF_MONTH, 1); // M月置1
        cal.set(Calendar.HOUR_OF_DAY, 0);// H置零
        cal.set(Calendar.MINUTE, 0);// m置零
        cal.set(Calendar.SECOND, 0);// s置零
        cal.set(Calendar.MILLISECOND, 0);// S置零
        return cal.getTime();
    }

    private static Date weekDay(int week) {
        Calendar cal = calendar();
        cal.set(Calendar.DAY_OF_WEEK, week);
        return cal.getTime();
    }

    /**
     * 获得周五日期
     * <p>
     * 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
     *
     * @return
     */
    public static Date friday() {
        return weekDay(Calendar.FRIDAY);
    }

    /**
     * 获得周六日期
     * <p>
     * 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
     *
     * @return
     */
    public static Date saturday() {
        return weekDay(Calendar.SATURDAY);
    }

    /**
     * 获得周日日期
     * <p>
     * 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
     *
     * @return
     */
    public static Date sunday() {
        return weekDay(Calendar.SUNDAY);
    }

    /**
     * 将字符串日期时间转换成java.util.Date类型
     * <p>
     * 日期时间格式yyyy-MM-dd HH:mm:ss
     *
     * @param datetime
     * @return
     */
    public static Date parseDatetime(String datetime) throws ParseException {
        return datetimeFormat.parse(datetime);
    }

    /**
     * 将字符串日期转换成java.util.Date类型
     * <p>
     * 日期时间格式yyyy-MM-dd
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String date) throws ParseException {
        return dateFormat.parse(date);
    }

    /**
     * 将字符串日期转换成java.util.Date类型
     * <p>
     * 时间格式 HH:mm:ss
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static Date parseTime(String time) throws ParseException {
        return timeFormat.parse(time);
    }

    /**
     * 根据自定义pattern将字符串日期转换成java.util.Date类型
     *
     * @param datetime
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parseDatetime(String datetime, String pattern)
            throws ParseException {
        SimpleDateFormat format = (SimpleDateFormat) datetimeFormat.clone();
        format.applyPattern(pattern);
        return format.parse(datetime);
    }

    /**
     * 得到几天前的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 描述:得到几天前的时间
     *
     * @param date    时间字符串
     * @param pattern 该时间字符串的格式
     * @param day     天数
     * @return
     * @throws ParseException
     * @author:huyongli
     * @time:2014-7-31下午05:04:43
     */
    public static Date getDateBefore(String date, String pattern, int day) throws ParseException {
        Date d = parseDatetime(date, pattern);
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 得到几天后的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小时间
     * @param bdate  较大时间
     * @return
     * @throws ParseException
     */
    public static int daysBetween(String smdate, String bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        long between_days = 0;
        try {
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            between_days = (time2 - time1) / (1000 * 3600 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Integer.parseInt(String.valueOf(between_days));
    }

    //java获取当前月的天数
    public static int getDayOfMonth(){
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        int day=aCalendar.getActualMaximum(Calendar.DATE);
        return day;
    }


}
