package com.moayong.api.domain.user.service;

import com.moayong.api.domain.auth.enums.AuthErrorCode;
import com.moayong.api.domain.auth.exception.AuthException;
import com.moayong.api.domain.user.domain.User;
import com.moayong.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                    return new AuthException(AuthErrorCode.INVALID_INPUT_VALUE, errorData);
                });
    }
}
