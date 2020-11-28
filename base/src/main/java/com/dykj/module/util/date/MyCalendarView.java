package com.dykj.module.util.date;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;
import java.util.List;

/**
 * Created by ${LCJ} on 2016/11/24.
 */

public class MyCalendarView extends View {

    private static final int NUM_COLUMNS = 7;
    private static final int NUM_ROWS = 6;
    private int mCurrentTextAlpha = 255;
    private int mOtherTextAlpha = 104;
    // 绘制 公历日期 (日程提示点) 选中日期的背景  农历日期
    private Paint mPaint, paint, lunarPaint;
    private int mDayColor = 0xFF453638;
    private int mSelectDayColor = 0xFFFFFFFF;
    private int mLunarColor = 0xFFacacac;
    private int mSelectBGColor = 0xFFDEDECC;
    private int mTodayColor = 0xFF033fff;//FFCC0000
    private int mCurrentColor = 0xFFCC0000;
    private int passColor = 0xffbbbbbb;
    private int y, m, d;//当前时间 年月日
    private int mYear, mMonth, mDay;//选中的年月日
    //列宽 行高
    private int mColumnSize, mRowSize;
    private DisplayMetrics mDisplayMetrics;
    private int mDaySize = 13;
    private int mLunarSize = 8;
    private int mCircleRadius = 4;
    private DateChangedListener dateChanged;
    private int mCircleColor = 0xFFFFBD00;
    private double lunarY = 0.36;
    private List<Integer> daysHasThingList;
    private int mRow = 5;

    //    private int ui=0;
    private int radiusCompensation;

    private MyCalendar.MC[][] calendars;
    //选中 背景的半径
    private float r;

    public MyCalendarView(Context context) {
        super(context);
        init(context);
    }

