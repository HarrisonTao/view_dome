package com.dykj.youfeng.home.receipt.activity;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.dykj.module.base.BaseWebViewActivity;
import com.dykj.module.entity.BaseWebViewData;
import com.dykj.youfeng.R;
import com.dykj.youfeng.home.base.Quick;
import com.dykj.youfeng.mode.receipt.Ipv4Bean;
import com.dykj.youfeng.mode.receipt.JftApplyPaySmsBean;
import com.dykj.youfeng.mode.receipt.JftpayCardBean;
import com.dykj.youfeng.mode.receipt.JftpayInfoBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AmapLocationUils;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.BaseToolsUtil;
import com.dykj.module.util.CountDownUtil;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.dykj.module.view.bean.AreaBean;
import com.dykj.module.view.dialog.ConfirmDialog;
import com.dykj.module.view.dialog.RemindDialog;
import com.dykj.module.view.dialog.SelectCityDialog;
import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import androidx.annotation.Nullable;
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
 * <p>文件描述：20200310佳付通收款<p>
 * <p>作者：lj<p>
 * <p>创建时间：2020/3/10<p>
 * <p>更改时间：2020/3/10<p>
 * <p>版本号：1<p>
 */
public class QuickReceiptInfoJftActivity extends BaseActivity {

    @BindView(R.id.tv_name) TextView mTvName;
    @BindView(R.id.tv_no) TextView mTvNo;
    @BindView(R.id.tv_money) TextView mTvMoney;
    @BindView(R.id.tv_fee_money) TextView mTvFeeMoney;
    @BindView(R.id.tv_debit_card) TextView mTvDebitCard;
    @BindView(R.id.tv_credit_card) TextView mTvCreditCard;
    @BindView(R.id.tv_city) TextView mTvCity;
    @BindView(R.id.et_phone) EditText mEtPhone;
    @BindView(R.id.et_code) EditText mEtCode;
    @BindView(R.id.tv_getCode) TextView mTvGetCode;
    @BindView(R.id.tv_submit) TextView mTvSubmit;
    @BindView(R.id.ll_city)
    LinearLayout llCity;

    private String mProvince;
    private String mCity;
    private JftpayCardBean mDataBean;

    private String mCityCode = "";
    private CountDownUtil mCountDownTimer;

    private String mCode;   // 验证码
    private String chSerialNo, tranStatus, orderNo;
    private String deviceLocation = "";
    private String IPV4 = "";

    @Override
    public int intiLayout() {
        return R.layout.activity_quickreceipt_info_jft;
    }

    @Override
    public void initData() {
        initTitle("收款信息确认");
        if (null != getIntentData()) {
            mDataBean = (JftpayCardBean) getIntentData();
        }
        if (TextUtils.isEmpty(SPUtils.getInstance().getString("IMEI"))) {
            SPUtils.getInstance().put("IMEI",
                    UUID.randomUUID().toString().replaceAll("-", "").substring(0, 15));
        }
        initDataLocation();
        initDataGetIpv4();
    }

    @Override
    public void doBusiness() {
        llCity.setVisibility(View.GONE);

        mTvName.setText(MMKV.defaultMMKV().decodeString(AppCacheInfo.mRealname, ""));
        mTvNo.setText(MMKV.defaultMMKV().decodeString(AppCacheInfo.mIdcard, ""));
        mCountDownTimer = new CountDownUtil(mTvGetCode);

        initNetDataInfo();

    }


    @OnClick({R.id.tv_city, R.id.tv_getCode, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_city:
//                selectCity();
//                getArea("", true);
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
                initNetDataSms(mPhone);
                break;
            case R.id.tv_submit:
              /*  if (TextUtils.isEmpty(mCityCode)) {
                    showToast("请选择消费城市!");
                    return;
                }*/
                String mPhone2 = mEtPhone.getText().toString();
                if (TextUtils.isEmpty(mPhone2)) {
                    showToast("请输入手机号码!");
                    return;
                }
                if (!BaseToolsUtil.isCellphone(mPhone2)) {
                    showToast("请输入手机格式不正确!");
                    return;
                }

                /*if (TextUtils.isEmpty(mEtCode.getText().toString())) {
                    showToast("请输入验证码!");
                    return;
                }*/

//                if (!mCode.equals()) {
//                    showToast("请校验验证码!");
//                    return;
//                }
               // initNetDataSubmit();
                addQuick(mDataBean.crediteid,mDataBean.debitid,mDataBean.money);
                break;
        }
    }

