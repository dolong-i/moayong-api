package com.moayong.api.domain.leaguemember.dto.service;

import com.moayong.api.domain.league.domain.League;
import com.moayong.api.domain.league.dto.response.LeagueResponse;
import com.moayong.api.domain.leaguemember.domain.LeagueMember;
import com.moayong.api.domain.leaguemember.dto.response.LeagueMemberResponse;
import lombok.Builder;

@Builder
public record LeagueMemberServiceDto (
        Long userId,
        Integer totalScore,
        Integer goalAmount,
        Integer rank,
        Integer rate,
        League league
) {
    public LeagueMemberResponse toResponse() {
        return LeagueMemberResponse.builder()
                .totalScore(totalScore)
                .goalAmount(goalAmount)
                .rank(rank)
                .rate(rate)
                .leagueInfo(new LeagueResponse(league))
                .build();
    }

    public LeagueMember toEntity() {
        return LeagueMember.builder()
                .userId(userId)
                .leagueId(league.getId())
                .totalScore(totalScore)
                .goalAmount(goalAmount)
                .build();
    }
}
