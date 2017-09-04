package com.example.heyukun.mvpretrorxmydemo.request;

import rx.Subscription;

/**
 * Created by heyukun on 2017/8/25.
 * 网络请求监听  可取消
 * 主要管理rxJava的Subscription描述
 */

public interface RxRetroActionListener<T> {
    void add(T tag, Subscription subscription);
    void remove(T tag);
    void removeAll();

    void cancel(T tag);

    void cancelAll();
}
