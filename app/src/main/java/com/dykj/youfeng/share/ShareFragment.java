package com.dykj.youfeng.share;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import com.dykj.module.base.BaseFragment;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.BaseToolsUtil;
import com.dykj.module.util.DialogUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.ShareListBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.share.activity.ShareLogActivity;
import com.dykj.youfeng.share.adapter.ShareAdapter;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.view.ZoomOutPageTransformer;
import com.tencent.mmkv.MMKV;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;


public class ShareFragment extends BaseFragment {
    @BindView(R.id.vp_gallery_vp)
    ViewPager vpGalleryVp;
    @BindView(R.id.ll_gallery_outer)
    RelativeLayout llGalleryOuter;
    private SHARE_MEDIA shareMedia;
    private String signImage = "signImage";
    private String shareFileName = "";

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case 0:
                    if (shareMedia == null) {
                        DyToast.getInstance().success("保存成功");
                    } else {
                        share(shareMedia);
                    }

                    break;
                case 1:
                    DyToast.getInstance().success("保存失败");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected int intiLayout() {
        return R.layout.fragment_share;
    }

    @Override
    protected void onViewReallyCreated(View view) {
    }

    @Override
    public void doBusiness() {
    }

    @Override
    public void onResume() {
        super.onResume();
        requestShareData();
    }

    @OnClick({R.id.tv_share_log, R.id.tv_share})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.tv_share_log: //  分享记录
                startAct(this, ShareLogActivity.class);
                break;
            case R.id.tv_share:
                BaseToolsUtil.askPermissions(getActivity(), new BaseToolsUtil.PermissionInterface() {
                    @Override
                    public void ok() {
                        showShareDialog();
                    }

                    @Override
                    public void error() {

                    }
                }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
            default:
                break;
        }
    }

    private void requestShareData() {
        HttpFactory.getInstance().getShareList(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""))
                .compose(HttpObserver.schedulers(getActivity()))
                .as(HttpObserver.life(ShareFragment.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<ShareListBean>>() {
                    @Override
                    public void success(HttpResponseData<ShareListBean> data) {
                        if ("9999".equals(data.status)) {
                            ShareListBean data1 = data.getData();
                            setViewData(data1);
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

    private void setViewData(ShareListBean bean) {
        ShareAdapter shareAdapter = new ShareAdapter(bean.list, bean.url, getActivity());
        shareAdapter.setSharePhone(MMKV.defaultMMKV().decodeString(AppCacheInfo.mPhone));
        vpGalleryVp.setAdapter(shareAdapter);
        vpGalleryVp.setOffscreenPageLimit(bean.list.size());//设置预加载数量
        vpGalleryVp.setPageMargin(10);//控制两幅图之间的间距
        vpGalleryVp.setPageTransformer(true, new ZoomOutPageTransformer());
        //viewPager左右两边滑动无效的处理
        llGalleryOuter.setOnTouchListener((view, motionEvent) -> vpGalleryVp.dispatchTouchEvent(motionEvent));
    }

    /**
     * 分享 dialog
     */
    public void showShareDialog() {
        DialogUtils.getInstance().with(getContext())
                .setlayoutPosition(Gravity.BOTTOM)
                .setlayoutPading(0, 0, 0, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FROM_BOTTOM)
                .setlayoutId(R.layout.dialog_share_view)
                .setlayoutPading(0, 0, 0, 0)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    view.findViewById(R.id.dialog_share_wechat).setOnClickListener(v -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {//Build.VERSION_CODES.Q
                            shareMedia = SHARE_MEDIA.WEIXIN;
                            saveBitmap(((ShareAdapter) vpGalleryVp.getAdapter()).getCurrentItemView(), System.currentTimeMillis() + "");
                        } else {
                            share(SHARE_MEDIA.WEIXIN);
                        }
                        DialogUtils.dismiss();
                    });
                    view.findViewById(R.id.dialog_share_friends).setOnClickListener(v -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {//Build.VERSION_CODES.Q
                            shareMedia = SHARE_MEDIA.WEIXIN_CIRCLE;
                            saveBitmap(((ShareAdapter) vpGalleryVp.getAdapter()).getCurrentItemView(), System.currentTimeMillis() + "");
                        } else {
                            share(SHARE_MEDIA.WEIXIN_CIRCLE);
                        }

                        DialogUtils.dismiss();
                    });
                    view.findViewById(R.id.dialog_share_qq).setOnClickListener(v -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {//Build.VERSION_CODES.Q
                            shareMedia = SHARE_MEDIA.QQ;
                            saveBitmap(((ShareAdapter) vpGalleryVp.getAdapter()).getCurrentItemView(), System.currentTimeMillis() + "");

                        } else {
                            share(SHARE_MEDIA.QQ);
                        }

                        DialogUtils.dismiss();
                    });

                    view.findViewById(R.id.dialog_share_save).setOnClickListener(v -> new Thread(new Runnable() {
                        @Override
                        public void run() {
                            shareMedia = null;
                            saveBitmap(((ShareAdapter) vpGalleryVp.getAdapter()).getCurrentItemView(), System.currentTimeMillis() + "");
                            DialogUtils.dismiss();
                        }
                    }).start());

                    view.findViewById(R.id.dialog_share_cancel).setOnClickListener(v -> DialogUtils.dismiss());
                })
                .show();
    }

    private void share(SHARE_MEDIA platform) {
        UMImage image;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {//Build.VERSION_CODES.Q
            image = new UMImage(getActivity(), new File(shareFileName));//本地文件
        } else {
            Bitmap bitmap = getViewBitmap(((ShareAdapter) Objects.requireNonNull(vpGalleryVp.getAdapter())).getCurrentItemView());
            image = new UMImage(getActivity(), bitmap);
        }

        new ShareAction(getActivity())
                .withText("")
                .setPlatform(platform)
                .withMedia(image)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        DyToast.getInstance().success("分享成功");
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        if (throwable != null) {
                            Log.d("throw", "throw:" + throwable.getMessage());
                        }
                        DyToast.getInstance().success("分享失败");

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        DyToast.getInstance().success("分享取消");

                    }
                })
                .share();
    }

    /**
     * 保存图片
     */
    public void saveBitmap(View v, String name) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "优丰";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = name + ".jpg";
        File file = new File(appDir, fileName);
        Bitmap bm = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        v.draw(canvas);
        Log.e("TAG", "保存图片");
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {//Build.VERSION_CODES.Q
                file = saveSignImageBox(fileName, bm);
            } else {
                FileOutputStream out = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
                mHandler.sendEmptyMessage(0);
                Log.e("TAG", "已经保存" + file.getAbsolutePath());
            }

        } catch (FileNotFoundException e) {
            Log.e("TAG", "保存文件失败");
            mHandler.sendEmptyMessage(1);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("TAG", "保存失败");
            mHandler.sendEmptyMessage(1);
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        // 最后通知图库更新
        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }

    //将文件保存到沙盒中
