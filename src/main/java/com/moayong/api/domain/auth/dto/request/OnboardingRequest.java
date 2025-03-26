package com.moayong.api.domain.auth.dto.request;

import com.moayong.api.domain.auth.dto.service.OnboardingServiceDto;
import com.moayong.api.domain.user.enums.SavingsBank;
import com.moayong.api.global.validation.ByteLength;
import com.moayong.api.global.validation.SavingsRate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record OnboardingRequest (
        @NotNull
        @Pattern(regexp = "^[가-힣a-zA-Z]+$", message = "공백, 특수문자, 숫자는 포함될 수 없습니다.")
        String name,

        @NotNull
        @Pattern(regexp = "^[가-힣a-zA-Z]+$", message = "공백, 특수문자, 숫자는 포함될 수 없습니다.")
        @ByteLength(min = 4, max = 16, message = "닉네임은 4바이트 이상 16바이트 이하여야 합니다.")
        String nickname,

        @NotNull
        SavingsBank savingsBank,
        @NotNull
        Integer monthlySalary,
        @SavingsRate
        Integer savingsRate,
        @NotNull
        String accountNumber
){
    public OnboardingServiceDto toServiceDto() {
        return new OnboardingServiceDto(name, nickname, savingsBank, monthlySalary, savingsRate, accountNumber);
    }
}

