package com.example.heyukun.mvpretrorxmydemo.domain.presenter;

import com.example.heyukun.mvpretrorxmydemo.domain.view.IMainView;

/**
 * Created by heyukun on 2017/8/25.
 */

public class MainPresenterImpl implements IPresenter {
    IMainView mainView;
    public MainPresenterImpl(IMainView mainView){
        this.mainView = mainView;
    }

    @Override
    public void login(String userName, String password) {

    }
}
