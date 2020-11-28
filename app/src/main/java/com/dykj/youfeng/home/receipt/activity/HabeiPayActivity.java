package com.dykj.youfeng.home.receipt.activity;

import android.content.Intent;
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
import com.dykj.module.util.BaseToolsUtil;
import com.dykj.module.util.CountDownUtil;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.WheelPickerBean;
import com.dykj.module.util.WheelPickerUtils;
import com.dykj.module.util.toast.DyToast;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.QuickChannelBean;
import com.dykj.youfeng.mode.SmsBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.tools.JsonUtil;
import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;

/**
 * 花呗支付确定界面
 */
public class HabeiPayActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_fee_money)
    TextView tvFeeMoney;
    @BindView(R.id.tv_debit_card)
    TextView tvDebitCard;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.ll_city)
    LinearLayout llCity;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_getCode)
    TextView tvGetCode;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;


    private String mCityCode = "";
    private CountDownUtil mCountDownTimer;

    private Intent intent;
    //储蓄卡ID
    private  String mDebitId="";
    private QuickChannelBean.ChannelBean bean;
    private  String money="";
    private  String stagesNum="";
    //银行卡号
    private  String bank_num="";
    private String mCode;

    private CountDownUtil mCount;
    @Override
    public int intiLayout() {
        return R.layout.activity_huabei_pay_layout;
    }

    @Override
    public void initData() {
        initTitle("收款信息确认");
        intent=getIntent();
        mDebitId= intent.getStringExtra("data");
        bean= (QuickChannelBean.ChannelBean) intent.getSerializableExtra("data2");
        money=intent.getStringExtra("money");

        tvMoney.setText(money);
        tvFeeMoney.setText(setFeeMoney(money,bean)+"元");

       // mCount = new CountDownUtil(tvGetCode);


        getCard(mDebitId);
    }

    private  double setFeeMoney(String money,QuickChannelBean.ChannelBean bean){
                double dMoney=convertToDouble(money,0);
                double rate = convertToDouble(bean.rate,0);
                double cost=convertToDouble(bean.cost,0);
                double div=div(rate,1000,4);
                        double sum=mul(dMoney,div);
        return add(sum,cost);
    }



    @Override
    public void doBusiness() {
        tvName.setText(MMKV.defaultMMKV().decodeString(AppCacheInfo.mRealname, "")+"");
        tvNo.setText(MMKV.defaultMMKV().decodeString(AppCacheInfo.mIdcard, "")+"");
        mCountDownTimer = new CountDownUtil(tvGetCode);
    }


    @OnClick({R.id.tv_getCode, R.id.tv_submit,R.id.tv_city})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_getCode:
                String mPhone = etPhone.getText().toString();
                if (TextUtils.isEmpty(mPhone)) {
                    showToast("请输入手机号码!");
                    return;
                }
                if (!BaseToolsUtil.isCellphone(mPhone)) {
                    showToast("请输入手机格式不正确!");
                    return;
                }
                getSmsCode(mPhone);
                break;
            case R.id.tv_city:
                showArea();
                break;
            case R.id.tv_submit:
                String smsCode = etCode.getText().toString();
                String mPhone1 = etPhone.getText().toString();
                if (TextUtils.isEmpty(mPhone1)) {
                    showToast("请输入手机号码!");
                    return;
                }
                if (!BaseToolsUtil.isCellphone(mPhone1)) {
                    showToast("请输入手机格式不正确!");
                    return;
                }
                /*
              if (TextUtils.isEmpty(mCode)) {
                    DyToast.getInstance().warning("请获取验证码");
                    return;
                }
                if (!mCode.equals(smsCode)) {
                    DyToast.getInstance().warning("验证码不合法!");
                    return;
                }
                if (TextUtils.isEmpty(stagesNum)) {
                    DyToast.getInstance().warning("请选择分期!");
                    return;
                }*/

                resementData(mPhone1);
                break;
        }
    }


    private  void resementData(String phone){
        Map<String, Object> parms = new HashMap<>();
        parms.put("userId", MMKV.defaultMMKV().decodeString(AppCacheInfo.mUid, ""));
        parms.put("toCardNo",bank_num);
        parms.put("toPhoneNo", phone );
        parms.put("IdCardNo", MMKV.defaultMMKV().decodeString(AppCacheInfo.mIdcard, ""));
        parms.put("name", MMKV.defaultMMKV().decodeString(AppCacheInfo.mRealname, ""));
        parms.put("orderAmount", money);
        double rate = convertToDouble(bean.rate,0);
        parms.put("tradeFee", div(rate,10,2));
        parms.put("feeAmount", bean.cost);
        parms.put("stagesNum", stagesNum);

        Log.d("wtf",new Gson().toJson(parms));

        HttpFactory.getInstance().hbPay(parms)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(HabeiPayActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<ResponseBody>() {
                    @Override
                    public void success(ResponseBody body) {
                        if (null != mCount) {
                            mCount.onFinish();
                        }
                        String rStr= "";
                        try {
                            rStr = body.string();
                            Log.d("wtf",rStr);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                     /*   {"status":"9999","message":"\u8fd4\u56de\u6210\u529f",
                                "info":"{\"success\":false,\"code\":null,\"" +
                                "message\":\"fastpayFee\u4e0d\u5408\u6cd5\",\"dateTime\":\"2020-06-24 18:06:55.506\"," +
                                "\"data\":null}"}*/

                        String info=JsonUtil.getJsonData(rStr,"info");

                        String type=JsonUtil.getJsonData(info,"success");
                        if ("true".equals(type)) {
                            String dataStr=JsonUtil.getJsonData(info,"data");
                            String pay=JsonUtil.getJsonData(dataStr,"pay");

                            Intent intent=new Intent(HabeiPayActivity.this,ImageActivity.class);
                            intent.putExtra("data",pay);
                            startActivity(intent);
                            finish();

                        } else {
                            String message= JsonUtil.getJsonData(info,"message");
                            DyToast.getInstance().error(message);
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                        Log.d("wtf",data.getMessage());
                    }
                }));

    }




    /**
     * 显示省市区弹框
     *
     *
     */
    private void showArea( ) {
        final ArrayList<WheelPickerBean> beans = new ArrayList<>();
                WheelPickerBean   wBease=new WheelPickerBean("6","6期");
                WheelPickerBean   wBease1=new WheelPickerBean("12","12期");
                beans.add(wBease);
                beans.add(wBease1);

                WheelPickerUtils.getInstance().initCustomOptionPicker(getAty(), "请选择分期", beans, current -> {
                   String mProvince = beans.get(current).getContext();
                    tvCity.setText(mProvince);
                    stagesNum=beans.get(current).getId();
                });


    }


    /**
     * 获取短信验证码
     *
     * @param mPhone
     */
    private void getSmsCode(String mPhone) {
        HttpFactory.getInstance().sendSms(mPhone, "getPass")
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(HabeiPayActivity.this))
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






    private void showToast(String message) {
        DyToast.getInstance().warning(message);
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
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }


    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, 2);

    }


    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.divide(b2, scale, BigDecimal.ROUND_UNNECESSARY).doubleValue();

    }

    /**
     * 获取储蓄卡
     * @param debitid
     */
    private  void getCard(String debitid){
        Map<String, Object> parms = new HashMap<>();
        parms.put("debitid",debitid);
        Log.d("wtf",new Gson().toJson(parms));
        HttpFactory.getInstance().getCard(parms)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(HabeiPayActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<ResponseBody>() {
                    @Override
                    public void success(ResponseBody body) {
                        String rStr= "";
                        try {
                            rStr = body.string();
                            Log.d("wtf",rStr);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String type= JsonUtil.getJsonData(rStr,"status");
                        if ("9999".equals(type)) {
                           String info=JsonUtil.getJsonData(rStr,"info");

                            bank_num=JsonUtil.getJsonData(info,"bank_num");
                            tvDebitCard.setText(bank_num);
                            etPhone.setText(JsonUtil.getJsonData(info,"phone"));
                         /*   {"status":"9999","message":"\u8fd4\u56de\u6210\u529f",
                                    "info":{"id":584,"bank_num":"6226183168681681",
                                    "bank_number":"310290000013",
                                    "bank_name":"\u6d66\u53d1\u94f6\u884c",
                                    "phone":"18670353700"}}*/

                        } else {
                            String message= JsonUtil.getJsonData(rStr,"message");
                            DyToast.getInstance().error(message);
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                        Log.d("wtf",data.getMessage());
                    }
                }));

    }



}