    /**
     *提交收款
     *   credit_card_id
     *     debit_card_id
     *     amount
     */
    private  void  addQuick(String crdeitCardId,String debitCardIt,String amount){

        HashMap<String,Object> map=new HashMap<>();
        map.put("credit_card_id",crdeitCardId);
        map.put("debit_card_id",debitCardIt);
        map.put("amount",amount);

        HttpFactory.getInstance().addQuick(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), map)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<Quick>>() {
                    @Override
                    public void success(HttpResponseData<Quick> data) {
                         if ("9999".equals(data.getStatus())) {
                            //用户已绑卡 收款{"info":{"url":"https://yfapi.xunyunsoft.com/api/card/h5_quick_pay?id\u003d2252"},"message":"请求成功","status":"9999"}
                            String url=data.getData().url;
                             BaseWebViewData viewData = new BaseWebViewData();
                             viewData.title = "收款";
                             viewData.content =url;

                             startAct(getAty(), BaseWebViewActivity.class, viewData);
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


    /**
     * 城市选择框
     */
    private void selectCity() {
        //1、初始化
        SelectCityDialog selectCityPopup = new SelectCityDialog(this, "选择城市");
        //2、设置监听回调
        selectCityPopup.setCallBack(new SelectCityDialog.SelectCityBack() {
            @Override
            public void getCity(String provinceId) {
                //通过网络请求获取数据
                HttpFactory.getJftSk().getJftArea(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent
                        , ""), provinceId)
                        .compose(HttpObserver.schedulers(getAty()))
                        .as(HttpObserver.life(QuickReceiptInfoJftActivity.this))
                        .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<List<AreaBean>>>() {
                            @Override
                            public void success(HttpResponseData<List<AreaBean>> data) {
                                if ("9999".equals(data.status)) {
                                    List<AreaBean> areaBeanList = data.getData();
                                    if (null != areaBeanList) {
                                        //设置数据
                                        selectCityPopup.setData(data.getData());
                                    }
                                } else {
                                    showToastError(data.message);
                                }
                            }

                            @Override
                            public void error(Throwable data) {
                                MyLogger.dLog().e(data);
                                showToastError(data.getMessage());
                            }
                        }));
            }

            //选择完成回调
            @Override
            public void select(Map<String, String> params) {

                mTvCity.setText(params.get("provinceName") + params.get("cityName"));
                mCityCode = params.get("cityId");

            }
        });
        //3、显示
        selectCityPopup.setLifecycle(getLifecycle()).show();
    }
    private void showToastError(String message) {
        DyToast.getInstance().error(message);
    }
    private void showToast(String message) {
        DyToast.getInstance().warning(message);
    }

