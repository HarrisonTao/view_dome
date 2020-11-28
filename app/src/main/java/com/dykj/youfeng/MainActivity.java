package com.dykj.youfeng;


import android.Manifest;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;


import com.blankj.utilcode.util.LogUtils;
import com.dykj.module.util.BaseToolsUtil;
import com.dykj.youfeng.home.fragment.HomeFragment;

import com.dykj.youfeng.mall.MallFragment;
import com.dykj.youfeng.mine.fragment.MeFragment;
import com.dykj.youfeng.mode.UserInfoBean;
import com.dykj.youfeng.mode.VersionBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.share.ShareFragment;
import com.dykj.youfeng.start.LoginActivity;
import com.dykj.youfeng.tools.AmapLocationUils;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.Flag;
import com.dykj.youfeng.tools.MmvkUtlis;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.base.BaseFragment;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.ApkDownloadUtils;
import com.dykj.module.util.AppManager;
import com.dykj.module.util.BaseEventData;
import com.dykj.module.util.DialogUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.google.gson.Gson;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

import static com.cretin.www.cretinautoupdatelibrary.utils.AppUtils.getVersionCode;
import static com.cretin.www.cretinautoupdatelibrary.utils.AppUtils.getVersionName;

public class MainActivity extends BaseActivity {

    @BindView(R.id.rb_home)
    RadioButton rbHome;
    @BindView(R.id.rb_mall)
    RadioButton rbMall;
    @BindView(R.id.rb_share)
    RadioButton rbShare;
    @BindView(R.id.rb_mine)
    RadioButton rbMine;
    @BindView(R.id.radio_group_button)
    RadioGroup radioGroupButton;


    private String TAG = "MALL";  // 默认选中第一个
    private HomeFragment homeFragment;
 // private MallFragment mallFragment;
   /// private ShareFragment shareFragment;
    private MeFragment meFragment;

    @Override
    public int intiLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        //版本跟新
      // checkVersion();

        BaseToolsUtil.askPermissions(this, new BaseToolsUtil.PermissionInterface() {
                    @Override
                    public void ok() {
                    }

                    @Override
                    public void error() {
                        DyToast.getInstance().warning("您已拒绝定位相关权限，无法使用定位功能");
                    }
                }, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if(homeFragment!=null){
            //homeFragment.planlist();
        }

    }

