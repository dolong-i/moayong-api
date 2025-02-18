package com.moayong.api.global.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {
    private int code;
    private T result;
    private String msg;

    private static final int SUCCESS = 200;

    private ApiResponse(int code, T result, String msg) {
        this.code = code;
        this.result = result;
        this.msg = msg;
    }

    public static <T> ApiResponse<T> success(T result, String message) {
        return new ApiResponse<T>(SUCCESS, result, message);
    }

    public static <T> ApiResponse<T> fail(ResponseCode responseCode, T result) {
        return new ApiResponse<T>(responseCode.getHttpStatusCode(), result, responseCode.getMessage());
    }
}
