package com.example.heyukun.mvpretrorxmydemo.domain.presenter;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.heyukun.mvpretrorxmydemo.MyApp;
import com.example.heyukun.mvpretrorxmydemo.domain.view.IMainView;
import com.example.heyukun.mvpretrorxmydemo.entity.ApiEntity;
import com.example.heyukun.mvpretrorxmydemo.request.CallBack;
import com.example.heyukun.mvpretrorxmydemo.request.RequestHelper;

import rx.Subscription;

/**
 * Created by heyukun on 2017/8/25.
 */

public class MainPresenterImpl implements IPresenter {
    IMainView mainView;
    public MainPresenterImpl(IMainView mainView){
        this.mainView = mainView;
    }

    @Override
    public Subscription login(String userName, String password) {
       if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)){
           Toast.makeText(MyApp.getInstance().getApplicationContext(),"不能为空",Toast.LENGTH_SHORT).show();
           return null;
       }

       if(!TextUtils.equals(userName,"13656632498")){
           Toast.makeText(MyApp.getInstance().getApplicationContext(),"账号错误",Toast.LENGTH_SHORT).show();
           return null;
       }
       if(!TextUtils.equals(password,"aijian")){
           Toast.makeText(MyApp.getInstance().getApplicationContext(),"密码错误",Toast.LENGTH_SHORT).show();
           return null;
       }
       mainView.showLoading();

        return RequestHelper.getInstance().getApiData(password, new CallBack() {
            @Override
            public void onError(String e) {
                mainView.hideLoading();
                Toast.makeText(MyApp.getInstance().getApplicationContext(),e,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object object) {
                mainView.hideLoading();
                ApiEntity apiEntity = (ApiEntity) object;
                Log.i("url",apiEntity.getUrl());
                mainView.loginSuccess();
            }
        });

    }
}