//注意：
//1. 这里的文件操作不再需要申请权限
//2. 沙盒中新建文件夹只能再系统指定的子文件夹中新建
    public File saveSignImageBox(String fileName, Bitmap bitmap) {
        try {
            //图片沙盒文件夹
            File PICTURES = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFileDirctory = new File(PICTURES + "/" + signImage);
            if (imageFileDirctory.exists()) {
                File imageFile = new File(PICTURES + "/" + signImage + "/" + fileName);
                FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                shareFileName = imageFile.getAbsolutePath();
                mHandler.sendEmptyMessage(0);
                Log.e("TAG", "已经保存" + shareFileName);
                return imageFile;
            } else if (imageFileDirctory.mkdir()) {//如果该文件夹不存在，则新建
                //new一个文件
                File imageFile = new File(PICTURES + "/" + signImage + "/" + fileName);
                //通过流将图片写入
                FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                shareFileName = imageFile.getAbsolutePath();
                mHandler.sendEmptyMessage(0);
                Log.e("TAG", "已经保存" + shareFileName);
                return imageFile;
            } else {
                Log.e("TAG", "保存失败");
                mHandler.sendEmptyMessage(1);
            }

        } catch (Exception e) {
            Log.e("TAG", "保存失败");
            mHandler.sendEmptyMessage(1);
            e.printStackTrace();
        }
        return null;
    }


    public Bitmap getViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getContext()).onActivityResult(requestCode, resultCode, data);
    }

}
