package com.moayong.api.domain.auth.controller;

import com.moayong.api.domain.auth.dto.request.OnboardingRequest;
import com.moayong.api.domain.auth.enums.AuthProvider;
import com.moayong.api.domain.auth.jwt.JwtTokenService;
import com.moayong.api.domain.auth.service.AuthService;
import com.moayong.api.domain.user.domain.User;
import com.moayong.api.domain.user.dto.response.UserResponse;
import com.moayong.api.global.api.ApiResponse;
import com.moayong.api.global.util.CookieUtil;
import com.moayong.api.global.util.TokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final JwtTokenService tokenService;
    private final TokenExtractor tokenExtractor;
    private final AuthService authService;

    @GetMapping("/authorize/{provider}")
    public String authorize(@PathVariable AuthProvider provider) {
        return "redirect:/oauth2/authorization/" + provider;
    }

    @PostMapping("/refresh")
    public ApiResponse<Void> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = tokenExtractor.extractRefreshTokenFromRequest(request);

        String newAccessToken = tokenService.refreshAccessToken(refreshToken);

        CookieUtil.addCookie(response, "accessToken", newAccessToken, (int) tokenService.getAccessTokenExpiration());
        return ApiResponse.success(null, "토큰 재발급 성공");
    }

    @GetMapping("/onboarding/nickname-check")
    public ApiResponse<Void> checkDuplicateNickname(@RequestParam("nickname") String nickname) {
        authService.checkDuplicateNickname(nickname);

        return ApiResponse.success(null, "사용 가능한 닉네임입니다.");
    }

    @PostMapping("/onboarding/complete")
    public ApiResponse<UserResponse> onboardingComplete(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid OnboardingRequest onboardingRequest) {
        String accessToken = tokenExtractor.extractAccessTokenFromRequest(request);

        User user = authService.completeOnboarding(accessToken, onboardingRequest.toServiceDto());
        Long userId = user.getId();
        String newAccessToken = tokenService.generateAccessToken(userId);
        String refreshToken = tokenService.generateRefreshToken(userId);

        CookieUtil.addCookie(response, "accessToken", newAccessToken, (int) tokenService.getAccessTokenExpiration());
        CookieUtil.addCookie(response, "refreshToken", refreshToken, (int) tokenService.getRefreshTokenExpiration());

        return ApiResponse.success(new UserResponse(user), "온보딩이 완료되었습니다.");
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = tokenExtractor.extractAccessTokenFromRequest(request);

        authService.logout(accessToken);

        CookieUtil.deleteCookie(response, "accessToken");
        CookieUtil.deleteCookie(response, "refreshToken");

        return ApiResponse.success(null, "로그아웃이 완료되었습니다.");
    }
}
