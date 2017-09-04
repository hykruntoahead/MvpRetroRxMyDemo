package com.example.heyukun.mvpretrorxmydemo.domain.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.heyukun.mvpretrorxmydemo.R;
import com.example.heyukun.mvpretrorxmydemo.entity.ApiEntity;
import com.example.heyukun.mvpretrorxmydemo.request.CallBack;
import com.example.heyukun.mvpretrorxmydemo.request.RequestHelper;
import com.example.heyukun.mvpretrorxmydemo.request.RxRetroActionManager;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {
    private TextView mTv;
    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTv = (TextView) findViewById(R.id.tv);

        //防止多次连击
       RxView.clicks(findViewById(R.id.btn))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Void>() {
                   @Override
                   public void call(Void aVoid) {

                   }
               });
    }


    @Override
    protected void onStart() {
        super.onStart();
        mSubscription = RequestHelper.getInstance().getApiData("aijian", new CallBack() {
            @Override
            public void onError(String e) {
                mTv.setText("error:"+e);
            }

            @Override
            public void onSuccess(Object object) {
                ApiEntity apiEntity = (ApiEntity) object;
                mTv.setText(apiEntity.getUrl());
            }
        });
        RxRetroActionManager.getsInstance().add("Main",mSubscription);
    }


    @Override
    protected void onStop() {
        super.onStop();
        RxRetroActionManager.getsInstance().cancel("Main");
    }
}
