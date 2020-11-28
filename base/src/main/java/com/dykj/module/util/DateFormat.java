package com.dykj.module.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lj on 2017/9/19.
 * 时间格式工具类
 */

public class DateFormat {

    public static DateFormat simpleDateFormat;
    public static DateFormat init(){
        if (simpleDateFormat==null){
            simpleDateFormat=new DateFormat();
        }
        return simpleDateFormat;
    }
    private SimpleDateFormat format24;
    private SimpleDateFormat format12;
    private SimpleDateFormat format24m;
    private SimpleDateFormat format24d;
    private SimpleDateFormat format24md;
    private DateFormat(){

    }
    /**
     * 时间格式24小时
     */
    public String format24d(long time){
        if (format24d==null){
            format24d=new SimpleDateFormat("yyyy-MM-dd");
        }
        return format24d.format(new Date(time*1000));
    }
    /**
     * 时间格式24小时 xx月xx日 HH:mm:ss;
     */
    public String format24Ms(long time){
        if (format24md==null){
            format24md=new SimpleDateFormat("MM-dd HH:mm:ss");
        }
        return format24md.format(new Date(time*1000));
    }
    /**
     * 时间格式24小时
     */
    public String format24m(long time){
        if (format24m==null){
            format24m=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }
        return format24m.format(new Date(time*1000));
    }
    /**
     * 时间格式24小时
     */
    public String format24(long time){
        if (format24==null){
            format24=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        return format24.format(new Date(time*1000));
    }
    /**
     * 时间格式24小时
     */
    public String format24_2(long time){
        if (format24==null){
            format24=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        return format24.format(new Date(time));
    }
    public String format12(long time){
        if (format12==null){
            format12=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        }
        return format12.format(new Date(time*1000));
    }
}
