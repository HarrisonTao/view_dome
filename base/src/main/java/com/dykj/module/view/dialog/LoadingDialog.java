package com.dykj.module.view.dialog;


import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dykj.module.R;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

public class LoadingDialog {
    private static LoadingDialog loadingDialog;
    private QMUITipDialog tipDialog;

    public static LoadingDialog getInstance() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog();
        }
        return loadingDialog;
    }

    /**
     * 显示进度条
     *
     * @param context    上下文
     * @param cancelable 是否可手动取消
     * @param tipWord    提示文字 null时 显示 “正在加载”
     */
    public void showLoadingDialog(@NonNull Context context, @NonNull boolean cancelable, @Nullable String tipWord) {
        if (tipDialog != null) {
            tipDialog.dismiss();
        }
        if (TextUtils.isEmpty(tipWord)) {
            tipWord = context.getString(R.string.loading_tip_word);
        }
        tipDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(tipWord).create(cancelable);
        if (!tipDialog.isShowing()) {
            tipDialog.show();
        }
    }

    /**
     * 隐藏进度条
     */
    public void dismissLoadingDialog() {
        if (tipDialog != null && tipDialog.isShowing()) {
            tipDialog.dismiss();
        }
    }
}
