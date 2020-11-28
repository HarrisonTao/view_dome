package com.dykj.youfeng.home.receipt.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.BaseToolsUtil;
import com.dykj.module.util.CountDownUtil;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.WheelPickerBean;
import com.dykj.module.util.WheelPickerUtils;
import com.dykj.module.util.toast.DyToast;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.AreaBean;
import com.dykj.youfeng.mode.CjSkInfoBean;
import com.dykj.youfeng.mode.JftSmsBean;
import com.dykj.youfeng.mode.receipt.JftpayCardBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 *           .----.
 *        _.'__    `.
 *    .--(Q)(OK)---/$\
 *  .' @          /$$$\
 *  :         ,   $$$$$
 *   `-..__.-' _.-\$$/
 *         `;_:    `"'
 *       .'"""""`.
 *      /,  oo  ,\
 *     //         \\
 *     `-._______.-'
 *     ___`. | .'___
 *    (______|______)
 * </pre>
 * <p>文件描述：20200310快捷通收款<p>
 * <p>作者：lj<p>
 * <p>创建时间：2020/3/10<p>
 * <p>更改时间：2020/3/10<p>
 * <p>版本号：1<p>
 */
public class QuickReceiptInfoKjtActivity extends BaseActivity {

    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_no)
    TextView mTvNo;
    @BindView(R.id.tv_money)
    TextView mTvMoney;
    @BindView(R.id.tv_fee_money)
    TextView mTvFeeMoney;
    @BindView(R.id.tv_debit_card)
    TextView mTvDebitCard;
    @BindView(R.id.tv_credit_card)
    TextView mTvCreditCard;
    @BindView(R.id.tv_city)
    TextView mTvCity;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.tv_getCode)
    TextView mTvGetCode;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;

    private String mProvince;
    private String mCity;
    private JftpayCardBean mDataBean;

    private String mCityCode = "";
    private CountDownUtil mCountDownTimer;

    private String mCode;   // 验证码
    private String chSerialNo, tranStatus, orderNo;
    private String deviceLocation = "";
    private String mBbizOrderNumber;

    @Override
    public int intiLayout() {
        return R.layout.activity_quickreceipt_info_jft;
    }

    @Override
    public void initData() {
        initTitle("收款信息确认");
        if (null != getIntentData()) {
            mDataBean = (JftpayCardBean) getIntentData();
            getInfo();
        }
    }

    /**
     * 收款详情
     */
    private void getInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        map.put("crediteid", mDataBean.crediteid);
        map.put("debitid", mDataBean.debitid);
        map.put("money", mDataBean.money);
        HttpFactory.getInstance().kjtQuickInfo(map)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<CjSkInfoBean>>() {
                    @Override
                    public void success(HttpResponseData<CjSkInfoBean> data) {
                        if ("9999".equals(data.status)) {
                            setViewData(data.getData());
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

    /**
     * 设置数据
     *
     * @param data
     */
    private void setViewData(CjSkInfoBean data) {
        mTvMoney.setText(data.money + "元");
        mTvName.setText(data.realname);
        mTvNo.setText(data.idcard);
        mTvFeeMoney.setText(data.fee_money);
        mTvDebitCard.setText(getEnd4(data.to_bank_name,
                data.to_bank_num));
        mTvCreditCard.setText(getEnd4(data.out_bank_name,
                data.out_bank_num));
        mEtPhone.setText(data.phone);
    }

    private String getEnd4(String name, String number) {
        if (number != null) {
            if (number.length() > 4) {
                return name + "(" +
                        number.substring(number
                                .length() - 4) + ")";
            } else {
                return name + "(" + number + ")";
            }
        } else {
            return name;
        }
    }

    @Override
    public void doBusiness() {
        mTvName.setText(MMKV.defaultMMKV().decodeString(AppCacheInfo.mRealname, ""));
        mTvNo.setText(MMKV.defaultMMKV().decodeString(AppCacheInfo.mIdcard, ""));
        mCountDownTimer = new CountDownUtil(mTvGetCode);
    }

    @OnClick({R.id.tv_city, R.id.tv_getCode, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_city:
                getArea("1", "", true);
                break;
            case R.id.tv_getCode:
                String mPhone = mEtPhone.getText().toString();
                if (TextUtils.isEmpty(mPhone)) {
                    showToast("请输入手机号码!");
                    return;
                }
                if (!BaseToolsUtil.isCellphone(mPhone)) {
                    showToast("请输入手机格式不正确!");
                    return;
                }

                if (TextUtils.isEmpty(mCity)) {
                    showToast("请选择消费地区!");
                    return;
                }

                initNetDataSms(mPhone);
                break;
            case R.id.tv_submit:
                String mPhone2 = mEtPhone.getText().toString();
                if (TextUtils.isEmpty(mPhone2)) {
                    showToast("请输入手机号码!");
                    return;
                }
                if (!BaseToolsUtil.isCellphone(mPhone2)) {
                    showToast("请输入手机格式不正确!");
                    return;
                }

                if (TextUtils.isEmpty(mEtCode.getText().toString())) {
                    showToast("请输入验证码!");
                    return;
                }

                if (TextUtils.isEmpty(mBbizOrderNumber)) {
                    showToast("请先获取验证码!");
                    return;
                }

//                if (!mCode.equals(mEtCode.getText().toString())) {
//                    showToast("请校验验证码!");
//                    return;
//                }
                initSubmit();
                break;
        }
    }

    private void initSubmit() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        map.put("bizOrderNumber", mBbizOrderNumber);
        map.put("smsCode", mEtCode.getText().toString().trim());
        HttpFactory.getInstance().kjtConfirmPay(map)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
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
    }


    private void showToast(String message) {
        DyToast.getInstance().warning(message);
    }


    /***
     * 快捷通  短信
     */
    private void initNetDataSms(String phone) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        map.put("crediteid", mDataBean.crediteid);
        map.put("debitid", mDataBean.debitid);
        map.put("money", mDataBean.money);
        map.put("city", mCity);
        HttpFactory.getInstance().kftApplyPaySms(map)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<JftSmsBean>>() {
                    @Override
                    public void success(HttpResponseData<JftSmsBean> data) {
                        Log.d("wtf",new Gson().toJson(data));

                        if ("8888".equals(data.status)) {
                            JftSmsBean bean = data.getData();
                            mBbizOrderNumber = bean.bizOrderNumber;
                            mCountDownTimer.start();
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


    @Override
    protected void onStop() {
        super.onStop();
        if (null != mCountDownTimer){
            mCountDownTimer.onFinish();
            mCountDownTimer = null;
        }
    }

    /**
     * 选择省份
     *
     * @param type
     * @param isProvince
     */
    private void getArea(String type, String name, boolean isProvince) {
        HttpFactory.getInstance().kjtArea(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), type, name)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<List<AreaBean>>>() {
                    @Override
                    public void success(HttpResponseData<List<AreaBean>> data) {
                        if ("9999".equals(data.status)) {
                            List<AreaBean> areaBeanList = data.getData();
                            if (null != areaBeanList) {
                                showArea(areaBeanList, isProvince);
                            }
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

    /**
     * 显示省市区弹框
     *
     * @param data
     * @param isProvince
     */
    private void showArea(List<AreaBean> data, boolean isProvince) {
        final ArrayList<WheelPickerBean> beans = new ArrayList<>();
        if (data != null && !data.isEmpty()) {
            for (int i = 0; i < data.size(); i++) {
                beans.add(new WheelPickerBean(data.get(i).id + "", data.get(i).name));
            }
            if (isProvince) {
                WheelPickerUtils.getInstance().initCustomOptionPicker(getAty(), "请选择省份", beans, current -> {
                    mProvince = beans.get(current).getContext();
                    getArea("2", beans.get(current).context, false);
                });
            } else {
                WheelPickerUtils.getInstance().initCustomOptionPicker(getAty(), "请选择城市", beans, current -> {
                    mCity = beans.get(current).getContext();
                    mTvCity.setText(mProvince + mCity);
                });
            }
        }
    }

}
