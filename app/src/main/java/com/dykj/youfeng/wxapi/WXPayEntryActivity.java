package com.dykj.youfeng.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.dykj.youfeng.home.repayment.activity.AddDepositCardActivity;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.AppConstant;
import com.dykj.module.util.toast.DyToast;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private String TAG = "WXPayEntryActivity";
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, null);
        // 将该app注册到微信
        api.registerApp(AppConstant.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }


    /**
     * 处理微信发出的向第三方应用请求app message
     * <p>
     * 在微信客户端中的聊天页面有“添加工具”，可以将本应用的图标添加到其中
     * 此后点击图标，下面的代码会被执行。Demo仅仅只是打开自己而已，但你可
     * 做点其他的事情，包括根本不打开任何页面
     */
    public void onGetMessageFromWXReq(WXMediaMessage msg) {
        if (msg != null) {
            Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(getPackageName());
            startActivity(iLaunchMyself);
        }
    }

    /**
     * 处理微信向第三方应用发起的消息
     * <p>
     * 此处用来接收从微信发送过来的消息，比方说本demo在wechatpage里面分享
     * 应用时可以不分享应用文件，而分享一段应用的自定义信息。接受方的微信
     * 客户端会通过这个方法，将这个信息发送回接收方手机上的本demo中，当作
     * 回调。
     * <p>
     * 本Demo只是将信息展示出来，但你可做点其他的事情，而不仅仅只是Toast
     */
    public void onShowMessageFromWXReq(WXMediaMessage msg) {
        if (msg != null && msg.mediaObject != null
                && (msg.mediaObject instanceof WXAppExtendObject)) {
            WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
            Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
        }
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq resp) {
    }


    private String payType;

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void event(String event) {
        if (TextUtils.isEmpty(event)) {
            return;
        }
        payType = event;
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp baseResp) {
        Log.e(TAG, baseResp.errCode + "---");
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                // 支付成功
                DyToast.getInstance().success("支付成功!");
                if (!TextUtils.isEmpty(payType)) {
                    EventBus.getDefault().postSticky(AppCacheInfo.mRefreshUserInfo);
                    //会员升级
                    if (payType.equals(AppCacheInfo.mUpVip)) {
                        EventBus.getDefault().postSticky(AppCacheInfo.wxPaySuccess);
                        // 储蓄卡首次绑卡
                    } else if (payType.equals(AppCacheInfo.mBindCredit)) {
                        startActivity(new Intent(this, AddDepositCardActivity.class));
                    }else if (payType.equals(AppCacheInfo.wxOrderPay)) {
                        // 信用卡首次绑卡
                        EventBus.getDefault().postSticky(AppCacheInfo.wxOrderPaySuccess);
                    }
                }
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                // 支付取消
                DyToast.getInstance().warning("您已取消改交易");
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //   发送被拒绝
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                // 不支持错误
                break;
            default:
                Log.e("BaseResp.getType = ", "-----------");
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
