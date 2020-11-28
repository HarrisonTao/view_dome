package com.dykj.module.net;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.dykj.module.Flag;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.base.BaseApplication;
import com.dykj.module.util.BaseEventData;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.dykj.module.view.dialog.ConfirmDialog;
import com.dykj.module.view.dialog.LoadingDialog;
import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import org.greenrobot.eventbus.EventBus;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class HttpObserver<T> extends DisposableObserver<T> {
    private static final String TAG = "Request";
    private HttpListener<T> mHttpListener;

    public HttpObserver() {

    }

    public HttpObserver(HttpListener<T> httpListener) {
        if (httpListener == null) {
            throw new NullPointerException("HttpListener not null");
        } else {
            this.mHttpListener = httpListener;
        }
    }

    public static <T> AutoDisposeConverter<T> life(LifecycleOwner context) {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(context, Lifecycle.Event.ON_DESTROY));
    }


    /**
     * 带loading
     */
    public static <T> ObservableTransformer<T, T> schedulers(@NonNull final Context context) {
        return schedulers(context, null);
    }

    /**
     * 带loading
     */
    public static <T> ObservableTransformer<T, T> schedulers(@NonNull final Context context, @Nullable String tipWord) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                LoadingDialog.getInstance().showLoadingDialog(context, true, tipWord);
                            }
                        })
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                LoadingDialog.getInstance().dismissLoadingDialog();
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 未带loading
     */
    public static <T> ObservableTransformer<T, T> schedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @Override
    public void onError(Throwable e) {
        Log.d("wtf",e.getMessage());
        e.printStackTrace();
        if (e instanceof NetworkErrorException || e instanceof UnknownHostException || e instanceof ConnectException) {
            //连接地址或者断网
            Throwable da = new Throwable("请检查网络连接是否畅通");
            this.mHttpListener.error(da);
            DyToast.getInstance().error("请检查网络连接是否畅通");
        } else if (e instanceof InterruptedIOException || e instanceof TimeoutException) {
            Throwable da = new Throwable("请求超时");
            DyToast.getInstance().error("请求超时");
            this.mHttpListener.error(da);
        } else if (e instanceof ApiException) {
            this.mHttpListener.error(new Throwable("您的登录信息已失效，请重新登录"));
            MMKV.defaultMMKV().encode("tokent", "");
            EventBus.getDefault().post(new BaseEventData<>(Flag.Event.JUMP_LOGIN));
//            new ConfirmDialog(BaseApplication.getInstance(), new ConfirmDialog.CallBack() {
//                @Override
//                public void confirm() {
//                    EventBus.getDefault().post(new BaseEventData<>(Flag.Event.JUMP_LOGIN));
//                }
//            }, "检测到您的登录信息已失效，是否重新登录？", "重新登录").show();

        } else if (e instanceof NullPointerException) {
            this.mHttpListener.error(new Throwable("数据解析异常"));
            DyToast.getInstance().error("数据解析异常");
        } else if (this.mHttpListener != null) {
            this.mHttpListener.error(e);
        } else {
            MyLogger.dLog().e(new Object[]{e.getMessage()});
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(T t) {
        try {
            Log.d("wtf",new Gson().toJson(t));
            this.mHttpListener.success(t);
        } catch (Exception exception) {
            exception.printStackTrace();
            Log.d("wtf",exception.getMessage());
            this.mHttpListener.error(new Throwable("数据解析异常"));
        }
    }
}
