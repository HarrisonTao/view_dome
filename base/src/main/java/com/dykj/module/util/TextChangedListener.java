package com.dykj.module.util;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by lcjingon 2019/10/28.
 * description:
 */

public abstract class TextChangedListener implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public abstract void afterTextChanged(Editable editable);
}
