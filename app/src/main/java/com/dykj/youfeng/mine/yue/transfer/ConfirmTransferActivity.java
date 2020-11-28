package com.dykj.youfeng.mine.yue.transfer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.AppManager;
import com.dykj.module.util.CountDownUtil;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mine.authentication.AuthenticationStep2Activity;
import com.dykj.youfeng.mine.yue.BalanceActivity;
import com.dykj.youfeng.mode.SmsBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.start.RegActivity;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 确认 转账
 *
 * @author zhaoyanhui
 */
public class ConfirmTransferActivity extends BaseActivity {
    @BindView(R.id.tv_rmb_res)
    TextView tvRmbRes;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.nameText)
    TextView nameText;
    @BindView(R.id.phoneText)
    TextView phoneText;
    @BindView(R.id.ll_1)
    LinearLayout ll1;
    @BindView(R.id.et_sms)
    EditText etSms;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    private CountDownUtil mCount;
    private Intent intent;
    private String name = "";
    private String userPhone = "";

    private String mCode;

    @Override
    public int intiLayout() {
        return R.layout.activity_confirm_transfer;
    }

    @Override
    public void initData() {
        initTitle("转账");
        intent = getIntent();
        name = intent.getStringExtra("name");
        userPhone = intent.getStringExtra("userPhone");
        nameText.setText(name);
        phoneText.setText(userPhone);
        mCount = new CountDownUtil(tvGetCode);
    }

    @Override
    public void doBusiness() {

    }


    @OnClick({R.id.tv_submit,R.id.tv_get_code})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case  R.id.tv_get_code:
                String mPhone = MMKV.defaultMMKV().decodeString(AppCacheInfo.mPhone);
                getSmsCode(mPhone);
                break;
            case  R.id.tv_submit:
                String smsCode = etSms.getText().toString();

                String moneyStr = etMoney.getText().toString();
                if (TextUtils.isEmpty(moneyStr)) {
                    DyToast.getInstance().warning("请输入转账金额!");
                    return;
                }

                double money = convertToDouble(moneyStr, 0);
                if (money < 1) {
                    DyToast.getInstance().warning("转账金额要大于1元!");
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
                transferAccounts(name,userPhone,money);
            break;
        }


    }

    //把String转化为double
    public static double convertToDouble(String number, double defaultValue) {
        if (TextUtils.isEmpty(number)) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(number);
        } catch (Exception e) {
            return defaultValue;
        }

    }


    /**
     * 获取短信验证码
     *
     * @param mPhone
     */
    private void getSmsCode(String mPhone) {
        HttpFactory.getInstance().sendSms(mPhone, "getPass")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(ConfirmTransferActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<SmsBean>>() {
                    @Override
                    public void success(HttpResponseData<SmsBean> data) {
                        Log.d("wtf",new Gson().toJson(data));
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

    /**
     * 转账
     * @param transfer_name
     * @param transfer_phone
     * @param transfer_money
     */
    private void transferAccounts(String transfer_name,String transfer_phone,double transfer_money){
        Map<String, Object> parms = new HashMap<>();
        parms.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        parms.put("transfer_name", transfer_name );
        parms.put("transfer_phone", transfer_phone );
        parms.put("transfer_money", transfer_money );

        HttpFactory.getInstance().transferAccounts(parms)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(ConfirmTransferActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {

                        if ("9999".equals(data.status)) {
                            AppManager.getAppManager().finishActivity(TransferActivity.class);
                           // AppManager.getAppManager().finishActivity(BalanceActivity.class);
                            DyToast.getInstance().error(data.message);
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
