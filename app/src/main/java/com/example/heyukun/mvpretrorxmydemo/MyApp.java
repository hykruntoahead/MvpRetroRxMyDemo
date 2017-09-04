package com.example.heyukun.mvpretrorxmydemo;

import android.app.Application;

/**
 * Created by heyukun on 2017/8/24.
 */

public class MyApp extends Application {
    public static MyApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static MyApp getInstance(){
        return sInstance;
    }
}
