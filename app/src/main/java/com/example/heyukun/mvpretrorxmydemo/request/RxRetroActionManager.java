package com.example.heyukun.mvpretrorxmydemo.request;
import android.util.ArrayMap;
import android.util.Log;

import java.util.Set;

import rx.Subscription;

/**
 * Created by heyukun on 2017/8/25.
 * 处理真实的取消请求作用，维护 rxjava 的订阅池
 */

public class RxRetroActionManager implements RxRetroActionListener<Object>{
    private static RxRetroActionManager sInstance;
    private ArrayMap<Object,Subscription> mArrayMap;

    public static RxRetroActionManager getsInstance(){
        synchronized (RxRetroActionManager.class) {
            if (sInstance == null) {
                sInstance = new RxRetroActionManager();
            }
        }
        return sInstance;
    }


    public RxRetroActionManager(){
        mArrayMap = new ArrayMap<>();
    }

    @Override
    public void add(Object tag, Subscription subscription) {
       mArrayMap.put(tag,subscription);
    }

    @Override
    public void remove(Object tag) {
        if(!mArrayMap.isEmpty()){
            mArrayMap.remove(tag);
        }

    }

    @Override
    public void removeAll() {
        if(!mArrayMap.isEmpty()){
            mArrayMap.clear();
        }
    }

    @Override
    public void cancel(Object tag) {
      if(mArrayMap.isEmpty()){
          return;
      }
      if(mArrayMap.get(tag)!=null){
          mArrayMap.get(tag).unsubscribe();
          mArrayMap.remove(tag);
      }
        Log.d("RxRetroActionManager","mArrayMap-size:"+mArrayMap.entrySet().size());
    }

    @Override
    public void cancelAll() {
     if(mArrayMap.isEmpty()){
         return;
     }

     Set<Object> keySet = mArrayMap.keySet();
        for (Object key : keySet){
            cancel(key);
        }
    }
}
