package com.dykj.youfeng.network.card_api;

import android.text.TextUtils;

import com.dykj.module.net.HttpException;
import com.dykj.module.util.MyLogger;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <pre>
 *           .----.
 *        _.'__    `.
 *    .--(Q)(OK)---/$\
 *  .' @          /$$$\
 *  :         ,   $$$$$
 *   `-..__.-' _.-\$$/
 *         `;_:    `"'
 *       .'"""""`.
 *      /,  oo  ,\
 *     //         \\
 *     `-._______.-'
 *     ___`. | .'___
 *    (______|______)
 * </pre>
 * <p>文件描述：GsonConverterFactory.create()<p>
 * <p>作者：lj<p>
 * <p>创建时间：2020/3/18<p>
 * <p>更改时间：2020/3/18<p>
 * <p>版本号：1<p>
 */
public class HttpResultConverterGson extends Converter.Factory {
    private static final String TAG = "HttpResultConverter";
    private final Gson mGson = new Gson();

    private HttpResultConverterGson() {
    }

    public static HttpResultConverterGson create() {
        return new HttpResultConverterGson();
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
        private Type mType;

        public BaseResponseBodyConverter(Type mType) {
            this.mType = mType;
        }

        @Override
        public T convert(ResponseBody response) {
            Object var6 = null;
            try {
                String strResponse = response.string();
                MyLogger.dLog().e("返回数据:   \n" + strResponse);
                if (TextUtils.isEmpty(strResponse)) {
                    throw new HttpException("请求服务器异常");
                } else {
                    var6 = mGson.fromJson(strResponse, this.mType);
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
