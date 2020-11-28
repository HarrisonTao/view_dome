package com.dykj.youfeng.mine.set;

import android.text.TextUtils;
import android.widget.EditText;

import com.dykj.youfeng.R;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingDealPswActivity extends BaseActivity {
    @BindView(R.id.et_deal_psw)
    EditText etDealPsw;
    @BindView(R.id.et_deal_next_psw)
    EditText etDealNextPsw;

    @Override
    public int intiLayout() {
        return R.layout.activity_setting_deal_psw;
    }

    @Override
    public void initData() {
        initTitle("设置交易密码");
    }

    @Override
    public void doBusiness() {

    }

    @OnClick(R.id.tv_submit)
    public void onClickView() {
        String mPsw = etDealPsw.getText().toString().trim();
        String mNextPsw = etDealNextPsw.getText().toString().trim();
        if (TextUtils.isEmpty(mPsw)) {
            DyToast.getInstance().warning("请输入交易密码");
            return;
        }

        if (mPsw.length() < 6) {
            DyToast.getInstance().warning("请交易密码不能少于6位数!");
            return;
        }

        if (TextUtils.isEmpty(mNextPsw)) {
            DyToast.getInstance().warning("请再次输入交易密码");
            return;
        }
        if (!mNextPsw.equals(mPsw)) {
            DyToast.getInstance().warning("两次输入密码不一致!");
            return;
        }

        Map<String, Object> parms = new HashMap<>();
        parms.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        parms.put("payPass", mPsw);
        parms.put("repPayPass", mNextPsw);
        HttpFactory.getInstance().setPayPassword(parms)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(SettingDealPswActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            EventBus.getDefault().postSticky(AppCacheInfo.mRefreshUserInfo);
                            DyToast.getInstance().success(data.message);
                            MMKV.defaultMMKV().encode(AppCacheInfo.mPayPass, 1);
                            finish();
                        } else {
                            DyToast.getInstance().error(data.message);
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));
    }
}
