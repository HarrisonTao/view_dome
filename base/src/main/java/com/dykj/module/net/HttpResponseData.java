package com.dykj.module.net;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class HttpResponseData<T> {
    public String status;
    public String message;
    @SerializedName("info")
    private T data;

    public HttpResponseData() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }


    @Override
    @NotNull
    public String toString() {
        return "\n   [code: " + this.status + "\n     msg: " + this.message + "\n    data: " + this.data + "\n";
    }
}
