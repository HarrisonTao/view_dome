package com.dykj.youfeng.home.receipt.activity;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.YbIntentData;
import com.dykj.youfeng.mode.YbSkSubmitBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.activity.WebViewActivity;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.entity.BaseWebViewData;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.BaseToolsUtil;
import com.dykj.module.util.GlideUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.PhotoGallery.GlideEngine;
import com.dykj.module.util.toast.DyToast;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tencent.mmkv.MMKV;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 易宝进件
 */
public class YbShopInfoActivity extends BaseActivity {

    @BindView(R.id.iv_card_1)
    ImageView ivCard1;
    @BindView(R.id.iv_card_2)
    ImageView ivCard2;
    @BindView(R.id.iv_bank_1)
    ImageView ivBank1;
    @BindView(R.id.iv_bank_2)
    ImageView ivBank2;
    @BindView(R.id.ll_bank)
    LinearLayout llBank;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    private String mBankType;
    private String mCardUrlZ;  // 身份证正路径
    private String mCardUrlF;  // 身份证反路径
    private String mBankUrlZ;  // 银行卡路径
    private String mBankUrlF;  // 银行卡路径


    private String TAG = "ShopInputYBActivity";
    private ConcurrentHashMap<String, Object> prams;
    private String mDebitId;
    private YbIntentData mYbIntentData;


    @Override
    public int intiLayout() {
        return R.layout.activity_yb_shop_info;
    }

    @Override
    public void initData() {
        initTitle("易宝进件");
        mYbIntentData = (YbIntentData) getIntentData();
        mDebitId = mYbIntentData.debitid;
    }

    @Override
    public void doBusiness() {
        BaseToolsUtil.askPermissions(this, new BaseToolsUtil.PermissionInterface() {
            @Override
            public void ok() {

            }

            @Override
            public void error() {
                DyToast.getInstance().warning("您已拒绝相机相关权限，无法使用该功能");
                finish();
            }
        }, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }


    @OnClick({R.id.ll_cardZ, R.id.ll_cardF, R.id.ll_bank1, R.id.ll_bank2, R.id.tv_submit})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.ll_cardZ:
                mBankType = "cardZ";
                getPhoto();
                break;

            case R.id.ll_cardF:
                mBankType = "cardF";
                getPhoto();
                break;

            case R.id.ll_bank1:   // 银行卡 正面
                mBankType = "bankZ";
                getPhoto();
                break;


            case R.id.ll_bank2:     //银行卡 反面
                mBankType = "bankF";
                getPhoto();
                break;

            case R.id.tv_submit:  // 提交
                if (TextUtils.isEmpty(mCardUrlZ) || TextUtils.isEmpty(mCardUrlF) || TextUtils.isEmpty(mBankUrlZ) || TextUtils.isEmpty(mBankUrlF)) {
                    DyToast.getInstance().warning("请上传证件");
                    return;
                }

                upAvatar(new File(mCardUrlZ), "cradIg1");   // 上传身份证正面
                break;
        }
    }


    /***
     * 银行卡拍照
     */
    private void getPhoto() {
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(1)// 最大图片选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(false)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .loadImageEngine(GlideEngine.createGlideEngine())
                .compress(true)// 是否压缩 true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


    /**
     * 图片结果回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList.size() > 0) {
                        LocalMedia localMedia = selectList.get(0);
                        if (localMedia.isCompressed()) {
                            String bankCard = localMedia.getCompressPath();
                            MyLogger.dLog().e("图片路径 " + 0 + " == " + bankCard);

                            if (bankCard != null && mBankType.equals("cardZ")) {
                                GlideUtils.setImageFile(ivCard1, R.mipmap.zhengm_bg, bankCard);
                                mCardUrlZ = bankCard;
                            } else if (bankCard != null && mBankType.equals("cardF")) {
                                GlideUtils.setImageFile(ivCard2, R.mipmap.fanm_bg, bankCard);
                                mCardUrlF = bankCard;
                            } else if (bankCard != null && mBankType.equals("bankZ")) {   // 银行卡正面
                                GlideUtils.setImageFile(ivBank1, R.mipmap.chuxuk_bg, bankCard);
                                mBankUrlZ = bankCard;
                            } else if (bankCard != null && mBankType.equals("bankF")) {  // 合照
                                GlideUtils.setImageFile(ivBank2, R.mipmap.hezhao_bg, bankCard);
                                mBankUrlF = bankCard;
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 上传证件
     *
     * @param file
     * @param type
     */
    private void upAvatar(File file, String type) {
        String token = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        MultipartBody build = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .addFormDataPart("token", token)
                .build();

        HttpFactory.getInstance().authenticateUpload(build)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<String>>() {
                    @Override
                    public void success(HttpResponseData<String> data) {
                        if ("9999".equals(data.status)) {
                            String image = data.getData();
                            if (mCardUrlF != null && type.equals("cradIg1")) {   // 上传身份证反面
                                prams = new ConcurrentHashMap<>();
                                prams.put("idCardPhoto", image);
                                upAvatar(new File(mCardUrlF), "cradIg2");
                            } else if (type.equals("cradIg2") && mBankUrlZ != null) {  // 上传银行卡正面
                                prams.put("idCardBackPhoto", image);
                                upAvatar(new File(mBankUrlZ), "bank1");
                            } else if (type.equals("bank1") && mBankUrlF != null) {    // 上传银行卡反面
                                prams.put("BankCardPhoto", image);
                                upAvatar(new File(mBankUrlF), "mBankF");
                            } else if (type.equals("mBankF")) {
                                prams.put("PersonPhoto", image);     // 银行卡与身份证及本人上半身合照
                                // 注册易宝通道
                                ybReg(prams);
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


    /***
     * 注册易宝
     * @param prams
     */
    private void ybReg(ConcurrentHashMap<String, Object> prams) {
        prams.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        prams.put("debitid", mDebitId.replace(".0", ""));

        HttpFactory.getInstance().ybReg(prams)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("9999".equals(data.status)) {
                            ybSumbit();
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
     * 易宝收款
     */
    private void ybSumbit() {
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        map.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        map.put("crediteid",mYbIntentData.crediteid);
        map.put("debitid", mDebitId);
        map.put("amount", mYbIntentData.money);
        HttpFactory.getInstance().ybSkSumbit(map)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<YbSkSubmitBean>>() {
                    @Override
                    public void success(HttpResponseData<YbSkSubmitBean> data) {
                        if ("9999".equals(data.status)) {
                            BaseWebViewData webViewData = new BaseWebViewData();
                            webViewData.title = "消费申请";
                            webViewData.content = data.getData().url;
                            webViewData.isOut = true;
                            startAct(WebViewActivity.class,webViewData);
                            finish();
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


}
