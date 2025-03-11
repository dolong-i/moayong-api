package com.moayong.api.domain.leaguemember.dto.service;

import com.moayong.api.domain.leaguemember.dto.response.MatchInfoResponse;
import com.moayong.api.domain.leaguemember.enums.PromotionStatus;
import lombok.Builder;

@Builder
public record MatchInfoServiceDto(
        Long memberId,
        Long leagueId,
        Integer totalScore,
        Integer goalAmount,
        Integer rank,
        Integer rate,
        Integer nextLevel,
        PromotionStatus promotionStatus
){
    public MatchInfoResponse toResponse() {
        return MatchInfoResponse.builder()
                .memberId(memberId)
                .totalScore(totalScore)
                .goalAmount(goalAmount)
                .rank(rank)
                .rate(rate)
                .nextLevel(nextLevel)
                .promotionStatus(promotionStatus)
                .build();
    }
}
