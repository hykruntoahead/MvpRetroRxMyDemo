package com.example.heyukun.mvpretrorxmydemo.api;

import com.example.heyukun.mvpretrorxmydemo.entity.ApiEntity;
import com.example.heyukun.mvpretrorxmydemo.entity.BaseEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by heyukun on 2017/8/21.
 */

public interface RetrofitService {
    @GET("v1/test/index")
    Observable<BaseEntity<ApiEntity>> getApiData(@Query("appkey") String appkey);
}
