package com.cslg.graduation.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * Created by jia on 2018/6/1.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ResponseService<T> implements Serializable {

    private Integer status;
    private String message;
    private T data;

    private ResponseService(Integer status) {
        this.status = status;
    }

    private ResponseService(Integer status, String message) {
        this.status = status;
        this.message = message;

    }

    private ResponseService(Integer status, T data) {
        this.status = status;
        this.data = data;
    }

    private ResponseService(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public Integer getStatus() {
        return status;
    }

    public String getmessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static <T> ResponseService<T> createBySuccess() {
        return new ResponseService<T>(ResponseCode.SUCCESS.getCode(),ResponseCode.SUCCESS.getMessage());
    }

    public static <T> ResponseService<T> createBySuccessMessage(String message) {
        return new ResponseService<T>(ResponseCode.SUCCESS.getCode(), message);
    }

    public static <T> ResponseService<T> createBySuccess(T data) {
        return new ResponseService<T>(ResponseCode.SUCCESS.getCode(),ResponseCode.SUCCESS.getMessage(), data);
    }

    public static <T> ResponseService<T> createBySuccess(String message, T data) {
        return new ResponseService<T>(ResponseCode.SUCCESS.getCode(), message, data);
    }

    public static <T> ResponseService<T> createByError() {
        return new ResponseService<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
    }

    public static <T> ResponseService<T> createByErrorMessage(String errorMessage) {
        return new ResponseService<T>(ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static <T> ResponseService<T> createByCodeErrorMessage(int errorCode, String errorMessage) {
        return new ResponseService<T>(errorCode, errorMessage);
    }

    public static <T> ResponseService<T> createByError(ResponseCode responseCode) {
        return new ResponseService<T>(responseCode.getCode(), responseCode.getMessage());
    }

}
