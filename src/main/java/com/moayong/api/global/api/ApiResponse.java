package com.moayong.api.global.api;

import org.springframework.http.HttpStatus;

import java.util.Map;

public record ApiResponse<T>(String code, T data, String message) {
    // 성공 응답
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(String.valueOf(HttpStatus.OK.value()), data, message);
    }

    // 에러 응답 (기본 형태)
    public static ApiResponse<Object> error(String code, String message) {
        return new ApiResponse<>(code, null, message);
    }

    // 에러 응답 (상세 정보 포함)
    public static ApiResponse<Object> error(String code, Map<String, Object> data, String message) {
        return new ApiResponse<>(code, data, message);
    }
}