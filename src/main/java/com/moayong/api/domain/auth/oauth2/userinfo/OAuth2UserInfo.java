package com.moayong.api.domain.auth.oauth2.userinfo;

import com.moayong.api.domain.auth.enums.AuthProvider;
import lombok.Getter;

import java.util.Map;

@Getter
public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getProviderId();
    public abstract AuthProvider getProvider();
}
