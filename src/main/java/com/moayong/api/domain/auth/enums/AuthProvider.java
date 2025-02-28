package com.moayong.api.domain.auth.enums;

import com.moayong.api.domain.auth.exception.AuthException;

import java.util.HashMap;
import java.util.Map;

public enum AuthProvider {
    KAKAO, NAVER, GOOGLE;

    public static AuthProvider fromString(String value) {
        if (value == null) {
            throw new AuthException(AuthErrorCode.INVALID_INPUT_VALUE, "소셜 로그인 제공자가 제공되지 않았습니다.");
        }

        for (AuthProvider authProvider : AuthProvider.values()) {
            if (authProvider.name().equalsIgnoreCase(value)) {
                return authProvider;
            }
        }

        Map<String, Object> errorData = new HashMap<>();
        errorData.put("auth_provider", value);

        throw new AuthException(AuthErrorCode.INVALID_INPUT_VALUE, errorData);
    }
}
