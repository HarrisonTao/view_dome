package com.dykj.module.view.dialog;


import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dykj.module.R;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.date.DateFormatUtil;
import com.dykj.module.util.date.DateUtil;
import com.dykj.module.util.date.MyCalendar;
import com.dykj.module.util.toast.DyToast;
import com.dykj.module.view.bean.DateBean;
import com.dykj.module.view.dialog.adapter.DateAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lcjing on 2019/3/20.
 */

public class RepaymentCalendarDialog extends BaseDialog {

    private CallBack callBack;
    private List<DateBean> list;
    private DateAdapter adapter;
    private int statementDate, repaymentDate;
    private FragmentActivity context;
    //是否是开始时间
    private boolean isStart;

    public RepaymentCalendarDialog(FragmentActivity activity, CallBack callBack
            , String startDate, String endDate, boolean isStart, int statementDate, int repaymentDate) {
        this.context = activity;
        this.callBack = callBack;
        this.isStart = isStart;
        if (isStart) {
            this.startDate = 0;
            this.endDate = DateFormatUtil.getDateTime(endDate);
        } else {
            this.startDate = DateFormatUtil.getDateTime(startDate);
            this.endDate = 0;
        }
        this.statementDate = statementDate;
        this.repaymentDate = repaymentDate;
    }

    private boolean canReset = false;

    public RepaymentCalendarDialog setCanReset(boolean canReset) {
        this.canReset = canReset;
        return this;
    }

