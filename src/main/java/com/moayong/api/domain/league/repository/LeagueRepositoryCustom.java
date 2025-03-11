package com.moayong.api.domain.league.repository;

import com.moayong.api.domain.league.domain.League;

import java.util.List;

public interface LeagueRepositoryCustom {
    List<League> findLeaguesBySeasonId(Long seasonId);
}
