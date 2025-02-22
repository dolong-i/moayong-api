package com.moayong.api.domain.league.repository;

import com.moayong.api.domain.league.domain.League;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueRepository extends JpaRepository<League, Long>, LeagueRepositoryCustom {
}
