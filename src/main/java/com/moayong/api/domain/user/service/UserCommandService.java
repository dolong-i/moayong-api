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
        User userEntity = userService.findUserById(id);
        userEntity.setNickname(user.getNickname());
        userEntity.setMonthlySalary(user.getMonthlySalary());
        userEntity.setSavingsRate(user.getSavingsRate());

        return userEntity;
    }

    @Transactional
    public User updateUserAccount(Long id, UserUpdateServiceDto dto) {
        User user = userService.findUserById(id);

        Integer prevSavingsAmount = savingsService.findSavingsTotalAmountByUserId(id);
        if (!dto.savingsAmount().equals(prevSavingsAmount)) {
            throw new UserException(UserErrorCode.TOTAL_SAVINGS_NOT_MATCH);
        }

        user.setSavingsBank(dto.savingsBank());
        user.setAccountNumber(dto.accountNumber());

        return user;
    }

    public void deleteUser(Long id) {
        User user = userService.findUserById(id);

        // TODO hard delete 할지 아니면 익명화할지 정해야함
        userRepository.delete(user);
    }
}
