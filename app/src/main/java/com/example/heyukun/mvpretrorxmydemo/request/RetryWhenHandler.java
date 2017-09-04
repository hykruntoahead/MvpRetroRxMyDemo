package com.example.heyukun.mvpretrorxmydemo.request;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by heyukun on 2017/8/25.
 *
 * 网络请求重试
 */


final class RetryWhenHandler implements Func1<Observable<? extends Throwable>, Observable<Long>> {

    private static final int INITIAL = 1;
    private int maxConnectCount = 1;

    RetryWhenHandler(int retryCount) {
        this.maxConnectCount += retryCount;
    }

    @Override public Observable<Long> call(Observable<? extends Throwable> errorObservable) {
        return errorObservable.zipWith(Observable.range(INITIAL, maxConnectCount),
                new Func2<Throwable, Integer, ThrowableWrapper>() {
                    @Override public ThrowableWrapper call(Throwable throwable, Integer i) {

                        //①只在IOException的情况下记录本次请求在最大请求次数中的位置，否则视为最后一次请求，避免多余的请求重试。
                        if (throwable instanceof IOException) return new ThrowableWrapper(throwable, i);

                        return new ThrowableWrapper(throwable, maxConnectCount);
                    }
                }).concatMap(new Func1<ThrowableWrapper, Observable<Long>>() {
            @Override public Observable<Long> call(ThrowableWrapper throwableWrapper) {

                final int retryCount = throwableWrapper.getRetryCount();

                //②如果最后一次网络请求依然遭遇了异常，则将此异常继续向下传递，以便在最后的onError()函数中处理。
                if (maxConnectCount == retryCount) {
                    return Observable.error(throwableWrapper.getSourceThrowable());
                }

                //③使用.timer()操作符实现一个简单的二进制指数退避算法，需要注意的是.timer()操作符默认执行在Schedulers.computation()，
                // 我们并不希望它切换到别的线程去执行重试逻辑，因此使用了它的重载函数，并指定在当前线程立即执行。
                return Observable.timer((long) Math.pow(2, retryCount), TimeUnit.SECONDS,
                        Schedulers.immediate());
            }
        });
    }

    private static final class ThrowableWrapper {

        private Throwable sourceThrowable;
        private Integer retryCount;

        ThrowableWrapper(Throwable sourceThrowable, Integer retryCount) {
            this.sourceThrowable = sourceThrowable;
            this.retryCount = retryCount;
        }

        Throwable getSourceThrowable() {
            return sourceThrowable;
        }

        Integer getRetryCount() {
            return retryCount;
        }
    }
}
