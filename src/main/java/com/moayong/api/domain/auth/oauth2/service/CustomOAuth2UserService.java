package com.moayong.api.domain.auth.oauth2.service;

import com.moayong.api.domain.auth.enums.AuthProvider;
import com.moayong.api.domain.auth.enums.Role;
import com.moayong.api.domain.auth.oauth2.userinfo.OAuth2UserInfo;
import com.moayong.api.domain.auth.oauth2.userinfo.OAuth2UserInfoFactory;
import com.moayong.api.domain.user.domain.User;
import com.moayong.api.domain.auth.security.UserPrincipal;
import com.moayong.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return processOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        // 등록된 OAuth2 서비스 제공자 식별
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        AuthProvider authProvider = AuthProvider.fromString(registrationId);

        // 제공자 별 사용자 정보 추출
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                authProvider, oAuth2User.getAttributes());

        // 기존 사용자 확인 또는 신규 등록
        User user = userRepository.findByProviderIdAndProvider(userInfo.getProviderId(), userInfo.getProvider())
                .orElseGet(() -> registerNewUser(userInfo));

        return new UserPrincipal(user, userInfo.getAttributes());
    }

    private User registerNewUser(OAuth2UserInfo userInfo) {
        User user = User.builder()
                .provider(userInfo.getProvider())
                .providerId(userInfo.getProviderId())
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }
}
