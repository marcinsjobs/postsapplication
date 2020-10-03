package com.apzumi.postsdataapplication;

public class ApiError {

    public int code;
    public String message;

    public ApiError() {
        code = 200;
        message = "OK";
    }

    public ApiError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
