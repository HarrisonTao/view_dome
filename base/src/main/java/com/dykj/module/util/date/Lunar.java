package com.dykj.module.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ${LCJ} on 2016/11/29.
 */

public class Lunar {
    private int year;
    private int month;
    private int day;
    private boolean leap;
    static final String[] cMonths = new String[]{"正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "冬", "腊"};
    static final String[] cDates = new String[]{"一", "二", "三", "四", "五", "六", "七", "八", "九"};
    static SimpleDateFormat cDatesFormat = new SimpleDateFormat("yyyy年MM月dd日");
    static final long[] lunarInfo = new long[]{19416L, 19168L, 42352L, 21717L, 53856L, 55632L, 91476L, 22176L, 39632L, 21970L, 19168L, 42422L, 42192L, 53840L, 119381L, 46400L, 54944L, 44450L, 38320L, 84343L, 18800L, 42160L, 46261L, 27216L, 27968L, 109396L, 11104L, 38256L, 21234L, 18800L, 25958L, 54432L, 59984L, 28309L, 23248L, 11104L, 100067L, 37600L, 116951L, 51536L, 54432L, 120998L, 46416L, 22176L, 107956L, 9680L, 37584L, 53938L, 43344L, 46423L, 27808L, 46416L, 86869L, 19872L, 42448L, 83315L, 21200L, 43432L, 59728L, 27296L, 44710L, 43856L, 19296L, 43748L, 42352L, 21088L, 62051L, 55632L, 23383L, 22176L, 38608L, 19925L, 19152L, 42192L, 54484L, 53840L, 54616L, 46400L, 46496L, 103846L, 38320L, 18864L, 43380L, 42160L, 45690L, 27216L, 27968L, 44870L, 43872L, 38256L, 19189L, 18800L, 25776L, 29859L, 59984L, 27480L, 21952L, 43872L, 38613L, 37600L, 51552L, 55636L, 54432L, 55888L, 30034L, 22176L, 43959L, 9680L, 37584L, 51893L, 43344L, 46240L, 47780L, 44368L, 21977L, 19360L, 42416L, 86390L, 21168L, 43312L, 31060L, 27296L, 44368L, 23378L, 19296L, 42726L, 42208L, 53856L, 60005L, 54576L, 23200L, 30371L, 38608L, 19415L, 19152L, 42192L, 118966L, 53840L, 54560L, 56645L, 46496L, 22224L, 21938L, 18864L, 42359L, 42160L, 43600L, 111189L, 27936L, 44448L};

    private static final int yearDays(int y) {
        int sum = 348;

        for(int i = '耀'; i > 8; i >>= 1) {
            if((lunarInfo[y - 1900] & (long)i) != 0L) {
                ++sum;
            }
        }

        return sum + leapDays(y);
    }

    private static final int leapDays(int y) {
        return leapMonth(y) != 0?((lunarInfo[y - 1900] & 65536L) != 0L?30:29):0;
    }

    private static final int leapMonth(int y) {
        return (int)(lunarInfo[y - 1900] & 15L);
    }

    private static final int monthDays(int y, int m) {
        return (lunarInfo[y - 1900] & (long)(65536 >> m)) == 0L?29:30;
    }

    public final String getAnimal() {
        String[] Animals = new String[]{"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
        return Animals[(this.year - 4) % 12];
    }

    private static final String cyclicalm(int num) {
        String[] Gan = new String[]{"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
        String[] Zhi = new String[]{"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
        return Gan[num % 10] + Zhi[num % 12];
    }

    public final String cyclical() {
        int num = this.year - 1900 + 36;
        return cyclicalm(num);
    }

    Lunar(Calendar cal) {
        boolean leapMonth = false;
        Date baseDate = null;

        try {
            baseDate = cDatesFormat.parse("1900年1月31日");
        } catch (ParseException var12) {
            var12.printStackTrace();
        }

        int offset = (int)((cal.getTime().getTime() - baseDate.getTime()) / 86400000L);
        int dayCyl = offset + 40;
        int monCyl = 14;
        int daysOfYear = 0;

        int iYear;
        for(iYear = 1900; iYear < 2050 && offset > 0; ++iYear) {
            daysOfYear = yearDays(iYear);
            offset -= daysOfYear;
            monCyl += 12;
        }

        if(offset < 0) {
            offset += daysOfYear;
            --iYear;
            monCyl -= 12;
        }

        this.year = iYear;
        int yearCyl = iYear - 1864;
        int var13 = leapMonth(iYear);
        this.leap = false;
        int daysOfMonth = 0;

        int iMonth;
        for(iMonth = 1; iMonth < 13 && offset > 0; ++iMonth) {
            if(var13 > 0 && iMonth == var13 + 1 && !this.leap) {
                --iMonth;
                this.leap = true;
                daysOfMonth = leapDays(this.year);
            } else {
                daysOfMonth = monthDays(this.year, iMonth);
            }

            offset -= daysOfMonth;
            if(this.leap && iMonth == var13 + 1) {
                this.leap = false;
            }

            if(!this.leap) {
                ++monCyl;
            }
        }

        if(offset == 0 && var13 > 0 && iMonth == var13 + 1) {
            if(this.leap) {
                this.leap = false;
            } else {
                this.leap = true;
                --iMonth;
                --monCyl;
            }
        }

        if(offset < 0) {
            offset += daysOfMonth;
            --iMonth;
            --monCyl;
        }

        this.month = iMonth;
        this.day = offset + 1;
    }

    private static String getChinaDayString(int day) {
        String[] cDays = new String[]{"初", "十", "廿", "卅"};
        int n = day % 10 == 0?9:day % 10 - 1;
        return day > 30?"":(day == 10?"初十":(day % 10 == 0?cDays[day / 10]:cDays[day / 10] + cDates[n]));
    }

    public String getCMonth() {
        return (this.leap?"闰":"") + cMonths[this.month - 1];
    }

    public String getCDate() {
        return getChinaDayString(this.day);
    }

    public String toString() {
        return (this.leap?"闰":"") + cMonths[this.month - 1] + "月" + getChinaDayString(this.day);
    }
}
