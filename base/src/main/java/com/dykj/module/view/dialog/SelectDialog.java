package com.dykj.module.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dykj.module.R;
import com.dykj.module.view.WheelView;

import java.util.List;

public class SelectDialog extends BaseDialog {
    private Context context;
    private List<String> list;
    private String title = "";
    private int index = 0;


    public SelectDialog(Context context, SelectCallBack callBack, String title, List<String> list) {
        this.context = context;
        this.title = title;
        this.list = list;
        this.callBack = callBack;
    }

    @Override
    public void show() {
        if (dialog != null) {
            dialog.show();
            return;
        }
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_select, null);
        LinearLayout llContent;
        TextView tvTitle;
        TextView tvConfirm, tvCancel;

        WheelView wheelView;
        llContent = dialogView.findViewById(R.id.llContent);
        tvTitle = dialogView.findViewById(R.id.tv_title);
        tvConfirm = dialogView.findViewById(R.id.tv_confirm);
        tvCancel = dialogView.findViewById(R.id.tv_cancel);
        wheelView = dialogView.findViewById(R.id.wheelView);

        tvTitle.setText(title);
        tvCancel.setOnClickListener(v -> {
            index = -5;
            dismiss();
        });
        tvConfirm.setOnClickListener(v -> {
            if (callBack != null && index >= 0) {
                callBack.callBack(index, list.get(index));
            }
            dismiss();
        });
        llContent.setOnClickListener(v -> dismiss());

        wheelView.setOffset(2);
        wheelView.setItems(list);
        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                index = selectedIndex - 2;
            }
        });

        dialog = new Dialog(context, R.style.dialog_bottom);
        dialog.setCancelable(true);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);

        dialog.setOnDismissListener(dialog -> {

        });
        dialog.show();
    }

    private SelectCallBack callBack;

    public interface SelectCallBack {
        void callBack(int position, String value);
    }
}
