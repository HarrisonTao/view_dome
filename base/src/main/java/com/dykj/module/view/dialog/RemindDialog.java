package com.dykj.module.view.dialog;

import android.app.Dialog;
import android.content.Context;
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
 * @author lcjing
 * @date 2018/11/29
 */

public class RemindDialog implements LifecycleObserver {
    private Dialog dialog;
    private Context context;
    private String msg, ok;


    public RemindDialog setLifecycle(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        return this;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void destroy() {
        // disconnect if connected
        MyLogger.dLog().e("Lifecycle.Event：" + "ON_DESTROY");
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    public RemindDialog(Context context, String msg) {
        this.context = context;
        this.msg = msg;
    }

    public RemindDialog(Context context, String msg, String ok) {
        this.context = context;
        this.msg = msg;
        this.ok = ok;
    }

    public RemindDialog setCallBack(CallBack callBack) {
        this.callBack = callBack;
        return this;
    }
    public RemindDialog setCancel( boolean cancel) {
        this.cancel = cancel;
        return this;
    }
    private boolean cancel=true;

    private TextView tvMsg;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int colorSource = R.color.font_ff33;

    public void setColorSource(int colorSource) {
        this.colorSource = colorSource;
    }

    public void show() {
        if (tvMsg != null && !StringUtils.isEmpty(msg)) {
            tvMsg.setText(msg);
            tvMsg.setTextColor(context.getResources().getColor(colorSource));
            if (colorSource == R.color.font_ff33) {
                TextPaint paint = tvMsg.getPaint();
                paint.setFakeBoldText(true);
            }
        }
        if (dialog != null) {
            dialog.show();
            return;
        }
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_remind, null);
        TextView tvOk;
        tvOk = dialogView.findViewById(R.id.tv_ok);
        tvMsg = dialogView.findViewById(R.id.tv_msg);
        tvMsg.getPaint().setFakeBoldText(true);
        tvMsg.setTextColor(context.getResources().getColor(colorSource));
        if (!StringUtils.isEmpty(msg)) {
            tvMsg.setText(msg);
//            TextPaint paint = tvTitle.getPaint();
//            paint.setFakeBoldText(true);
        }

        if (!StringUtils.isEmpty(ok)) {
            tvOk.setText(ok);
        }
        tvOk.setOnClickListener(view -> {
            dialog.cancel();
            dialog.dismiss();
            if (callBack != null) {
                callBack.confirm();
            }
        });

        dialog = new Dialog(context, R.style.dialog_center);
        dialog.setCancelable(cancel);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(cancel);
        dialog.show();
    }

    private CallBack callBack;


    public interface CallBack {
        /**
         * 确认
         */
        void confirm();
    }

}

