package com.moayong.api.domain.auth.jwt;

import com.moayong.api.domain.auth.config.JwtProperties;
import com.moayong.api.domain.auth.enums.AuthErrorCode;
import com.moayong.api.domain.auth.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
    private final String jwtSecret;
    @Getter
    private final long accessTokenExpiration;
    @Getter
    private final long refreshTokenExpiration;
    @Getter
    private final long onboardingAccessTokenExpiration;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtSecret = jwtProperties.getSecret();
        this.accessTokenExpiration = jwtProperties.getAccessTokenExpiration();
        this.refreshTokenExpiration = jwtProperties.getRefreshTokenExpiration();
        this.onboardingAccessTokenExpiration = jwtProperties.getOnboardingAccessTokenExpiration();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration * 1000);

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("role", Role.USER.name())
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateOnboardingAccessToken(String id) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + onboardingAccessTokenExpiration * 1000);

        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("role", Role.ONBOARDING.name())
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public Role getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String roleName = claims.get("role", String.class);
        return Role.valueOf(roleName);
    }


    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpiration * 1000);

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getSubjectFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public Date getExpirationFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    public AuthErrorCode getTokenValidateCode(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return AuthErrorCode.SUCCESS;
        } catch (MalformedJwtException ex) {
            return AuthErrorCode.INVALID_INPUT_VALUE;
        } catch (ExpiredJwtException ex) {
            return AuthErrorCode.TOKEN_EXPIRED;
        } catch (UnsupportedJwtException ex) {
            return AuthErrorCode.UNSUPPORTED_TOKEN;
        } catch (IllegalArgumentException ex) {
            return AuthErrorCode.EMPTY_JWT_CLAIMS;
        }
    }
}
