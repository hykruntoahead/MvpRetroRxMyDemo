package com.example.heyukun.mvpretrorxmydemo.entity;

/**
 * Created by heyukun on 2017/8/24.
 */

public class BaseEntity<T> {
    /**
     * code : 200
     * msg : 调用成功
     * data : {"url":"http://api.iaijian.com"}
     */

    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
