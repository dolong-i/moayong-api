package com.moayong.api.domain.league.dto.response;

import com.moayong.api.domain.league.domain.League;

public record LeagueResponse (
        Long id,
        Long seasonId,
        Integer tier_id
){
    public LeagueResponse(League league) {
        this(
                league.getId(),
                league.getSeason().getId(),
                league.getTierId()
        );
    }
}
