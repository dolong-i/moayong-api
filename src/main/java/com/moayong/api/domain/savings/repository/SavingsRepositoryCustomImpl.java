package com.moayong.api.domain.savings.repository;

import com.moayong.api.domain.leaguemember.domain.QLeagueMember;
import com.moayong.api.domain.savings.domain.QSavings;
import com.moayong.api.domain.savings.domain.Savings;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class SavingsRepositoryCustomImpl implements SavingsRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Savings> findByUserId(Long userId) {
        QSavings qSavings = QSavings.savings;
        QLeagueMember qLeagueMember = QLeagueMember.leagueMember;

        return queryFactory
                .selectFrom(qSavings)
                .join(qLeagueMember).on(qSavings.leagueMemberId.eq(qLeagueMember.id))
                .where(qLeagueMember.userId.eq(userId))
                .fetch();
    }

    @Override
    public Integer findSavingsTotalAmountByUserId(Long userId) {
        QSavings qSavings = QSavings.savings;
        QLeagueMember qLeagueMember = QLeagueMember.leagueMember;

        Integer totalAmount = queryFactory
                .select(qSavings.amount.sum().coalesce(0))
                .from(qSavings)
                .join(qLeagueMember).on(qSavings.leagueMemberId.eq(qLeagueMember.id))
                .where(qLeagueMember.userId.eq(userId))
                .fetchOne();

        return totalAmount != null ? totalAmount : 0;
    }


    @Override
    public Integer findSavingsTotalScoreByLeagueMemberId(Long leagueMemberId) {
        QSavings qSavings = QSavings.savings;
        QLeagueMember qLeagueMember = QLeagueMember.leagueMember;

        Integer totalScore = queryFactory
                .select(qSavings.score.sum().coalesce(0))
                .from(qSavings)
                .where(qSavings.leagueMemberId.eq(leagueMemberId))
                .fetchOne();

        return totalScore != null ? totalScore : 0;
    }

    @Override
    public Integer findSavingsTotalAmountByLeagueMemberId(Long leagueMemberId) {
        QSavings qSavings = QSavings.savings;
        QLeagueMember qLeagueMember = QLeagueMember.leagueMember;

        Integer totalAmount = queryFactory
                .select(qSavings.amount.sum().coalesce(0))
                .from(qSavings)
                .where(qSavings.leagueMemberId.eq(leagueMemberId))
                .fetchOne();

        return totalAmount != null ? totalAmount : 0;
    }
}
