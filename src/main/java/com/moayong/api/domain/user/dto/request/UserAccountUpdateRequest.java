package com.moayong.api.domain.user.dto.request;

import com.moayong.api.domain.user.dto.service.UserUpdateServiceDto;
import com.moayong.api.domain.user.enums.SavingsBank;
import jakarta.validation.constraints.NotNull;

public record UserAccountUpdateRequest(
        @NotNull
        SavingsBank savingsBank,

        @NotNull
        Integer savingsAmount,

        @NotNull
        String accountNumber
) {
        public UserUpdateServiceDto toDto() {
                return UserUpdateServiceDto.builder()
                        .savingsBank(savingsBank)
                        .savingsAmount(savingsAmount)
                        .accountNumber(accountNumber)
                        .build();
        }
}
