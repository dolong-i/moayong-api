package com.moayong.api.domain.user.service;

import com.moayong.api.domain.user.domain.security.OAuth2UserInfo;
import com.moayong.api.domain.user.domain.security.OAuth2UserInfoFactory;
import com.moayong.api.domain.user.domain.User;
import com.moayong.api.domain.user.domain.security.UserPrincipal;
import com.moayong.api.domain.user.exception.OAuth2AuthenticationProcessingException;
import com.moayong.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationProcessingException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return processOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        // 등록된 OAuth2 서비스 제공자 식별
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 제공자 별 사용자 정보 추출
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                registrationId, oAuth2User.getAttributes());

        if (userInfo.getEmail() == null || userInfo.getEmail().isEmpty()) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        // 기존 사용자 확인 또는 신규 등록
        User user = userRepository.findByEmailAndAuthProvider(userInfo.getEmail(), userInfo.getAuthProvider())
                .map(existingUser -> updateExistingUser(existingUser, userInfo))
                .orElseGet(() -> registerNewUser(userInfo));

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserInfo userInfo) {
        User user = User.builder()
                .email(userInfo.getEmail())
                .nickname(userInfo.getName())
                .authProvider(userInfo.getAuthProvider())
                .providerId(userInfo.getId())
                .build();

        return userRepository.save(user);
    }

    private User updateExistingUser(User user, OAuth2UserInfo userInfo) {
        // 사용자 정보 업데이트
        if (!user.getAuthProvider().equals(userInfo.getAuthProvider())) {
            user.setAuthProvider(userInfo.getAuthProvider());
            user.setProviderId(userInfo.getId());
        }

        // 닉네임 업데이트가 필요한 경우
        if (userInfo.getName() != null && !userInfo.getName().equals(user.getNickname())) {
            user.setNickname(userInfo.getName());
        }

        return userRepository.save(user);
    }
}
