package com.dykj.youfeng.tools;

import com.dykj.youfeng.mode.UserInfoBean;
import com.tencent.mmkv.MMKV;

public class MmvkUtlis {
    private MMKV mmkv;

    public MmvkUtlis() {
        mmkv = MMKV.defaultMMKV();
    }

    public void saveUserInfo(UserInfoBean bean) {
        mmkv.encode(AppCacheInfo.mUid,bean.id);
        mmkv.encode(AppCacheInfo.mImage, bean.image);
        mmkv.encode(AppCacheInfo.mNickName, bean.nickName);
        mmkv.encode(AppCacheInfo.mRegTime,bean.regTime);  // 注册时间
        mmkv.encode(AppCacheInfo.mRealname, bean.realname);
        mmkv.encode(AppCacheInfo.mIdcard, bean.idcard);  // 身份证号
        mmkv.encode(AppCacheInfo.mPhone, bean.phone);
        mmkv.encode(AppCacheInfo.mKefuwechat, bean.kefuwechat);

        mmkv.encode(AppCacheInfo.mRecommend,bean.recommend);  // 推荐人
        mmkv.encode(AppCacheInfo.mRealnameStatus, bean.realnameStatus);  // 0 未认证 1 认证
        mmkv.encode(AppCacheInfo.mAccountMoney, bean.accountMoney); //余额
        mmkv.encode(AppCacheInfo.mLevel, bean.level);  // 1.普通会员，2.VIP 3.黄金VIP 4.钻石VIP
        mmkv.encode(AppCacheInfo.mLevelName, bean.levelName);  // 等级名称
        mmkv.encode(AppCacheInfo.mPayPass, bean.payPass);  // 支付密码  - 1已设 0 未设
        mmkv.encode(AppCacheInfo.merchantCode,bean.merchantCode);  // 畅捷进件 - 0 未进件   其他进件
        mmkv.encode(AppCacheInfo.checkPay,bean.is_check_pay);   // 是否支付绑卡服务费
        mmkv.encode(AppCacheInfo.mPoints,bean.points); // 积分
        mmkv.encode(AppCacheInfo.messageCount,bean.messageCount); // 未读消息
        mmkv.encode(AppCacheInfo.todayMoney,bean.todayMoney); // 今日收入
    }


    public UserInfoBean getUserInfo() {
        UserInfoBean bean = new UserInfoBean();
        bean.image = mmkv.decodeString(AppCacheInfo.mImage,"");
        bean.nickName = mmkv.decodeString(AppCacheInfo.mNickName,"");
        bean.regTime = mmkv.decodeString(AppCacheInfo.mRegTime,"");
        bean.realname =  mmkv.decodeString(AppCacheInfo.mRealname,"");
        bean.idcard =  mmkv.decodeString(AppCacheInfo.mIdcard,"");
        bean.phone = mmkv.decodeString(AppCacheInfo.mPhone,"");

        bean.recommend = mmkv.decodeString(AppCacheInfo.mRecommend,"");
        bean.realnameStatus = mmkv.decodeInt(AppCacheInfo.mRealnameStatus,0);
        bean.accountMoney = mmkv.decodeString(AppCacheInfo.mAccountMoney,"");
        bean.level = mmkv.decodeInt(AppCacheInfo.mLevel,0);
        bean.levelName = mmkv.decodeString(AppCacheInfo.mLevelName,"");

        bean.payPass = mmkv.decodeInt(AppCacheInfo.mPayPass,0);
        bean.merchantCode = mmkv.decodeString(AppCacheInfo.merchantCode,"");
        bean.is_check_pay = mmkv.decodeInt(AppCacheInfo.checkPay,0);
        bean.points = mmkv.decodeInt(AppCacheInfo.mPoints,0);
        bean.messageCount = mmkv.decodeInt(AppCacheInfo.messageCount,0);
        bean.todayMoney = mmkv.decodeDouble(AppCacheInfo.todayMoney,0);
        return bean;
    }


    public void removeUserInfo() {
        mmkv.remove(AppCacheInfo.mImage);
        mmkv.remove(AppCacheInfo.mNickName);
        mmkv.remove(AppCacheInfo.mRegTime);
        mmkv.remove(AppCacheInfo.mRealname);
        mmkv.remove(AppCacheInfo.mIdcard);
        mmkv.remove(AppCacheInfo.mPhone);
        mmkv.remove(AppCacheInfo.mRecommend);
        mmkv.remove(AppCacheInfo.mRealnameStatus);
        mmkv.remove(AppCacheInfo.mAccountMoney);
        mmkv.remove(AppCacheInfo.mLevel);
        mmkv.remove(AppCacheInfo.mLevelName);
        mmkv.remove(AppCacheInfo.mPayPass);
        mmkv.remove(AppCacheInfo.merchantCode);
        mmkv.remove(AppCacheInfo.checkPay);
        mmkv.remove(AppCacheInfo.mPoints);
        mmkv.remove(AppCacheInfo.messageCount);
        mmkv.remove(AppCacheInfo.todayMoney);
        mmkv.remove(AppCacheInfo.mUid);
    }

}

