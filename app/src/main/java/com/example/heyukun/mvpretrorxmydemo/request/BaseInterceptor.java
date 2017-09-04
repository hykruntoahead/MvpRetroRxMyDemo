package com.example.heyukun.mvpretrorxmydemo.request;

/**
 * Created by heyukun on 2017/8/24.
 * 构建基础拦截器
 * 用来设置基础headers
 * 这里是通过MAP来构建
 * 将header加入到request中
 */

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
public class BaseInterceptor {
    private Map<String,String> headers;
    public BaseInterceptor(Map<String,String> headers){
        this.headers = headers;
    }

    public Response intercept(Interceptor.Chain chain) throws IOException{
        Request.Builder builder = chain.request().newBuilder();
        if(headers != null && headers.size() > 0){
            Set<String> keys = headers.keySet();
            for (String header : keys){
                builder.addHeader(header,headers.get(header)).build();
            }
        }
        return chain.proceed(builder.build());
    }



}
