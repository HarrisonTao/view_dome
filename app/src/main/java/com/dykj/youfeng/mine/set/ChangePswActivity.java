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

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePswActivity extends BaseActivity {
    @BindView(R.id.et_old_phone)
    EditText etOldPhone;
    @BindView(R.id.et_new_psw)
    EditText etNewPsw;
    @BindView(R.id.et_next_new_psw)
    EditText etNextNewPsw;

    @Override
    public int intiLayout() {
        return R.layout.activity_changepsw;
    }

    @Override
    public void initData() {
        initTitle("修改密码");
    }

    @Override
    public void doBusiness() {

    }


    @OnClick(R.id.tv_submit)
    public void onClickView() {
        String oldPsw = etOldPhone.getText().toString().trim();
        String newPsw = etNewPsw.getText().toString().trim();
        String nextNewPsw = etNextNewPsw.getText().toString().trim();

        if (TextUtils.isEmpty(oldPsw)) {
            DyToast.getInstance().warning("请输入原登录密码");
            return;
        }
        if (TextUtils.isEmpty(newPsw)) {
            DyToast.getInstance().warning("请输入新登录密码");
            return;
        }
        if (newPsw.length() < 6) {
            DyToast.getInstance().warning("登录密码不能少于6位数");
            return;
        }

        if (TextUtils.isEmpty(nextNewPsw)) {
            DyToast.getInstance().warning("请再次输入新登录密码");
            return;
        }

        if (!nextNewPsw.equals(newPsw)) {
            DyToast.getInstance().warning("两次输入密码不一致!");
            return;
        }

        Map<String, Object> parms = new HashMap<>();
        parms.put("token",MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent,""));
        parms.put("oldPassword", oldPsw);
        parms.put("password", newPsw);
        parms.put("repPassword", nextNewPsw);
        HttpFactory.getInstance().editPassword(parms)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(ChangePswActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
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

}
