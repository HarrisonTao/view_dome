package com.dykj.youfeng.network.card_api;

import com.dykj.youfeng.mode.JftSmsBean;
import com.dykj.youfeng.mode.SmsBean;
import com.dykj.youfeng.mode.YbBankList;
import com.dykj.youfeng.mode.receipt.Ipv4Bean;
import com.dykj.youfeng.mode.receipt.JftApplyPaySmsBean;
import com.dykj.youfeng.mode.receipt.JftpayInfoBean;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.view.bean.AreaBean;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

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
 * <p>文件描述：<p>
 * <p>作者：lj<p>
 * <p>创建时间：2020/3/10<p>
 * <p>更改时间：2020/3/10<p>
 * <p>版本号：1<p>
 */
public interface JftApi {

    /**
     * 佳付通  地区
     */
    @POST("JftRepay/area")
    @FormUrlEncoded
    Observable<HttpResponseData<List<AreaBean>>> getJftArea(@Field("token") String token, @Field(
            "pid") String pid);

    ConcurrentHashMap<String, Object> params=new ConcurrentHashMap<>();

    //5. 绑卡短信申请 （针对 2003，2005无需绑卡）
    /*userid	是	string	用户id
        cardid	是	string	银行卡id
        code	是	string	类型（2005 大额商旅收款 2003 大额落地快捷）*/
    @FormUrlEncoded
    @POST("Jftpay/apply_sms")
    Observable<JftSmsBean> jftApplySms(@FieldMap ConcurrentHashMap<String, Object> params);

    //8.申请短信（2005）
    @FormUrlEncoded
    @POST("Jftpay/applyPaySms")
    Observable<JftApplyPaySmsBean> jftApplyPaySms(@FieldMap ConcurrentHashMap<String,
            Object> params);

    //9.收款提交接口（2005）
    @FormUrlEncoded
    @POST("Jftpay/applyConfirm")
    Observable<HttpResponseData<SmsBean>> jftApplyConfirm(@FieldMap ConcurrentHashMap<String,
            Object> params);
    //佳付通支持银行
    @POST("JftPay/bank_jft")
    Observable<HttpResponseData<YbBankList>> bankJft(@Query("token") String token);
    //IP地址
    @POST
    Observable<Ipv4Bean> getIP4(@Url String url, @Header("user-agent") String header);

    //废弃_13.收款短信 put("phone",phone)
    @FormUrlEncoded
    @POST("Jftpay/sms")
    Observable<HttpResponseData<SmsBean>> getSms(@FieldMap ConcurrentHashMap<String, Object> params);

    //7.收款详情 14.收款信息
    @FormUrlEncoded
    @POST(" Jftpay/pay_info")
    Observable<JftpayInfoBean> skInfo(@FieldMap ConcurrentHashMap<String, Object> params);

    //废弃_15.收款提交     map.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
    //        map.put("crediteid", mDataBean.getCrediteid());
    //        map.put("debitid", mDataBean.getDebitid());
    //        map.put("money", mDataBean.getMoney());
    //        map.put("areacode", mCityCode);*/
    @FormUrlEncoded
    @POST("Jftpay/applyPayConfirm")
    Observable<HttpResponseData> skApply(@FieldMap ConcurrentHashMap<String, Object> params);

}