    public MyCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        Calendar calendar = Calendar.getInstance();
        y = calendar.get(Calendar.YEAR);
        m = calendar.get(Calendar.MONTH);
        d = calendar.get(Calendar.DATE);
        setSelectYearMonth(y, m, d);
        mDisplayMetrics = getResources().getDisplayMetrics();
        radiusCompensation = 12;
        mCircleRadius = 4;
        if (radiusCompensation < 8) {
            radiusCompensation = 8;
        }
        initPaint();
        paint.setStyle(Paint.Style.FILL);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//防锯齿
        mPaint.setDither(true);//防抖动
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        lunarPaint = new Paint();
        lunarPaint.setAntiAlias(true);
        lunarPaint.setDither(true);
        Typeface font = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD);
        lunarPaint.setTypeface(font);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calendars = MyCalendar.getCalendars(mYear, mMonth);
        initSize();
        mPaint.setTextSize(mDaySize * mDisplayMetrics.scaledDensity);
        mPaint.setFakeBoldText(false);//不加粗
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        lunarPaint.setTextSize(mLunarSize * mDisplayMetrics.scaledDensity);
        lunarPaint.setFakeBoldText(true);
        MyCalendar.MC mc;
        int day, row, column;
        boolean isThisMonth;
        for (int i = 0; i < 42; i++) {
            column = (i) % 7;//列
            row = (i) / 7;//行
            mc = calendars[row][column];
            day = mc.getDay();
            mRow = row;
            r = (float) MathUtils.getHypotenuse(mPaint.measureText(String.valueOf(30)) / 2, (mPaint.ascent() + mPaint.descent()) / 2) + radiusCompensation / 2;
            isThisMonth = mc.isThisMonth();
            if (!isThisMonth) {
                continue;
            }
            // mPaint.measureText(s) 获取绘制字符串s所占位置的长度  getTextBounds()获取绘制字符串s的长度
            float startX = mColumnSize * column + (mColumnSize - mPaint.measureText(String.valueOf(day))) / 2;
            //mPaint.ascent()上坡度 mPaint.descent()下坡度
            float startY = mRowSize * row + (float) (mRowSize * 0.45) - (mPaint.ascent() + mPaint.descent()) / 2;
            if (day == mDay && isThisMonth) {
                //*这是今天**********************************************************************************************************************************************************************
                if (day == d && mc.isThisMonth() && m == mMonth && y == mYear) {
                    paint.setColor(mTodayColor);
                    lunarPaint.setColor(mTodayColor);
//                    canvas.drawCircle(drawX, drawY, r, paint);
                } else {
                    if ((d <= mc.getDay() && mYear == y && mMonth >= m) || (mYear > y || (mYear == y && mMonth > m))) {//
                        lunarPaint.setColor(0xFF48aa60);
                        paint.setColor(0xFF033fff);
                    } else {
//                        lunarPaint.setColor(passColor);
//                        paint.setColor(0xFF48aa60);
                    }
                }
//                //绘制圆形背景
//                canvas.drawCircle(drawX, drawY,r, paint);
//                sy = (float) (mRowSize * row + mRowSize * 0.42 - (lunarPaint.ascent() + lunarPaint.descent()) / 2 + 1.35 * r);
            } else {
//                lunarPaint.setColor(mLunarColor);
//                sy = startY + (float) Math.ceil(fm.descent - fm.ascent) / 2 - 2;
            }
            //绘制事务圆形标志
//            drawCircle(row, column, mc, canvas, sx1, (float) (mRowSize * row + mRowSize * lunarY + Math.ceil(fm.descent - fm.ascent) * 0.8));
            float drawX, drawY;
            drawX = mColumnSize * column + mColumnSize / 2;
            drawY = mRowSize * row + (float) (mRowSize * 0.46);
            drawCircle(drawX, drawY, mc, canvas);
            mPaint.setColor(0xFF000000);
            /**
             * 设置日期颜色 选中  今天未选中 未选中
             */
//            if (day == mDay && m == mMonth && isThisMonth) {//&&m==mMonth
//                mPaint.setColor(mSelectDayColor);//选中的日期
//                lunarPaint.setColor(paint.getColor());
//            } else if (day == d && m == mMonth && y == mYear && day != mDay && isThisMonth) {
//                //选中其他日期时，设置今天的颜色
//                mPaint.setColor(mTodayColor);
//                lunarPaint.setColor(mTodayColor);
//            } else {
//                //其它未选中的日期
//                mPaint.setColor(mDayColor);
//                lunarPaint.setColor(mLunarColor);
//            }
            if (calendars[row][column].isThisMonth()) {
                mPaint.setAlpha(mCurrentTextAlpha);
                lunarPaint.setAlpha(mCurrentTextAlpha);
            } else {
                mPaint.setAlpha(mOtherTextAlpha);
                lunarPaint.setAlpha(mOtherTextAlpha);
            }
            canvas.drawText(String.valueOf(day), startX, startY, mPaint);
//            canvas.drawText(mc.getLunar(),sx,sy,lunarPaint);
        }
    }


    //绘制事件背景
    private void drawCircle(float drawX, float drawY, MyCalendar.MC mc, Canvas canvas) {
        if (daysHasThingList != null && daysHasThingList.size() > 0) {
            for (int i = 0; i < daysHasThingList.size(); i++) {
                if (mc.isThisMonth() && daysHasThingList.get(i) == mc.getDay()) {
//                    mPaint.setColor(mTodayColor);
//                    if (y==mYear&&m==mMonth&&mc.getDay()==mDay) {
//                        mPaint.setColor(Color.parseColor("#ffffff"));
//                    }
                    paint.setColor(0xFFFFF3B2);
                    canvas.drawCircle(drawX, drawY, r + 10, paint);
                    paint.setColor(0xFFFFD700);
                    canvas.drawCircle(drawX, drawY, r, paint);
                }
            }
        }
    }


    //设置农历日期显示的纵坐标
    public void setLunarY(double y) {
        lunarY = y;
        invalidate();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private int downX = 0, downY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventCode = event.getAction();
        switch (eventCode) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if (Math.abs(upX - downX) < 20 && Math.abs(upY - downY) < 20) {//点击事件
                    performClick();
//                    doClickAction( (downX+upX)/2,(downY+upY)/2);  注掉这行就屏蔽了点击事件
                }
                break;
        }
        return true;
    }


    /**
     * 初始化列宽行高
     */
    private void initSize() {
        mColumnSize = getWidth() / NUM_COLUMNS;
        mRowSize = getHeight() / NUM_ROWS;
    }

    /**
     * 设置年月
     *
     * @param year
     * @param month
     */
    public void setSelectYearMonth(int year, int month, int day) {
        if (mMonth == month && mYear == year && mDay == day) {
            return;
        }
        if (year >= 0) {
            mYear = year;
        }
        if (month >= 0) {
            mMonth = month;
        }
        if (day >= 0) {
            mDay = day;
        }
        invalidate();
        if (dateChanged == null) {
            return;
        }
        calendars = MyCalendar.getCalendars(mYear, mMonth);
        if (calendars == null || calendars.length < 6) {
            return;
        }

        int column, row;
        MyCalendar.MC mc;
        for (int i = 0; i < 42; i++) {
            column = (i) % 7;//列
            row = (i) / 7;//行
            mc = calendars[row][column];
            if (mDay == mc.getDay() && mc.isThisMonth()) {
                mRow = row;
                dateChanged.onDateChanged(mYear, mMonth, mDay, mRow);
                break;
            }
        }

    }

    /**
     * 执行点击事件
     *
     * @param x
     * @param y
     */
    private void doClickAction(int x, int y) {
        int row = y / mRowSize;
        mRow = row;
        int column = x / mColumnSize;
        if (calendars[row][column].isThisMonth()) {
            setSelectYearMonth(mYear, mMonth, calendars[row][column].getDay());
        } else {
            if (calendars[row][column].getMonth() == -1) {
                doLeftClick(calendars[row][column].getDay());
                dateChanged.otherChange(-1, mDay);
            } else {
                doRightClick(calendars[row][column].getDay());
                dateChanged.otherChange(1, mDay);
            }
        }
        //   runnable.run();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (dateChanged != null) {
                dateChanged.onDateChanged(mYear, mMonth, mDay, mRow);
            }
        }
    };

    private void doLeftClick(int day) {
        mDay = day;
        onLeftClick();

    }

    private void doRightClick(int day) {
        mDay = day;
        onRightClick();

    }

    /**
     * 左点击，日历向后翻页
     */
    public void onLeftClick() {
        int year = mYear;
        int month = mMonth;
        int day = mDay;
        if (month == 0) {//若果是1月份，则变成12月份
            year = mYear - 1;
            month = 11;
        } else {
            month = month - 1;
        }
        if (DateUtil.getMonthDays(year, month) < day) {
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            day = DateUtil.getMonthDays(year, month);
        }
        setSelectYearMonth(year, month, day);
    }

    /**
     * 右点击，日历向前翻页
     */
    public void onRightClick() {
        int year = mYear;
        int month = mMonth;
        int day = mDay;
        if (month == 11) {//若果是12月份，则变成1月份
            year = mYear + 1;
            month = 0;
        } else {
            month = month + 1;
        }
        if (DateUtil.getMonthDays(year, month) < day) {
            day = DateUtil.getMonthDays(year, month);
        }
        setSelectYearMonth(year, month, day);
    }

    /**
     * 获取选择的年份
     *
     * @return
     */
    public int getSelYear() {
        return mYear;
    }

    /**
     * 获取选择的月份
     *
     * @return
     */
    public int getSelMonth() {
        return mMonth;
    }

    /**
     * 获取选择的日期
     *
     * @param
     */
    public int getSelDay() {
        return this.mDay;
    }


    /**
     * 设置事务天数
     *
     * @param daysHasThingList
     */
    public void setDaysHasThingList(List<Integer> daysHasThingList) {
        this.daysHasThingList = daysHasThingList;
        invalidate();
//        setSelectYearMonth(mYear,mMonth, mDay);
    }


    /**
     * 设置日期的点击回调事件
     *
     * @author shiwei.deng
     */
    public interface DateChangedListener {

        /**
         * @param rows
         * @param year
         * @param month
         * @param day
         */
        void onDateChanged(int year, int month, int day, int rows);

        /**
         * 当前view的日期改变时 通知其他MyCalendarView改变日期
         *
         * @param lOR-1 left  各个日历控件跳向前一个月 1right各个日历控件跳到后一个月
         */
        void otherChange(int lOR, int day);

    }

    /**
     * 设置日期点击事件
     *
     * @param dateChanged
     */
    public void setDateChanged(DateChangedListener dateChanged) {
        this.dateChanged = dateChanged;
    }

    /**
     * 跳转至今天
     */
    public void setTodayToView() {
        setSelectYearMonth(y, m, d);
        invalidate();
    }

}
