package com.moayong.api.domain.auth.jwt;

import com.moayong.api.domain.auth.config.UserPrincipal;
import com.moayong.api.domain.auth.domain.UserTemporary;
import com.moayong.api.domain.auth.enums.AuthErrorCode;
import com.moayong.api.domain.auth.enums.Role;
import com.moayong.api.domain.auth.exception.AuthException;
import com.moayong.api.domain.auth.service.AuthService;
import com.moayong.api.domain.user.domain.User;
import com.moayong.api.domain.user.service.UserService;
import com.moayong.api.global.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final JwtTokenService tokenService;
    private final UserService userService;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        // 토큰 검증을 건너뛰기
        if (requestURI.startsWith("/api/v1/auth/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = CookieUtil.getCookieValue(request, "accessToken").orElse(null);

        if (!StringUtils.hasText(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        tokenService.validateToken(accessToken);

        if (tokenService.isTokenBlacklisted(accessToken)) {
            throw new AuthException(AuthErrorCode.FORBIDDEN_ACCESS);
        }

        UserPrincipal userPrincipal;
        Role role = tokenProvider.getRoleFromToken(accessToken);
        if (role.equals(Role.ONBOARDING)) {
            if (!(requestURI.startsWith("/api/v1/auth/onboarding/") || requestURI.startsWith("/api/v1/verification/account/"))) {
                throw new AuthException(AuthErrorCode.ONBOARDING_ACCESS_ONLY);
            }

            String userTemporaryId = tokenService.getUserTemporaryIdFromToken(accessToken);
            UserTemporary userTemporary = authService.findUserTemporaryById(userTemporaryId);

            userPrincipal = new UserPrincipal(userTemporary, new HashMap<>());
        } else {
            if (requestURI.startsWith("/api/v1/auth/onboarding/")) {
                throw new AuthException(AuthErrorCode.FORBIDDEN_ACCESS);
            }
            Long userId = tokenService.getUserIdFromToken(accessToken);

            User user = userService.findUserByIdOptional(userId)
                    .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND));

            userPrincipal = new UserPrincipal(user, new HashMap<>());
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
