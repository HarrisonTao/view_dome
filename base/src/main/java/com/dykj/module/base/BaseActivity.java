package com.dykj.module.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.dykj.module.R;
import com.dykj.module.util.AppManager;
import com.dykj.module.util.MyLogger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @author DIYUN
 * @date 2019/4/22.
 * description：activity基类
 */
public abstract class BaseActivity extends FragmentActivity {
    public Object mIntentData;
    protected Unbinder bind;
    private TextView tvTitle;
    public TextView tvRight;
    private ImageView ivRight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局
        setContentView(R.layout.activity_base);
        tvTitle = findViewById(R.id.tv_title);
        tvRight = findViewById(R.id.tv_right);
        ivRight = findViewById(R.id.iv_right);
        View view = LayoutInflater.from(this).inflate(intiLayout(), null);
        ((LinearLayout) findViewById(R.id.ll_root)).addView(view);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        findViewById(R.id.ll_title).setVisibility(View.GONE);
        //绑定view
        bind = ButterKnife.bind(this);
        //设置状态栏字体黑色
        initToolBar(true);
        //传递数据
        initIntentData();
        //添加activity到集合
        AppManager.getAppManager().addActivity(this);
        //初始化控件
        try {
            initData();
            doBusiness();
        } catch (Exception e) {
            e.printStackTrace();
            MyLogger.dLog().e(e);
        }
    }

    public void hindLine() {
        findViewById(R.id.v_line).setVisibility(View.GONE);
    }

    /**
     * 初始化标题栏
     *
     * @param title 标题
     */
    public void initTitle(String title) {
        findViewById(R.id.ll_title).setVisibility(View.VISIBLE);
        tvTitle.setText(title);
    }

    /**
     * 初始化标题栏
     *
     * @param title      标题
     * @param rightTitle 标题栏右上角的操作
     */
    public void initTitle(String title, String rightTitle) {
        findViewById(R.id.ll_title).setVisibility(View.VISIBLE);
        tvTitle.setText(title);
        tvRight.setText(rightTitle);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setOnClickListener(view -> rightClick());

    }

    /**
     * 初始化标题栏
     *
     * @param title            标题
     * @param rightImgResource 标题栏右上角的图片资源
     */
    public void initTitle(String title, int rightImgResource) {
        findViewById(R.id.ll_title).setVisibility(View.VISIBLE);
        tvTitle.setText(title);
        ivRight.setImageResource(rightImgResource);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setOnClickListener(view -> rightClick());
    }

    /**
     * 如果标题栏右侧有点击事件
     * 重写该方法 在该方法中进行处理
     */
    public void rightClick() {

    }

    public void initToolBar(boolean statusBarFontBlack) {
        //沉浸式
        QMUIStatusBarHelper.translucent(this);
        if (statusBarFontBlack) {
            //设置状态栏黑色字体与图标
            QMUIStatusBarHelper.setStatusBarLightMode(this);
        } else {
            //设置状态栏白色字体与图标
            QMUIStatusBarHelper.setStatusBarDarkMode(this);
        }
    }

    protected Activity getAty() {
        return this;
    }

    /**
     * 设置布局
     *
     * @return 布局
     */
    public abstract int intiLayout();

    /**
     * 设置数据
     */
    public abstract void initData();

    /**
     * 业务逻辑处理
     */
    public abstract void doBusiness();

    /**
     * 标题返回箭头点击事件
     */
    public void back(View view) {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 传递数据初始化
     */
    private void initIntentData() {
        this.mIntentData = this.getIntent().getSerializableExtra("Data");
    }


    public Object getIntentData() {
        return this.mIntentData;
    }

    protected void startAct(Class cls) {
        this.startAct(this, cls, null, -1);
    }

    protected void startAct(Class cls, Object obj) {
        this.startAct(this, cls, obj, -1);
    }

    protected void startAct(Activity act, Class cls) {
        this.startAct(act, cls, null, -1);
    }

    public void startAct(Activity act, Class cls, Object obj) {
        this.startAct(act, cls, obj, -1);
    }

    protected void startAct(Activity act, Class cls, Object obj, int requestCode) {
        AppManager.getAppManager().startActivity(act, cls, obj, requestCode);
    }

    protected void startAct(Class cls, Object obj, int requestCode) {
        AppManager.getAppManager().startActivity(this, cls, obj, requestCode);
    }

    /**
     * 打印日志
     *
     * @param msg
     */
    protected void showLog(String msg) {
        MyLogger.dLog().d(msg);
    }
}
