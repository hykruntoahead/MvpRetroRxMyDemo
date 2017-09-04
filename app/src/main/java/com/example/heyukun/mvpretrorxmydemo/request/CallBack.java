package com.example.heyukun.mvpretrorxmydemo.request;

/**
 * Created by heyukun on 2017/8/24.
 * 网络请求回调接口
 */

public interface CallBack {
    void onError(String e);
    void onSuccess(Object object);
}
