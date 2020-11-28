package com.dykj.module.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.blankj.utilcode.util.StringUtils;
import com.dykj.module.R;
import com.dykj.module.util.MyLogger;

/**
 *
 * @author lcjing
 * @date 2018/11/29
 */

public class ConfirmDialog implements LifecycleObserver {
    private Dialog dialog;
    private Context context;
    private String msg, ok, cancel;

    public ConfirmDialog setLifecycle(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        return this;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void destroy() {
        // disconnect if connected
        MyLogger.dLog().e("Lifecycle.Event："+"ON_DESTROY");
        dismiss();
    }

    public ConfirmDialog(Context context, CallBack callBack, String msg) {
        this.callBack = callBack;
        this.context = context;
        this.msg = msg;
    }

    public ConfirmDialog(Context context, CallBack callBack, String msg, String ok) {
        this.callBack = callBack;
        this.context = context;
        this.msg = msg;
        this.ok = ok;
    }

    public ConfirmDialog( Context context, CallBack callBack,String msg, String cancel, String ok) {
        this.callBack = callBack;
        this.context = context;
        this.msg = msg;
        this.cancel = cancel;
        this.ok = ok;
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
            return;
        }
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
        TextView tvOk, tvTitle;
        TextView tvCancel;
        tvOk = dialogView.findViewById(R.id.tv_confirm);
        tvTitle = dialogView.findViewById(R.id.tv_msg);
        tvCancel = dialogView.findViewById(R.id.tv_cancel);
        if (!StringUtils.isEmpty(cancel)) {
            tvCancel.setText(cancel);
        }
        if (!StringUtils.isEmpty(msg)) {
            tvTitle.setText(msg);
            TextPaint paint = tvTitle.getPaint();
            paint.setFakeBoldText(true);
        }

        if (!StringUtils.isEmpty(ok)) {
            tvOk.setText(ok);
        }
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                dialog.dismiss();
                if (callBack != null) {
                    callBack.confirm();
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog = new Dialog(context, R.style.dialog_center);
        dialog.setCancelable(true);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        dialog.show();
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private CallBack callBack;

    public interface CallBack{
        /**
         * 确认
         */
        void confirm();
    }
}

