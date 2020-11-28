package com.dykj.youfeng.mine.authentication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.aip.FaceSDKManager;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.dykj.youfeng.R;
import com.dykj.youfeng.baidu.FaceDetectActivity;
import com.dykj.youfeng.baidu.utils.ImageSaveUtil;
import com.dykj.youfeng.mode.UserInfoBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.tools.ArithUtil;
import com.dykj.youfeng.tools.MmvkUtlis;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 实名认证 2
 */
public class AuthenticationStep3Activity extends BaseActivity {

    private static final int REQUEST_CODE_DETECT_FACE = 1000;
    private static final int REQUEST_CODE_PICK_IMAGE = 1001;
    private String facePath;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_card_number)
    TextView tvCardNumber;


    private Bitmap mHeadBmp;
    @Override
    public int intiLayout() {
        return R.layout.activity_authentication_step3;
    }

    @Override
    public void initData() {
        initTitle("实名认证");
        UserInfoBean userInfo = new MmvkUtlis().getUserInfo();

        tvName.setText(TextUtils.isEmpty(userInfo.realname) ? "您未实名" : userInfo.realname);
        tvCardNumber.setText(TextUtils.isEmpty(userInfo.idcard) ? "您未实名" : userInfo.idcard);
    }


    @Override
    public void doBusiness() {

    }







}
