package com.moayong.api.domain.memberQuiz.repository;

import com.moayong.api.domain.league.domain.QLeague;
import com.moayong.api.domain.leaguemember.domain.QLeagueMember;
import com.moayong.api.domain.memberQuiz.domain.QMemberQuiz;
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
public class MemberQuizRepositoryCustomImpl implements MemberQuizRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Quiz> findSolvedQuizzesByUserAndSeason(Long userId, Long seasonId) {
        QQuiz qQuiz = QQuiz.quiz;
        QMemberQuiz qMemberQuiz = QMemberQuiz.memberQuiz;
        QLeagueMember qLeagueMember = QLeagueMember.leagueMember;
        QLeague qLeague = QLeague.league;
        QSeason qSeason = QSeason.season;

        return queryFactory.select(qQuiz)
                .from(qQuiz)
                .join(qMemberQuiz).on(qQuiz.id.eq(qMemberQuiz.quizId))
                .join(qLeagueMember).on(qMemberQuiz.leagueMemberId.eq(qLeagueMember.id))
                .join(qLeague).on(qLeagueMember.leagueId.eq(qLeague.id))
                .join(qSeason).on(qLeague.season.id.eq(qSeason.id))
                .where(qLeagueMember.userId.eq(userId)
                        .and(qSeason.id.eq(seasonId)))
                .fetch();
    }

    @Override
    public List<Long> findSolvedQuizzesByUserId(Long userId) {
        QMemberQuiz qMemberQuiz = QMemberQuiz.memberQuiz;
        QLeagueMember qLeagueMember = QLeagueMember.leagueMember;

        return queryFactory.select(qMemberQuiz.quizId)
                .from(qMemberQuiz)
                .where(qMemberQuiz.leagueMemberId.in(
                        JPAExpressions.select(qLeagueMember.id)
                                .from(qLeagueMember)
                                .where(qLeagueMember.userId.eq(userId))
                ))
                .fetch();
    }
}
