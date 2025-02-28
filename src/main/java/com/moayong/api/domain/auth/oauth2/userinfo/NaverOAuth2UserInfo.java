package com.moayong.api.domain.auth.oauth2.userinfo;

import com.moayong.api.domain.auth.enums.AuthProvider;
import lombok.Getter;

import java.util.Map;

@Getter
public class NaverOAuth2UserInfo extends OAuth2UserInfo {
    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getProviderId() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return (String) response.get("id");
    }

    @Override
    public AuthProvider getProvider() {
        return AuthProvider.NAVER;
    }
}
