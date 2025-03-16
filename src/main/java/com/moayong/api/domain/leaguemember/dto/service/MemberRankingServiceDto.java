package com.moayong.api.domain.leaguemember.dto.service;

import com.moayong.api.domain.leaguemember.dto.response.MemberRankingResponse;
import com.moayong.api.domain.leaguemember.enums.PromotionStatus;
import lombok.Builder;

@Builder
public record MemberRankingServiceDto(
        Long memberId,
        Long userId,
        String nickname,
        PromotionStatus promotionStatus,
        Integer rank,
        Float rate,
        Integer totalScore
){
    public MemberRankingResponse toResponse() {
        return MemberRankingResponse.builder()
                .memberId(memberId)
                .userId(userId)
                .nickname(nickname)
                .promotionStatus(promotionStatus)
                .rank(rank)
                .rate(rate)
                .totalScore(totalScore)
                .build();
    }
}
