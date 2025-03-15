package com.moayong.api.domain.verification.dto.service;

import com.moayong.api.domain.verification.enums.TransactionType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PaymentServiceDto(
        String name,
        TransactionType type,
        LocalDateTime date,
        Integer amount,
        Integer balance
) {

}
