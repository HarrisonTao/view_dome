package com.dykj.youfeng.start;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.SmsBean;
import com.dykj.youfeng.mode.UserInfoBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.tools.MmvkUtlis;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.base.BaseWebViewActivity;
import com.dykj.module.entity.BaseWebViewData;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.BaseToolsUtil;
import com.dykj.module.util.CountDownUtil;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.google.gson.Gson;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class RegActivity extends BaseActivity {


    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_sms)
    EditText etSms;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.et_pass)
    EditText etPass;
    @BindView(R.id.et_c_pass)
    EditText etCPass;
    @BindView(R.id.et_invite)
    EditText etInvite;
    @BindView(R.id.cb_agree)
    CheckBox cbAgree;
    @BindView(R.id.tv_reg)
    TextView tvReg;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    private CountDownUtil mCount;
    private String mCode;


    @Override
    public int intiLayout() {
        return R.layout.activity_reg;
    }

    @Override
    public void initData() {
        QMUIStatusBarHelper.setStatusBarDarkMode(this);
        SpanUtils.with(tvLogin).appendLine("去登录").setUnderline().create();
        mCount = new CountDownUtil(tvGetCode);
    }

    @Override
    public void doBusiness() {
        if (getIntentData() != null) {
            Map<String, String> map = (Map<String, String>) getIntentData();
            etPhone.setText(map.get("phone"));
            etPass.setText(map.get("pass"));
        }
    }


    @OnClick({R.id.iv_back, R.id.tv_get_code, R.id.tv_agreement, R.id.tv_login, R.id.tv_reg})
    public void onViewClicked(View view) {
        String mPhone = etPhone.getText().toString().trim();
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_get_code:
                if (TextUtils.isEmpty(mPhone)) {
                    DyToast.getInstance().warning("请输入手机号!");
                    return;
                }
                if (!BaseToolsUtil.isCellphone(mPhone)) {
                    DyToast.getInstance().warning("手机号不合法!");
                    return;
                }


                getSmsCode(mPhone);
                break;
            case R.id.tv_agreement:  // 注册协议
                getQuestion();
                break;
            case R.id.tv_login:
                Map<String, String> map = new HashMap<>();
                map.put("phone", etPhone.getText().toString());
                map.put("pass", etPass.getText().toString());
                startAct(LoginActivity.class, map);
                finish();
                break;

            case R.id.tv_reg:  // 注册
                checkReg(mPhone);
                break;

            default:
                break;
        }
    }

    /**
     * 注册协议
     */
    private void getQuestion() {
        HttpFactory.getInstance().questionRegister()
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(RegActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<String>>() {
                    @Override
                    public void success(HttpResponseData<String> data) {
                        BaseWebViewData webViewData = new BaseWebViewData();
                        webViewData.title = "注册协议";
                        webViewData.content = data.getData();
                        startAct(RegActivity.this, BaseWebViewActivity.class, webViewData);
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));
    }

    /**
     * 注册
     *
     * @param mPhone
     */
    private void checkReg(String mPhone) {
        String smsCode = etSms.getText().toString().trim();
        String mPass = etPass.getText().toString().trim();
        String mNextPass = etCPass.getText().toString().trim();
        boolean checked = cbAgree.isChecked();

        if (TextUtils.isEmpty(mPhone)) {
            DyToast.getInstance().warning("请输入手机号!");
            return;
        }
        if (!BaseToolsUtil.isCellphone(mPhone)) {
            DyToast.getInstance().warning("手机号不合法!");
            return;
        }
        if (TextUtils.isEmpty(mCode)) {
            DyToast.getInstance().warning("请获取验证码");
            return;
        }

        if (!mCode.equals(smsCode)) {
            DyToast.getInstance().warning("验证码不合法!");
            return;
        }
        if (TextUtils.isEmpty(mPass)) {
            DyToast.getInstance().warning("请设置登录密码!");
            return;
        }

        if (mPass.length() < 6) {
            DyToast.getInstance().warning("登录密码不能少于6位数");
            return;
        }

        if (!mPass.equals(mNextPass)) {
            DyToast.getInstance().warning("两次输入密码不一致!");
            return;
        }

        if (!checked) {
            DyToast.getInstance().warning("请阅读并同意用户注册协议!");
            return;
        }
        register(mPhone, mPass, mNextPass);

    }

    /**
     * 注册
     */
    private void register(String phone, String pass, String nextPass) {
        Map<String, Object> parms = new HashMap<>();
        parms.put("userIphone", phone);
        parms.put("password", pass);
        parms.put("repPassword", nextPass);
        String invitePhone = etInvite.getText().toString();   // 推荐人手机号
        if (!TextUtils.isEmpty(invitePhone)) {
            if (BaseToolsUtil.isCellphone(invitePhone)) {
                parms.put("merPhone", invitePhone);
            }
        }

        HttpFactory.getInstance().userRegister(parms)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<String>>() {
                    @Override
                    public void success(HttpResponseData<String> data) {
                        Log.d("wtf",new Gson().toJson(data));
                        if (null != mCount) {
                            mCount.onFinish();
                        }
                        if("9999".equals(data.status)){
                            MMKV.defaultMMKV().encode(AppCacheInfo.mTokent, data.getData());
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
        HttpFactory.getInstance().getUserInfo(mToken)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<UserInfoBean>>() {
                    @Override
                    public void success(HttpResponseData<UserInfoBean> data) {
                        if ("9999".equals(data.status)){
                            new MmvkUtlis().saveUserInfo(data.getData());
                            finish();
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
     * 获取短信验证码
     *
     * @param mPhone
     */
    private void getSmsCode(String mPhone) {
        HttpFactory.getInstance().sendSms(mPhone, "register")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(RegActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<SmsBean>>() {
                    @Override
                    public void success(HttpResponseData<SmsBean> data) {
                        Log.d("wtf",new Gson().toJson(data));
                        if("9999".equals(data.status)){
                            mCount.startCount();
                            mCode = data.getData().code + "";
                        }else {
                            DyToast.getInstance().warning(data.message);
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
