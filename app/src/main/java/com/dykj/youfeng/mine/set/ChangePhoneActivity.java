package com.dykj.youfeng.mine.set;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.SmsBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.BaseToolsUtil;
import com.dykj.module.util.CountDownUtil;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.tencent.mmkv.MMKV;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePhoneActivity extends BaseActivity {

    @BindView(R.id.tv_phone_type)
    TextView tvPhoneType;
    @BindView(R.id.et_old_phone)
    EditText etOldPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_explain)
    TextView tvExplain;
    private String mCode;
    private String mType;
    private CountDownUtil mCount;


    @Override
    public int intiLayout() {
        return R.layout.activity_changephone;
    }

    @Override
    public void initData() {
        initTitle("更换手机号");
        mCount = new CountDownUtil(tvCode);
        if (getIntentData() != null) {
            mType = getIntentData().toString();
            if (mType.equals("2")) {
                tvPhoneType.setText("新手机号");
                tvSubmit.setText("确定");
                etOldPhone.setHint("请输入新手机号");
                tvExplain.setVisibility(View.GONE);
            } else {
                String oldPhone = MMKV.defaultMMKV().decodeString(AppCacheInfo.mPhone, "");
                etOldPhone.setText(oldPhone);
                etOldPhone.setSelection(oldPhone.length());
            }
        }
    }

    @Override
    public void doBusiness() {

    }


    @OnClick({R.id.tv_submit, R.id.tv_code})
    public void onViewClick(View view) {
        String strBtn = tvSubmit.getText().toString();
        String mEtPhone = etOldPhone.getText().toString();
        switch (view.getId()) {
            case R.id.tv_submit:
                if (TextUtils.isEmpty(mEtPhone)) {
                    DyToast.getInstance().warning("请输入手机号");
                    return;
                }
                if (!BaseToolsUtil.isCellphone(mEtPhone)) {
                    DyToast.getInstance().warning("手机号格式不正确");
                    return;
                }
                if (TextUtils.isEmpty(mCode)) {
                    DyToast.getInstance().warning("请获取验证码");
                    return;
                }
                if (!mCode.equals(etCode.getText().toString().trim())) {
                    DyToast.getInstance().warning("验证码有误!");
                    return;
                }

                if (strBtn.equals("下一步")) {
                    checkOldPhone();
                } else {
                    editPhone(mEtPhone);
                }
                break;

            case R.id.tv_code:
                if (TextUtils.isEmpty(mEtPhone)) {
                    DyToast.getInstance().warning("请输入手机号");
                    return;
                }
                if (!BaseToolsUtil.isCellphone(mEtPhone)) {
                    DyToast.getInstance().warning("手机号格式不正确");
                    return;
                }
                getCode(mEtPhone);
                break;
        }
    }

    /***
     * 检测原手机号
     */
    private void checkOldPhone() {
        HttpFactory.getInstance().loginCheckPhone(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent), etOldPhone.getText().toString().trim())
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(ChangePhoneActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            if (null != mCount){
                                mCount.onFinish();
                            }
                            finish();
                            startAct(getAty(), ChangePhoneActivity.class, "2");
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));
    }

    /***
     * 修改手机号 提交
     * @param mEtPhone
     */
    private void editPhone(String mEtPhone) {
        HttpFactory.getInstance().editPhone(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent),mEtPhone)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(ChangePhoneActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
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
     *
     * @param mPhone
     */
    private void getCode(String mPhone) {
        mCount.startCount();
        HttpFactory.getInstance().sendSms(mPhone, mType.equals("1") ? "modPhone" : "newmodPhone")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(ChangePhoneActivity.this))
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
