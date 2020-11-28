package com.dykj.youfeng.home.kf;

import android.text.TextUtils;
import com.dykj.youfeng.R;
import com.dykj.module.activity.WebViewFragment;
import com.dykj.module.base.BaseActivity;

public class HelpBookActivity extends BaseActivity {

    @Override
    public int intiLayout() {
        return R.layout.activity_help_book;
    }

    @Override
    public void initData() {
        initTitle("操作手册");

        if(null != getIntentData()){
            String helpInfoData = (String) getIntentData();
            if (!TextUtils.isEmpty(helpInfoData)){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_info, WebViewFragment.getInstance(helpInfoData))
                        .commit();
            }
        }
    }

    @Override
    public void doBusiness() {

    }

}
