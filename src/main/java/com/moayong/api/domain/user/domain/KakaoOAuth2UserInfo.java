package com.moayong.api.domain.user.domain;

import com.moayong.api.domain.user.domain.security.OAuth2UserInfo;
import com.moayong.api.domain.user.enums.AuthProvider;
import lombok.Getter;

import java.util.Map;

@Getter
public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        if (properties == null) {
            return null;
        }
        return (String) properties.get("nickname");
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount == null) {
            return null;
        }
        return (String) kakaoAccount.get("email");
    }

    @Override
    public AuthProvider getAuthProvider() {
        return AuthProvider.KAKAO;
    }
}
