package com.mass.sdk.models;

import com.google.gson.annotations.SerializedName;

public final class ApiResponse<T> {
    @SerializedName("code")
    private int code;
    @SerializedName("msg")
    private String msg = "";
    @SerializedName("data")
    private T data;

    public int getCode() {
        return code;
    }

    public ApiResponse<T> withCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ApiResponse<T> withMsg(String msg) {
        this.msg = msg == null ? "" : msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public ApiResponse<T> withData(T data) {
        this.data = data;
        return this;
    }
}
