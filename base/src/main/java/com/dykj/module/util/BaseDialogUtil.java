package com.dykj.module.util;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.dykj.module.R;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;
import static android.app.AlertDialog.THEME_HOLO_LIGHT;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * @author WZ
 * @date 2019/5/16.
 * 严格要求自己，对每一行代码负责
 * description：
 */
public class BaseDialogUtil {
    private static ProgressDialog sProgressDialog = null;
    private static final int DEFAULT_COLOR = R.color.main;

    public BaseDialogUtil() {
    }

    public static void showProgressDialog(Context context, Object message) {
        if (context != null) {
            WeakReference<Context> wf = new WeakReference(context);
            String content = "正在加载...";
            if (message != null && message instanceof Integer) {
                content = ((Context) wf.get()).getString((Integer) message);
            } else if (message != null && message instanceof String && !BaseStringUtil.isEmpty((String) message)) {
                content = (String) message;
            }

            if (sProgressDialog == null) {
                sProgressDialog = new ProgressDialog((Context) wf.get(), 3);
                sProgressDialog.setCanceledOnTouchOutside(false);
                sProgressDialog.setCancelable(false);
                sProgressDialog.setMessage(content);
            }

            sProgressDialog.setMessage(content);

            try {
                sProgressDialog.show();
            } catch (Exception var5) {
                ;
            }

        }
    }

    public static void dismissProgressDialog() {
        try {
            if (sProgressDialog != null) {
                sProgressDialog.dismiss();
                sProgressDialog = null;
            }
        } catch (Exception var1) {
            var1.printStackTrace();
        }

    }

    private static AlertDialog.Builder getAlertDialog(Context context) {
        WeakReference<Context> wf = new WeakReference(context);
        return VERSION.SDK_INT >= 11 ? new AlertDialog.Builder((Context) wf.get(), THEME_DEVICE_DEFAULT_LIGHT) : new AlertDialog.Builder((Context) wf.get());
    }

    public static void getInputDialog(Context context, int colorRes, View view, String title, DialogInterface.OnClickListener confirm, DialogInterface.OnClickListener cancel) {
        AlertDialog builder = getAlertDialog(context).setView(view).setTitle(title).setPositiveButton("确定", confirm).setNegativeButton("取消", cancel).create();
        builder.show();
        setDialogTitleColor(builder, colorRes);
    }

    public static void getInputDialog(Context context, View view, String title, DialogInterface.OnClickListener confirm, DialogInterface.OnClickListener cancel) {
        AlertDialog builder = getAlertDialog(context).setView(view).setTitle(title).setPositiveButton("确定", confirm).setNegativeButton("取消", cancel).create();
        builder.show();
        setDialogTitleColor(builder, DEFAULT_COLOR);
    }

    public static void choiceTime(Context context, int colorRes, String date, OnDateSetListener dateSetListener) {
        Calendar c = Calendar.getInstance();
        if (!TextUtils.isEmpty(date)) {
            c.setTime(strToDate("yyyy-MM-dd", date));
        }

        DatePickerDialog dialog = new DatePickerDialog(context, THEME_HOLO_LIGHT, dateSetListener, c.get(YEAR), c.get(MONTH), c.get(DAY_OF_MONTH));
        dialog.show();
        setDialogTitleColor(dialog, colorRes);
        setPickerDividerColor(dialog.getDatePicker(), colorRes);
    }

    public static void choiceTime(Context context, String date, OnDateSetListener dateSetListener) {
        Calendar c = Calendar.getInstance();
        if (!TextUtils.isEmpty(date)) {
            c.setTime(strToDate("yyyy-MM-dd", date));
        }

        DatePickerDialog dialog = new DatePickerDialog(context, THEME_HOLO_LIGHT, dateSetListener,c.get(YEAR), c.get(MONTH), c.get(DAY_OF_MONTH));
        dialog.show();
        setDialogTitleColor(dialog, DEFAULT_COLOR);
        setPickerDividerColor(dialog.getDatePicker(), DEFAULT_COLOR);
    }

    public static void choiceTime(Context context, int colorRes, String date, DialogInterface.OnClickListener confirm, DialogInterface.OnClickListener cancel) {
        Calendar c = Calendar.getInstance();
        if (!TextUtils.isEmpty(date)) {
            c.setTime(strToDate("yyyy-MM-dd", date));
        }

        DatePickerDialog dialog = new DatePickerDialog(context, THEME_HOLO_LIGHT, (OnDateSetListener) null, c.get(YEAR), c.get(MONTH), c.get(DAY_OF_MONTH));
        dialog.setButton(-1, "确认", confirm);
        dialog.setButton(-2, "取消", cancel);
        dialog.show();
        setDialogTitleColor(dialog, colorRes);
        setPickerDividerColor(dialog.getDatePicker(), colorRes);
    }

    public static void choiceTime(Context context, String date, DialogInterface.OnClickListener confirm, DialogInterface.OnClickListener cancel) {
        Calendar c = Calendar.getInstance();
        if (!TextUtils.isEmpty(date)) {
            c.setTime(strToDate("yyyy-MM-dd", date));
        }

        DatePickerDialog dialog = new DatePickerDialog(context, THEME_HOLO_LIGHT, (OnDateSetListener) null, c.get(YEAR), c.get(MONTH), c.get(DAY_OF_MONTH));
        dialog.setButton(-1, "确认", confirm);
        dialog.setButton(-2, "取消", cancel);
        dialog.show();
        setDialogTitleColor(dialog, DEFAULT_COLOR);
        setPickerDividerColor(dialog.getDatePicker(), DEFAULT_COLOR);
    }

