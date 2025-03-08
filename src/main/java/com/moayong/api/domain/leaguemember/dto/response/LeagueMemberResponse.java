package com.moayong.api.domain.leaguemember.dto.response;

import com.moayong.api.domain.league.dto.response.LeagueResponse;
import lombok.Builder;

@Builder
public record LeagueMemberResponse (
        Integer totalScore,
        Integer goalAmount,
        Integer rank,
        Integer rate,
        LeagueResponse leagueInfo
) {
}
