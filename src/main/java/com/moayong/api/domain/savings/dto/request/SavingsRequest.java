package com.moayong.api.domain.savings.dto.request;

import com.moayong.api.domain.savings.dto.service.SavingsServiceDto;
import com.moayong.api.domain.verification.enums.TransactionType;

import java.time.LocalDateTime;

public record SavingsRequest (
        String name,
        String imageUrl,
        TransactionType type,
        LocalDateTime datetime,
        Integer amount,
        Integer balance
) {
    public SavingsServiceDto toServiceDto() {
        return SavingsServiceDto.builder()
                .name(name)
                .imageUrl(imageUrl)
                .type(type)
                .datetime(datetime)
                .amount(amount)
                .balance(balance)
                .build();
    }
}