    public static void choiceYear(Context context, int colorRes, String date, OnDateSetListener dateSetListener) {
        Calendar c = Calendar.getInstance();
        if (!TextUtils.isEmpty(date)) {
            c.setTime(strToDate("yyyy", date));
        }

        DatePickerDialog dialog = new DatePickerDialog(context, THEME_HOLO_LIGHT, dateSetListener, c.get(YEAR), c.get(MONTH), c.get(DAY_OF_MONTH));
        dialog.show();
        DatePicker dp = findDatePicker((ViewGroup) dialog.getWindow().getDecorView());
        if (dp != null) {
            ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
            ((ViewGroup) ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))).getChildAt(1).setVisibility(View.GONE);
        }

        setDialogTitleColor(dialog, colorRes);
        setPickerDividerColor(dialog.getDatePicker(), colorRes);
    }

    public static void choiceYear(Context context, String date, OnDateSetListener dateSetListener) {
        Calendar c = Calendar.getInstance();
        if (!TextUtils.isEmpty(date)) {
            c.setTime(strToDate("yyyy", date));
        }

        DatePickerDialog dialog = new DatePickerDialog(context, THEME_HOLO_LIGHT, dateSetListener, c.get(YEAR), c.get(MONTH), c.get(DAY_OF_MONTH));
        dialog.show();
        DatePicker dp = findDatePicker((ViewGroup) dialog.getWindow().getDecorView());
        if (dp != null) {
            ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
            ((ViewGroup) ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))).getChildAt(1).setVisibility(View.GONE);
        }

        setDialogTitleColor(dialog, DEFAULT_COLOR);
        setPickerDividerColor(dialog.getDatePicker(), DEFAULT_COLOR);
    }

    public static void getDialogItem(Context context, int colorRes, String title, String[] items, DialogInterface.OnClickListener listener) {
        AlertDialog builder = getAlertDialog(context).setItems(items, listener).setTitle(title).create();
        builder.show();
        setDialogTitleColor(builder, colorRes);
    }

    public static void getDialogItem(Context context, String title, String[] items, DialogInterface.OnClickListener listener) {
        AlertDialog builder = getAlertDialog(context).setItems(items, listener).setTitle(title).create();
        builder.show();
        setDialogTitleColor(builder, DEFAULT_COLOR);
    }

    public static void showDialog(Context context, int colorRes, String title, String content, DialogInterface.OnClickListener confirmListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog builder = getAlertDialog(context).setNegativeButton("取消", cancelListener).setPositiveButton("确认", confirmListener).setTitle(title).setMessage(content).create();
        builder.show();
        setDialogTitleColor(builder, colorRes);
    }

    public static void showDialog(Context context, String title, String content, DialogInterface.OnClickListener confirmListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog builder = getAlertDialog(context).setNegativeButton("取消", cancelListener).setPositiveButton("确认", confirmListener).setTitle(title).setMessage(content).create();
        builder.show();
        setDialogTitleColor(builder, DEFAULT_COLOR);
    }

    public static void showDialog(Context context, int colorRes, String title, String content) {
        AlertDialog builder = getAlertDialog(context).setTitle(title).setMessage(content).create();
        builder.show();
        setDialogTitleColor(builder, colorRes);
    }

    public static void showDialog(Context context, String title, String content) {
        AlertDialog builder = getAlertDialog(context).setTitle(title).setMessage(content).create();
        builder.show();
        setDialogTitleColor(builder, DEFAULT_COLOR);
    }

    private static void setPickerDividerColor(DatePicker datePicker, int color) {
        LinearLayout llFirst = (LinearLayout) datePicker.getChildAt(0);
        LinearLayout mSpinners = (LinearLayout) llFirst.getChildAt(0);

        for (int i = 0; i < mSpinners.getChildCount(); ++i) {
            NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);
            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            Field[] var7 = pickerFields;
            int var8 = pickerFields.length;

            for (int var9 = 0; var9 < var8; ++var9) {
                Field pf = var7[var9];
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true);

                    try {
                        pf.set(picker, new ColorDrawable(datePicker.getContext().getResources().getColor(color)));
                    } catch (Exception var12) {
                        var12.printStackTrace();
                    }
                    break;
                }
            }
        }

    }

    private static Date strToDate(String style, String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);

        try {
            return formatter.parse(date);
        } catch (ParseException var4) {
            var4.printStackTrace();
            return new Date();
        }
    }

    private static DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            int i = 0;

            for (int j = group.getChildCount(); i < j; ++i) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                }

                if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }

        return null;
    }

    private static void setDialogTitleColor(Dialog dialog, int color) {
        try {
            Context context = dialog.getContext();
            int dividerId = context.getResources().getIdentifier("android:id/titleDivider", (String) null, (String) null);
            View divider = dialog.findViewById(dividerId);
            divider.setBackgroundColor(context.getResources().getColor(color));
            int alertTitleId = context.getResources().getIdentifier("alertTitle", "id", "android");
            TextView alertTitle = (TextView) dialog.findViewById(alertTitleId);
            alertTitle.setTextColor(context.getResources().getColor(color));
        } catch (Exception var7) {
            ;
        }

    }
}
