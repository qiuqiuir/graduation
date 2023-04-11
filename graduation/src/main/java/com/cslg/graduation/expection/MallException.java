package com.cslg.graduation.expection;

import com.cslg.graduation.common.ResponseCode;

/**
 * API异常处理类
 */
public class MallException extends RuntimeException {

    private Integer status;

    private String message;

    public Integer getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public MallException(ResponseCode resultEnum) {
        this.status = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }

    public MallException(Integer code, String message) {
        this.message = message;
        this.status = code;
    }

    public MallException(String message) {
        this.message = message;
        this.status = 400;
    }
}
