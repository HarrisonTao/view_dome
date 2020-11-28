package com.dykj.youfeng.start;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.SmsBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.BaseToolsUtil;
import com.dykj.module.util.CountDownUtil;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgetPassActivity extends BaseActivity {


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
    private CountDownUtil mCount;
    private String mCode;

    @Override
    public int intiLayout() {
        return R.layout.activity_forget_pass;
    }

    @Override
    public void initData() {
        initTitle("忘记密码");
        mCount = new CountDownUtil(tvGetCode);
        if (getIntentData() != null) {
            Map<String, String> map = (Map<String, String>) getIntentData();
            etPhone.setText(map.get("phone"));
        }
    }

    @Override
    public void doBusiness() {

    }


    @OnClick({R.id.tv_get_code, R.id.tv_confirm})
    public void onViewClicked(View view) {
        String mPhone = etPhone.getText().toString().trim();
        switch (view.getId()) {
            case R.id.tv_get_code:
                if(TextUtils.isEmpty(mPhone)){
                    DyToast.getInstance().warning("请输入手机号");
                    return;
                }
                if(!BaseToolsUtil.isCellphone(mPhone)){
                    DyToast.getInstance().warning("手机号格式不正确!");
                    return;
                }
                getCode(mPhone);
                break;
            case R.id.tv_confirm:
                submitForgetPass(mPhone);
                break;
            default:
                break;
        }
    }

    /**
     * 提交忘记密码
     */
    private void submitForgetPass(String mPhone) {
        String mSms = etSms.getText().toString().trim();
        String mPass = etPass.getText().toString().trim();
        String mCpass = etCPass.getText().toString().trim();

        if(TextUtils.isEmpty(mPhone)){
            DyToast.getInstance().warning("请输入手机号");
            return;
        }
        if(!BaseToolsUtil.isCellphone(mPhone)){
            DyToast.getInstance().warning("手机号格式不正确!");
            return;
        }
        if(TextUtils.isEmpty(mCode)){
            DyToast.getInstance().warning("请获取验证码");
            return;
        }
        if (!mCode.equals(mSms)){
            DyToast.getInstance().warning("验证码有误!");
            return;
        }
        if(TextUtils.isEmpty(mPass)){
            DyToast.getInstance().warning("请输入新的登录密码!");
            return;
        }
        if(mPass.length() < 6){
            DyToast.getInstance().warning("登录密码不能少于6位!");
            return;
        }
        if(TextUtils.isEmpty(mCpass)){
            DyToast.getInstance().warning("请再次输入新的登录密码!");
            return;
        }
        if (!mPass.equals(mCpass)){
            DyToast.getInstance().warning("两次登录密码不一致");
            return;
        }


        HttpFactory.getInstance().forGetPassword(mPhone, mPass,mCpass)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(ForgetPassActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<String>>() {
                    @Override
                    public void success(HttpResponseData<String> data) {
                        if (null != mCount){
                           mCount.onFinish();
                        }
                        DyToast.getInstance().success(data.message);
                        finish();
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));


    }

    /**
     * 获取验证码
     */
    private void getCode(String mPhone) {
        mCount.startCount();
        HttpFactory.getInstance().sendSms(mPhone, "getPass")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(ForgetPassActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<SmsBean>>() {
                    @Override
                    public void success(HttpResponseData<SmsBean> data) {
                        if("9999".equals(data.status)){
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
