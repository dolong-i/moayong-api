package com.moayong.api.domain.auth.oauth2.handler;

import com.moayong.api.domain.auth.config.UserPrincipal;
import com.moayong.api.domain.auth.enums.Role;
import com.moayong.api.domain.auth.jwt.JwtTokenService;
import com.moayong.api.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenService tokenService;

    @Value("${app.oauth2.redirect-uri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Role role = userPrincipal.getRole();

        if (role.equals(Role.ONBOARDING)) {
            String accessToken = tokenService.generateOnboardingAccessToken(userPrincipal.getUserId());
            CookieUtil.addCookie(response, "accessToken", accessToken, (int) (tokenService.getOnboardingAccessTokenExpiration()));
        } else {
            String accessToken = tokenService.generateAccessToken(Long.valueOf(userPrincipal.getUserId()));
            String refreshToken = tokenService.generateRefreshToken(Long.valueOf(userPrincipal.getUserId()));

            CookieUtil.addCookie(response, "accessToken", accessToken, (int) (tokenService.getAccessTokenExpiration()));
            CookieUtil.addCookie(response, "refreshToken", refreshToken, (int) (tokenService.getRefreshTokenExpiration()));
        }

        String loginSuccessRedirectUri = redirectUri + "/login-success";

        // 프론트엔드로 리다이렉트 (토큰 포함)
        return UriComponentsBuilder.fromUriString(loginSuccessRedirectUri)
                .build().toUriString();
    }
}
