package com.dykj.module.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dykj.module.util.AppManager;
import com.dykj.module.util.MyLogger;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @author WZ
 * @date 2019/5/7.
 * 严格要求自己，对每一行代码负责
 * description：
 */
public abstract class BaseFragment extends Fragment {
    protected Context mContext;
    private View mRootView;
    private Unbinder mUnbinder;
    private QMUITipDialog loadingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (null == mRootView) {
            mRootView = inflater.inflate(intiLayout(), null);
        } else {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, mRootView);
        onViewReallyCreated(mRootView);
        try {
            doBusiness();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置布局
     *
     * @return
     */
    protected abstract int intiLayout();

    /**
     * 设置数据
     *
     * @param view
     */
    protected abstract void onViewReallyCreated(View view);

    /**
     * 业务逻辑处理
     */
    public abstract void doBusiness();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    protected Fragment getFragment() {
        return this;
    }

    protected void startAct(Class cls) {
        this.startAct(this, cls, null, -1);
    }

    protected void startAct(Fragment act, Class cls) {
        this.startAct(act, cls, null, -1);
    }

    protected void startAct(Fragment act, Class cls, Object obj) {
        this.startAct(act, cls, obj, -1);
    }

    protected void startAct(Class cls, Object obj) {
        this.startAct(this, cls, obj, -1);
    }

    protected void startAct(Fragment fragment, Class cls, Object obj, int requestCode) {
        AppManager.getAppManager().startActivityForFrg(fragment, cls, obj, requestCode);
    }
    protected void startAct(Class cls, Object obj, int requestCode) {
        AppManager.getAppManager().startActivityForFrg(this, cls, obj, requestCode);
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
