package com.cslg.graduation.common;


public enum ResponseCode {
    SUCCESS(200, "ok"),
    ERROR(400, "ERROR"),
    ;


    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
