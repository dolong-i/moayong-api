package com.moayong.api.domain.auth.dto.service;

import com.moayong.api.domain.user.enums.SavingsBank;

public record OnboardingServiceDto (
    String name,
    String nickname,
    SavingsBank savingsBank,
    Integer monthlySalary,
    Integer savingsRate,
    String accountNumber
) {
}