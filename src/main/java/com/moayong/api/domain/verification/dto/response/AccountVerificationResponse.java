package com.moayong.api.domain.verification.dto.response;

import com.moayong.api.domain.user.enums.SavingsBank;
import com.moayong.api.domain.verification.domain.Verification;
import com.moayong.api.domain.verification.dto.service.AccountServiceDto;
import com.moayong.api.domain.verification.enums.VerificationStatus;
import lombok.Builder;

@Builder
public record AccountVerificationResponse (
        String id,
        SavingsBank bank,
        VerificationStatus status,
        String imageUrl,
        String accountNumber,
        Integer accountBalance
) {
    public AccountVerificationResponse(Verification verification, AccountServiceDto account) {
        this(
                verification.getId(),
                verification.getBank(),
                verification.getStatus(),
                verification.getImageUrl(),
                account.accountNumber(),
                account.accountBalance()
        );
    }
}