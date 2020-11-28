package com.dykj.module.net;

import android.text.TextUtils;
import android.util.Log;

import com.dykj.module.util.MyLogger;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Converter.Factory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpResultConverter extends Factory {
    private static final String TAG = "HttpResultConverter";
    private final Gson mGson = new Gson();

    private HttpResultConverter() {
    }

    public static HttpResultConverter create() {
        return new HttpResultConverter();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type mType, Annotation[] annotations, Retrofit retrofit) {
        return new BaseResponseBodyConverter(mType);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type mType, Annotation[] parameterAnnotations, Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        return GsonConverterFactory.create().requestBodyConverter(mType, parameterAnnotations, methodAnnotations, retrofit);
    }

    private class BaseResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private static final String SERVICE_ERROR = "请求服务器异常";
        private static final int SERVICE_STATE_SUCCESS = 1;
        private static final String REQUEST_SUCCESS = "9999";
        private static final int TOKEN_ERROR = 30000;
        private Type mType;

        public BaseResponseBodyConverter(Type mType) {
            this.mType = mType;
        }

        @Override
        public T convert(ResponseBody response) {
            Object var6 = null;
            try {
                String strResponse = response.string();
                Log.d("wtf","返回数据"+strResponse);
                if (TextUtils.isEmpty(strResponse)) {
                    throw new HttpException("请求服务器异常");
                } else {
                    HttpResponseData httpResponse = mGson.fromJson(strResponse, HttpResponseData.class);
                    String state = httpResponse.getStatus();
                    if (state.equals("0000")) {
                        //单点登录
                        throw new ApiException(state, httpResponse.message);
                    } else {
                        var6 = mGson.fromJson(strResponse, this.mType);
                    }
//                    if (state.equals(REQUEST_SUCCESS)) {
//                        if (httpResponse.getData() == null) {
//                            var6 = true;
//                        } else {
//                            var6 = mGson.fromJson(mGson.toJson(httpResponse.getData()), this.mType);
//                        }
//                    } else {
//                        if (httpResponse.getData() == null) {
//                            var6 = false;
//                        } else {
//                            throw new HttpException(httpResponse.getMessage());
//                        }
//                    }
                }
            } catch (IOException var10) {
                throw new HttpException(var10.getMessage());
            } finally {
                response.close();
            }
            return (T) var6;
        }
    }
}
