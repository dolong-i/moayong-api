package com.moayong.api.domain.league.repository;

import com.moayong.api.domain.league.domain.League;
import com.moayong.api.domain.league.domain.QLeague;
import com.moayong.api.domain.season.domain.Season;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class LeagueRepositoryCustomImpl implements LeagueRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<League> findLeaguesBySeason(Season season) {
        QLeague qLeague = QLeague.league;

        return queryFactory.selectFrom(qLeague)
            .where(qLeague.season.eq(season))
            .fetch();
    }
}
