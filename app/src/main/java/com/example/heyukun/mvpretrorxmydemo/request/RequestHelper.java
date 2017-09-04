package com.example.heyukun.mvpretrorxmydemo.request;

import android.content.Context;
import android.util.Log;

import com.example.heyukun.mvpretrorxmydemo.MyApp;
import com.example.heyukun.mvpretrorxmydemo.R;
import com.example.heyukun.mvpretrorxmydemo.api.RetrofitService;
import com.example.heyukun.mvpretrorxmydemo.constants.Error;
import com.example.heyukun.mvpretrorxmydemo.entity.ApiEntity;
import com.example.heyukun.mvpretrorxmydemo.entity.BaseEntity;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.heyukun.mvpretrorxmydemo.constants.Constant.CACHE_DIR;

/**
 * Created by heyukun on 2017/8/24.
 */

public class RequestHelper {
    private static final String TAG = "NETWORK_INFO";
    private static final long TIMEOUT = 30;
    private static Context AppCtx = MyApp.getInstance().getApplicationContext();

    private static final String BASE_URL = "https://robot.apis.iaijian.com/";
    private Retrofit mRetrofit;
    private static RequestHelper INSTANCE;
    private RetrofitService mService;

    public static RequestHelper getInstance() {
        synchronized (RequestHelper.class) {
            if (INSTANCE == null) {
                INSTANCE = new RequestHelper();
            }
            return INSTANCE;
        }
    }


    RequestHelper() {
        init();
    }

    private void init() {
        //缓存
        File httpCacheDirectory = new File(MyApp.getInstance().getCacheDir(), CACHE_DIR);
        Cache cache = new Cache(httpCacheDirectory,10 * 1024 * 1024);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.d(TAG, message);
                    }
                }).setLevel(HttpLoggingInterceptor.Level.BASIC))
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT,TimeUnit.SECONDS)
                .cache(cache)
                .build();
        if (mRetrofit == null) {

            mRetrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }

        if (mService == null) {
            mService = mRetrofit.create(RetrofitService.class);
        }
    }


    public Subscription getApiData(String appKey, final CallBack callBack) {
        return mService.getApiData(appKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWhenHandler(3))
                .subscribe(new Subscriber<BaseEntity<ApiEntity>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "getApiData_Complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(BaseEntity<ApiEntity> apiEntityBaseEntity) {
                        Log.d(TAG, "getApiData_onNext");
                        if (apiEntityBaseEntity == null) {
                            callBack.onError(AppCtx.getString(R.string.error_network));
                            return;
                        }
                        if (apiEntityBaseEntity.getCode() != Error.RES_SUCCESS) {
                            switch (apiEntityBaseEntity.getCode()) {
                                case Error.ERR_APPKEY:
                                    callBack.onError(AppCtx.getString(R.string.error_appkey));
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            callBack.onSuccess(apiEntityBaseEntity.getData());
                        }

                    }
                });
    }

}
