package com.dykj.module.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dykj.module.R;
import com.dykj.module.util.date.DateUtil;
import com.dykj.module.view.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lcjingon 2019/10/16.
 * description:
 */

public class SelectDateScrollDialog extends BaseDialog {

    private Context context;
    private String title = "";
    private SelectDateBack callBack;
    private List<String> yearList, moneyList, dayList;
    private int year, month, day;
    private String strYear="", strMoney="", strDay="";
    private View llDay;
    private WheelView wvMonth, wvDay, wvYear;

    public SelectDateScrollDialog(Context context, String title, SelectDateBack callBack) {
        this.context = context;
        this.title = title;
        this.callBack = callBack;
        yearList = new ArrayList<>();
        moneyList = new ArrayList<>();
        dayList = new ArrayList<>();
    }

    public SelectDateScrollDialog(Context context, String title) {
        this.context = context;
        this.title = title;
        yearList = new ArrayList<>();
        moneyList = new ArrayList<>();
        dayList = new ArrayList<>();
    }

    public void setCallBack(SelectDateBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void show() {
        if (dialog != null) {
            dialog.show();
            return;
        }
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_wheelview_date, null);
        View llContent;
        TextView tvTitle;
        TextView tvConfirm, tvCancel;
        llContent = dialogView.findViewById(R.id.llContent);
        tvTitle = dialogView.findViewById(R.id.tvTitle);
        tvCancel = dialogView.findViewById(R.id.tv_cancel);
        tvConfirm = dialogView.findViewById(R.id.tvConfirm);
        wvYear = dialogView.findViewById(R.id.wv_year);
        wvMonth = dialogView.findViewById(R.id.wv_month);
        wvDay = dialogView.findViewById(R.id.wv_day);
        llDay=dialogView.findViewById(R.id.ll_day);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DATE);
        moneyList.add("");
        dayList.add("");
        llDay.setVisibility(isSelectDay ? View.VISIBLE : View.GONE);
        tvTitle.setText(title);
        tvCancel.setOnClickListener(v -> dismiss());
        tvConfirm.setOnClickListener(v -> {
            if (callBack != null) {
                if (strMoney.length() == 1) {
                    strMoney = "0" + strMoney;
                }
                if (strDay.length() == 1) {
                    strDay = "0" + strDay;
                }
                callBack.select(strYear, strMoney, strDay);
            }
            dismiss();
        });
        llContent.setOnClickListener(v -> dismiss());

        final int p = 2;
        wvYear.setOffset(p);
        wvYear.setItems(yearList);
        wvYear.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                moneyList.clear();
                dayList.clear();
                year = Integer.parseInt(item);
                strYear = item;
                getMonth(month - 1);
            }
        });
        wvMonth.setOffset(p);
        wvMonth.setItems(moneyList);
        wvMonth.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                if (item.length()<1) {
                    item=month+"";
                }
                strMoney = item;
                dayList.clear();
                month = Integer.parseInt(item);
                getDay(year, month, day);
            }
        });
        wvDay.setOffset(p);
        wvDay.setItems(dayList);
        wvDay.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                if (item.length()<1) {
                    item=day+"";
                }
                strDay = item;
                day = Integer.parseInt(item);
                if (day > dayList.size()) {
                    day = dayList.size() - 1;
                    strDay = String.valueOf(day);
                }
            }
        });
        dialog = new Dialog(context, R.style.dialog_bottom);
        dialog.setCancelable(true);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        dialog.show();
        getYear(year);
    }

    public boolean isShow() {
        if (dialog != null) {
            return dialog.isShowing();
        } else {
            return false;
        }
    }

    private void getYear(int selectTime) {
        yearList.clear();
        int index = 0;
        for (int i = 0; i < 100; i++) {
            int year = 2000 + i;
            if (year == selectTime) {
                index = i;
            }
            yearList.add("" + year);
        }
        wvYear.setItems(yearList);
        strYear = yearList.get(index);
        wvYear.setSeletion(index);
        handler.sendEmptyMessageDelayed(1, 60);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    getMonth(month - 1);
                    break;
                case 2:
                    getDay(year, month, day);
                    break;
            }
        }
    };

    private void getMonth(int selectMoney) {
        if (selectMoney < 0) {
            selectMoney = 0;
        }
        moneyList.clear();
        moneyList = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            moneyList.add("" + i);
        }
        strMoney = moneyList.get(selectMoney);
        wvMonth.setItems(moneyList);
        wvMonth.setSeletion(selectMoney);
        if (isSelectDay) {
            handler.sendEmptyMessageDelayed(2, 60);
        }
    }

    private void getDay(int year, int month, int selectDay) {
        dayList.clear();
        dayList = new ArrayList<>();
        for (int i = 1; i <= DateUtil.getMaxDay(year, month); i++) {
            dayList.add("" + i);
        }
        wvDay.setItems(dayList);
        if (day > dayList.size()) {
            day = dayList.size();
            selectDay = dayList.size();
        }
        strDay = dayList.get(selectDay - 1);

        wvDay.setSeletion(selectDay - 1);
    }

    //是否选择天
    private boolean isSelectDay = false;

    public SelectDateScrollDialog setSelectDay(boolean selectDay) {
        isSelectDay = selectDay;
        if (llDay!=null) {
            llDay.setVisibility(isSelectDay ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public interface SelectDateBack {
        //回调
        void select(String year, String money, String day);
    }

}
