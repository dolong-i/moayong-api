package com.moayong.api.domain.savings.dto.service;

import com.moayong.api.domain.verification.enums.TransactionType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SavingsServiceDto (
        String imageUrl,
        String name,
        TransactionType type,
        LocalDateTime datetime,
        Integer amount,
        Integer balance
) {

}
