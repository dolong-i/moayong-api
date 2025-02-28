package com.moayong.api.domain.auth.jwt;

import com.moayong.api.domain.auth.enums.AuthErrorCode;
import com.moayong.api.domain.auth.security.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
    private final String jwtSecret;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtSecret = jwtProperties.getSecret();
        this.accessTokenExpiration = jwtProperties.getAccessTokenExpiration();
        this.refreshTokenExpiration = jwtProperties.getRefreshTokenExpiration();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public AuthErrorCode validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return AuthErrorCode.SUCCESS;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
            return AuthErrorCode.INVALID_INPUT_VALUE;
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
            return AuthErrorCode.TOKEN_EXPIRED;
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
            return AuthErrorCode.UNSUPPORTED_TOKEN;
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
            return AuthErrorCode.EMPTY_JWT_CLAIMS;
        }
    }

    public long getRefreshTokenExpirationMillis() {
        return refreshTokenExpiration;
    }

    public long getAccessTokenExpirationMillis() {
        return accessTokenExpiration;
    }
}
