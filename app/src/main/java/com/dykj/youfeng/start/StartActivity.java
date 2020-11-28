package com.dykj.youfeng.start;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.dykj.youfeng.MainActivity;
import com.dykj.youfeng.R;
import com.dykj.module.Flag;
import com.dykj.module.base.BaseActivity;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.tencent.mmkv.MMKV;

/**
 * Created by lcjingon 2019/10/22.
 * description: 启动页
 */

public class StartActivity extends BaseActivity {

    @Override
    public int intiLayout() {
        return R.layout.activity_start;
    }

    @Override
    public void initData() {

    }

    @Override
    public void doBusiness() {
        handler.sendEmptyMessageDelayed(1, 1000);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (MMKV.defaultMMKV().decodeBool(Flag.IS_FIRST, true)) {
                startAct(GuidActivity.class);
            } else {
                if (TextUtils.isEmpty(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""))) {
                    startAct(LoginActivity.class);
                } else {
                    startAct(MainActivity.class);
                }
            }
            MMKV.defaultMMKV().encode(Flag.IS_FIRST, false);
            finish();
        }
    };
}
