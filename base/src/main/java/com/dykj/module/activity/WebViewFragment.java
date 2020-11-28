package com.dykj.module.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.dykj.module.AppConstant;
import com.dykj.module.R;
import com.dykj.module.base.BaseFragment;
import com.dykj.module.util.toast.DyToast;
import com.dykj.module.util.webview.MyTagHandler;
import com.dykj.module.util.webview.URLImageParser;
import com.dykj.module.view.imgbrowser.util.JMatrixUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lcjing on 2018/12/12.
 */

public class WebViewFragment extends BaseFragment {


    private ProgressBar progressBar;
    private WebView webView;
    private TextView tvContent;
    private ScrollView scrollView;
    private boolean isOut;

    public static WebViewFragment getInstance(String url, boolean isOut) {
        WebViewFragment webViewFragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putBoolean("isOut", isOut);
        webViewFragment.setArguments(bundle);
        return webViewFragment;
    }

    public static WebViewFragment getInstance(String url) {
        WebViewFragment webViewFragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putBoolean("isOut", false);
        webViewFragment.setArguments(bundle);
        return webViewFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, null);
        progressBar = view.findViewById(R.id.progressBar);
        webView = view.findViewById(R.id.webView);
        tvContent = view.findViewById(R.id.tvContent);
        scrollView = view.findViewById(R.id.scrollView);

        return view;
    }

    @Override
    protected int intiLayout() {
        return R.layout.fragment_webview;
    }

    @Override
    protected void onViewReallyCreated(View view) {
        String url = getArguments().getString("url");
        isOut = getArguments().getBoolean("isOut");
        if (url == null || url.isEmpty()) {
            return;
        }
//        if (url.startsWith("http")) {
        webView.setVisibility(View.VISIBLE);
        tvContent.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
        setWebData(url);
//        } else {
//            webView.setVisibility(View.GONE);
//            tvContent.setVisibility(View.VISIBLE);
//            scrollView.setVisibility(View.VISIBLE);
//            URLImageParser imageGetter = new URLImageParser(tvContent,getContext());
//            tvContent.setText(Html.fromHtml(url, imageGetter, new MyTagHandler(getContext())));
//        }
        showLog("url：" + url);
    }

    @Override
    public void doBusiness() {

    }


//---------------------
//    作者：潇潇凤儿
//    来源：CSDN
//    原文：https://blog.csdn.net/smileiam/article/details/72123227
//    版权声明：本文为博主原创文章，转载请附上博文链接！


    @SuppressLint("SetJavaScriptEnabled")
    private void setWebData(String goodsDesc) {
//        if (bWebView == null) {
//            return;
//        }
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//关键点
        webSettings.setBlockNetworkLoads(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        if (isOut) {
            webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        } else {
            webSettings.setTextSize(WebSettings.TextSize.LARGEST);
        }

        webView.getSettings().setDefaultTextEncodingName("utf-8");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//        } else {
//            webSettings.setTextSize(WebSettings.TextSize.LARGEST);
//        }

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });


        webView.setWebViewClient(new MyWebViewClient());
        webView.addJavascriptInterface(new JavaScriptInterface(getContext()), "imagelistner");//这个是给图片设置点击监听的，如果你项目需要webview中图片，点击查看大图功能，可以这么添加
        if (goodsDesc.startsWith("http")) {
            webView.loadUrl(goodsDesc);
        } else {
            goodsDesc = goodsDesc.replaceAll("src=\"/Uploads/php", "src=\"" + AppConstant.MALL_HOST_IMG + "/Uploads/php");
//            webView.loadData(getHtmlData(goodsDesc), "rank_label/html; charset=utf-8", "utf-8");
            webView.loadDataWithBaseURL(null, goodsDesc + "", "text/html", "utf-8", null);
        }
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            imgReset();//重置webview中img标签的图片大小
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListner();
            progressBar.setVisibility(View.GONE);
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
//            imgReset();//重置webview中img标签的图片大小
//            // html加载完成之后，添加监听图片的点击js函数
//            addImageClickListner();
        }
    }



    private static ArrayList<String> imgs = new ArrayList<>();

    public static class JavaScriptInterface {

        private Context context;

        public JavaScriptInterface(Context context) {
            this.context = context;
        }

        //点击图片回调方法
        //必须添加注解,否则无法响应
        @JavascriptInterface
        public void openImage(String img) {
            Log.i("TAG", "响应点击事件!");

//            JBrowseImgActivity.start(context, img);
            Intent intent = new Intent();
            intent.putExtra("url", img);
            intent.setClass(context, PhotoViewActivity.class);//BigImageActivity查看大图的类，自己定义就好
            context.startActivity(intent);
        }

    }

    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

    /**
     * 对图片进行重置大小，宽度就是手机屏幕宽度，高度根据宽度比便自动缩放
     **/
    private void imgReset() {
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                "    img.style.width = '100%'; img.style.height = 'auto';  " +
                "}" +
                "})()");
    }


    // 绘制HTML
    private String getHtmlData(String bodyHTML) {
        String head = "<head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, user-scalable=yes\"> "
                + "<style>img{max-width: 100%; width:auto; height:auto;}</style>"
                + "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }


    public boolean onBack() {
//        WebSettings webSettings = webView.getSettings();
        boolean canBack = webView.canGoBack();
        if (canBack) {
            webView.goBack();
        }
        return canBack;
    }

}
