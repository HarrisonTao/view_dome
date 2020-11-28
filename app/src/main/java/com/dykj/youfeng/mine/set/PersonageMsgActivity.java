package com.dykj.youfeng.mine.set;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.BaseToolsUtil;
import com.dykj.module.util.DialogUtils;
import com.dykj.module.util.GlideUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.PhotoGallery.GlideEngine;
import com.dykj.module.util.toast.DyToast;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.UserInfoBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.tools.ArithUtil;
import com.dykj.youfeng.tools.MmvkUtlis;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 个人信息
 */
public class PersonageMsgActivity extends BaseActivity {
    @BindView(R.id.iv_image)
    RoundedImageView ivImage;
    @BindView(R.id.rl_setting_header)
    RelativeLayout rlSettingHeader;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.tv_true_name)
    TextView tvTrueName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_rl_card)
    TextView tvRlCard;
    @BindView(R.id.rl_card)
    RelativeLayout rlCard;
    @BindView(R.id.tv_recommend)
    TextView tvRecommend;
    @BindView(R.id.rl_call)
    RelativeLayout rlCall;
    @BindView(R.id.tv_reg_time)
    TextView tvRegTime;
    private String mPhotoPath;

    @Override
    public int intiLayout() {
        return R.layout.activity_personage_msg;
    }

    @Override
    public void initData() {
        initTitle("个人信息");
        UserInfoBean userInfo = new MmvkUtlis().getUserInfo();
        tvNickName.setText(TextUtils.isEmpty(userInfo.nickName) ? "您未设置昵称" : userInfo.nickName);
        tvRegTime.setText(userInfo.regTime);
        tvTrueName.setText(TextUtils.isEmpty(userInfo.realname) ? "您未实名" : userInfo.realname);
        tvPhone.setText((userInfo.phone));
        tvRlCard.setText(TextUtils.isEmpty(userInfo.idcard) ? "您未实名" : ArithUtil.hintIdNum(userInfo.idcard));
        tvRecommend.setText(TextUtils.isEmpty(userInfo.recommend) ? "暂无推荐人" : userInfo.recommend);
        GlideUtils.setImage(ivImage, R.mipmap.moren_tx, userInfo.image);
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


    @OnClick({R.id.rl_setting_header, R.id.rl_etnick_name, R.id.rl_face})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.rl_setting_header:
                showSetHeaderDialog();
                break;


            case R.id.rl_etnick_name:
                // 修改昵称
                startAct(ChangeNickActiivty.class);
                break;

            case R.id.rl_face:
                // 人脸识别
                startAct(FaceActivity.class);
                break;


        }
    }

    private void showSetHeaderDialog() {
        DialogUtils.getInstance()
                .with(this)
                .setlayoutPosition(Gravity.BOTTOM)
                .setlayoutPading(0, 0, 0, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FROM_BOTTOM)
                .setlayoutId(R.layout.dialog_set_header)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    view.findViewById(R.id.tv_camera).setOnClickListener(v -> {
                        DialogUtils.dismiss();
                        getPhoto(true);
                    });
                    view.findViewById(R.id.tv_photo).setOnClickListener(v -> {
                        DialogUtils.dismiss();
                        getPhoto(false);
                    });
                    view.findViewById(R.id.tv_cancel).setOnClickListener(v -> DialogUtils.dismiss());

                })
                .show();
    }

    /**
     * 选择照片
     */
    public void getPhoto(boolean isCamera) {
        DialogUtils.dismiss();
        if (isCamera) {
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
        } else {
            PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .maxSelectNum(1)// 最大图片选择数量 int
                    .imageSpanCount(4)// 每行显示个数 int
                    .previewImage(true)// 是否可预览图片 true or false
                    .isCamera(false)// 是否显示拍照按钮 true or false
                    .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                    .loadImageEngine(GlideEngine.createGlideEngine())
                    .compress(true)// 是否压缩 true or false
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        }
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
                            mPhotoPath = localMedia.getCompressPath();
                            MyLogger.dLog().e("图片路径 " + 0 + " == " + mPhotoPath);
                        }
                        if (mPhotoPath != null) {
                            upAvatar(mPhotoPath);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 上传头像
     *
     * @param mPhotoPath
     */
    private void upAvatar(String mPhotoPath) {
        String token = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        File file = new File(mPhotoPath);
        MultipartBody build = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .addFormDataPart("token", token)
                .build();


        HttpFactory.getInstance().upAvatar(build)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(PersonageMsgActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<String>>() {
                    @Override
                    public void success(HttpResponseData<String> data) {
                        if ("9999".equals(data.status)) {
                            MMKV.defaultMMKV().encode(AppCacheInfo.mImage, data.getData());
                            GlideUtils.setImage(ivImage, R.mipmap.moren_tx, data.getData());
//                            Glide.with(getAty()).load(data.getData()).apply(new RequestOptions().centerCrop())
//                                    .into(ivImage);
                            EventBus.getDefault().postSticky(AppCacheInfo.mRefreshUserInfo);
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

    @Override
    protected void onResume() {
        super.onResume();
        String nickName = MMKV.defaultMMKV().decodeString(AppCacheInfo.mNickName, "");
        tvNickName.setText(TextUtils.isEmpty(nickName) ? "您未设置昵称" : nickName);
    }
}
