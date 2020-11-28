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
import com.dykj.module.util.CountDownUtil;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.tencent.mmkv.MMKV;

import butterknife.BindView;
import butterknife.OnClick;

public class ForGetDealPwsActivity extends BaseActivity {
    @BindView(R.id.et_phone)
    TextView etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.tv_next)
    TextView tvNext;

    private CountDownUtil mCount;
    private String mCode;

    @Override
    public int intiLayout() {
        return R.layout.activity_forget_deal_psw;
    }

    @Override
    public void initData() {
        initTitle("忘记交易密码");
    }

    @Override
    public void doBusiness() {
        mCount = new CountDownUtil(tvCode);
        etPhone.setText(MMKV.defaultMMKV().decodeString(AppCacheInfo.mPhone, ""));
    }


    @OnClick({R.id.tv_next, R.id.tv_code})
    public void onClickView(View view) {    // 下一步
        switch (view.getId()) {
            case R.id.tv_next:
                if (!TextUtils.isEmpty(mCode) && mCode.equals(etCode.getText().toString().trim())) {
                    startAct(this, ResetDealPswActivity.class);
                    finish();
                }
                break;

            case R.id.tv_code:
                getCode();
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        String mPhone = etPhone.getText().toString();
        if (TextUtils.isEmpty(mPhone)) {
            return;
        }
        mCount.startCount();
        HttpFactory.getInstance().sendSms(mPhone, "modPayPass")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(ForGetDealPwsActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<SmsBean>>() {
                    @Override
                    public void success(HttpResponseData<SmsBean> data) {
                        if ("9999".equals(data.status)) {
                            mCode = data.getData().code + "";
                        } else {
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
