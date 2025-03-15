package com.moayong.api.domain.verification.dto.service;

import com.moayong.api.domain.user.enums.SavingsBank;
import com.moayong.api.domain.verification.domain.Verification;
import com.moayong.api.domain.verification.dto.response.AccountVerificationResponse;
import com.moayong.api.domain.verification.enums.VerificationStatus;

public record AccountVerificationServiceDto (
        String id,
        SavingsBank bank,
        VerificationStatus status,
        String imageUrl,
        String accountNumber,
        Integer accountBalance
)
{
    public AccountVerificationServiceDto(Verification verification, AccountServiceDto account) {
        this(
                verification.getId(),
                verification.getBank(),
                verification.getStatus(),
                verification.getImageUrl(),
                account.accountNumber(),
                account.accountBalance()
        );
    }

    public AccountVerificationResponse toResponse() {
        return AccountVerificationResponse.builder()
                .id(id)
                .bank(bank)
                .status(status)
                .imageUrl(imageUrl)
                .accountNumber(accountNumber)
                .accountBalance(accountBalance)
                .build();
    }
}
