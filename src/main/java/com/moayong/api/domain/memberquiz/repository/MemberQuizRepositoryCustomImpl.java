package com.moayong.api.domain.memberquiz.repository;

import com.moayong.api.domain.leaguemember.domain.QLeagueMember;
import com.moayong.api.domain.memberquiz.domain.QMemberQuiz;
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