    private String getToken() {
        return MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 601) {
            initDataLocation();
        }
    }

    /***
     * 畅捷 收款提交
     */
    private void initNetDataSms(String phone) {
//        if (TextUtils.equals("131452100", mEtCode.getText())) {
//            mCountDownTimer.startCount();
//
//            ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
//            map.put("token", getToken());
//            map.put("phone", phone);
//            HttpFactory.getJftSk().getSms(map)
//                    .compose(HttpObserver.schedulers(getAty()))
//                    .as(HttpObserver.life(this))
//                    .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<SmsBean>>() {
//                        @Override
//                        public void success(HttpResponseData<SmsBean> data) {
//                            if ("9999".equals(data.getStatus())) {
//                                mCode = data.getData().code + "";
//                            } else {
//                                showToast("短信获取失败~");
//                            }
//                        }
//
//                        @Override
//                        public void error(Throwable data) {
//                            MyLogger.dLog().e(data);
//                            showToast("短信获取失败~"+data.getMessage());
//                        }
//                    }));
//
//            return;
//        }
        if (TextUtils.isEmpty(deviceLocation)) {
            new ConfirmDialog(this,
                    () -> {
                        Intent intent = new Intent();
                        intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, 601);
                    }, "需要开启定位权限")
                    .setLifecycle(getLifecycle()).show();

            return;
        }


        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        map.put("token", getToken());
        map.put("user_id", getToken());
        map.put("crediteid", mDataBean.getCrediteid());
        map.put("debitid", mDataBean.getDebitid());
        map.put("code", "2005");
        map.put("money", mDataBean.getMoney());
        map.put("deviceLocation", deviceLocation);
        map.put("deviceSN", SPUtils.getInstance().getString("IMEI", ""));
        map.put("deviceIP", TextUtils.isEmpty(IPV4) ? NetworkUtils.getIPAddress(true) : IPV4);

        HttpFactory.getJftSk().jftApplyPaySms(map)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<JftApplyPaySmsBean>() {
                    @Override
                    public void success(JftApplyPaySmsBean data) {
                        if ("8888".equals(data.getStatus())) {
                            Map<String,Object> dataMap= (Map<String, Object>) data.getMessage();
                            chSerialNo = dataMap.get("chSerialNo").toString();
                            tranStatus = dataMap.get("tranStatus").toString();
                            orderNo = dataMap.get("orderNo").toString();
                            mCountDownTimer.startCount();
                        } else {
                            showToast(data.getMessage().toString()+",短信获取失败~");
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        showToastError("短信获取失败~");
                    }
                }));
    }

    /***
     * 畅捷 收款提交
     */
    private void initNetDataInfo() {
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        map.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        map.put("crediteid", mDataBean.getCrediteid());
        map.put("debitid", mDataBean.getDebitid());
        map.put("money", mDataBean.getMoney());

        HttpFactory.getJftSk().skInfo(map)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<JftpayInfoBean>() {
                    @Override
                    public void success(JftpayInfoBean data) {
                        String mStatus = data.getStatus();
                        if ("9999".equals(mStatus)) {
                            mTvMoney.setText(data.getInfo().getMoney() + "元");
                            mTvName.setText(data.getInfo().getRealname());
                            mTvNo.setText(data.getInfo().getIdcard());
                            mTvFeeMoney.setText(data.getInfo().getFee_money());
                            mTvDebitCard.setText(getEnd4(data.getInfo().getTo_bank_name(),
                                    data.getInfo().getTo_bank_num()));
                            mTvCreditCard.setText(getEnd4(data.getInfo().getOut_bank_name(),
                                    data.getInfo().getOut_bank_num()));
                            mEtPhone.setText(data.getInfo().getPhone());


                        } else {
                            DyToast.getInstance().error(data.getMessage());
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));
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

    /***
     * 畅捷 收款提交
     */
    private void initNetDataSubmit() {
//        if (TextUtils.equals("131452100", mEtCode.getText())) {
//            ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
//            map.put("token", getToken());
//            map.put("crediteid", mDataBean.getCrediteid());
//            map.put("debitid", mDataBean.getDebitid());
//            map.put("money", mDataBean.getMoney());
//            map.put("areacode", mCityCode);
//            HttpFactory.getJftSk().skApply(map)
//                    .compose(HttpObserver.schedulers(getAty()))
//                    .as(HttpObserver.life(this))
//                    .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
//                        @Override
//                        public void success(HttpResponseData data) {
//                            String mStatus = data.status;
//                            if ("9999".equals(mStatus)) {
//                                DyToast.getInstance().success(data.message);
//                                finish();
//                            } else {
//                                showToastError(data.message);
//                            }
//                        }
//
//                        @Override
//                        public void error(Throwable data) {
//                            MyLogger.dLog().e(data);
//                            showToastError(data.getMessage());
//                        }
//                    }));
//            return;
//        }
        if (TextUtils.isEmpty(chSerialNo)) {
//            chSerialNo="123";
//            orderNo="123";
            showToast("收款单号获取失败！");
            return;
        }

        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        map.put("token", getToken());
        map.put("user_id", getToken());
        map.put("chSerialNo", chSerialNo);
        map.put("checkCode", mEtCode.getText().toString());
        map.put("orderNo", orderNo);
        HttpFactory.getJftSk().jftApplyConfirm(map)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        String mStatus = data.status;
                        if ("9999".equals(mStatus)) {
                            toastDialogOk(data.message);
                        } else {
                            showToastError(data.message);
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        showToastError(data.getMessage());
                    }
                }));
    }

    private void toastDialogOk(String message){
        new RemindDialog(this,message).setCancel(false).setCallBack(new RemindDialog.CallBack() {
            @Override
            public void confirm() {
                finish();
            }
        }).setLifecycle(getLifecycle()).show();

    }
    private void initDataLocation() {
        AmapLocationUils.initLocation(this, new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                    deviceLocation =
                            "+" + aMapLocation.getLatitude() + "/+" + aMapLocation.getLongitude();
                    showLog("onLocationChanged: " + aMapLocation.getCity());
                    showLog("onLocationChanged: " + aMapLocation.getDistrict());
                    String province = aMapLocation.getProvince();//省信息
                    String city = aMapLocation.getCity();//城市信息
                    String district = aMapLocation.getDistrict();//城区信息
                    String street = aMapLocation.getStreet();//街道信息
                    mTvCity.setText(province + city);
                    mCityCode = aMapLocation.getCityCode();
                } else {
                    showToast("定位失败，请开启GPS和定位权限!");
                }
            }
        });
    }

    private void initDataGetIpv4() {
        String url = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip";
        String header = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like " +
                "Gecko) Chrome/51.0.2704.7 Safari/537.36";
        HttpFactory.getJftSk().getIP4(url, header).compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<Ipv4Bean>() {
                    @Override
                    public void success(Ipv4Bean data) {
                        IPV4 = data.data.ip;
                    }

                    @Override
                    public void error(Throwable data) {

                    }
                }));
        ;
    }

}
