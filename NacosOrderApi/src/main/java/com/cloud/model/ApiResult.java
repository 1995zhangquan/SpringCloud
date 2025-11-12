package com.cloud.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApiResult implements Serializable {
    private int code;        // 状态码
    private String message;  // 返回消息
    private Object data;     // 返回数据

    public ApiResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ApiResult fail(int code, String message) {
        return new ApiResult(code, message);
    }

}
