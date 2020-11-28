package com.dykj.youfeng.mine.msg;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.MsgInfoBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.tencent.mmkv.MMKV;

public class MessageInfoActivity extends BaseActivity {
    private WebView mWebView;
    private TextView mTitle;
    private TextView mTvContent;


    @Override
    public int intiLayout() {
        return R.layout.activity_base_web_view;
    }

    @Override
    public void initData() {
        initTitle("消息中心");
        mTitle = findViewById(com.dykj.module.R.id.tv_toolbar_title);
        mWebView = findViewById(com.dykj.module.R.id.web_view);
        mTvContent = findViewById(R.id.tv_content);

        if (getIntentData() != null) {
            String msgId = (String) getIntentData();
            getMsgInfo(msgId);
        }
    }

    /**
     * 信息详情
     *
     * @param msgId
     */
    private void getMsgInfo(String msgId) {
        HttpFactory.getInstance().msgInfo(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), msgId)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<MsgInfoBean>>() {
                    @Override
                    public void success(HttpResponseData<MsgInfoBean> data) {
                        if ("9999".equals(data.status)) {
                            MsgInfoBean msgInfoBean = data.getData();
                            if (null != msgInfoBean) {
                                String content = msgInfoBean.content;
                                String title = msgInfoBean.title;
                                setViewData(content,title);
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


    private void setViewData(String content, String title) {
        mTitle.setText(title);
        setUp(content);
    }


    private void setUp(String content) {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        //设置此属性，可任意比例缩放
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //设置支持缩放
        settings.setBuiltInZoomControls(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setBlockNetworkLoads(false);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setDisplayZoomControls(false);

        // 设置字体的大小
        settings.setSupportZoom(true);
        settings.setTextSize(WebSettings.TextSize.LARGEST);
        settings.setTextZoom(100);



        //水平不显示
        mWebView.setHorizontalScrollBarEnabled(false);
        //垂直不显示
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setWebViewClient(new MyWebViewClient(this));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }


        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (content.startsWith("http")) {
            mWebView.loadUrl(content);
        } else {
//            mWebView.loadData(getHtmlData(content), "rank_label/html;charset=utf-8", "utf-8");
//            mWebView.loadDataWithBaseURL("http://yx.diyunkeji.com/api/", content, "text/html", "utf-8", null);

            mWebView.setVisibility(View.GONE);
            mTvContent.setVisibility(View.VISIBLE);
            mTvContent.setText(content);
            Log.e("------",content);
        }
    }



    static class MyWebViewClient extends WebViewClient {
        private Activity activity;

        MyWebViewClient(Activity activity) {
            this.activity = activity;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }


    @Override
    public void doBusiness() {
    }
}
