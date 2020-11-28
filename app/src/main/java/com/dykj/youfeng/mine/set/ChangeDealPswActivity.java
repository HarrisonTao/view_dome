package com.dykj.youfeng.mine.set;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

public class ChangeDealPswActivity extends BaseActivity {
    @BindView(R.id.et_old_psw)
    EditText etOldPsw;
    @BindView(R.id.tv_forgetDealPsw)
    TextView tvForgetDealPsw;
    @BindView(R.id.et_new_psw)
    EditText etNewPsw;
    @BindView(R.id.et_next_new_psw)
    EditText etNextNewPsw;

    @Override
    public int intiLayout() {
        return R.layout.activity_change_deal_psw;
    }

    @Override
    public void initData() {
        initTitle("修改交易密码");
    }

    @Override
    public void doBusiness() {

    }


    @OnClick({R.id.tv_forgetDealPsw, R.id.tv_submit})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.tv_forgetDealPsw:   // 忘记交易密码
                startAct(this, ForGetDealPwsActivity.class);
                break;

            case R.id.tv_submit:
                submit();
                break;
        }
    }


    /**
     * 提交修改交易密码
     */
    private void submit() {
        String mOldPsw = etOldPsw.getText().toString().trim();
        String mNewPsw = etNewPsw.getText().toString().trim();
        String mNextNewPsw = etNextNewPsw.getText().toString().trim();
        if (TextUtils.isEmpty(mOldPsw)) {
            DyToast.getInstance().warning("请输入原交易密码");
            return;
        }

        if (TextUtils.isEmpty(mNewPsw)) {
            DyToast.getInstance().warning("请输入新交易密码");
            return;
        }

        if (mNewPsw.length() != 6) {
            DyToast.getInstance().warning("交易密码为6位!");
            return;
        }

        if (TextUtils.isEmpty(mNextNewPsw)) {
            DyToast.getInstance().warning("请确认交易密码");
            return;
        }

        if(!mNextNewPsw.equals(mNewPsw)){
            DyToast.getInstance().warning("两次输入密码不一致!");
            return;
        }

        Map<String, Object> parms = new HashMap<>();
        parms.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        parms.put("oldPayPass", mOldPsw);
        parms.put("payPass", mNewPsw);
        parms.put("repPayPass", mNextNewPsw);
        HttpFactory.getInstance().editPayPassword(parms)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(ChangeDealPswActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            EventBus.getDefault().postSticky(AppCacheInfo.mRefreshUserInfo);
                            DyToast.getInstance().success(data.message);
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
