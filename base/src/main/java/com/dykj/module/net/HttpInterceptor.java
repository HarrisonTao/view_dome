package com.dykj.module.net;


import com.dykj.module.util.MyLogger;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class HttpInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final String TAG = "请求数据";

    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64L ? buffer.size() : 64L;
            buffer.copyTo(prefix, 0L, byteCount);

            for (int i = 0; i < 16 && !prefix.exhausted(); ++i) {
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }

            return true;
        } catch (EOFException var6) {
            return false;
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();
        HttpUrl url = request.url();
        HttpUrl.Builder nUrl = url.newBuilder();
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        String parameter = buffer.readString(UTF8);
        MyLogger.dLog().i(
                "              ：" + "\n" +
                        "param ->[T_T]     ：" + parameter + "\n" +
                        "url   ->[Q_Q]     ：" + url + "\n" +
                        "host  ->[@_@]     ：" + url.host() + "\n" +
                        "method->[^_^]     ：" + request.method());
        buffer.flush();
        buffer.close();
//        String token = "";
//        String md5 = "";
//        nUrl.addQueryParameter("sign", md5).addQueryParameter("token", token);
        okhttp3.Request.Builder requestBuilder = request.newBuilder().method(request.method(),
                request.body()).url(nUrl.build());
        Request newRequest = requestBuilder.build();
        long startNs = System.nanoTime();
        Response response = chain.proceed(newRequest);
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE);
        Buffer responseBuffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF8);
            } catch (UnsupportedCharsetException var18) {
                return response;
            }
        }
        if (!isPlaintext(responseBuffer)) {
            return response;
        } else {
            if (contentLength != 0L) {
            }
            return response;
        }
    }
}
