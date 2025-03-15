package com.moayong.api.domain.verification.dto.response;

import com.moayong.api.domain.user.enums.SavingsBank;
import com.moayong.api.domain.verification.enums.TransactionType;
import com.moayong.api.domain.verification.enums.VerificationStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PaymentVerificationResponse (
        String id,
        SavingsBank bank,
        VerificationStatus status,
        String imageUrl,
        String name,
        TransactionType type,
        LocalDateTime date,
        Integer amount,
        Integer balance
) {

}