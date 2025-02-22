package com.moayong.api.domain.league.repository;

import com.moayong.api.domain.league.domain.League;
import com.moayong.api.domain.season.domain.Season;

import java.util.List;

public interface LeagueRepositoryCustom {
    List<League> findLeaguesBySeason(Season season);
}
