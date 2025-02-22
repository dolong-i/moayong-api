package com.moayong.api.domain.user.domain.security;

import com.moayong.api.domain.user.domain.GoogleOAuth2UserInfo;
import com.moayong.api.domain.user.domain.KakaoOAuth2UserInfo;
import com.moayong.api.domain.user.domain.NaverOAuth2UserInfo;
import com.moayong.api.domain.user.enums.AuthProvider;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.KAKAO.toString())) {
            return new KakaoOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.NAVER.toString())) {
            return new NaverOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
