package com.moayong.api.domain.user.service;

import com.moayong.api.domain.auth.domain.UserTemporary;
import com.moayong.api.domain.auth.dto.service.OnboardingServiceDto;
import com.moayong.api.domain.auth.enums.AuthProvider;
import com.moayong.api.domain.user.domain.User;
import com.moayong.api.domain.user.dto.request.UserAccountUpdateRequest;
import com.moayong.api.domain.user.enums.UserErrorCode;
import com.moayong.api.domain.user.exception.UserException;
import com.moayong.api.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
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

    public User saveFromTemporary(UserTemporary userTemporary, OnboardingServiceDto onboardingServiceDto) {
        return userRepository.save(new User(userTemporary, onboardingServiceDto));
    }

    @Transactional
    public User updateUserInfo(Long id, User user) {
        User userEntity = findUserById(id);
        userEntity.setNickname(user.getNickname());
        userEntity.setMonthlySalary(user.getMonthlySalary());
        userEntity.setSavingsRate(user.getSavingsRate());

        return userEntity;
    }

    @Transactional
    public User updateUserAccount(Long id, UserAccountUpdateRequest dto) {
        User user = findUserById(id);
        // TODO 새 통장의 잔액이 기존 통장의 누적 저축금액과 동일한지 검증

        user.setSavingsBank(dto.savingsBank());
        user.setAccountNumber(dto.accountNumber());

        return user;
    }

    public void deleteUser(Long id) {
        User user = findUserById(id);

        // TODO hard delete 할지 아니면 익명화할지 정해야함
        userRepository.delete(user);
    }
}
