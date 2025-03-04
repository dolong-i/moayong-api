package com.moayong.api.domain.quiz.repository;

import com.moayong.api.domain.leaguemember.domain.QLeagueMember;
import com.moayong.api.domain.leaguememberquiz.domain.QLeagueMemberQuiz;
import com.moayong.api.domain.quiz.domain.QQuiz;
import com.moayong.api.domain.quiz.domain.Quiz;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Repository
public class QuizRepositoryCustomImpl implements QuizRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    // TODO 하루 5개 제한 추가하기
    @Override
    public Optional<Quiz> findRandomQuiz(Long userId) {
        QQuiz qQuiz = QQuiz.quiz;

        List<Long> solvedQuizIds = findSolvedQuizIds(userId);

        List<Quiz> unsolvedQuizzes = queryFactory.selectFrom(qQuiz)
                .where(qQuiz.id.notIn(solvedQuizIds))
                .fetch();

        if (unsolvedQuizzes.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(unsolvedQuizzes.get(new Random().nextInt(unsolvedQuizzes.size())));
    }

    private List<Long> findSolvedQuizIds(Long userId) {
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
