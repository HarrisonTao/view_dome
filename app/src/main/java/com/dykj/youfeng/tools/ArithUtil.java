package com.dykj.youfeng.tools;

import android.text.TextUtils;

import com.dykj.module.util.date.CalendarUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * @author tangji
 * @date 2017/12/29 15:12
 */

public class ArithUtil {
    //默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    //这个类不能实例化
    private ArithUtil() {
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

    public static String getEndDays(String repaymentDate) {
        int repaymentDay = Integer.parseInt(repaymentDate);
        Calendar calendar = Calendar.getInstance();
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (repaymentDay > mDay) {
            repaymentDay = repaymentDay - mDay;
        } else {
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            if (month == 11) {
                year++;
                month = 0;
            } else {
                month++;
            }
            repaymentDay = CalendarUtil.getMonthMaxDay(year, month) + repaymentDay - mDay;
        }
        return repaymentDay + "天";
    }

    /**
     * 截取银行卡为**** **** **** 4234的形式
     */
    public static String hintBank(String bank) {
        if (bank.contains("*")) {
            return bank;
        }
        if (bank.length()==4) {
            return "****\t****\t****\t" + bank;
        }else if (bank.length() > 16) {
            String str1 = bank.substring(0, 4);
            String str2 = bank.substring(bank.length() - 4);
            return "****\t****\t****\t****\t" + str2;
        } else if (bank.length() > 12) {
            String str2 = bank.substring(bank.length() - 4);
            return "****\t****\t****\t" + str2;
        } else {
            return bank;
        }
    }


    /**
     * 截取身份证号卡为**** **** **** 4234的形式
     */
    public static String hintIdNum(String num) {
        if (num.length() > 6) {
            String str2 = num.substring(num.length() - 4);
            String str1 = num.substring(0,4);
            return str1+"*********" + str2;
        } else {
            return "**************" + num;
        }
    }

    /**
     * 获取四位尾号
     */
    public static String getBankEnding(String bank) {
        if (TextUtils.isEmpty(bank)) {
            return "";
        } else if (bank.length() > 4) {
            return bank.substring(bank.length() - 4);
        } else {
            return bank;
        }
    }

    public static String removeTrim(String str) {
        int index = str.indexOf(".");
        if (index > 0) {
//            str = str.replaceAll("0+?$", "");//去掉多余的0
//            str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
            str = str.substring(0, index);
        }
        return str;
    }


    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or kf_zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double formatNum(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or kf_zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    public static String formatNum(float rate) {
        DecimalFormat mFormat = new DecimalFormat(".00");
        String formatNum = mFormat.format(rate);
        if (formatNum.indexOf(".")==0) {
            formatNum="0"+formatNum;
        }
        return formatNum;
    }
    public static String formatIntNum(float rate) {
        return formatNum(rate);
//        rate= (float)Math.floor(rate);
//        DecimalFormat mFormat = new DecimalFormat("0");
//        String formatNum = mFormat.format(rate);
//        if (formatNum.indexOf(".")==0) {
//            formatNum="0";
//        }
//        return formatNum;
    }

    public static String formatDoubleNum(double rate) {
        DecimalFormat mFormat = new DecimalFormat(".00");
        String formatNum = mFormat.format(rate);
        if (formatNum.indexOf(".")==0) {
            formatNum="0"+formatNum;
        }
        return formatNum;
    }
}
