package com.dykj.module.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.text.Html;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewUtil {

    public static void setUp(String content, WebView mWebView, Activity activity) {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        //设置此属性，可任意比例缩放
        settings.setUseWideViewPort(true);
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //设置支持缩放
        settings.setBuiltInZoomControls(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setBlockNetworkLoads(false);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(false);
        settings.setTextSize(WebSettings.TextSize.LARGEST);
        //水平不显示
        mWebView.setHorizontalScrollBarEnabled(false);
        //垂直不显示
        mWebView.setVerticalScrollBarEnabled(false);

        mWebView.setWebViewClient(new MyWebViewClient(activity));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
//        } else {
//            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
//        }
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (content.startsWith("http")) {
            mWebView.loadUrl(content);
        } else {
            mWebView.loadData(content, "rank_label/html;charset=utf-8", "utf-8");
        }
    }

    private static String getHtmlData(String bodyHTML) {
        String head = "<head>" +
            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
            "<style>html{padding:15px;} body{word-wrap:break-word;font-size:13px;padding:0px;margin:0px} p{padding:0px;margin:0px;font-size:13px;color:#222222;line-height:1.3;} img{padding:0px,margin:0px;max-width:100%; width:auto; height:auto;}</style>" +
            "</head>";
        return "<html>" + head + "<body>" + Html.fromHtml(bodyHTML) + "</body></html>";
    }

    static class MyWebViewClient extends WebViewClient {
        private Activity activity;

        public MyWebViewClient(Activity activity) {
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
}
