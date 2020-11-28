package com.dykj.module.activity;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.dykj.module.R;
import com.dykj.module.util.MessageWrap;
import com.dykj.module.view.imgbrowser.beans.JPhotosInfos;
import com.dykj.module.view.imgbrowser.third.beans.PhotoInfo;
import com.dykj.module.view.imgbrowser.third.widget.GFViewPager;
import com.dykj.module.view.imgbrowser.third.widget.PhotoPreviewAdapter;
import com.dykj.module.view.imgbrowser.util.JAnimationUtil;
import com.dykj.module.view.imgbrowser.util.JBitmapUtils;
import com.dykj.module.view.imgbrowser.util.JMatrixUtil;
import com.dykj.module.view.imgbrowser.util.JStringUtils;
import com.dykj.module.view.imgbrowser.util.JWindowUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * 仿朋友圈 查看图片
 * Created by jincancan on 2017/9/29.
 * Description:
 */

public class JBrowseImgActivity extends AppCompatActivity implements
        ViewPager.OnPageChangeListener, View.OnClickListener, PhotoPreviewAdapter.PhotoCallback {

    public static final String PARAMS_IMGS = "imgs";
    public static final String PARAMS_INDEX = "index";
    public static final String PARAMS_IMGS_INFO = "imgs_info";

    public static void start(Context context, ArrayList<String> imgs, int position, List<Rect> rects) {
        Intent intent = new Intent(context, JBrowseImgActivity.class);
        intent.putExtra(PARAMS_IMGS, imgs);
        intent.putExtra(PARAMS_INDEX, position);
        ArrayList<JPhotosInfos> infos = new ArrayList<>();
        for (int i = 0; i < rects.size(); i++) {
            JPhotosInfos photosInfos = new JPhotosInfos();
            infos.add(photosInfos.build(rects.get(i)));
        }
        intent.putExtra(PARAMS_IMGS_INFO, infos);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    public static void start(Context context, String imgUrl, ImageView imgView) {
        String[] mPaths = new String[]{imgUrl};
        List<Rect> rects = new ArrayList<>();
        rects.add(JMatrixUtil.getDrawableBoundsInView(imgView));
        start(context, new ArrayList<>(Arrays.asList(mPaths)), 0, rects);
    }

    public static void start(Context context, String imgUrl) {
        String[] mPaths = new String[]{imgUrl};
        List<Rect> rects = new ArrayList<>();
        start(context, new ArrayList<>(Arrays.asList(mPaths)), 0, rects);
    }


    private Activity mActivity;

    private ViewPager mViewPager;
    private RelativeLayout mRlRoot;
    private PhotoPreviewAdapter mAdapter;

    private boolean bFirstResume = true;
    private ArrayList<String> mImgs;
    private int mCurrentIndex; //
    private boolean bLocal; // 是否是本地图片
    private ArrayList<JPhotosInfos> mInfos; // 各个图片位置

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int sdk = Build.VERSION.SDK_INT;
        if (sdk >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        setContentView(R.layout.fragment_show_img);
        initData();
        initView();
    }


    public void initData() {
        mActivity = this;
//        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
//        mViews = new ArrayList<>();
        mImgs = intent.getStringArrayListExtra(PARAMS_IMGS);
        mCurrentIndex = intent.getIntExtra(PARAMS_INDEX, 0);
        mInfos = (ArrayList<JPhotosInfos>) intent.getSerializableExtra(PARAMS_IMGS_INFO);
        if (mInfos == null) {
            mInfos = new ArrayList<>();
        }
        if (mInfos.size() == 0) {
            JPhotosInfos photosInfos = new JPhotosInfos();
            mInfos.add(photosInfos.build(JMatrixUtil.getDrawableBoundsInView(findViewById(R.id.img))));
        }
        ArrayList<PhotoInfo> mPhotoList = new ArrayList<>();
//        mAdapter = new TLShowImgAdapter(mViews);
        if (mImgs != null) {
            for (int i = 0; i < mImgs.size(); i++) {
//                TLShowImageView view = new TLShowImageView(mActivity);
//                view.setImg(mImgs.get(i));
//                mViews.add(view);
                PhotoInfo info = new PhotoInfo();
                info.setPhotoPath(mImgs.get(i));
                mPhotoList.add(info);
            }
        }
        mAdapter = new PhotoPreviewAdapter(mActivity, mPhotoList, this);
    }

    public void initView() {
        if (mImgs == null || mImgs.size() == 0) {
            mActivity.finish();
            return;
        }
        mRlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        mViewPager = (GFViewPager) findViewById(R.id.vp_pager);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentIndex);
        mViewPager.addOnPageChangeListener(this);
        setPage();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bFirstResume) {
            startImgAnim();
            bFirstResume = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setPage() {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentIndex = position;
        setPage();
        MessageWrap messageWrap = MessageWrap.getInstance("JBrowseImgPageSelected",new HashMap<>());
        messageWrap.map.put("position",position);
        EventBus.getDefault().post(messageWrap);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onPhotoClick() {
        startExitAnim();
//        mActivity.finish();
    }

    @Override
    public void onDrag(float x, float y) {
        startDrag(x, y);
    }

    @Override
    public void onDragFinish() {
        if (mViewPager.getScaleX() > 0.7f) {
            mViewPager.setTranslationX(0);
            mViewPager.setTranslationY(0);
            mViewPager.setScaleX(1);
            mViewPager.setScaleY(1);
            mRlRoot.setBackgroundColor(Color.parseColor(JStringUtils.getBlackAlphaBg(1)));
        } else {
            startExitAnim();
        }
    }

    public void startExitAnim() {
        MessageWrap messageWrap = MessageWrap.getInstance("JBrowseImgPageSelected",new HashMap<>());
        messageWrap.map.put("position",mCurrentIndex);
        EventBus.getDefault().post(messageWrap);
        mRlRoot.setBackgroundColor(Color.parseColor(JStringUtils.getBlackAlphaBg(0)));
        JAnimationUtil.startExitViewScaleAnim(mViewPager, JBitmapUtils.getCurrentPicOriginalScale(mActivity, mInfos.get(mCurrentIndex)), mInfos.get(mCurrentIndex), new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mActivity.finish();
                mActivity.overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void startImgAnim() {
        JAnimationUtil.startEnterViewScaleAnim(mViewPager, JBitmapUtils.getCurrentPicOriginalScale(mActivity, mInfos.get(mCurrentIndex)), mInfos.get(mCurrentIndex), new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                mIvTest.setVisibility(View.GONE);
//                mViewPager.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        JAnimationUtil.startEnterViewAlphaAnim(mRlRoot, JBitmapUtils.getCurrentPicOriginalScale(mActivity, mInfos.get(mCurrentIndex)));
    }

    private void startDrag(float x, float y) {
        mViewPager.setTranslationX(x);
        mViewPager.setTranslationY(y);
        if (y > 0) {
            mViewPager.setPivotX(JWindowUtil.getWindowWidth(this) / 2);
            mViewPager.setPivotY(JWindowUtil.getWindowHeight(this) / 2);
            float scale = Math.abs(y) / JWindowUtil.getWindowHeight(this);
            if (scale < 1 && scale > 0) {
                mViewPager.setScaleX(1 - scale);
                mViewPager.setScaleY(1 - scale);
                mRlRoot.setBackgroundColor(Color.parseColor(JStringUtils.getBlackAlphaBg(1 - scale)));
            }
        }
        Log.i("TTTT", x + "   " + y);
    }

}
