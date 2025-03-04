package com.moayong.api.global.util;

import com.moayong.api.domain.auth.enums.AuthErrorCode;
import com.moayong.api.domain.auth.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class TokenExtractor {
    private static final String ACCESS_TOKEN_COOKIE = "accessToken";
    private static final String REFRESH_TOKEN_COOKIE = "refreshToken";

    public String extractAccessTokenFromRequest(HttpServletRequest request) {
        return CookieUtil.getCookieValue(request, ACCESS_TOKEN_COOKIE)
                .orElseThrow(() -> new AuthException(AuthErrorCode.INVALID_INPUT_VALUE, "엑세스 토큰이 없습니다."));
    }

    public String extractRefreshTokenFromRequest(HttpServletRequest request) {
        return CookieUtil.getCookieValue(request, REFRESH_TOKEN_COOKIE)
                .orElseThrow(() -> new AuthException(AuthErrorCode.INVALID_INPUT_VALUE, "리프레시 토큰이 없습니다."));
    }
}
