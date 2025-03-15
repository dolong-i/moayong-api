package com.moayong.api.domain.user.dto.service;

import com.moayong.api.domain.user.enums.SavingsBank;
import lombok.Builder;

@Builder
public record UserUpdateServiceDto(
        SavingsBank savingsBank,
        Integer savingsAmount,
        String accountNumber
) {
}
