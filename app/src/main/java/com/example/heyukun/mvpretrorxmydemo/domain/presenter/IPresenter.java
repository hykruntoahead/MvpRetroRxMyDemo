package com.example.heyukun.mvpretrorxmydemo.domain.presenter;

import rx.Subscription;

/**
 * Created by heyukun on 2017/8/25.
 */

public interface IPresenter {
    Subscription login(String userName, String password);
}
