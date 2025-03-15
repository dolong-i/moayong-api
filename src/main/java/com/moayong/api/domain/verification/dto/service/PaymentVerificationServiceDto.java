package com.moayong.api.domain.verification.dto.service;

import com.moayong.api.domain.user.enums.SavingsBank;
import com.moayong.api.domain.verification.domain.Verification;
import com.moayong.api.domain.verification.dto.response.PaymentVerificationResponse;
import com.moayong.api.domain.verification.enums.TransactionType;
import com.moayong.api.domain.verification.enums.VerificationStatus;

import java.time.LocalDateTime;

public record PaymentVerificationServiceDto (
        String id,
        SavingsBank bank,
        VerificationStatus status,
        String imageUrl,
        String name,
        TransactionType type,
        LocalDateTime date,
        Integer amount,
        Integer balance
)
{
    public PaymentVerificationServiceDto(Verification verification, PaymentServiceDto payment) {
        this(
                verification.getId(),
                verification.getBank(),
                verification.getStatus(),
                verification.getImageUrl(),
                payment.name(),
                payment.type(),
                payment.date(),
                payment.amount(),
                payment.balance()
        );
    }

    public PaymentVerificationResponse toResponse() {
        return PaymentVerificationResponse.builder()
                .id(id)
                .bank(bank)
                .status(status)
                .imageUrl(imageUrl)
                .name(name)
                .type(type)
                .date(date)
                .amount(amount)
                .balance(balance)
                .build();
    }
}