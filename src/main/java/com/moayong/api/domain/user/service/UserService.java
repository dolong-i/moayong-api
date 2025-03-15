package com.moayong.api.domain.user.service;

import com.moayong.api.domain.auth.enums.AuthProvider;
import com.moayong.api.domain.user.domain.User;
import com.moayong.api.domain.user.enums.UserErrorCode;
import com.moayong.api.domain.user.exception.UserException;
import com.moayong.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND, Map.of("id", id)));
    }

    public Optional<User> findUserByIdOptional(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByProviderAndProviderIdOptional(AuthProvider provider, String providerId) {
        return userRepository.findByProviderAndProviderId(provider, providerId);
    }


    public void checkDuplicateNickname(String nickname) {
        boolean isDuplicated = userRepository.findByNickname(nickname).isPresent();
        if (isDuplicated) {
            throw new UserException(UserErrorCode.DUPLICATED_NICKNAME);
        }
    }
}
