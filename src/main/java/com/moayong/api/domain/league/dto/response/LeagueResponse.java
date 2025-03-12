package com.moayong.api.domain.league.dto.response;

import com.moayong.api.domain.league.domain.League;

public record LeagueResponse (
        Long id,
        Long seasonId,
        Integer level,
        String name,
        String imageUrl,
        Integer promotionRate,
        Integer relegationRate
){
    public LeagueResponse(League league) {
        this(
                league.getId(),
                league.getSeasonId(),
                league.getLevel(),
                league.getName(),
                league.getImageUrl(),
                league.getPromotionRate(),
                league.getRelegationRate()
        );
    }
}
