package com.moayong.api.domain.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moayong.api.domain.auth.enums.AuthProvider;
import com.moayong.api.domain.auth.enums.Role;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserTemporary {
    private String providerId;
    private AuthProvider provider;
    private String email;
    private Role role;

    @Builder
    public UserTemporary(String providerId, AuthProvider provider, Role role, String email) {
        this.providerId = providerId;
        this.provider = provider;
        this.role = role;
        this.email = email;
    }

    // 복합 키: provider + providerId
    @JsonIgnore
    public String getCompositeKey() {
        return provider.name() + providerId;
    }
}