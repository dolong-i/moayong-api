package com.moayong.api.domain.user.service;

import com.moayong.api.domain.auth.domain.UserTemporary;
import com.moayong.api.domain.auth.dto.service.OnboardingServiceDto;
import com.moayong.api.domain.savings.service.SavingsService;
import com.moayong.api.domain.user.domain.User;
import com.moayong.api.domain.user.dto.service.UserUpdateServiceDto;
import com.moayong.api.domain.user.enums.UserErrorCode;
import com.moayong.api.domain.user.exception.UserException;
import com.moayong.api.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserCommandService {
    private final SavingsService savingsService;
    private final UserService userService;
    private final UserRepository userRepository;

    public User saveFromTemporary(UserTemporary userTemporary, OnboardingServiceDto onboardingServiceDto) {
        return userRepository.save(new User(userTemporary, onboardingServiceDto));
    }

    @Transactional
    public User updateUserInfo(Long id, User user) {
        User userEntity = userService.findActiveUserById(id);
        userEntity.setNickname(user.getNickname());
        userEntity.setMonthlySalary(user.getMonthlySalary());
        userEntity.setSavingsRate(user.getSavingsRate());

        return userEntity;
    }

    @Transactional
    public User updateUserAccount(Long id, UserUpdateServiceDto dto) {
        User user = userService.findActiveUserById(id);

        Integer prevSavingsAmount = savingsService.findSavingsTotalAmountByUserId(id);
        if (!dto.savingsAmount().equals(prevSavingsAmount)) {
            throw new UserException(UserErrorCode.TOTAL_SAVINGS_NOT_MATCH);
        }

        user.setSavingsBank(dto.savingsBank());
        user.setAccountNumber(dto.accountNumber());

        return user;
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userService.findActiveUserById(id);

        anonymizeUserInfo(user);
        userRepository.save(user);

        userRepository.delete(user);
    }

    public void anonymizeUserInfo(User user) {
        user.setName("탈퇴유저");
        user.setNickname(getShortUuid());
        user.setEmail("deleted-user@example.com");
        user.setAccountNumber("0000000000");
    }

    // 8자리 UUID 생성
    private String getShortUuid() {
        String uuid = UUID.randomUUID().toString();

        byte[] uuidBytes = uuid.getBytes(StandardCharsets.UTF_8);
        byte[] hashBytes;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            hashBytes = messageDigest.digest(uuidBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new UserException(UserErrorCode.ERROR_WHILE_DELETING_USER);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(String.format("%02x", hashBytes[i]));
        }

        return sb.toString();
    }
}
