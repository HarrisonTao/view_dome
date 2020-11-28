package com.dykj.youfeng.mine.authentication;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.dykj.youfeng.R;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.view.ImageUtils;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.BaseToolsUtil;
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

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 实名认证
 */
public class AuthenticationActivity extends BaseActivity {

    @BindView(R.id.iv_1)
    ImageView iv1;
    @BindView(R.id.iv_2)
    ImageView iv2;
    @BindView(R.id.tv_next)
    TextView tvNext;
    private String mBankType;
    private String mCardUrlZ, mCardUrlF;   // 本地路径
    private String mLoadaUrlZ, mLoadaUrlF;   // 服务器路径

    @Override
    public int intiLayout() {
        return R.layout.activity_authentication;
    }

    @Override
    public void initData() {
        initTitle("实名认证");
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


    @OnClick({R.id.tv_next, R.id.ll_1, R.id.ll_2})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.tv_next:   // 下一步
                if (TextUtils.isEmpty(mCardUrlZ) || TextUtils.isEmpty(mCardUrlF)) {
                    DyToast.getInstance().warning("请上传证件!");
                    return;
                }
                upAvatar(new File(mCardUrlZ),"z");
                break;

            case R.id.ll_1:   // 身份证正面
                mBankType = "cardZ";
                getPhoto();
                break;


            case R.id.ll_2:   //身份证反面
                mBankType = "cardF";
                getPhoto();
                break;
            default:
                break;

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
               /* .addFormDataPart("id_front", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .addFormDataPart("id_backend", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), bfile))
*/
                .addFormDataPart("token", token)
                .build();

        HttpFactory.getInstance().authenticateUpload(build)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(AuthenticationActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<String>>() {
                    @Override
                    public void success(HttpResponseData<String> data) {
                        if ("9999".equals(data.status)) {
                            if ("z".equals(type)) {
                                mLoadaUrlZ = data.getData();
                                upAvatar(new File(mCardUrlF), "f");
                            } else {
                                mLoadaUrlF = data.getData();
                                startAct(AuthenticationStep2Activity.class, mLoadaUrlZ + "," + mLoadaUrlF);
                                finish();
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
     * 证件拍照
     */
    private void getPhoto() {
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(1)// 最大图片选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(false)// 是否显示拍照按钮 true or false
                .cameraFileName(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .loadImageEngine(GlideEngine.createGlideEngine())
                .compress(true)// 是否压缩 true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


    /**
     * 图片结果回调
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                            if (TextUtils.isEmpty(mBankType)) {
                                return;
                            }
                            if (bankCard != null && mBankType.equals("cardZ")) {
                                ImageUtils.loadShopImage(R.mipmap.zhengm_bg, bankCard, iv1);
                                mCardUrlZ = bankCard;
                            } else if (bankCard != null && mBankType.equals("cardF")) {
                                ImageUtils.loadShopImage(R.mipmap.fanm_bg, bankCard, iv2);
                                mCardUrlF = bankCard;
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
