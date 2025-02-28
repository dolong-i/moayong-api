package com.moayong.api.domain.user.dto.response;

import com.moayong.api.domain.auth.enums.AuthProvider;
import com.moayong.api.domain.auth.enums.Role;
import com.moayong.api.domain.user.domain.User;
import com.moayong.api.domain.user.enums.SavingsBank;

public record UserResponse (
        Long id,
        AuthProvider provider,
        String providerId,
        Role role,
        String name,
        String nickname,
        Integer monthlySalary,
        Integer savingsRate,
        SavingsBank savingsBank,
        String accountNumber
){
    public UserResponse(User user) {
        this(
                user.getId(),
                user.getProvider(),
                user.getProviderId(),
                user.getRole(),
                user.getName(),
                user.getNickname(),
                user.getMonthlySalary(),
                user.getSavingsRate(),
                user.getSavingsBank(),
                user.getAccountNumber()
        );
    }
}
