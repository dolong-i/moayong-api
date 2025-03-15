package com.moayong.api.domain.leaguemember.repository;

import com.moayong.api.domain.leaguemember.domain.LeagueMember;
import com.moayong.api.domain.leaguemember.domain.QLeagueMember;
import com.moayong.api.domain.leaguemember.dto.service.LeagueMemberWithNickname;
import com.moayong.api.domain.leaguemember.enums.LeagueMemberStatus;
import com.moayong.api.domain.user.domain.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class LeagueMemberRepositoryCustomImpl implements LeagueMemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public List<LeagueMember> findByLeagueIdOrderByTotalScoreDesc(Long leagueId) {
        QLeagueMember leagueMember = QLeagueMember.leagueMember;
        return queryFactory
                .selectFrom(leagueMember)
                .where(leagueMember.leagueId.eq(leagueId)) // 리그 아이디로 필터링
                .orderBy(leagueMember.totalScore.desc())  // 점수 내림차순 정렬
                .fetch();
    }

    public LeagueMember findMostRecentLeagueMemberByUserId(Long userId) {
        QLeagueMember leagueMember = QLeagueMember.leagueMember;
        return queryFactory
                .selectFrom(leagueMember)
                .where(leagueMember.userId.eq(userId)) // userId로 필터링
                .orderBy(leagueMember.createdAt.desc()) // createdAt 기준 내림차순 정렬
                .limit(1) // 가장 최근 1개 레코드만 가져옴
                .fetchOne(); // 단일 레코드 반환
    }

    public Optional<LeagueMember> findActiveMemberByUserId(Long userId) {
        QLeagueMember leagueMember = QLeagueMember.leagueMember;

        return Optional.ofNullable(queryFactory
                .selectFrom(leagueMember)
                .where(
                        leagueMember.userId.eq(userId),
                        leagueMember.status.eq(LeagueMemberStatus.ACTIVE)
                )
                .fetchOne());
    }

    public List<LeagueMemberWithNickname> getLeagueMembersWithNicknameByLeagueId(Long leagueId) {
        QLeagueMember leagueMember = QLeagueMember.leagueMember;
        QUser user = QUser.user;

        return queryFactory
                .select(Projections.constructor(LeagueMemberWithNickname.class,
                        leagueMember,
                        user.nickname
                ))
                .from(leagueMember)
                .join(user).on(leagueMember.userId.eq(user.id)) // userId와 user.id JOIN
                .where(leagueMember.leagueId.eq(leagueId))
                .orderBy(leagueMember.totalScore.desc()) // 총점 내림차순 정렬
                .fetch();
    }
}
