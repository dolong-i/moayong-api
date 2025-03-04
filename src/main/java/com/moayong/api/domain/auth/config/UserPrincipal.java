package com.moayong.api.domain.auth.config;

import com.moayong.api.domain.auth.domain.UserTemporary;
import com.moayong.api.domain.auth.enums.AuthProvider;
import com.moayong.api.domain.auth.enums.Role;
import com.moayong.api.domain.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class UserPrincipal implements OAuth2User, UserDetails {
    private String userId;
    private String providerId;
    private AuthProvider provider;
    private Role role;
    private String email;
    private Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    // OAuth 로그인 생성자
    public UserPrincipal(UserTemporary userTemporary, Map<String, Object> attributes ) {
        this.userId = userTemporary.getCompositeKey();
        this.providerId = userTemporary.getProviderId();
        this.provider = userTemporary.getProvider();
        this.role = userTemporary.getRole();
        this.email = userTemporary.getEmail();
        this.attributes = attributes;
    }

    // 유저 로그인 생성자
    public UserPrincipal(User user, Map<String, Object> attributes ) {
        this.userId = String.valueOf(user.getId());
        this.providerId = user.getProviderId();
        this.provider = user.getProvider();
        this.role = user.getRole();
        this.email = user.getEmail();
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
        return this.email;
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
