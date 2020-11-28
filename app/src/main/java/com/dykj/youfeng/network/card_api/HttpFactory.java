package com.dykj.youfeng.network.card_api;


import android.util.Log;

import com.dykj.youfeng.baidu.utils.LogUtil;
import com.dykj.youfeng.network.AppConstant;
import com.dykj.youfeng.network.mall_api.MallApi;
import com.dykj.module.net.HttpInterceptor;
import com.dykj.module.net.HttpResultConverter;
import com.zhy.http.okhttp.intercepter.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;



public class HttpFactory {


    private static OkHttpClient okHttpClient
            = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(new HttpInterceptor())
            .build();

    private static OkHttpClient okHttpClient2
            = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            //.addInterceptor(new HttpInterceptor())
            .build();

    public static Api getInstance() {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(AppConstant.CARD_HOST_TEST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(HttpResultConverter.create())
                .build().create(Api.class);
    }

    public static Api getInstance(String url) {


        return new Retrofit.Builder()
               .client(okHttpClient2)
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
               .addConverterFactory(HttpResultConverter.create())
                .build().create(Api.class);
    }

    //20200310佳付通收款
    public static JftApi getJftSk() {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(AppConstant.CARD_HOST_TEST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(HttpResultConverterGson.create())
                .build().create(JftApi.class);
    }
    public static MallApi getMallInstance() {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(com.dykj.module.AppConstant.MALL_HOST_TEST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(HttpResultConverter.create())
                .build().create(MallApi.class);
    }
}
