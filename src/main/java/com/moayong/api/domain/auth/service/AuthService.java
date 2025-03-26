package com.moayong.api.domain.auth.service;

import com.moayong.api.domain.auth.domain.UserTemporary;
import com.moayong.api.domain.auth.dto.service.OnboardingServiceDto;
import com.moayong.api.domain.auth.enums.AuthErrorCode;
import com.moayong.api.domain.auth.enums.Role;
import com.moayong.api.domain.auth.exception.AuthException;
import com.moayong.api.domain.auth.jwt.JwtTokenService;
import com.moayong.api.domain.auth.oauth2.userinfo.OAuth2UserInfo;
import com.moayong.api.domain.auth.repository.UserTemporaryRepository;
import com.moayong.api.domain.leaguemember.service.LeagueMatchService;
import com.moayong.api.domain.user.domain.User;
import com.moayong.api.domain.user.service.UserCommandService;
import com.moayong.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserTemporaryRepository userTemporaryRepository;
    private final JwtTokenService tokenService;
    private final UserCommandService userCommandService;
    private final UserService userService;
    private final LeagueMatchService matchService;

    public UserTemporary findUserTemporaryById(String id) {
        return userTemporaryRepository.findById(id)
                .orElseThrow(() -> new AuthException(AuthErrorCode.TOKEN_EXPIRED, "온보딩 토큰이 만료되었습니다"));
    }

    public void checkDuplicateNickname(String nickname) {
        userService.checkDuplicateNickname(nickname);
    }

    public User completeOnboarding(String accessToken, OnboardingServiceDto onboardingServiceDto) {
        checkDuplicateNickname(onboardingServiceDto.nickname());

        String userTemporaryId = tokenService.getUserTemporaryIdFromToken(accessToken);
        UserTemporary userTemporary = findUserTemporaryById(userTemporaryId);
        userTemporary.setRole(Role.USER);

        userTemporaryRepository.deleteById(userTemporaryId);

        // 유저 저장
        User user = userCommandService.saveFromTemporary(userTemporary, onboardingServiceDto);

        // 가장 낮은 리그 매칭 후 저장
        matchService.matchUserToLeagueByLevel(user, 1);

        return user;
    }

    public void logout(String accessToken) {
        Long id = tokenService.getUserIdFromToken(accessToken);
        tokenService.deleteRefreshTokenInRedis(id);
        tokenService.addTokenToBlacklist(accessToken);
    }

    public void validateUserAccess(Long requestUserId, Long loggedInUserId) {
        if (!requestUserId.equals(loggedInUserId)) {
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("requestUserId", requestUserId);
            errorData.put("loggedInUserId", loggedInUserId);
            throw new AuthException(AuthErrorCode.FORBIDDEN_ACCESS, "자신의 정보만 조회할 수 있습니다.", errorData);
        }
    }

    public UserTemporary saveUserTemporary(OAuth2UserInfo userInfo) {
        UserTemporary userTemporary = UserTemporary.builder()
                .provider(userInfo.getProvider())
                .providerId(userInfo.getProviderId())
                .role(Role.ONBOARDING)
                .email(userInfo.getEmail())
                .build();

        userTemporaryRepository.save(userTemporary);

        return userTemporary;
    }
}