    @Override
    public void doBusiness() {
        homeFragment = new HomeFragment();
        replace(homeFragment);
        radioGroupButton.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_home:
                    if (homeFragment == null) {
                        homeFragment = new HomeFragment();
                    }
                    TAG = "HOME";
                    replace(homeFragment);
                    QMUIStatusBarHelper.setStatusBarLightMode(this);
                    break;
                case R.id.rb_mall:
                  /*  if (mallFragment == null) {
                        mallFragment = new MallFragment();
                    }
                    TAG = "MALL";
                    replace(mallFragment);*/
                    //QMUIStatusBarHelper.setStatusBarLightMode(this);
                    break;
                case R.id.rb_share:
                    /*if (TextUtils.isEmpty(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""))) {
                        startAct(LoginActivity.class);
                    } else {
                        if (shareFragment == null) {
                            shareFragment = new ShareFragment();
                        }
                        TAG = "SHARE";
                        replace(shareFragment);
                        QMUIStatusBarHelper.setStatusBarLightMode(this);
                    }*/
                    break;
                case R.id.rb_mine:
                    if (TextUtils.isEmpty(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""))) {
                        startAct(LoginActivity.class);
                    } else {
                        if (meFragment == null) {
                            meFragment = new MeFragment();
                        }
                        TAG = "MINE";
                        replace(meFragment);
                        //状态栏字体改成白色
                        QMUIStatusBarHelper.setStatusBarDarkMode(this);
                    }
                    break;
                default:
                    break;
            }
        });
    }


    // 防止从注册界面返回  引导栏点击错乱.
    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(TAG)) {
            if (TAG.equals("HOME")) {
                rbHome.setChecked(true);
              //  rbMall.setChecked(false);
              //  rbShare.setChecked(false);
                rbMine.setChecked(false);
            } else if (TAG.equals("MALL")) {
              //  rbMall.setChecked(true);
                rbHome.setChecked(true);
             //   rbShare.setChecked(false);
                rbMine.setChecked(false);
            }
//            else if(TAG.equals("SHARE")){
//                rbMall.setChecked(false);
//                rbHome.setChecked(false);
//                rbShare.setChecked(true);
//                rbMine.setChecked(false);
//            }else if(TAG.equals("MINE")){
//                rbMall.setChecked(false);
//                rbHome.setChecked(false);
//                rbShare.setChecked(false);
//                rbMine.setChecked(true);
//            }
        }
    }

    private BaseFragment from = null;

    private void replace(BaseFragment fragment) {
        if (from != null && from == fragment) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (from == null) {
            if (!fragment.isAdded()) {
                transaction.add(R.id.fl_content, fragment).commitAllowingStateLoss();
            } else {
                transaction.show(fragment).commitAllowingStateLoss();
            }
        } else if (!fragment.isAdded()) {
            // 隐藏当前的fragment，add下一个到Activity中
            transaction.hide(from).add(R.id.fl_content, fragment).commitAllowingStateLoss();
        } else {
            // 隐藏当前的fragment，显示下一个
            transaction.hide(from).show(fragment).commitAllowingStateLoss();
        }
        from = fragment;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(BaseEventData data) {
       LogUtils.e("--->" + data.key);
        if (data.key == Flag.Event.JUMP_LOGIN) {
            startAct(LoginActivity.class);
        } else if (data.key == Flag.Event.JUMP_TAB2) {
            rbMall.setChecked(true);
            replace(homeFragment);
        }

    }

    private long backtime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
           if (from != homeFragment) {
               rbMall.toggle();
                return true;
            } else if (System.currentTimeMillis() - backtime > 1200) {
                DyToast.getInstance().info("再次返回退出应用");
                backtime = System.currentTimeMillis();
                return true;
            } else {
                AppManager.getAppManager().AppExit(this);
//                finish();
            }
        }

        return super.onKeyDown(keyCode, event);
    }


    /**
     * 检测版本更新
     */
    private void checkVersion() {
        HttpFactory.getInstance().checkVersion("1")
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(MainActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<List<VersionBean>>>() {
                    @Override
                    public void success(HttpResponseData<List<VersionBean>> data) {
                        Log.d("wtf",new Gson().toJson(data));
                        if ("9999".equals(data.status)) {
                            VersionBean versionBean = data.getData().get(0);
                            if (!TextUtils.isEmpty(versionBean.version_code)) {
                                if (!versionBean.version_code.equals(getVersionName(getAty()))) {
                                    LogUtils.e(versionBean.version_code + "~~~" +getVersionName(getAty() ));
                                    showUpVersionDialog(versionBean);
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
                        ApkDownloadUtils.getInstance().downLoad(MainActivity.this, versionBean.url);
                        DialogUtils.dismiss();
                    });
                })
                .show();
    }


    @Subscribe(sticky = true)
    public void onEvent(String event) {
        if (!TextUtils.isEmpty(event) && event.equals(AppCacheInfo.mRefreshUserInfo)) {
            LogUtils.e(TAG,"-------MainAct----mRefreshUserInfo");
            initNetDataUpUserInfo();
        }
    }

    /**
     * 请求用户信息
     */
    private void initNetDataUpUserInfo() {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        if (TextUtils.isEmpty(mToken)){
            return;
        }
        HttpFactory.getInstance().getUserInfo(mToken)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(MainActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<UserInfoBean>>() {
                    @Override
                    public void success(HttpResponseData<UserInfoBean> data) {
                        if ("9999".equals(data.status)) {
                            new MmvkUtlis().saveUserInfo(data.getData());
                            if (null != homeFragment){
                                homeFragment.setMsgStaus();
                                homeFragment.planlist();
                                homeFragment.requestHomeData();
                            }
                        } else {
                            DyToast.getInstance().info(data.message);
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}

