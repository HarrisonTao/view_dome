package com.dykj.youfeng.mine.set;

import android.text.TextUtils;
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

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class ResetDealPswActivity extends BaseActivity {
    @BindView(R.id.et_new_psw)
    EditText etNewPsw;
    @BindView(R.id.et_next_new_psw)
    EditText etNextNewPsw;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    @Override
    public int intiLayout() {
        return R.layout.activity_reset_deal_psw;
    }

    @Override
    public void initData() {
        initTitle("重置交易密码");
    }

    @Override
    public void doBusiness() {
        tvSubmit.setOnClickListener(v -> {
            String mpsw = etNewPsw.getText().toString().trim();
            String mNpsw = etNextNewPsw.getText().toString().trim();
            if (TextUtils.isEmpty(mpsw)){
                DyToast.getInstance().warning("请输入交易密码");
                return;
            }

            if(mpsw.length() < 6){
                DyToast.getInstance().warning("交易密码不能低于6位!");
                return;
            }

            if (TextUtils.isEmpty(mNpsw)){
                DyToast.getInstance().warning("请输入确认交易密码");
                return;
            }

            if (!mpsw.equals(mNpsw)){
                DyToast.getInstance().warning("两次密码不一致!");
                return;
            }

            Map<String, Object> parms = new HashMap<>();
            parms.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
            parms.put("payPass", mpsw);
            parms.put("repPayPass", mNpsw);
            HttpFactory.getInstance().setNewPayPassword(parms)
                    .compose(HttpObserver.schedulers(getAty()))
                    .as(HttpObserver.life(ResetDealPswActivity.this))
                    .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                        @Override
                        public void success(HttpResponseData data) {
                            if ("9999".equals(data.status)) {
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

        });
    }

}
