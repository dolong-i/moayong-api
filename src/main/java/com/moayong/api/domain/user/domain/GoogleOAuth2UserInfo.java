package com.moayong.api.domain.user.domain;

import com.moayong.api.domain.user.domain.security.OAuth2UserInfo;
import com.moayong.api.domain.user.enums.AuthProvider;
import lombok.Getter;

import java.util.Map;

@Getter
public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public AuthProvider getAuthProvider() {
        return AuthProvider.GOOGLE;
    }
}
