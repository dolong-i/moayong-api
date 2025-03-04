package com.moayong.api.domain.leaguememberquiz.repository;

import com.moayong.api.domain.league.domain.QLeague;
import com.moayong.api.domain.leaguemember.domain.QLeagueMember;
import com.moayong.api.domain.leaguememberquiz.domain.QLeagueMemberQuiz;
import com.moayong.api.domain.quiz.domain.QQuiz;
import com.moayong.api.domain.quiz.domain.Quiz;
import com.moayong.api.domain.season.domain.QSeason;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class LeagueMemberQuizRepositoryCustomImpl implements LeagueMemberQuizRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Quiz> findUnsolvedQuizzes(List<Long> solvedQuizIds) {
        QQuiz qQuiz = QQuiz.quiz;

        return queryFactory.selectFrom(qQuiz)
                .where(qQuiz.id.notIn(solvedQuizIds))
                .fetch();
    }

    @Override
    public List<Quiz> findAllSolvedQuizzes(Long userId) {
        QQuiz qQuiz = QQuiz.quiz;

        List<Long> solvedQuizIds = findSolvedQuizIds(userId);

        return queryFactory.selectFrom(qQuiz)
                .where(qQuiz.id.in(solvedQuizIds))
                .fetch();
    }

    @Override
    public List<Quiz> findSolvedQuizzesByUserAndSeason(Long userId, Long seasonId) {
        QQuiz qQuiz = QQuiz.quiz;
        QLeagueMemberQuiz qLeagueMemberQuiz = QLeagueMemberQuiz.leagueMemberQuiz;
        QLeagueMember qLeagueMember = QLeagueMember.leagueMember;
        QLeague qLeague = QLeague.league;
        QSeason qSeason = QSeason.season;

        return queryFactory.select(qQuiz)
                .from(qQuiz)
                .join(qLeagueMemberQuiz).on(qQuiz.id.eq(qLeagueMemberQuiz.quizId))
                .join(qLeagueMember).on(qLeagueMemberQuiz.leagueMemberId.eq(qLeagueMember.id))
                .join(qLeague).on(qLeagueMember.leagueId.eq(qLeague.id))
                .join(qSeason).on(qLeague.season.id.eq(qSeason.id))
                .where(qLeagueMember.userId.eq(userId)
                        .and(qSeason.id.eq(seasonId)))
                .fetch();
    }

    @Override
    public List<Long> findSolvedQuizIds(Long userId) {
        QLeagueMemberQuiz qLeagueMemberQuiz = QLeagueMemberQuiz.leagueMemberQuiz;
        QLeagueMember qLeagueMember = QLeagueMember.leagueMember;

        return queryFactory.select(qLeagueMemberQuiz.quizId)
                .from(qLeagueMemberQuiz)
                .where(qLeagueMemberQuiz.leagueMemberId.in(
                        JPAExpressions.select(qLeagueMember.id)
                                .from(qLeagueMember)
                                .where(qLeagueMember.userId.eq(userId))
                ))
                .fetch();
    }
}
