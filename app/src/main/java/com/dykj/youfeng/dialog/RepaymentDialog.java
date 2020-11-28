package com.dykj.youfeng.dialog;

import android.app.Activity;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.SmallRepaymentDateBean;
import com.dykj.module.util.DialogUtils;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.RequiresApi;

public class RepaymentDialog {
    private int mStatementDate;
    private int mRepaymentDate;
    private Activity mCt;
    private TextView mTvRepayTime;

    private Map<String, List<SmallRepaymentDateBean>> repaymentDateMap;

    public RepaymentDialog(int statementDate, int repaymentDay, Activity context, TextView tvRepayTime) {
        this.mStatementDate = statementDate;
        this.mRepaymentDate = repaymentDay;
        this.mCt = context;
        this.mTvRepayTime = tvRepayTime;
    }

    public void showDialog() {
        DialogUtils.getInstance()
                .with(mCt)
                .setlayoutPosition(Gravity.CENTER)
                .setlayoutPading(140, 0, 140, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FADE_IN)
                .setlayoutId(R.layout.dialog_small_repayment_date)
                .setOnChildViewclickListener(new DialogUtils.ViewInterface() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void getChildView(View view, int layoutResId) {


                        LinearLayout llDateParent = view.findViewById(R.id.ll_date_parent);


                        fillRepaymentTime(mStatementDate, mRepaymentDate, llDateParent);


                        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                DialogUtils.dismiss();
                            }
                        });
                        view.findViewById(R.id.tv_safety_quit).setOnClickListener(v -> {
                            if (repaymentDateMap != null) {
                                StringBuilder stringBuffer = new StringBuilder();
                                if (repaymentDateMap.get("01") != null && repaymentDateMap.get("12") != null) {
                                    for (SmallRepaymentDateBean dateBean : Objects.requireNonNull(repaymentDateMap.get("12"))) {
                                        dateBean.setCheck(dateBean.isClickCheck());
                                        if (dateBean.isCheck()) {
                                            stringBuffer.append(dateBean.getYear())
                                                    .append("-").append(dateBean.getMonth())
                                                    .append("-").append(dateBean.getDay()).append(",");
                                        }
                                    }
                                    for (SmallRepaymentDateBean dateBean : Objects.requireNonNull(repaymentDateMap.get("01"))) {
                                        dateBean.setCheck(dateBean.isClickCheck());
                                        if (dateBean.isCheck()) {
                                            stringBuffer.append(dateBean.getYear())
                                                    .append("-").append(dateBean.getMonth())
                                                    .append("-").append(dateBean.getDay()).append(",");
                                        }

                                    }

                                } else {
                                    for (String next : repaymentDateMap.keySet()) {
                                        for (SmallRepaymentDateBean dateBean : Objects.requireNonNull(repaymentDateMap.get(next))) {
                                            dateBean.setCheck(dateBean.isClickCheck());
                                            if (dateBean.isCheck()) {
                                                stringBuffer.append(dateBean.getYear())
                                                        .append("-").append(dateBean.getMonth())
                                                        .append("-").append(dateBean.getDay()).append(",");
                                            }
                                        }
                                    }
                                }


                                if (stringBuffer.length() > 1) {
                                    String substring = stringBuffer.substring(0, stringBuffer.length() - 1);
                                    mTvRepayTime.setText(substring);
                                } else {
                                    mTvRepayTime.setText("");
                                }
                            }

                            DialogUtils.dismiss();
                        });
                    }
                })
                .show();
    }


    /**
     * 填充还款时间
     *
     * @param statementDate
     * @param repaymentDate
     * @param llParent
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void fillRepaymentTime(int statementDate, int repaymentDate, LinearLayout llParent) {
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_MONTH);


        if (statementDate < repaymentDate && repaymentDate < today) {//账单日<还款日<今天  下月


            calendar.roll(Calendar.MONTH, true);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            if (repaymentDateMap == null) {
                repaymentDateMap = new HashMap<>();


                repaymentDateMap.put(month < 10 ? "0" + month : "" + month,
                        getRepaymentDateList(month, year,
                                maxDays, statementDate,
                                repaymentDate));


            }


        } else if ((statementDate <= today && statementDate < repaymentDate)
                || (today <= statementDate && statementDate < repaymentDate)) {//账单日<=今天<还款日||今天<=账单日<还款日  本月

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);


            if (repaymentDateMap == null) {
                repaymentDateMap = new HashMap<>();

                repaymentDateMap.put(month < 10 ? "0" + month : "" + month, getRepaymentDateList(month,
                        year,
                        maxDays,
                        today <= statementDate ? statementDate : today,
                        repaymentDate));

            }
        } else if (statementDate > repaymentDate && today < repaymentDate) {

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);


            if (repaymentDateMap == null) {
                repaymentDateMap = new HashMap<>();

                repaymentDateMap.put(month < 10 ? "0" + month : "" + month, getRepaymentDateList(month,
                        year,
                        maxDays,
                        today,
                        repaymentDate));

            }
        } else if (repaymentDate < today && today < statementDate) {
            if (repaymentDateMap == null) {
                repaymentDateMap = new HashMap<>();
                //本月
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

                repaymentDateMap.put(month < 10 ? "0" + month : "" + month, getRepaymentDateList(month,
                        year,
                        maxDays,
                        statementDate,
                        maxDays));

                //下月
                calendar.roll(Calendar.MONTH, true);
                int nextYear = calendar.get(Calendar.YEAR);
                if (month == 12 && nextYear == year) {
                    nextYear = year + 1;
                }
                int nextMonth = calendar.get(Calendar.MONTH) + 1;
                int nextMaxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                repaymentDateMap.put(nextMonth < 10 ? "0" + nextMonth : "" + nextMonth, getRepaymentDateList(nextMonth,
                        nextYear,
                        nextMaxDays,
                        1,
                        repaymentDate));

            }
        } else {//本+下月


            if (repaymentDateMap == null) {
                repaymentDateMap = new HashMap<>();
                //本月
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

                repaymentDateMap.put(month < 10 ? "0" + month : "" + month, getRepaymentDateList(month,
                        year,
                        maxDays,
                        today <= statementDate ? statementDate : today,
                        maxDays));

                //下月
                calendar.roll(Calendar.MONTH, true);
                int nextYear = calendar.get(Calendar.YEAR);
                if (month == 12 && nextYear == year) {
                    nextYear = year + 1;
                }
                int nextMonth = calendar.get(Calendar.MONTH) + 1;
                int nextMaxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                repaymentDateMap.put(nextMonth < 10 ? "0" + nextMonth : "" + nextMonth, getRepaymentDateList(nextMonth,
                        nextYear,
                        nextMaxDays,
                        1,
                        repaymentDate));

            }
        }


        if (repaymentDateMap != null) {
            if (repaymentDateMap.get("01") != null && repaymentDateMap.get("12") != null) {
                llParent.addView(getRepaymentDayView("12", Objects.requireNonNull(repaymentDateMap.get("12"))));
                llParent.addView(getRepaymentDayView("01", Objects.requireNonNull(repaymentDateMap.get("01"))));
            } else {
                Iterator<String> month = repaymentDateMap.keySet().iterator();
                while (month.hasNext()) {
                    String next = month.next();
                    llParent.addView(getRepaymentDayView(next, Objects.requireNonNull(repaymentDateMap.get(next))));
                }

            }

        }


    }

    /**
     * 获取日期列表
     *
     * @param month
     * @param year
     * @param maxDays
     * @param startDay
     * @param endDay
     * @return
     */
    private List<SmallRepaymentDateBean> getRepaymentDateList(int month, int year, int maxDays, int startDay, int endDay) {
        List<SmallRepaymentDateBean> dateBeans = new ArrayList<>();
        for (int i = 1; i < maxDays + 1; i++) {

            if ((startDay != 1 && i <= startDay) || i > endDay) {
            } else {
                SmallRepaymentDateBean date = new SmallRepaymentDateBean();
                date.setCheck(false);
                date.setClickCheck(false);
                date.setYear(year + "");
                date.setMonth(month + "");
                if (i < 10) {
                    date.setDay("0" + i);
                } else {
                    date.setDay(i + "");
                }
                date.setCantCheck(false);
                dateBeans.add(date);
            }
        }


        int size = dateBeans.size();
        if (size < 7) {
            for (int i = size; i < 7; i++) {
                SmallRepaymentDateBean date = new SmallRepaymentDateBean();
                date.setCheck(false);
                date.setClickCheck(false);
                date.setYear(year + "");
                date.setMonth(month + "");
                if (i < 10) {
                    date.setDay("0" + i);
                } else {
                    date.setDay(i + "");
                }
                date.setCantCheck(true);
                dateBeans.add(date);
            }
        }

        return dateBeans;
    }


    /**
     * 获取天数的View
     *
     * @param month     月份
     * @param dateBeans 日期集合
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private View getRepaymentDayView(String month, List<SmallRepaymentDateBean> dateBeans) {

        View itemView = LayoutInflater.from(mCt).inflate(R.layout.dialog_small_repayment_date_item, null);
        TextView tvMonth = itemView.findViewById(R.id.tv_month);
        tvMonth.setText(month + "月");
        GridLayout glDay = itemView.findViewById(R.id.gl_day);


        for (int i = 0; i < dateBeans.size(); i++) {

            SmallRepaymentDateBean dateBean = dateBeans.get(i);

            View dayView = LayoutInflater.from(mCt).inflate(R.layout.dialog_small_repayment_date_day, null);

            TextView textView = dayView.findViewById(R.id.tv_day);
            FrameLayout flDay = dayView.findViewById(R.id.fl_day);
            textView.setText(dateBean.getDay());
            if (dateBean.isCantCheck()) {
                textView.setVisibility(View.INVISIBLE);
            }

            if (dateBean.isCheck()) {
                textView.setBackground(mCt.getResources().getDrawable(R.drawable.circle_shape3));
                textView.setTextColor(mCt.getResources().getColor(R.color.white));
            } else {
                textView.setBackgroundColor(mCt.getResources().getColor(R.color.white));
                if (dateBean.isCantCheck()) {
                    textView.setTextColor(0xff666666);
                } else {

                    textView.setTextColor(0xff333333);
                }
            }

            glDay.addView(dayView);

            DisplayMetrics outMetrics = new DisplayMetrics();
            Display defaultDisplay = mCt.getWindowManager().getDefaultDisplay();
            defaultDisplay.getMetrics(outMetrics);

            if (outMetrics.widthPixels > 540) {

                Log.d("wys", outMetrics.widthPixels + "");
                int row = i / 7 + 1;
                int column = i;
                if (row > 1) {
                    column = i - (row - 1) * 7;
                }

                GridLayout.LayoutParams lp = (GridLayout.LayoutParams) flDay.getLayoutParams();
                GridLayout.Spec rowSpec = GridLayout.spec(row, 1.0f);
                GridLayout.Spec columnSpec = GridLayout.spec(column, 1.0f);
                lp.rowSpec = rowSpec;
                lp.columnSpec = columnSpec;
//                lp.leftMargin = DensityUtil.dp2px(12);
                lp.bottomMargin = DensityUtil.dp2px(17);
            } else {
                glDay.setColumnCount(5);

            }

            textView.setTag(dateBean);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SmallRepaymentDateBean tag = (SmallRepaymentDateBean) v.getTag();
                    if (!tag.isCantCheck()) {
                        tag.setClickCheck(!tag.isClickCheck());
                        if (tag.isClickCheck()) {
                            ((TextView) v).setBackground(mCt.getDrawable(R.drawable.circle_shape3));
                            ((TextView) v).setTextColor(mCt.getResources().getColor(R.color.white));
                        } else {
                            ((TextView) v).setBackgroundColor(mCt.getResources().getColor(R.color.white));
                            if (tag.isCantCheck()) {
                                ((TextView) v).setTextColor(0xff666666);
                            } else {
                                ((TextView) v).setTextColor(0xff333333);
                            }
                        }

                    }
                }
            });


        }

        return itemView;
    }

}
