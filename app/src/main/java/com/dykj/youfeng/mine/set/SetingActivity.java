package com.dykj.youfeng.mine.set;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.ApkDownloadUtils;
import com.dykj.module.util.BaseToolsUtil;
import com.dykj.module.util.DialogUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.SmsBean;
import com.dykj.youfeng.mode.VersionBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.start.LoginActivity;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.tools.DataCleanManager;
import com.dykj.youfeng.tools.MmvkUtlis;
import com.tencent.mmkv.MMKV;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cretin.www.cretinautoupdatelibrary.utils.AppUtils.getVersionName;

public class SetingActivity extends BaseActivity {
    @BindView(R.id.tv_cancle)
    TextView tvCancle;
    @BindView(R.id.tv_version)
    TextView tvVersionName;

    @BindView(R.id.tv_call_number)
    TextView tvCallNumber;

    @BindView(R.id.tv_change_phone_Relative)
    RelativeLayout tvChangePhoneRelative;
    @BindView(R.id.tv_change_psw_relative)
    RelativeLayout tvChangePswRelative;
    @BindView(R.id.phoneText)
    TextView phoneText;

    private String mServiceTel;


    @Override
    public int intiLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void initData() {
        initTitle("设置");
        String mPhone=MMKV.defaultMMKV().decodeString(AppCacheInfo.mPhone);
        if(mPhone!=null){
            String str=mPhone.substring(0,3);
            String str2=mPhone.substring(7,11);
            phoneText.setText(str+"****"+str2);
        }

    }

