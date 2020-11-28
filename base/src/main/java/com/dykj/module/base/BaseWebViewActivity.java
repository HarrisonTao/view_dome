package com.dykj.module.base;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dykj.module.AppConstant;
import com.dykj.module.R;
import com.dykj.module.entity.BaseWebViewData;

public class BaseWebViewActivity extends BaseActivity {
    private BaseWebViewData mData = new BaseWebViewData();
    private WebView mWebView;
    private TextView title;
    private FrameLayout flProgressBar;
    
    @Override
    public int intiLayout() {
        return R.layout.activity_base_web_view;
    }
    
    
    @Override
    public void initData() {
        flProgressBar = findViewById(R.id.fl_progressbar);
        title = findViewById(R.id.tv_toolbar_title);
        mWebView = findViewById(R.id.web_view);
        if (getIntentData() != null) {
            mData = (BaseWebViewData) getIntentData();
        }
    }
    
    @Override
    public void doBusiness() {
        title.setText(mData.title);
        setUp(mData.content);
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
        //水平不显示
        mWebView.setHorizontalScrollBarEnabled(false);
        //垂直不显示
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setWebViewClient(new MyWebViewClient(this, flProgressBar));
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
            flProgressBar.setVisibility(View.VISIBLE);
        } else {
//            mWebView.loadDataWithBaseURL("http://yx.diyunkeji.com/api/", content, "text/html", "utf-8", null);
            mWebView.loadDataWithBaseURL(AppConstant.HOST_IMAGE + "/api/", content, "text/html", "utf-8", null);
        }
    }
    
    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>html{padding:15px;} body{word-wrap:break-word;font-size:13px;padding:0px;margin:0px} p{padding:0px;margin:0px;font-size:13px;color:#222222;line-height:1.3;} img{padding:0px,margin:0px;max-width:100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + Html.fromHtml(bodyHTML) + "</body></html>";
    }
    
    static class MyWebViewClient extends WebViewClient {
        private Activity activity;
        private FrameLayout mFlProgressBar;
        
        MyWebViewClient(Activity activity, FrameLayout flProgressBar) {
            this.activity = activity;
            this.mFlProgressBar = flProgressBar;
        }
        
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (TextUtils.indexOf(url, "repayment/h5Repayment") > -1) {
                activity.finish();
            }
            if (TextUtils.indexOf(url, "/verapay_return") > -1) {
                activity.finish();
            }
            if (TextUtils.indexOf(url, "/bindCardFrontCallBack") > -1) {
                activity.finish();
            }
            view.loadUrl(url);
            return true;
        }
        
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
        
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mFlProgressBar.setVisibility(View.GONE);
        }
        
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
            super.onReceivedSslError(view, handler, error);
        }
    }
}
