package com.moayong.api.domain.auth.jwt;

import com.moayong.api.domain.auth.enums.AuthErrorCode;
import com.moayong.api.domain.auth.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final JwtTokenProvider tokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String REFRESH_TOKEN_PREFIX = "RT:";
    private static final String BLACKLIST_PREFIX = "BL:";

    public String generateAccessToken(Long userId) {
        return tokenProvider.generateAccessToken(userId);
    }

    public String generateOnboardingAccessToken(String id) {
        // JWT 생성 시 "onboarding" 역할만 가지도록 설정
        return tokenProvider.generateOnboardingAccessToken(id);
    }

    public String refreshAccessToken(String refreshToken) {
        validateToken(refreshToken);
        Long userId = getUserIdFromToken(refreshToken);
        validateRefreshTokenInRedis(userId, refreshToken);
        return generateAccessToken(userId);
    }

    public void validateToken(String token) {
        AuthErrorCode validateCode = tokenProvider.getTokenValidateCode(token);
        if (!validateCode.equals(AuthErrorCode.SUCCESS)) {
            throw new AuthException(validateCode);
        }
    }

    public Long getUserIdFromToken(String token) {
        return Long.parseLong(tokenProvider.getSubjectFromToken(token));
    }

    public String getUserTemporaryIdFromToken(String token) {
        return tokenProvider.getSubjectFromToken(token);
    }

    public String generateRefreshToken(Long userId) {
        String refreshToken = tokenProvider.generateRefreshToken(userId);
        storeRefreshTokenInRedis(userId, refreshToken);
        return refreshToken;
    }

    private void storeRefreshTokenInRedis(Long userId, String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + userId;
        long expiration = getRefreshTokenExpiration();
        redisTemplate.opsForValue().set(key, refreshToken, expiration, TimeUnit.SECONDS);
    }

    public void deleteRefreshTokenInRedis(Long userId) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + userId);
    }

    public void addTokenToBlacklist(String accessToken) {
        if (isTokenBlacklisted(accessToken)) {
            return;
        }

        Date expiryDate = tokenProvider.getExpirationFromToken(accessToken);
        long now = System.currentTimeMillis();
        long expiration = (expiryDate.getTime() - now) / 1000;

        if (expiration <= 0) {
            return;
        }

        String blacklistKey = BLACKLIST_PREFIX + accessToken;
        redisTemplate.opsForValue().set(blacklistKey, "logout", expiration, TimeUnit.SECONDS);
    }

    public boolean isTokenBlacklisted(String accessToken) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + accessToken));
    }

    public void validateRefreshTokenInRedis(Long userId, String refreshToken) {
        String storedToken = (String) redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + userId);
        // null check 부터 실행하도록
        if (storedToken == null || !refreshToken.equals(storedToken)) {
            throw new AuthException(AuthErrorCode.TOKEN_EXPIRED, "refresh token 이 만료했거나 존재하지 않습니다");
        }
    }

    public long getAccessTokenExpiration() {
        return tokenProvider.getAccessTokenExpiration();
    }

    public long getOnboardingAccessTokenExpiration() {
        return tokenProvider.getOnboardingAccessTokenExpiration();
    }

    public long getRefreshTokenExpiration() {
        return tokenProvider.getRefreshTokenExpiration();
    }
}
