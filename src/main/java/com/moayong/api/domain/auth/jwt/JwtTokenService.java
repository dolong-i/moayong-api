package com.moayong.api.domain.auth.jwt;

import com.moayong.api.domain.auth.enums.AuthErrorCode;
import com.moayong.api.domain.auth.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String REFRESH_TOKEN_PREFIX = "RT:";
    private static final String BLACKLIST_PREFIX = "BL:";

    public String generateAccessToken(Long userId) {
        return jwtTokenProvider.generateAccessToken(userId);
    }

    public String refreshAccessToken(String refreshToken) {
        AuthErrorCode refreshTokenValidateCode = jwtTokenProvider.validateToken(refreshToken);
        if (!refreshTokenValidateCode.equals(AuthErrorCode.SUCCESS)) {
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("refresh_token", refreshTokenValidateCode.getMessage());
            throw new AuthException(refreshTokenValidateCode, errorData);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

        AuthErrorCode refreshTokenValidateCodeInRedis = validateRefreshTokenInRedis(userId, refreshToken);
        if (!refreshTokenValidateCodeInRedis.equals(AuthErrorCode.SUCCESS)) {
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("refresh_token", refreshTokenValidateCode.getMessage());
            throw new AuthException(refreshTokenValidateCode, errorData);
        }

        return generateAccessToken(userId);
    }

    public void logout(Long userId, String accessToken) {
        // Refresh Token 삭제
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + userId);

        try {
            long expiration = jwtTokenProvider.getAccessTokenExpirationMillis();
            String blacklistKey = BLACKLIST_PREFIX + accessToken;
            redisTemplate.opsForValue().set(blacklistKey, "logout", expiration, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.warn("Failed to add access token to blacklist", e);
        }
    }

    public String generateRefreshToken(Long userId) {
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId);
        storeRefreshToken(userId, refreshToken);
        return refreshToken;
    }

    private void storeRefreshToken(Long userId, String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + userId;
        long expiration = jwtTokenProvider.getRefreshTokenExpirationMillis() / 1000;
        redisTemplate.opsForValue().set(key, refreshToken, expiration, TimeUnit.SECONDS);
    }

    public boolean isTokenBlacklisted(String accessToken) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + accessToken));
    }

    public AuthErrorCode validateRefreshTokenInRedis(Long userId, String refreshToken) {
        String storedToken = (String) redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + userId);
        // null check 부터 실행하도록
        if (storedToken == null || !refreshToken.equals(storedToken)) {
            return AuthErrorCode.TOKEN_EXPIRED;
        }

        return jwtTokenProvider.validateToken(refreshToken);
    }
}
