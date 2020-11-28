package com.dykj.youfeng.start;


import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.dykj.youfeng.MainActivity;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.UserInfoBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.tools.MmvkUtlis;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.BaseToolsUtil;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.google.gson.Gson;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_pass)
    EditText etPass;
    @BindView(R.id.tv_reg)
    TextView tvReg;

    @Override
    public int intiLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initData() {
        QMUIStatusBarHelper.setStatusBarDarkMode(this);
        SpanUtils.with(tvReg).appendLine("去注册").setUnderline().create();
    }

    @Override
    public void doBusiness() {
        if (getIntentData() != null) {
            Map<String, String> map = (Map<String, String>) getIntentData();
            etPhone.setText(map.get("phone"));
            etPass.setText(map.get("pass"));
        }
    }


    @OnClick({R.id.iv_back, R.id.tv_forget, R.id.tv_login, R.id.tv_reg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                startAct(MainActivity.class);
                break;
            case R.id.tv_forget:
                Map<String, String> forget = new HashMap<>();
                forget.put("phone", etPhone.getText().toString());
                startAct(ForgetPassActivity.class, forget);
                break;
            case R.id.tv_login:
                login();
                break;
            case R.id.tv_reg:
                Map<String, String> map = new HashMap<>();
                map.put("phone", etPhone.getText().toString());
                map.put("pass", etPass.getText().toString());
                startAct(RegActivity.class, map);
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 用户登录
     */
    private void login() {
        String phone = etPhone.getText().toString().trim();
        String pass = etPass.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            DyToast.getInstance().warning("请输入手机号!");
            return;
        }

        if (!BaseToolsUtil.isCellphone(phone)) {
            DyToast.getInstance().warning("手机号格式不正确!");
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            DyToast.getInstance().warning("请输入登录密码!");
            return;
        }

        Map<String, Object> parms = new HashMap<>();
        parms.put("userIphone", phone);
        parms.put("password", pass);
        HttpFactory.getInstance().userLogin(parms)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(LoginActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<String>>() {
                    @Override
                    public void success(HttpResponseData<String> data) {
                        Log.d("wtf",new Gson().toJson(data));
                        if("9999".equals(data.status)){
                            MMKV.defaultMMKV().encode(AppCacheInfo.mTokent, data.getData());
                          //  mmkv.encode(AppCacheInfo.mKefuwechat, bean.kefuwechat);
                            requestUserInfo(data.getData());
                        }else {
                            DyToast.getInstance().info(data.message);
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));
    }

    /**
     * 请求用户信息
     */
    private void requestUserInfo(String mToken) {
        Log.d("wtf",mToken);
        HttpFactory.getInstance().getUserInfo(mToken)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(LoginActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<UserInfoBean>>() {
                    @Override
                    public void success(HttpResponseData<UserInfoBean> data) {
                        Log.d("wtf","---"+new Gson().toJson(data));
                        if ("9999".equals(data.status)){

                            new MmvkUtlis().saveUserInfo(data.getData());
                            startAct(MainActivity.class);
                            finish();
                        }else {
                            DyToast.getInstance().info(data.message);
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        Log.d("wtf","----"+new Gson().toJson(data));
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            startAct(MainActivity.class);
        }

        return super.onKeyDown(keyCode, event);
    }


}
