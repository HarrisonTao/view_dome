package com.dykj.module.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author WZ
 * @date 2019/5/16.
 * 严格要求自己，对每一行代码负责
 * description：
 */
public class BaseStringUtil {
    public BaseStringUtil() {
    }

    public static String parseEmpty(String str) {
        if (str == null || "null".equals(str.trim())) {
            str = "";
        }

        return str.trim();
    }

    public static boolean isEmpty(String str) {
        if (str != null) {
            str.replace(" ", "");
        }

        return str == null || str.trim().length() == 0;
    }

    public static boolean compare(String str1, String str2) {
        return !isEmpty(str1) && !isEmpty(str2) ? str1.equalsIgnoreCase(str2) : false;
    }

    public static int chineseLength(String str) {
        int valueLength = 0;
        String chinese = "[Α-¥]";
        if (!isEmpty(str)) {
            for(int i = 0; i < str.length(); ++i) {
                String temp = str.substring(i, i + 1);
                if (temp.matches(chinese)) {
                    valueLength += 2;
                }
            }
        }

        return valueLength;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }

        return dest;
    }

    public static int strLength(String str) {
        int valueLength = 0;
        String chinese = "[Α-¥]";
        if (!isEmpty(str)) {
            for(int i = 0; i < str.length(); ++i) {
                String temp = str.substring(i, i + 1);
                if (temp.matches(chinese)) {
                    valueLength += 2;
                } else {
                    ++valueLength;
                }
            }
        }

        return valueLength;
    }

    public static int subStringLength(String str, int maxL) {
        int currentIndex = 0;
        int valueLength = 0;
        String chinese = "[Α-¥]";

        for(int i = 0; i < str.length(); ++i) {
            String temp = str.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                ++valueLength;
            }

            if (valueLength >= maxL) {
                currentIndex = i;
                break;
            }
        }

        return currentIndex;
    }

    public static Boolean isMobile(String str) {
        Boolean isMobileNo = false;

        try {
            Pattern p = Pattern.compile("^((13[0-9])|(147)|(15[^4,\\D])|(17[6-8])|(18[0-9]))\\d{8}$");
            Matcher m = p.matcher(str);
            isMobileNo = m.matches();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return isMobileNo;
    }

    public static boolean isIdCard(String idCard) {
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex, idCard);
    }

    public static Boolean isNumberLetter(String str) {
        Boolean isNoLetter = false;
        String expr = "^[A-Za-z0-9]+$";
        if (str.matches(expr)) {
            isNoLetter = true;
        }

        return isNoLetter;
    }

    public static boolean isPhone(String phone) {
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }

    public static boolean isPostcode(String postcode) {
        String regex = "[1-9]\\d{5}";
        return Pattern.matches(regex, postcode);
    }

    public static Boolean isNumber(String str) {
        Boolean isNumber = false;
        String expr = "^[0-9]+$";
        if (str.matches(expr)) {
            isNumber = true;
        }

        return isNumber;
    }

    public static Boolean isEmail(String str) {
        Boolean isEmail = false;
        String expr = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        if (str.matches(expr)) {
            isEmail = true;
        }

        return isEmail;
    }

    public static Boolean isChinese(String str) {
        Boolean isChinese = true;
        String chinese = "[Α-¥]";
        if (!isEmpty(str)) {
            for(int i = 0; i < str.length(); ++i) {
                String temp = str.substring(i, i + 1);
                if (!temp.matches(chinese)) {
                    isChinese = false;
                }
            }
        }

        return isChinese;
    }

    public static Boolean isContainChinese(String str) {
        Boolean isChinese = false;
        String chinese = "[Α-¥]";
        if (!isEmpty(str)) {
            for(int i = 0; i < str.length(); ++i) {
                String temp = str.substring(i, i + 1);
                if (temp.matches(chinese)) {
                    isChinese = true;
                }
            }
        }

        return isChinese;
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            if (sb.indexOf("\n") != -1 && sb.lastIndexOf("\n") == sb.length() - 1) {
                sb.delete(sb.lastIndexOf("\n"), sb.lastIndexOf("\n") + 1);
            }
        } catch (IOException var13) {
            var13.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException var12) {
                var12.printStackTrace();
            }

        }

        return sb.toString();
    }

    public static String dateTimeFormat(String dateTime) {
        StringBuilder sb = new StringBuilder();

        try {
            if (isEmpty(dateTime)) {
                return null;
            }

            String[] dateAndTime = dateTime.split(" ");
            if (dateAndTime.length > 0) {
                String[] var3 = dateAndTime;
                int var4 = dateAndTime.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    String str = var3[var5];
                    String[] date;
                    int i;
                    String str1;
                    if (str.indexOf("-") != -1) {
                        date = str.split("-");

                        for(i = 0; i < date.length; ++i) {
                            str1 = date[i];
                            sb.append(strFormat2(str1));
                            if (i < date.length - 1) {
                                sb.append("-");
                            }
                        }
                    } else if (str.indexOf(":") != -1) {
                        sb.append(" ");
                        date = str.split(":");

                        for(i = 0; i < date.length; ++i) {
                            str1 = date[i];
                            sb.append(strFormat2(str1));
                            if (i < date.length - 1) {
                                sb.append(":");
                            }
                        }
                    }
                }
            }
        } catch (Exception var10) {
            var10.printStackTrace();
            return null;
        }

        return sb.toString();
    }

    public static String strFormat2(String str) {
        try {
            if (str.length() <= 1) {
                str = "0" + str;
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        return str;
    }

    public static String cutString(String str, int length) {
        return cutString(str, length, "");
    }

    public static String cutString(String str, int length, String dot) {
        int strBLen = strlen(str, "GBK");
        if (strBLen <= length) {
            return str;
        } else {
            int temp = 0;
            StringBuffer sb = new StringBuffer(length);
            char[] ch = str.toCharArray();
            char[] var7 = ch;
            int var8 = ch.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                char c = var7[var9];
                sb.append(c);
                if (c > 256) {
                    temp += 2;
                } else {
                    ++temp;
                }

                if (temp >= length) {
                    if (dot != null) {
                        sb.append(dot);
                    }
                    break;
                }
            }

            return sb.toString();
        }
    }

    public static String cutStringFromChar(String str1, String str2, int offset) {
        if (isEmpty(str1)) {
            return "";
        } else {
            int start = str1.indexOf(str2);
            return start != -1 && str1.length() > start + offset ? str1.substring(start + offset) : "";
        }
    }

    public static int strlen(String str, String charset) {
        if (str != null && str.length() != 0) {
            int length = 0;

            try {
                length = str.getBytes(charset).length;
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return length;
        } else {
            return 0;
        }
    }

    public static String getSizeDesc(long size) {
        String suffix = "B";
        if (size >= 1024L) {
            suffix = "K";
            size >>= 10;
            if (size >= 1024L) {
                suffix = "M";
                size >>= 10;
                if (size >= 1024L) {
                    suffix = "G";
                    size >>= 10;
                }
            }
        }

        return size + suffix;
    }

    public static long ip2int(String ip) {
        ip = ip.replace(".", ",");
        String[] items = ip.split(",");
        return Long.valueOf(items[0]) << 24 | Long.valueOf(items[1]) << 16 | Long.valueOf(items[2]) << 8 | Long.valueOf(items[3]);
    }

    public static String MD5(String original) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(original.getBytes("UTF-8"));
        } catch (Exception var5) {
            return "";
        }

        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();

        for(int i = 0; i < byteArray.length; ++i) {
            if (Integer.toHexString(255 & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(255 & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(255 & byteArray[i]));
            }
        }

        return md5StrBuff.toString().toLowerCase();
    }

    public static boolean IDCardValidate(String IDStr) {
        String errorInfo = "";
        String[] ValCodeArr = new String[]{"1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] Wi = new String[]{"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            return false;
        } else {
            if (IDStr.length() == 18) {
                Ai = IDStr.substring(0, 17);
            } else if (IDStr.length() == 15) {
                Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
            }

            if (!isNumeric(Ai)) {
                return false;
            } else {
                String strYear = Ai.substring(6, 10);
                String strMonth = Ai.substring(10, 12);
                String strDay = Ai.substring(12, 14);
                if (!isDataFormat(strYear + "-" + strMonth + "-" + strDay)) {
                    return false;
                } else {
                    GregorianCalendar gc = new GregorianCalendar();
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");

                    try {
                        if (gc.get(1) - Integer.parseInt(strYear) > 150 || gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime() < 0L) {
                            return false;
                        }
                    } catch (NumberFormatException var14) {
                        return false;
                    } catch (ParseException var15) {
                        return false;
                    }

                    if (Integer.parseInt(strMonth) <= 12 && Integer.parseInt(strMonth) != 0) {
                        if (Integer.parseInt(strDay) <= 31 && Integer.parseInt(strDay) != 0) {
                            Hashtable h = GetAreaCode();
                            if (h.get(Ai.substring(0, 2)) == null) {
                                return false;
                            } else {
                                int TotalmulAiWi = 0;

                                int modValue;
                                for(modValue = 0; modValue < 17; ++modValue) {
                                    TotalmulAiWi += Integer.parseInt(String.valueOf(Ai.charAt(modValue))) * Integer.parseInt(Wi[modValue]);
                                }

                                modValue = TotalmulAiWi % 11;
                                String strVerifyCode = ValCodeArr[modValue];
                                Ai = Ai + strVerifyCode;
                                if (IDStr.length() == 18) {
                                    return Ai.equalsIgnoreCase(IDStr);
                                } else {
                                    return true;
                                }
                            }
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }
    }

    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    public static boolean isDataFormat(String str) {
        boolean flag = false;
        String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern1 = Pattern.compile(regxStr);
        Matcher isNo = pattern1.matcher(str);
        if (isNo.matches()) {
            flag = true;
        }

        return flag;
    }
}
