package com.venues.lt.demo.util;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData<T> {

    private Integer status;

    private String message;

    private T body;

    public static ResponseData success() {
        return new ResponseData<>(ResponseCode.SUCCESS, ResponseMsg.SUCCESS, null);
    }

    public static ResponseData success(String message) {
        return new ResponseData<>(ResponseCode.SUCCESS, message, null);
    }

    public static <T> ResponseData<T> success(T body) {
        return new ResponseData<>(ResponseCode.SUCCESS, ResponseMsg.SUCCESS, body);
    }

    public static ResponseData fail() {
        return new ResponseData<>(ResponseCode.FAIL, ResponseMsg.FAIL, null);
    }

    public static ResponseData fail(String message) {
        return new ResponseData<>(ResponseCode.FAIL, message, null);
    }

    public static ResponseData fail(Integer status, String message) {
        return new ResponseData<>(status, message, null);
    }
}
