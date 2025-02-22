package com.moayong.api.domain.user.service;

import com.moayong.api.domain.user.domain.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String REFRESH_TOKEN_PREFIX = "RT:";
    private static final String BLACKLIST_PREFIX = "BL:";

    public String generateAccessToken(Long userId) {
        return jwtTokenProvider.generateAccessToken(userId);
    }

    public String generateRefreshToken(Long userId) {
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId);
        // Redis에 Refresh Token 저장
        storeRefreshToken(userId, refreshToken);
        return refreshToken;
    }

    private void storeRefreshToken(Long userId, String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + userId;
        long expiration = jwtTokenProvider.getRefreshTokenExpirationMillis() / 1000;
        redisTemplate.opsForValue().set(key, refreshToken, expiration, TimeUnit.SECONDS);
    }

    public void logout(Long userId, String accessToken) {
        // Refresh Token 삭제
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + userId);

        // Access Token을 블랙리스트에 추가
        try {
            long expiration = jwtTokenProvider.getUserIdFromToken(accessToken);
            String blacklistKey = BLACKLIST_PREFIX + accessToken;
            redisTemplate.opsForValue().set(blacklistKey, "logout", expiration, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            // 만료된 토큰이어도 로그아웃 처리는 성공으로 간주
        }
    }

    public boolean isTokenBlacklisted(String accessToken) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + accessToken));
    }

    public boolean validateRefreshToken(Long userId, String refreshToken) {
        String storedToken = (String) redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + userId);
        return refreshToken.equals(storedToken) && jwtTokenProvider.validateToken(refreshToken);
    }
}
