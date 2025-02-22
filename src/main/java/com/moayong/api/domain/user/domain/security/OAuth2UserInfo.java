package com.moayong.api.domain.user.domain.security;

import com.moayong.api.domain.user.enums.AuthProvider;
import lombok.Getter;

import java.util.Map;

@Getter
public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId();
    public abstract String getName();
    public abstract String getEmail();
    public abstract AuthProvider getAuthProvider();
}
