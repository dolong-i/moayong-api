package com.moayong.api.domain.season.repository;

import com.moayong.api.domain.season.domain.QSeason;
import com.moayong.api.domain.season.domain.Season;
import com.moayong.api.domain.season.enums.SeasonStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class SeasonRepositoryCustomImpl implements SeasonRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public SeasonRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<Season> findCurrentSeason() {
        QSeason qSeason = QSeason.season;

        Season season = queryFactory.selectFrom(qSeason)
                .where(qSeason.status.eq(SeasonStatus.OPEN))
                .orderBy(qSeason.id.desc())
                .limit(1)
                .fetchOne();

        return Optional.ofNullable(season);
    }
}