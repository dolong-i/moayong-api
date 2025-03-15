package com.moayong.api.domain.leaguemember.dto.response;

import com.moayong.api.domain.leaguemember.enums.PromotionStatus;
import lombok.Builder;

@Builder
public record MemberRankingResponse (
        Long memberId,
        Long userId,
        String nickname,
        PromotionStatus promotionStatus,
        Integer rank,
        Float rate,
        Integer totalScore
){
}
