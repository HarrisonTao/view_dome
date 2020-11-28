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
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.aip.FaceSDKManager;
import com.dykj.youfeng.R;
import com.dykj.youfeng.baidu.FaceDetectActivity;
import com.dykj.youfeng.baidu.utils.ImageSaveUtil;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.google.gson.Gson;
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
public class AuthenticationStep2Activity extends BaseActivity {

    private static final int REQUEST_CODE_DETECT_FACE = 1000;
    private static final int REQUEST_CODE_PICK_IMAGE = 1001;
    private String facePath;
    @BindView(R.id.tv_name)
    EditText tvName;
    @BindView(R.id.tv_card_number)
    EditText tvCardNumber;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    private Bitmap mHeadBmp;
    String[] split;
    @Override
    public int intiLayout() {
        return R.layout.activity_authentication_step2;
    }

    @Override
    public void initData() {
        initTitle("实名认证");
        init();



        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* String urlStr = (String) getIntentData();
                String[] split = urlStr.split(",");
                String name = tvName.getText().toString().trim();
                String idCard = tvCardNumber.getText().toString().trim();


                if (TextUtils.isEmpty(name)) {
                    DyToast.getInstance().warning("请输入姓名!");
                    return;
                }
                if (TextUtils.isEmpty(idCard)) {
                    DyToast.getInstance().warning("请输入证件号码!");
                    return;
                }
                Map<String, Object> parms = new HashMap<>();
                parms.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
                parms.put("realName",name);
                parms.put("idCard", idCard);
                parms.put("image1", split[0]);
                parms.put("image2", split[1]);
                HttpFactory.getInstance().realName(parms)
                        .compose(HttpObserver.schedulers(getAty()))
                        .as(HttpObserver.life(AuthenticationStep2Activity.this))
                        .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                            @Override
                            public void success(HttpResponseData data) {
                                Log.d("wtf",new Gson().toJson(data));
                                if ("9999".equals(data.status)) {*/
                Intent it = new Intent(AuthenticationStep2Activity.this, FaceDetectActivity.class);
                startActivityForResult(it, REQUEST_CODE_DETECT_FACE);

                  /*              } else {
                                    DyToast.getInstance().error(data.message);
                                }
                            }

                            @Override
                            public void error(Throwable data) {
                                MyLogger.dLog().e(data);
                                DyToast.getInstance().error(data.getMessage());
                            }
                           */
            }
        });


    }


    @Override
    public void doBusiness() {

    }


    private void init() {
        // 如果图片中的人脸小于200*200个像素，将不能检测出人脸，可以根据需求在100-400间调节大小
        FaceSDKManager.getInstance().getFaceTracker(this).set_min_face_size(200);
        FaceSDKManager.getInstance().getFaceTracker(this).set_isCheckQuality(true);
        // 该角度为商学，左右，偏头的角度的阀值，大于将无法检测出人脸，为了在1：n的时候分数高，注册尽量使用比较正的人脸，可自行条件角度
        FaceSDKManager.getInstance().getFaceTracker(this).set_eulur_angle_thr(45, 45, 45);
        FaceSDKManager.getInstance().getFaceTracker(this).set_isVerifyLive(true);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DETECT_FACE && resultCode == Activity.RESULT_OK) {

            facePath = ImageSaveUtil.loadCameraBitmapPath(this, "head_tmp.jpg");
            if (mHeadBmp != null) {
                mHeadBmp.recycle();
            }
            mHeadBmp = ImageSaveUtil.loadBitmapFromPath(this, facePath);
            if (mHeadBmp != null) {


                setFcae(mHeadBmp);
            }

        }
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    /**
     * 设置人脸
     * @param bitmap 人脸图片
     */
    private  void  setFcae(Bitmap bitmap){
        String name = tvName.getText().toString().trim();
        String idCard = tvCardNumber.getText().toString().trim();


        if (TextUtils.isEmpty(name)) {
            DyToast.getInstance().warning("请输入姓名!");
            return;
        }
        if (TextUtils.isEmpty(idCard)) {
            DyToast.getInstance().warning("请输入证件号码!");
            return;
        }
        String urlStr = (String) getIntentData();
        split = urlStr.split(",");
        Map<String, Object> parms = new HashMap<>();
        parms.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        parms.put("face_img",bitmapToBase64(bitmap));
        parms.put("card_number", idCard );
        parms.put("user_name", name);
        parms.put("id_front", split[0]);
        parms.put("id_backend", split[1]);
        Log.d("wtf", split[0]);
        Log.d("wtf",split[1]);
        Log.d("wtf",idCard);
        Log.d("wtf",name);
        HttpFactory.getInstance().baiduVerifyFace(parms)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(AuthenticationStep2Activity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {

                        if ("9999".equals(data.status)) {
                        EventBus.getDefault().postSticky(AppCacheInfo.mRefreshUserInfo);
                                    MMKV.defaultMMKV().encode(AppCacheInfo.mRealnameStatus,1);
                                    MMKV.defaultMMKV().encode(AppCacheInfo.mRealname,name);
                                    MMKV.defaultMMKV().encode(AppCacheInfo.mIdcard,idCard);
                                    DyToast.getInstance().success(data.message);
                                    finish();


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

    public String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.NO_CLOSE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }



}
