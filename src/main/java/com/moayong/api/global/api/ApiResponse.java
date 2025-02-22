package com.moayong.api.global.api;

import org.springframework.http.HttpStatus;

public record ApiResponse<T>(int code, T data, String message) {
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(HttpStatus.OK.value(), data, message);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, null, message);
    }
}