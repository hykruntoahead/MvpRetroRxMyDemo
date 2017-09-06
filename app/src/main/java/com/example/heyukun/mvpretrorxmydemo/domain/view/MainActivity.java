package com.example.heyukun.mvpretrorxmydemo.domain.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.heyukun.mvpretrorxmydemo.R;
import com.example.heyukun.mvpretrorxmydemo.domain.presenter.IPresenter;
import com.example.heyukun.mvpretrorxmydemo.domain.presenter.MainPresenterImpl;
import com.example.heyukun.mvpretrorxmydemo.request.RxRetroActionManager;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements IMainView{
    private EditText mEtNum,mEtPassword;
    private Button mBtn;
    private Subscription mSubscription;
    private IPresenter mPresenter;
    private ProgressDialog mProDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new MainPresenterImpl(this);
        initWidgets();
    }

    private void initWidgets() {
        mEtNum = (EditText) findViewById(R.id.et_number);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mBtn = (Button) findViewById(R.id.btn_login);

        //防止多次连击
        RxView.clicks(mBtn)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.d("RxView","click");
                        mSubscription = mPresenter.login(mEtNum.getText().toString(),mEtPassword.getText().toString());
                        if(mSubscription!=null) {
                            RxRetroActionManager.getsInstance().add("Main", mSubscription);
                        }
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
        RxRetroActionManager.getsInstance().cancel("Main");
    }

    @Override
    public void showLoading() {
        if(mProDialog == null) {
            mProDialog = ProgressDialog.show(this, "登录", "正在加载中...");
        }
    }

    @Override
    public void hideLoading() {
       if(mProDialog !=null){
           mProDialog.cancel();
       }
    }

    @Override
    public void loginSuccess() {
        startActivity(new Intent(MainActivity.this,SecondActivity.class));
    }
}
