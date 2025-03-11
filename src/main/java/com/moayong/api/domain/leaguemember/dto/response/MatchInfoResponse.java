package com.moayong.api.domain.leaguemember.dto.response;

import com.moayong.api.domain.leaguemember.enums.PromotionStatus;
import lombok.Builder;

@Builder
public record MatchInfoResponse (
        Long memberId,
        Long leagueId,
        Integer totalScore,
        Integer goalAmount,
        Integer rank,
        Integer rate,
        Integer nextLevel,
        PromotionStatus promotionStatus
){

}