    @Override
    public void doBusiness() {
        try {
            tvCancle.setText("" + DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
            tvCancle.setText("0");
        }

        kfService();
        tvVersionName.setText("V" + getVersionName(this));

    }

    @OnClick({R.id.tv_change_phone_Relative, R.id.tv_change_psw_relative, R.id.tv_deal_psw, R.id.rl_kf_call, R.id.rl_clear_cache, R.id.rl_versions, R.id.tv_Quit})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.tv_change_phone_Relative:
                // 更换手机号
                startAct(getAty(), ChangePhoneActivity.class, "1");
                break;

            case R.id.tv_change_psw_relative:
                // 更换登录密码
                startAct(getAty(), ChangePswActivity.class);
                break;

            case R.id.tv_deal_psw:
                // 更换交易密码
                int passType = MMKV.defaultMMKV().decodeInt(AppCacheInfo.mPayPass, 0);
                if (passType == 0) {
                    startAct(getAty(), SettingDealPswActivity.class);
                } else {
                    startAct(getAty(), ChangeDealPswActivity.class);
                }
                break;

            case R.id.rl_kf_call:
                // 拨打客服电话
                BaseToolsUtil.askPermissions(this, new BaseToolsUtil.PermissionInterface() {
                    @Override
                    public void ok() {
                        if (!TextUtils.isEmpty(mServiceTel)) {
                            showCallDialog();
                        }
                    }

                    @Override
                    public void error() {
                        DyToast.getInstance().warning("您已拒绝拨打电话相关权限，无法使用该功能");
                        finish();
                    }
                }, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE);


                break;

            case R.id.rl_versions:
                // 检查版本更新
                checkVersion();
                break;

            case R.id.rl_clear_cache:
                // 清理缓存
                showClearCache();
                break;

            case R.id.tv_Quit:
                // 退出登录
                showQuitDialog();
                break;
        }
    }

    /**
     * 退出登录
     */
    private void showQuitDialog() {
        DialogUtils.getInstance()
                .with(this)
                .setlayoutPosition(Gravity.CENTER)
                .setlayoutPading(140, 0, 140, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FADE_IN)
                .setlayoutId(R.layout.dialog_public)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    TextView tvTitle = view.findViewById(R.id.tv_title);
                    tvTitle.setText("确定退出登录?");
                    view.findViewById(R.id.tv_content).setVisibility(View.GONE);
                    view.findViewById(R.id.tv_cancel).setOnClickListener(v -> DialogUtils.dismiss());
                    view.findViewById(R.id.tv_safety_quit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MMKV.defaultMMKV().encode(AppCacheInfo.mTokent, "");
                            new MmvkUtlis().removeUserInfo();
                            finish();
                            DialogUtils.dismiss();

                            startAct(LoginActivity.class);
                        }
                    });
                })
                .show();
    }


    /**
     * 清理缓存
     */
    private void showClearCache() {
        DialogUtils.getInstance()
                .with(this)
                .setlayoutPosition(Gravity.CENTER)
                .setlayoutPading(140, 0, 140, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FADE_IN)
                .setlayoutId(R.layout.dialog_public)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    TextView tvTitle = view.findViewById(R.id.tv_title);
                    tvTitle.setText("清理缓存?");

                    TextView tvContent = view.findViewById(R.id.tv_content);


                    String mCancleSize = tvCancle.getText().toString().trim();
                    SpannableString s2 = new SpannableString("是否清除" + mCancleSize + "缓存？");
                    s2.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    s2.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.main_text)), 4, mCancleSize.length() + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    s2.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), mCancleSize.length() + 4, s2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvContent.setText(s2);


                    view.findViewById(R.id.tv_cancel).setOnClickListener(v -> DialogUtils.dismiss());
                    view.findViewById(R.id.tv_safety_quit).setOnClickListener(v -> {
                        DataCleanManager.clearAllCache(SetingActivity.this);
                        try {
                            tvCancle.setText("" + DataCleanManager.getTotalCacheSize(SetingActivity.this));
                        } catch (Exception e) {
                            tvCancle.setText("0 k");
                        }
                        DialogUtils.dismiss();
                    });
                })
                .show();
    }

    /**
     * 版本更新
     */
    private void checkVersion() {
        HttpFactory.getInstance().checkVersion("1")
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(SetingActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<List<VersionBean>>>() {
                    @Override
                    public void success(HttpResponseData<List<VersionBean>> data) {
                        if ("9999".equals(data.status)) {
                            VersionBean versionBean = data.getData().get(0);
                            if (!TextUtils.isEmpty(versionBean.version_code)) {
                                if (!versionBean.version_code.equals(getVersionName(getAty()))) {
                                    showUpVersionDialog(versionBean);
                                } else {
                                    DyToast.getInstance().success("已是最新版本~");
                                }
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
     * 拨打客服电话
     */
    private void showCallDialog() {
        DialogUtils.getInstance()
                .with(this)
                .setlayoutPosition(Gravity.CENTER)
                .setlayoutPading(140, 0, 140, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FADE_IN)
                .setlayoutId(R.layout.dialog_public)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    TextView tvTel = view.findViewById(R.id.tv_content);
                    tvTel.setText(mServiceTel);

                    view.findViewById(R.id.tv_cancel).setOnClickListener(v -> DialogUtils.dismiss());
                    view.findViewById(R.id.tv_safety_quit).setOnClickListener(v -> {
                        BaseToolsUtil.call(SetingActivity.this, mServiceTel);
                        DialogUtils.dismiss();
                    });
                })
                .show();
    }


    /**
     * 客服中心
     */
    private void kfService() {
        HttpFactory.getInstance().kfService()
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(SetingActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<SmsBean>>() {

                    @Override
                    public void success(HttpResponseData<SmsBean> data) {
                        if ("9999".equals(data.status)) {
                            mServiceTel = data.getData().serviceTel;
                            tvCallNumber.setText(mServiceTel + "");
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

    private void showUpVersionDialog(VersionBean versionBean) {
        DialogUtils.getInstance()
                .with(this)
                .setlayoutPosition(Gravity.CENTER)
                .setlayoutPading(140, 0, 140, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FADE_IN)
                .setlayoutId(R.layout.up_version_dialog)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    TextView tvMsg = view.findViewById(R.id.tv_msg);
                    tvMsg.setText(versionBean.remark);

                    view.findViewById(R.id.tv_cancel).setOnClickListener(v -> DialogUtils.dismiss());
                    view.findViewById(R.id.tv_up_version).setOnClickListener(v -> {
                        ApkDownloadUtils.getInstance().downLoad(this, versionBean.url);
                        DialogUtils.dismiss();
                    });
                })
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
