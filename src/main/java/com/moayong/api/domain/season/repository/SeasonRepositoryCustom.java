package com.moayong.api.domain.season.repository;

import com.moayong.api.domain.season.domain.Season;

import java.util.Optional;

public interface SeasonRepositoryCustom {
    Optional<Season> findCurrentSeason();
}
