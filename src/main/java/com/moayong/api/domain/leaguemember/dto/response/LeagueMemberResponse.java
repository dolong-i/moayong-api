package com.moayong.api.domain.leaguemember.dto.response;

import com.moayong.api.domain.leaguemember.domain.LeagueMember;
import com.moayong.api.domain.leaguemember.enums.LeagueMemberStatus;
import lombok.Builder;

@Builder
public record LeagueMemberResponse (
        Long id,
        Long userId,
        Long leagueId,
        LeagueMemberStatus status,
        Integer totalScore,
        Integer goalAmount
) {
    public LeagueMemberResponse(LeagueMember member) {
        this(
                member.getId(),
                member.getUserId(),
                member.getLeagueId(),
                member.getStatus(),
                member.getTotalScore(),
                member.getGoalAmount()
        );
    }
}