    private String data;


    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        nowDate = DateFormatUtil.getDateTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, mDay);
        //这个月的最后一天
        int lastDay = DateUtil.getMonthDays(mYear, mMonth);
        if (lastDay < repaymentDate) {
            repaymentDate = lastDay;
        }
        if (lastDay < statementDate) {
            statementDate = lastDay;
        }
        int nextDay = mDay == lastDay ? 1 : mDay + 1;
        if ((repaymentDate == mDay || repaymentDate == nextDay)) {//&&isInit
            ToastUtils.showShort("还款日以及还款日前一天不能制定计划！");
            return;
        }

        stateDate = DateFormatUtil.getDateTime(mYear, mMonth + 1, statementDate);
        //2种情况  还款日大于当前日期 并且大于账单日  取本月 否则取下月
        if (mDay < repaymentDate && statementDate < repaymentDate) {
            repayDate = DateFormatUtil.getDateTime(mYear, mMonth + 1, repaymentDate);
        //1种情况
        } else if (mDay < repaymentDate && repaymentDate < statementDate) {
            stateDate = getLastMonthDate(statementDate);
            repayDate = DateFormatUtil.getDateTime(mYear, mMonth + 1, repaymentDate);
            //3 种情况
        } else {
            int nextMonth, nextYear;
            if (mMonth == 11) {
                nextMonth = 0;
                nextYear = mYear + 1;
            } else {
                nextMonth = mMonth + 1;
                nextYear = mYear;
            }
            lastDay = DateUtil.getMonthDays(nextYear, nextMonth);
            if (lastDay < repaymentDate) {
                repaymentDate = lastDay;
            }
            if (lastDay < statementDate) {
                statementDate = lastDay;
            }
            //1
            if (statementDate < repaymentDate) {
                stateDate = DateFormatUtil.getDateTime(nextYear, nextMonth + 1, statementDate);
            }
            repayDate = DateFormatUtil.getDateTime(nextYear, nextMonth + 1, repaymentDate);
        }

        DateFormatUtil.lastDayOfMonth().getTime();
        int month = mMonth + 1;
        if (tvDate != null) {
            tvDate.setText(mYear + "年" + month + "月 ");
        }
        getDate(true);
    }

    private TextView tvDate;

    private long getLastMonthDate(int day) {
        int lastMonth, lastYear;
        if (mMonth == 0) {
            lastMonth = 11;
            lastYear = mYear - 1;
        } else {
            lastMonth = mMonth - 1;
            lastYear = mYear;
        }
        return DateFormatUtil.getDateTime(lastYear, lastMonth + 1, day);
    }


    @Override
    public void show() {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_repayment_calendar, null);
        ImageView ivLast = dialogView.findViewById(R.id.iv_last);
        ImageView ivNext = dialogView.findViewById(R.id.iv_next);
        tvDate = dialogView.findViewById(R.id.tv_date);
        dialogView.findViewById(R.id.tv_cancel).setOnClickListener(v -> dismiss());
        dialogView.findViewById(R.id.tv_ok).setOnClickListener(v -> {
            if (StringUtils.isEmpty(data)) {
                ToastUtils.showShort("请先选择日期");
                return;
            }
            callBack.select(data);
            dismiss();
        });
        if (canReset) {
            dialogView.findViewById(R.id.tv_reset).setOnClickListener(view -> {
                callBack.select("");
                startDate = 0;
                endDate = 0;
                mYear = Calendar.getInstance().get(Calendar.YEAR);
                mMonth = Calendar.getInstance().get(Calendar.MONTH);
                mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                initDate();
            });
        }else {
            dialogView.findViewById(R.id.tv_reset).setVisibility(View.GONE);
        }
        RecyclerView recyclerView = dialogView.findViewById(R.id.rv_calendar);
        list = new ArrayList<>();
        adapter = new DateAdapter(list);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            if (!list.get(position).isEnable()) {
                return;
            }
            if (!list.get(position).isClickable()) {
                DyToast.getInstance().warning(isStart ? "不能在最后一天才开始哦~" : "不能在第一天就结束哦~");
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setSelect(i == position);
            }
            adapter.notifyDataSetChanged();
            data = list.get(position).getDate();
        });
        recyclerView.setLayoutManager(new GridLayoutManager(context, 7));
        recyclerView.setAdapter(adapter);
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        initDate();
        ivLast.setOnClickListener(v -> onLeftClick());
        ivNext.setOnClickListener(v -> onRightClick());

        dialog = new Dialog(context, R.style.dialog_center);
        dialog.setCancelable(true);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private int mYear, mMonth, mDay;

    /**
     * 左点击，日历向后翻页
     */
    private void onLeftClick() {
        int year = mYear;
        int month = mMonth;
        int day = mDay;
        //若果是1月份，则变成12月份
        if (month == 0) {
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

    //分别是开始时间/结束时间/账单日/还款日/今天  对应的时间戳
    private long startDate, endDate, stateDate, repayDate, nowDate;


    /**
     * 右点击，日历向前翻页
     */
    private void onRightClick() {
        int year = mYear;
        int month = mMonth;
        int day = mDay;
        //若果是12月份，则变成1月份
        if (month == 11) {
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

    private void setSelectYearMonth(int year, int month, int day) {
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
        getDate(false);
    }


    private void getDate(boolean isInit) {
        list.clear();
        list.addAll(MyCalendar.getCalendarLists(mYear, mMonth));
        MyLogger.dLog().e("RepaymentCalendarDialog" + mYear + "-" + mMonth);
        long start;
        if (nowDate > stateDate) {
            start = startDate > 0 ? startDate : nowDate;
        } else {
            start = startDate > 0 ? startDate : stateDate - 1;
        }

        boolean hasEnable = false;
        for (DateBean dateBean : list) {
            if (endDate > 0) {
                dateBean.setEnable(dateBean.getDateTime() > start && dateBean.getDateTime() < endDate);
            } else {
                dateBean.setEnable(dateBean.getDateTime() > start && dateBean.getDateTime() <= repayDate);
            }
            if (dateBean.isEnable() && dateBean.isShow()) {
                hasEnable = true;
            }
            if (isStart) {
                //还款日为初始化时间的最后一天、该天不能选为开始时间
                dateBean.setClickable(dateBean.getDateTime() != repayDate);
            } else {
                //明天或账单日对应的时间戳大的为初始化时间的第一天、该天不能选为结束时间
                dateBean.setClickable(nowDate > stateDate ? dateBean.getDateTime() - nowDate != 24 * 60 * 60 * 1000 : dateBean.getDateTime() != stateDate);
            }
        }
        if (!hasEnable && isInit) {
            onRightClick();
            return;
        }
        adapter.notifyDataSetChanged();
        if (tvDate != null) {
            int month = mMonth + 1;
            tvDate.setText(mYear + "年" + month + "月 ");
        }
    }

    public interface CallBack {
        void select(String date);
    }
}
