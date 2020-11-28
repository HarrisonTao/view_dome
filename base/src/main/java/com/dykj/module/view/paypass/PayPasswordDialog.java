package com.dykj.module.view.paypass;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.dykj.module.R;
import com.dykj.module.util.MyLogger;

/**
 * Created by Administrator on 2018/3/19.
 */

public class PayPasswordDialog extends Dialog implements View.OnClickListener, LifecycleObserver {

    Context context;
    private TextView tvReturn;
    private PayPasswordView payPassword;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private TextView tv7;
    private TextView tv8;
    private TextView tv9;
    private TextView tv;
    private TextView tvDel;

    public PayPasswordDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public PayPasswordDialog(Context context) {
        super(context, R.style.dialog_bottom);
        this.context = context;
    }

    public PayPasswordDialog setLifecycle(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        return this;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void destroy() {
        // disconnect if connected
        MyLogger.dLog().e("Lifecycle.Event：" + "ON_DESTROY");
        dismiss();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_pay_pwd, null);
        setContentView(view);
        initView();

        Window window = getWindow();
        WindowManager.LayoutParams mParams = window.getAttributes();
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(mParams);
        setCanceledOnTouchOutside(true);


        tvReturn.setOnClickListener(view1 -> dismiss());

        tvDel.setOnClickListener(view12 -> payPassword.delLastPassword());
        payPassword.setPayPasswordEndListener(password -> {
            if (dialogClick != null) {
                dialogClick.doConfirm(password);
            }
            dismiss();
        });
        tv.setOnClickListener(this);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);
        tv6.setOnClickListener(this);
        tv7.setOnClickListener(this);
        tv8.setOnClickListener(this);
        tv9.setOnClickListener(this);
        tvDel.getPaint().setFakeBoldText(true);
        tv.getPaint().setFakeBoldText(true);
        tv1.getPaint().setFakeBoldText(true);
        tv2.getPaint().setFakeBoldText(true);
        tv3.getPaint().setFakeBoldText(true);
        tv4.getPaint().setFakeBoldText(true);
        tv5.getPaint().setFakeBoldText(true);
        tv6.getPaint().setFakeBoldText(true);
        tv7.getPaint().setFakeBoldText(true);
        tv8.getPaint().setFakeBoldText(true);
        tv9.getPaint().setFakeBoldText(true);
    }

    private void initView() {
        tvReturn = findViewById(R.id.tv_return);
        payPassword = findViewById(R.id.pay_password);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);
        tv7 = findViewById(R.id.tv7);
        tv8 = findViewById(R.id.tv8);
        tv9 = findViewById(R.id.tv9);
        tv = findViewById(R.id.tv);
        tvDel = findViewById(R.id.tv_del);
        findViewById(R.id.fl_bg).setOnClickListener(view -> dismiss());
    }

    DialogClick dialogClick;

    public PayPasswordDialog setDialogClick(DialogClick dialogClick) {
        this.dialogClick = dialogClick;
        return this;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv) {
            payPassword.addPassword("0");
        } else if (id == R.id.tv1) {
            payPassword.addPassword("1");
        } else if (id == R.id.tv2) {
            payPassword.addPassword("2");
        } else if (id == R.id.tv3) {
            payPassword.addPassword("3");
        } else if (id == R.id.tv4) {
            payPassword.addPassword("4");
        } else if (id == R.id.tv5) {
            payPassword.addPassword("5");
        } else if (id == R.id.tv6) {
            payPassword.addPassword("6");
        } else if (id == R.id.tv7) {
            payPassword.addPassword("7");
        } else if (id == R.id.tv8) {
            payPassword.addPassword("8");
        } else if (id == R.id.tv9) {
            payPassword.addPassword("9");
        }
    }

    public interface DialogClick {
        void doConfirm(String password);
    }

}
