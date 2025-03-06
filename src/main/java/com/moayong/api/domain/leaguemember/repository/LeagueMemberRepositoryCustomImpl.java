package com.moayong.api.domain.leaguemember.repository;

import com.moayong.api.domain.leaguemember.domain.LeagueMember;
import com.moayong.api.domain.leaguemember.domain.QLeagueMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class LeagueMemberRepositoryCustomImpl implements LeagueMemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<LeagueMember> findLeagueMemberByUserAndLeagues(Long userId, List<Long> leagueIds) {
        QLeagueMember qLeagueMember = QLeagueMember.leagueMember;
        LeagueMember leagueMember = queryFactory.selectFrom(qLeagueMember)
                .where(qLeagueMember.userId.eq(userId).and(qLeagueMember.leagueId.in(leagueIds)))
                .fetchOne();

        return Optional.ofNullable(leagueMember);
    }
}
