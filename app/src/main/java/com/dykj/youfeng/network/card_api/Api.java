package com.dykj.youfeng.network.card_api;

import com.dykj.module.net.HttpResponseData;
import com.dykj.youfeng.home.base.CardResponse;
import com.dykj.youfeng.home.base.Planlist;
import com.dykj.youfeng.home.base.Quick;
import com.dykj.youfeng.mode.AgentListBean;
import com.dykj.youfeng.mode.AreaBean;
import com.dykj.youfeng.mode.BalanceBean;
import com.dykj.youfeng.mode.BankListBean;
import com.dykj.youfeng.mode.BonusIndexBean;
import com.dykj.youfeng.mode.CardpaymentBean;
import com.dykj.youfeng.mode.CashApplyBean;
import com.dykj.youfeng.mode.CashListBean;
import com.dykj.youfeng.mode.ChannelList;
import com.dykj.youfeng.mode.CjBankBean;
import com.dykj.youfeng.mode.CjSkInfoBean;
import com.dykj.youfeng.mode.CommonRepayBean;
import com.dykj.youfeng.mode.CreditcardListBean;
import com.dykj.youfeng.mode.DebitcardListBean;
import com.dykj.youfeng.mode.DsBindCardData;
import com.dykj.youfeng.mode.GroupBean;
import com.dykj.youfeng.mode.HomeBean;
import com.dykj.youfeng.mode.IndexBillList;
import com.dykj.youfeng.mode.IntegralBean;
import com.dykj.youfeng.mode.JftSmsBean;
import com.dykj.youfeng.mode.KfBean;
import com.dykj.youfeng.mode.MsgInfoBean;
import com.dykj.youfeng.mode.OneMoreRepaymetBean;
import com.dykj.youfeng.mode.PayWxBean;
import com.dykj.youfeng.mode.PlanInfoBean;
import com.dykj.youfeng.mode.QuickChannelBean;
import com.dykj.youfeng.mode.QuickInfoBean;
import com.dykj.youfeng.mode.QuickListBean;
import com.dykj.youfeng.mode.RepaymentPlanListBean;
import com.dykj.youfeng.mode.RepaymentPreviewBean;
import com.dykj.youfeng.mode.RepaymentPreviewPlanBean;
import com.dykj.youfeng.mode.ShareBean;
import com.dykj.youfeng.mode.ShareListBean;
import com.dykj.youfeng.mode.SignInfoBean;
import com.dykj.youfeng.mode.SignListBean;
import com.dykj.youfeng.mode.SmsBean;
import com.dykj.youfeng.mode.SystemMegBean;
import com.dykj.youfeng.mode.UserInfoBean;
import com.dykj.youfeng.mode.VersionBean;
import com.dykj.youfeng.mode.VipBean;
import com.dykj.youfeng.mode.YbBankList;
import com.dykj.youfeng.mode.YbSkSubmitBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface Api {

    /**
     * 用户注册
     */
    @POST("login/register")
    @FormUrlEncoded
    Observable<HttpResponseData<String>> userRegister(@FieldMap Map<String, Object> map);

    /**
     * 注册协议
     */
    @POST("question/register")
    Observable<HttpResponseData<String>> questionRegister();

    /**
     * 操作手册
     */
    @POST("question/help")
    Observable<HttpResponseData<String>> questionHelp();


    /**
     * 用户登录
     */
    @POST("login/login")
    @FormUrlEncoded
    Observable<HttpResponseData<String>> userLogin(@FieldMap Map<String, Object> map);

    /**
     * 短信验证码
     * register   注册
     * getPass    找回密码
     * modPhone   修改手机号
     * newmodPhone 修改手机信号
     * modPayPass  修改交易密码
     * bindBank  绑卡短信
     * channel 渠道进件
     */

    @POST("sms/index")
    @FormUrlEncoded
    Observable<HttpResponseData<SmsBean>> sendSms(@Field("phone") String phone, @Field("smsCode") String coede);


    /**
     * 忘记密码
     */
    @POST("login/forGetPassword")
    @FormUrlEncoded
    Observable<HttpResponseData<String>> forGetPassword(@Field("userIphone") String userIphone, @Field("password") String password, @Field("repPassword") String repPassword);


    /**
     * 用户信息
     */
    @POST("index/info_user")
    @FormUrlEncoded
    Observable<HttpResponseData<UserInfoBean>> getUserInfo(@Field("token") String token);


    /**
     * 修改手机号 原手机号验证
     */
    @POST("login/checkPhone")
    @FormUrlEncoded
    Observable<HttpResponseData> loginCheckPhone(@Field("token") String token, @Field("userIphone") String userIphone);

    /**
     * 修改手机号 提交
     */
    @POST("login/editPhone")
    @FormUrlEncoded
    Observable<HttpResponseData> editPhone(@Field("token") String token, @Field("userIphone") String userIphone);

    /**
     * 修改密码
     */
    @POST("login/editPassword")
    @FormUrlEncoded
    Observable<HttpResponseData> editPassword(@FieldMap Map<String, Object> map);


    /**
     * 设置支付密码
     */
    @POST("login/setPayPassword")
    @FormUrlEncoded
    Observable<HttpResponseData> setPayPassword(@FieldMap Map<String, Object> map);


    /**
     * 修改交易密码
     */
    @POST("login/editPayPassword")
    @FormUrlEncoded
    Observable<HttpResponseData> editPayPassword(@FieldMap Map<String, Object> map);


    /**
     * 修改昵称
     */
    @POST("login/editNickname")
    @FormUrlEncoded
    Observable<HttpResponseData> editNick(@FieldMap Map<String, Object> map);


    /**
     * 重置支付密码
     */
    @POST("login/setNewPayPassword")
    @FormUrlEncoded
    Observable<HttpResponseData> setNewPayPassword(@FieldMap Map<String, Object> map);


    /**
     * 头像上传
     */
    @POST("login/upload")
    Observable<HttpResponseData<String>> upAvatar(@Body RequestBody Body);


    /**
     * 我的团队
     */
    @POST("index/listing_team")
    @FormUrlEncoded
    Observable<HttpResponseData<GroupBean>> meTeamList(@FieldMap Map<String, Object> map);


    /**
     * 我的积分
     */
    @POST("index/listing_points")
    @FormUrlEncoded
    Observable<HttpResponseData<IntegralBean>> mePointsList(@FieldMap Map<String, Object> map);


    /**
     * 实名认证图片上传
     */
    @POST("authenticate/upload")
    Observable<HttpResponseData<String>> authenticateUpload(@Body RequestBody Body);


    /**
     * 实名认证
     */
    @POST("authenticate/realName")
    @FormUrlEncoded
    Observable<HttpResponseData> realName(@FieldMap Map<String, Object> map);


    /**
     * 升级
     */
    @POST("index/reinfopay")
    @FormUrlEncoded
    Observable<HttpResponseData<VipBean>> upVip(@Field("token") String token);

    /**
     * 余额支付
     */
    @POST("index/buy_vip")
    @FormUrlEncoded
    Observable<HttpResponseData> acountBuy(@Field("token") String token, @Field("pass") String pass, @Field("type") String type);

    /**
     * 微信支付
     */
    @POST("wechat/index")
    @FormUrlEncoded
    Observable<HttpResponseData<PayWxBean>> wechatBuy(@Field("token") String token, @Field("type") String type);


    /**
     * 支付宝支付
     */
    @POST("alipay/index")
    @FormUrlEncoded
    Observable<HttpResponseData<String>> aliPay(@Field("token") String token, @Field("type") String type);


    /**
     * 我的收益
     */
    @POST("index/listing_money")
    @FormUrlEncoded
    Observable<HttpResponseData<BalanceBean>> listMoney(@Field("token") String token, @Field("page") String page);


    /**
     * 首页
     */
    @POST("index/index")
    @FormUrlEncoded
    Observable<HttpResponseData<HomeBean>> homeIndex(@Field("token") String token);

    /**
     * 获取还款计划
     * api/user/planlist
     */
    @POST("user/planlist")
    @FormUrlEncoded
    //@Headers("content-type:application/x-www-form-urlencoded")
    Observable<HttpResponseData<Planlist>> planlist(@Field("token") String token,@Field("page") int page,@Field("limit") int limit);

    /**
     * 版本更新
     */
    @POST("index/version")
    @FormUrlEncoded
    Observable<HttpResponseData<List<VersionBean>>> checkVersion(@Field("type") String type);


    /**
     * 公告列表
     */
    @POST("article/listing")
    @FormUrlEncoded
    Observable<HttpResponseData<SystemMegBean>> articleList(@Field("token") String token, @Field("page") String page);

    /**
     * 消息列表
     */
    @POST("message/listing")
    @FormUrlEncoded
    Observable<HttpResponseData<SystemMegBean>> msgList(@Field("token") String token, @Field("page") String page);


    /**
     * 消息详情
     */
    @POST("message/info")
    @FormUrlEncoded
    Observable<HttpResponseData<MsgInfoBean>> msgInfo(@Field("token") String token, @Field("messageId") String messageId);


    /**
     * 在线客服
     */
    @POST("question/listing")
    Observable<HttpResponseData<KfBean>> kfList();

    /**
     * 在线客服 详情
     */
    @POST("question/info")
    @FormUrlEncoded
    Observable<HttpResponseData<String>> kfInfo(@Field("token") String token, @Field("id") String id);

    /**
     * 客服中心
     */
    @POST("question/service")
    Observable<HttpResponseData<SmsBean>> kfService();

    /**
     * 分享
     */
    @POST("share/listing_share_image")
    @FormUrlEncoded
    Observable<HttpResponseData<ShareListBean>> getShareList(@Field("token") String token);


    /**
     * 分享记录
     */
    @POST("share/listing_share")
    @FormUrlEncoded
    Observable<HttpResponseData<ShareBean>> shareList(@Field("token") String token, @Field("page") String page);

    /**
     * 交易记录
     */
    @POST("index/bill_list")
    @FormUrlEncoded
    Observable<HttpResponseData<IndexBillList>> getIndexBillList(@FieldMap ConcurrentHashMap<String, Object> map);


    /**
     * 积分查询
     */
    @POST("Sign/index")
    @FormUrlEncoded
    Observable<HttpResponseData<SignInfoBean>> signIndex(@Field("token") String token);


    /**
     * 检测是否签到
     */
    @POST("sign/signStatus")
    @FormUrlEncoded
    Observable<HttpResponseData> checkIsSign(@Field("token") String token);


    /**
     * 签到
     */
    @POST("Sign/signIn")
    @FormUrlEncoded
    Observable<HttpResponseData<Integer>> signSignIn(@Field("token") String token);

    /**
     * 签到日期
     */
    @POST("Sign/dates")
    @FormUrlEncoded
    Observable<HttpResponseData> signDates(@Field("token") String token);


    /**
     * 签到记录
     */
    @POST("Sign/assigned")
    @FormUrlEncoded
    Observable<HttpResponseData<SignListBean>> signLog(@Field("token") String token, @Field("page") String page);


    /**
     * 排行榜
     */
    @POST("bonus/index")
    @FormUrlEncoded
    Observable<HttpResponseData<BonusIndexBean>> bonusIndex(@FieldMap ConcurrentHashMap<String, Object> map);


    /**
     * 月榜
     */
    @POST("agent/month")
    @FormUrlEncoded
    Observable<HttpResponseData<List<AgentListBean>>> bonusAgentMonthList(@Field("token") String token, @Field("month") String month, @Field("page") String page);


    /**
     * 半年榜
     */
    @POST("agent/halfYear")
    @FormUrlEncoded
    Observable<HttpResponseData<List<AgentListBean>>> bonusAgentYearList(@Field("token") String token, @Field("year") String year, @Field("type") String type, @Field("page") String page);


    /**
     * 提现记录
     */
    @POST("cash/listing")
    @FormUrlEncoded
    Observable<HttpResponseData<CashListBean>> cashList(@Field("token") String token, @Field("page") String page);


    /**
     * 提现申请界面
     */
    @POST("cash/add_msg")
    @FormUrlEncoded
    Observable<HttpResponseData<CashApplyBean>> cashApply(@Field("token") String token);


    /**
     * 提现申请
     */
    @POST("cash/add")
    @FormUrlEncoded
    Observable<HttpResponseData> addCash(@FieldMap Map<String, Object> map);


    /**
     * 银行发卡行列表
     */
    @POST("index/list_banks")
    @FormUrlEncoded
    Observable<HttpResponseData<List<BankListBean>>> bankList(@Field("token") String token);


    /**
     * 添加储蓄卡  城市列表
     */
    @POST("repayment/area")
    @FormUrlEncoded
    Observable<HttpResponseData<List<AreaBean>>> areaList(@Field("token") String token, @Field("fid") String fid);


    /**
     * 添加储蓄卡
     */
    @POST("debitcard/add")
    @FormUrlEncoded
    Observable<HttpResponseData> addDebitcard(@FieldMap Map<String, Object> map);


    /**
     * 储蓄卡 列表
     */
    @POST("debitcard/listing")
    @FormUrlEncoded
    Observable<HttpResponseData<List<DebitcardListBean>>> debitcardList(@Field("token") String token, @Field("status") String status, @Field("page") String page);

    /**
     * 删除储蓄卡
     */
    @POST("debitcard/del")
    @FormUrlEncoded
    Observable<HttpResponseData> deleteDebitcard(@Field("token") String token, @Field("debitid") String debitid);


    /**
     * 添加信用卡
     */
    @POST("creditcard/add")
    @FormUrlEncoded
    Observable<HttpResponseData> addCreditcard(@FieldMap Map<String, Object> map);

    /**
     * 信用卡 列表
     *
     * http://yfapi.xunyunsoft.com/api/creditcard/listing?status=2&page=1&token=0df08a38c2377292c8551d2fa449752c
     */
    @POST("creditcard/listing")
    @FormUrlEncoded
    Observable<HttpResponseData<CreditcardListBean>> creditcardList(@Field("token") String token, @Field("status") String status, @Field("page") String page, @Field("limit") String limit);

    /**
     * 删除信用卡
     */
    @POST("creditcard/del")
    @FormUrlEncoded
    Observable<HttpResponseData> deleteCreditcard(@Field("token") String token, @Field("creditcardId") String debitid);


    /**
     * 编辑信用卡
     */
    @POST("creditcard/edit")
    @FormUrlEncoded
    Observable<HttpResponseData> editCreditcard(@FieldMap Map<String, Object> map);


    /**
     * 检测通道是否注册
     */
    @POST("Cardpayment/join_status")
    @FormUrlEncoded
    Observable<HttpResponseData> join_status(@Field("token") String token, @Field("channel") String channel);


    /**
     * 查看银行卡 状态
     */
    @POST("Cardpayment/card")
    @FormUrlEncoded
    Observable<HttpResponseData<CardpaymentBean>> cardpaymentStatus(@Field("token") String token, @Field("channel") String channel, @Field("id") String id);


    /**
     * 畅捷绑卡
     */
    @POST("Cardpayment/card_confirm")
    @FormUrlEncoded
    Observable<HttpResponseData> cjConfirmCard(@FieldMap Map<String, Object> map);

    /**
     * 畅捷通道注册  一卡多还
     */
    @POST("Cardpayment/reg")
    @FormUrlEncoded
    Observable<HttpResponseData> cardpaymentReg(@FieldMap Map<String, Object> map);

    /***
     * 通道注册  代还
     */
    @POST("changjie/reg")
    @FormUrlEncoded
    Observable<HttpResponseData> changeJieReg(@FieldMap Map<String, Object> map);


    /**
     * 一卡还多卡 操作协议
     */
    @POST("cardpayment/cardrepaymentInfo")
    @FormUrlEncoded
    Observable<HttpResponseData<SmsBean>> getCardrepaymentInfo(@Field("token") String token);

    /**
     * 一卡多还 制定计划
     */
    @POST("Cardpayment/preview")
    @FormUrlEncoded
    Observable<HttpResponseData<OneMoreRepaymetBean>> oneMoreRepaymetPreview(@FieldMap ConcurrentHashMap<String, Object> map);

    /**
     * 一卡多还  提交计划
     */
    @POST("Cardpayment/dosubmit")
    @FormUrlEncoded
    Observable<HttpResponseData> oneMoreDosubmit(@FieldMap ConcurrentHashMap<String, Object> map);


    /**
     * 一卡多还  计划列表
     */
    @POST("Cardpayment/listing")
    @FormUrlEncoded
    Observable<HttpResponseData<RepaymentPlanListBean>> oneMoreRepaymentList(@FieldMap ConcurrentHashMap<String, Object> map);


    /**
     * 一卡多还 取消计划
     */
    @POST("Cardpayment/cancel")
    @FormUrlEncoded
    Observable<HttpResponseData> cancelPlan(@Field("token") String token, @Field("repId") String cardId);


    /**
     * 计划详情  卡id
     */
    @POST("Cardpayment/infocard")
    @FormUrlEncoded
    Observable<HttpResponseData<PlanInfoBean>> repaymentInfoCard(@Field("token") String token, @Field("cardId") String cardId);

    /**
     * 计划详情 计划id
     */
    @POST("Cardpayment/info")
    @FormUrlEncoded
    Observable<HttpResponseData<PlanInfoBean>> repaymentInfo(@Field("token") String token, @Field("repId") String cardId);

    /**
     * 还款 通道列表
     *
     * http://yfapi.xunyunsoft.com/api/channel/listing_repay?token=bd1c22f5956c24cf1e37ff81f095afa0
     */
    @POST("channel/listing_repay")
    @FormUrlEncoded
    Observable<HttpResponseData<List<ChannelList>>> getChannelList(@Field("token") String token);


    /**
     * 代还 绑卡
     */
    @POST("changjie/card")
    @FormUrlEncoded
    Observable<HttpResponseData<CardpaymentBean>> changjieCard(@FieldMap ConcurrentHashMap<String, Object> map);

    /**
     * 代还  绑卡确认~
     */
    @POST("changjie/cardconfirm")
    @FormUrlEncoded
    Observable<HttpResponseData<SmsBean>> changjiecardconfirm(@FieldMap ConcurrentHashMap<String, Object> map);

    /**
     * 代还 计划预览 生成
     */
    @POST("repayment/preview_money")
    @FormUrlEncoded
    Observable<HttpResponseData<RepaymentPreviewBean>> repaymentPreview(@FieldMap ConcurrentHashMap<String, Object> map);


    /**
     * 计划预览
     */
    @POST("repayment/preview_plan")
    @FormUrlEncoded
    Observable<HttpResponseData<RepaymentPreviewPlanBean>> repaymentPreviewPlan(@Field("token") String token, @Field("cardId") String cardId);

    /**
     * 提交计划
     */
    @POST("repayment/sub_plan")
    @FormUrlEncoded
    Observable<HttpResponseData> repaymentPlanSubmit(@Field("token") String token, @Field("cardId") String cardId, @Field("mccString") String mccString);


    /**
     * 计划列表
     */
    @POST("repayment/listing")
    @FormUrlEncoded
    Observable<HttpResponseData<RepaymentPlanListBean>> oneRepaymentList(@FieldMap ConcurrentHashMap<String, Object> map);

    /**
     * 计划详情  计划id
     */
    @POST("repayment/info")
    @FormUrlEncoded
    Observable<HttpResponseData<CommonRepayBean>> commonRepaymentInfo(@Field("token") String token, @Field("repId") String cardId);


    /**
     * 计划详情  卡id
     * repayment_id
     */
    @POST("repayment/cardInfo")
    @FormUrlEncoded
    Observable<HttpResponseData<CommonRepayBean>> commonRepaymentCardInfo(@Field("token") String token, @Field("cardId") String cardId);


    /**
     * 普通还款 取消计划
     */
    @POST("repayment/cancel_repayment")
    @FormUrlEncoded
    Observable<HttpResponseData> commonCancelPlan(@Field("token") String token, @Field("repId") String cardId);


    /**
     * 收款 通道列表
     */
    @POST("channel/listing_quick")
    @FormUrlEncoded
    Observable<HttpResponseData<QuickChannelBean>> quickChannelList(@Field("token") String token);

    /**
     * 畅捷收款 预览界面
     */
    @POST("Cjsk/pay_info")
    @FormUrlEncoded
    Observable<HttpResponseData<CjSkInfoBean>> cjSkPlan(@FieldMap ConcurrentHashMap<String, Object> map);

    /**
     * 8
     * 畅捷收款 提交
     */
    @POST("Cjsk/apply")
    @FormUrlEncoded
    Observable<HttpResponseData> cjSkSubmit(@FieldMap ConcurrentHashMap<String, Object> map);


    /**
     * 检测易宝是否鉴权
     */
    @POST("Ybsk/is_check")
    @FormUrlEncoded
    Observable<HttpResponseData> ybCheck(@Field("token") String token);

    /**
     * 易宝进件
     */
    @POST("Ybsk/reg")
    @FormUrlEncoded
    Observable<HttpResponseData> ybReg(@FieldMap ConcurrentHashMap<String, Object> map);

    /**
     * 易宝收款 提交
     */
    @POST("Ybsk/trade")
    @FormUrlEncoded
    Observable<HttpResponseData<YbSkSubmitBean>> ybSkSumbit(@FieldMap ConcurrentHashMap<String, Object> map);


    /**
     * 收款列表
     */
    @POST("index/listing_quick")
    @FormUrlEncoded
    Observable<HttpResponseData<QuickListBean>> quickList(@Field("token") String token, @Field("page") String page);

    /**
     * 收款详情
     */
    @POST("index/info_quick")
    @FormUrlEncoded
    Observable<HttpResponseData<QuickInfoBean>> quickInfo(@Field("token") String token, @Field("id") String id);


    /***
     * 易宝注册支持银行列表
     */
    @POST("Ybsk/bank_yb")
    @FormUrlEncoded
    Observable<HttpResponseData<YbBankList>> ybBankList(@Field("token") String token);

    /**
     * 畅捷银行列表
     */
    @POST("cjsk/cjbank")
    @FormUrlEncoded
    Observable<HttpResponseData<List<CjBankBean>>> cjBankList(@Field("token") String token, @Field("channel") String channel);


    /**
     * 佳付通  是否进件
     */
    @POST("JftRepay/is_join")
    @FormUrlEncoded
    Observable<HttpResponseData> jftIsJoin(@Field("token") String token, @Field("code") String code);

    /**
     * jftrepay/add_quick
     * @param token

    credit_card_id
    debit_card_id
    amount
     * @return
     */
    @POST("jftrepay/add_quick")
    @FormUrlEncoded
    Observable<HttpResponseData<Quick>> addQuick(@Field("token") String token,
                                                 @FieldMap HashMap<String ,Object> map
                                       );


    @POST("JftRepay/is_join")
    @FormUrlEncoded
    Observable<HttpResponseData> jftIsJoin(@Field("token") String token, @Field("code") String code,@Field("bank_num")String bank_num);

    /**
     * 得仕 是否绑卡
     */
    @POST("dsrepay/is_card")
    @FormUrlEncoded
    Observable<HttpResponseData> dsIsCard(@Field("token") String token, @Field("cardid") String cardid);

    /**
     * 得仕 开卡
     */
    @POST("dsrepay/open_card")
    @FormUrlEncoded
    Observable<HttpResponseData<DsBindCardData>> dsOpenCard(@Field("token") String token, @Field("cardid") String cardid, @Field("ip") String ip);

    /**
     * 得仕 提交计划
     */
    @POST("dsrepay/submit")
    @FormUrlEncoded
    Observable<HttpResponseData> dsSubmit(@Field("token") String token, @Field("cardid") String cardid);

    /**
     * 佳付通  是否开卡
     */
    @POST("JftRepay/is_card")
    @FormUrlEncoded
    Observable<HttpResponseData> jftIsBindCard(@Field("token") String token, @Field("cardid") String cardid, @Field("code") String code);

    /**
     * 佳付通  进件
     */
   @POST("JftRepay/card_info")
    @FormUrlEncoded
    Observable<HttpResponseData> jftIncome(@FieldMap ConcurrentHashMap<String, Object> map);


    @POST("JftRepay/card_info_add")
    @FormUrlEncoded
    Observable<HttpResponseData> cardInfoAdd(@FieldMap ConcurrentHashMap<String, Object> map);

    /**
     * 校验是否进件
     * Cardpayment/join_status
     */
    @POST("Jftrepay/is_join")
    @FormUrlEncoded
    Observable<HttpResponseData> isPayOpen(@Field("token") String token);

    /**
     * 佳付通  开卡短信
     */
    @POST("JftRepay/apply_sms")
    @FormUrlEncoded
    Observable<JftSmsBean> jftApplySms(@Field("token") String token, @Field("cardid") String cardid, @Field("code") String code);

    /**
     * 佳付通  开卡确认
     */
    @POST("JftRepay/confirm")
    @FormUrlEncoded
    Observable<HttpResponseData> jftApplyConfirm(@FieldMap ConcurrentHashMap<String, Object> map);

    /**
     * 佳付通  提交计划
     */
    @POST("JftRepay/submit")
    @FormUrlEncoded
    Observable<HttpResponseData> jftSubmit(@Field("token") String token, @Field("cardid") String cardId, @Field("code") String code);

    /**
     * 佳付通  取消计划
     */
    @POST("JftRepay/cancel")
    @FormUrlEncoded
    Observable<HttpResponseData> jftCancel(@Field("token") String token, @Field("cardid") String cardId, @Field("code") String code);

    /**
     * 佳付通  地区
     */
    @POST("JftRepay/area")
    @FormUrlEncoded
    Observable<HttpResponseData<List<AreaBean>>> getJftArea(@Field("token") String token, @Field("pid") String pid);


    /**
     * 操作手册  文档
     */
    @POST("question/help")
    Observable<HttpResponseData<String>> helpBook();


         ////////////////////// 快捷通    还款 //////////////////
    /**
     * 是否绑卡
     * @param token
     * @param pid
     * @return
     */
    @POST("Kjtrepay/is_card")
    @FormUrlEncoded
    Observable<HttpResponseData> kjtIsBindCard(@Field("token") String token, @Field("cardid") String pid);


    /**
     * 绑卡短信 开卡短信
     * @param token
     * @param pid
     * @return
     */
    @POST("Kjtrepay/apply_sms")
    @FormUrlEncoded
    Observable<JftSmsBean> kjtSendSms(@Field("token") String token, @Field("cardid") String pid);



    /**
     * 快捷通 绑卡
     * @return
     */
    @POST("Kjtrepay/confirm")
    @FormUrlEncoded
    Observable<HttpResponseData> kjtBindCard(@FieldMap Map<String,Object> map);


    /**
     * 提交计划
     */
    @POST("Kjtrepay/submit")
    @FormUrlEncoded
    Observable<HttpResponseData> kjtRepaySubmit(@Field("token") String token, @Field("cardid") String pid);


    /**
     * 取消计划
     */
    @POST("Kjtrepay/cancel")
    @FormUrlEncoded
    Observable<HttpResponseData> kjtRepayCancel(@Field("token") String token, @Field("cardid") String pid);



    ////////////////////// 快捷通    还款  end  ////////////////
    ////////////////////// 快捷通    收款  start  //////////////

    /**
     * 地区信息
     * @param
     * @return
     */
    @POST("Kjtrepay/kjt_area")
    @FormUrlEncoded
    Observable<HttpResponseData<List<AreaBean>>> kjtArea (@Field("token") String token, @Field("type") String type, @Field("name") String name);


    /**
     * 下单短信
     * @param
     * @return
     */
    @POST("Kjtrepay/applyPay")
    @FormUrlEncoded
    Observable<HttpResponseData<JftSmsBean>> kftApplyPaySms (@FieldMap Map<String,Object> map);


    /**
     * 收款详情
     * @param map
     * @return
     */
    @POST("Kjtrepay/pay_info")
    @FormUrlEncoded
    Observable<HttpResponseData<CjSkInfoBean>> kjtQuickInfo (@FieldMap Map<String,Object> map);


    /**
     * 确认支付
     * @param map
     * @return
     */
    @POST("Kjtrepay/applyConfirm")
    @FormUrlEncoded
    Observable<HttpResponseData> kjtConfirmPay (@FieldMap Map<String,Object> map);

    ////////////////////// 快捷通    收款  end  ////////////////


    /**
     * 佳付通    一键还款  大额
     * @param map
     * @return
     */
    @POST("Repayment/auto")
    @FormUrlEncoded
    Observable<HttpResponseData<RepaymentPreviewBean>> getRepaymentOnePreview (@FieldMap Map<String,Object> map);





    ////////////////////// 一卡多还  国付通   ////////////////

    /**
     * 检测通道信息
     */
    @POST("Cardpayment/isSupport")
    @FormUrlEncoded
    Observable<HttpResponseData<JftSmsBean>> gftChannel (@Field("token") String token, @Field("cardId") String cardId);



    /**
     * 一卡多还 检测通道是否注册
     */
    @POST("Cardpayment/join_status")
    @FormUrlEncoded
    Observable<HttpResponseData> gftJoin_status (@Field("token") String token, @Field("cardId") String cardId);


    /**
     * 一卡多还 检测通道是否注册
     */
    @POST("Cardpayment/reg")
    @FormUrlEncoded
    Observable<HttpResponseData> gftReg (@Field("token") String token, @Field("cardId") String cardId);



    /**
     * 一卡多还 是否签约
     */
    @POST("Cardpayment/card")
    @FormUrlEncoded
    Observable<HttpResponseData> gftIsBindCrad (@Field("token") String token, @Field("cardId") String cardId);


    /**
     * 一卡多还 银行短信认证
     */
    @POST("Cardpayment/card_confirm")
    @FormUrlEncoded
    Observable<HttpResponseData> gftConfirmSms (@Field("token") String token, @Field("cardId") String cardId,@Field("smsCode") String smsCode);

    /**
     * 一卡多还 获取城市信息
     */
    @POST("Cardpayment/get_areas")
    @FormUrlEncoded
    Observable<HttpResponseData<List<AreaBean>>> gftGetAreas (@Field("id") String id,@Field("type") int type);

/**
 * 人脸识别
 * Authenticate/baiduVerifyFace
 */
@POST("Authenticate/baiduVerifyFace")
@FormUrlEncoded
Observable<HttpResponseData<RepaymentPreviewBean>> baiduVerifyFace (@FieldMap Map<String,Object> map);

/**
 * 获取认证数据
 * authenticate/resUserinfo
 */
@POST("Cardpayment/card_confirm")
@FormUrlEncoded
Observable<HttpResponseData> resUserinfo (@Field("token") String token, @Field("cardId") String cardId,@Field("smsCode") String smsCode);


/**
 * 转账功能
 * cash/transfer_accounts
 */
@POST("cash/transfer_accounts")
@FormUrlEncoded
Observable<HttpResponseData<RepaymentPreviewBean>> transferAccounts (@FieldMap Map<String,Object> map);

    /**
     * 花呗支付接口
     * debitcard/hbPay
     * http://youfeng.yuexinqg.cn/api/debitcard/hbPay
     */
    @POST("debitcard/hbPay")
    @FormUrlEncoded
 Observable<ResponseBody> hbPay (@FieldMap Map<String,Object> map);


    @GET("order/pay")
    Observable<ResponseBody> gethbPay (@QueryMap Map<String,Object> map);
    /**
     * 获取储蓄卡
     * debitcard/card_info
     */
    @POST("debitcard/card_info")
    @FormUrlEncoded
    Observable<ResponseBody> getCard(@FieldMap Map<String,Object> map);

    /**
     * VIP查询接口
     * index/vipInfo
     */
    @POST("index/vipInfo")
    @FormUrlEncoded
    Observable<HttpResponseData<VipBean>> vipInfo (@Field("token") String token);

}
