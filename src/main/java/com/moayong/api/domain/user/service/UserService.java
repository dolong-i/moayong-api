package com.moayong.api.domain.user.service;

import com.moayong.api.domain.auth.domain.UserTemporary;
import com.moayong.api.domain.auth.dto.service.OnboardingServiceDto;
import com.moayong.api.domain.auth.enums.AuthProvider;
import com.moayong.api.domain.user.domain.User;
import com.moayong.api.domain.user.enums.UserErrorCode;
import com.moayong.api.domain.user.exception.UserException;
import com.moayong.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    Map<String, Object> errorData = new HashMap<>();
                    errorData.put("id", id);
                    return new UserException(UserErrorCode.USER_NOT_FOUND, errorData);
                });
    }

    public Optional<User> findUserByIdOptional(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByProviderAndProviderIdOptional(AuthProvider provider, String providerId) {
        return userRepository.findByProviderAndProviderId(provider, providerId);
    }

    public User saveFromTemporary(UserTemporary userTemporary, OnboardingServiceDto onboardingServiceDto) {
        return userRepository.save(new User(userTemporary, onboardingServiceDto));
    }
}
