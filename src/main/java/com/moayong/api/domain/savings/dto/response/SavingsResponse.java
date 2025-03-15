package com.moayong.api.domain.savings.dto.response;

import com.moayong.api.domain.savings.domain.Savings;

import java.time.LocalDateTime;

public record SavingsResponse (
    Long id,
    Long memberId,
    String imageUrl,
    Integer score,
    Integer amount,
    LocalDateTime datetime
) {
    public SavingsResponse(Savings savings) {
        this(
                savings.getId(),
                savings.getLeagueMemberId(),
                savings.getImageUrl(),
                savings.getScore(),
                savings.getAmount(),
                savings.getDatetime()
        );
    }
}