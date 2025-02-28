package com.moayong.api.domain.auth.oauth2.userinfo;

import com.moayong.api.domain.auth.enums.AuthProvider;
import lombok.Getter;

import java.util.Map;

@Getter
public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public AuthProvider getProvider() {
        return AuthProvider.GOOGLE;
    }
}
