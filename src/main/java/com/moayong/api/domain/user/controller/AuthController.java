package com.moayong.api.domain.user.controller;

import com.moayong.api.domain.user.domain.jwt.JwtTokenProvider;
import com.moayong.api.domain.user.domain.security.UserPrincipal;
import com.moayong.api.domain.user.dto.request.RefreshTokenRequest;
import com.moayong.api.domain.user.dto.response.TokenResponse;
import com.moayong.api.domain.user.repository.UserRepository;
import com.moayong.api.domain.user.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final JwtTokenProvider jwtTokenProvider;

    // 소셜 로그인 테스트
    @GetMapping("/test/social-login")
    public ResponseEntity<?> testSocialLogin() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "소셜 로그인 테스트");
        response.put("socialLoginUrls", getSocialLoginUrls());
        return ResponseEntity.ok(response);
    }

    private Map<String, String> getSocialLoginUrls() {
        String baseUrl = "http://localhost:8080";
        Map<String, String> urls = new HashMap<>();
        urls.put("google", baseUrl + "/oauth2/authorize/google");
        urls.put("kakao", baseUrl + "/oauth2/authorize/kakao");
        urls.put("naver", baseUrl + "/oauth2/authorize/naver");
        return urls;
    }

    // 현재 인증된 사용자 정보 조회
    @GetMapping("/user/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(userPrincipal);
    }

    // 리프레시 토큰으로 액세스 토큰 갱신
    @PostMapping("/auth/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.badRequest().body("Invalid refresh token");
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

        if (!tokenService.validateRefreshToken(userId, refreshToken)) {
            return ResponseEntity.badRequest().body("Refresh token is not valid");
        }

        String newAccessToken = tokenService.generateAccessToken(userId);

        return ResponseEntity.ok(new TokenResponse(newAccessToken, refreshToken));
    }

    // 로그아웃
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                    @RequestHeader("Authorization") String authHeader) {
        String accessToken = authHeader.substring(7);  // "Bearer " 제거
        tokenService.logout(userPrincipal.getId(), accessToken);
        return ResponseEntity.ok("Successfully logged out");
    }
}
