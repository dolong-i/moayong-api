package com.moayong.api.domain.user.dto.request;

import com.moayong.api.domain.user.domain.User;
import com.moayong.api.global.validation.ByteLength;
import com.moayong.api.global.validation.SavingsRate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UserUpdateRequest(
        @ByteLength(min = 4, max = 16, message = "닉네임은 4바이트 이상 16바이트 이하여야 합니다.")
        String nickname,

        @NotNull
        @Positive
        Integer monthlySalary,

        @SavingsRate
        Integer savingsRate
) {
        public User toEntity() {
                return User.builder()
                        .nickname(nickname)
                        .monthlySalary(monthlySalary)
                        .savingsRate(savingsRate)
                        .build();
        }
}
