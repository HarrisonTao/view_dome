package com.dykj.module.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.ToastUtils;
import com.dykj.module.R;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.util.FragmentKeyDown;
import com.dykj.module.util.toast.DyToast;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.LogUtils;
import com.just.agentweb.WebChromeClient;

/**
 * @author lcj
 *         调用AgentWeb支持微信支付宝跳转
 * @date 2017/12/29 15:12
 */

public class WebAgentActivity extends BaseActivity implements FragmentKeyDown {

    private ProgressBar progressBar;
    private FrameLayout layoutAgent;
    private LinearLayout vLoading;
    private TextView tvTitleTitle;
    protected AgentWeb mAgentWeb;



    @Override
    public int intiLayout() {
        return R.layout.activity_web_agent;
    }

    @Override
    public void initData() {
        progressBar = findViewById(R.id.progressBar);
        layoutAgent = findViewById(R.id.content_frame);
        vLoading = findViewById(R.id.layout_load);
        tvTitleTitle = findViewById(R.id.tvTitleTitle);

    }

    @Override
    public void doBusiness() {
        initView();
    }


    protected void initView() {
        String title = getIntent().getStringExtra("title");
        if (title == null || title.isEmpty()) {
            initTitle("");
        } else {
            initTitle(title);
        }
        TextPaint paint = tvTitleTitle.getPaint();
        paint.setFakeBoldText(true);
        String url = getIntent().getStringExtra("url");
        if (TextUtils.isEmpty(url)) {
            DyToast.getInstance().warning("地址有误");
            return;
        }
        if (url.startsWith("http")) {
            vLoading.setVisibility(View.VISIBLE);
            int layoutPa = ViewGroup.LayoutParams.MATCH_PARENT;
            mAgentWeb = AgentWeb.with(this)//传入Activity
                    .setAgentWebParent(layoutAgent, new LinearLayout.LayoutParams(layoutPa, -1))
                    .useDefaultIndicator()
                    .setWebChromeClient(mWebChromeClient)
//                    .setWebViewClient(mWebViewClient)
//                    .setSecutityType(AgentWeb.SecurityType.strict)
//                    .defaultProgressBarColor()
//                    .setReceivedTitleCallback(mCallback)
                    .createAgentWeb().ready().go(url);
//            mAgentWeb.getAgentWebSettings().getWebSettings().setTextSize(WebSettings.TextSize.LARGEST);
            mAgentWeb.getAgentWebSettings().getWebSettings().setUseWideViewPort(true); //将图片调整到适合webview的大小
            mAgentWeb.getAgentWebSettings().getWebSettings().setLoadWithOverviewMode(true);
            mAgentWeb.getAgentWebSettings().getWebSettings().setSupportZoom(true);
            mAgentWeb.getAgentWebSettings().getWebSettings().setBuiltInZoomControls(true);
        } else {
            DyToast.getInstance().warning("地址有误");
        }
        LogUtils.e("url:", url);
    }


    @Override
    public void onBackPressed() {
        if (mAgentWeb != null && mAgentWeb.back()) {
            //返回上一页面
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onResume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroy();
    }


    protected WebChromeClient mWebChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
           LogUtils.e("mWebChromeClient-url",view.getUrl());
            if (view.getUrl().contains("https://cash.yeepay.com/wap/query/result")) {
                ToastUtils.showShort("收款完成！");
                setResult(1);
                finish();
            }

        }
    };
    protected WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return shouldOverrideUrlLoading(view, request.getUrl() + "");
        }

        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            LogUtils.e("mWebChromeClient-url",view.getUrl());
            if (view.getUrl().contains("https://cash.yeepay.com/wap/query/result")) {
                setResult(1);
                finish();
            }
            LogUtils.e("shouldOverrideUrlLoading:", url);
            if (TextUtils.indexOf(url, "paymentinterface/recallWechatH5?bussinessNoWechatH5") > 0) {
                loadSunshinePayUrl = url;
            }
            if (url.contains("/index.php/Api/Index/recharge_back/info/")) {
                WebAgentActivity.this.finish();
            } else if (url.startsWith("weixin://")) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    new AlertDialog.Builder(WebAgentActivity.this)
                            .setMessage("未检测到微信客户端，请安装后重试！")
                            .setPositiveButton("知道了", null).show();
                }
                LogUtils.e("shouldOverrideUrlLoading2222:", loadSunshinePayUrl + url);
                if (!TextUtils.isEmpty(loadSunshinePayUrl)) {
                    view.loadUrl(loadSunshinePayUrl);
                }
                //1、 默认返回：return super.shouldOverrideUrlLoading(view, url);
                // 这个返回的方法会调用父类方法，也就是跳转至手机浏览器，平时写webview一般都在方法里面写 webView.loadUrl(url);
                // 然后把这个返回值改成下面的false。
                //2、返回: return true; webview处理url是根据程序来执行的。
                //3、返回: return false; webview处理url是在webview内部执行。
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.i("Info", "url:" + url + " onPageStarted  target:" + view.getUrl());
            vLoading.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            vLoading.setVisibility(View.GONE);
        }
    };
    //阳光保险支付H5页面
    private String loadSunshinePayUrl;

    // 绘制HTML
    private String getHtmlData(String bodyHTML) {
        String head = "<head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=0.5, user-scalable=no\"> "
                + "<style>img{max-width: 100%; width:auto; height:auto;}</style>"
                + "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (mAgentWeb != null) {
////            mAgentWeb.getUrlLoader(requestCode, resultCode, data);
//        }
    }


    @Override
    public void onPause() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onPause();
        }
        super.onPause();
    }

    @Override
    public boolean onFragmentKeyDown(int keyCode, KeyEvent event) {
        boolean keyEvent = false;
        if (mAgentWeb != null) {
            keyEvent = mAgentWeb.handleKeyEvent(keyCode, event);
        }
        return keyEvent;
    }

}
