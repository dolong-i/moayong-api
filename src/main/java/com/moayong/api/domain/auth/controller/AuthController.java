package com.moayong.api.domain.auth.controller;

import com.moayong.api.domain.auth.jwt.JwtTokenService;
import com.moayong.api.domain.auth.security.UserPrincipal;
import com.moayong.api.domain.auth.dto.request.RefreshTokenRequest;
import com.moayong.api.domain.auth.dto.response.TokenResponse;
import com.moayong.api.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {
    private final JwtTokenService jwtTokenService;

    // 리프레시 토큰으로 액세스 토큰 갱신
    @PostMapping("/auth/refresh")
    public ApiResponse<TokenResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        String newAccessToken = jwtTokenService.refreshAccessToken(refreshToken);

        return ApiResponse.success(new TokenResponse(newAccessToken, refreshToken), "토큰 재발급 성공");
    }

    // 로그아웃
    @PostMapping("/auth/logout")
    public ApiResponse<?> logout(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                      @RequestHeader("Authorization") String authHeader) {
        String accessToken = authHeader.substring(7);  // "Bearer " 제거
        jwtTokenService.logout(userPrincipal.getUserId(), accessToken);
        return ApiResponse.success(null, "로그아웃 성공");
    }
}
