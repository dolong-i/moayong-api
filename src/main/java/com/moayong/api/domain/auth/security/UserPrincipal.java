package com.moayong.api.domain.auth.security;

import com.moayong.api.domain.auth.enums.AuthProvider;
import com.moayong.api.domain.auth.enums.Role;
import com.moayong.api.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Getter
@RequiredArgsConstructor
public class UserPrincipal implements OAuth2User, UserDetails {
    private Long userId;
    private String providerId;
    private AuthProvider provider;
    private Role role;
    private Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    //일반 로그인 생성자
    public UserPrincipal(User user) {
        this.userId = user.getId();
        this.providerId = user.getProviderId();
        this.provider = user.getProvider();
        this.role = user.getRole();
        this.attributes = new HashMap<>();
    }

    //OAuth 로그인 생성자
    public UserPrincipal(User user, Map<String, Object> attributes ) {
        this.userId = user.getId();
        this.providerId = user.getProviderId();
        this.provider = user.getProvider();
        this.role = user.getRole();
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.providerId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 휴먼 계정 로직
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
