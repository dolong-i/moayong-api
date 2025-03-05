package com.moayong.api.domain.memberQuiz.repository;

import com.moayong.api.domain.quiz.domain.Quiz;

import java.util.List;

public interface MemberQuizRepositoryCustom {
    List<Quiz> findAllSolvedQuizzes(Long userId);

    List<Quiz> findUnsolvedQuizzes(List<Long> solvedQuizIds);

    List<Quiz> findSolvedQuizzesByUserAndSeason(Long userId, Long seasonId);

    List<Long> findSolvedQuizIds(Long userId);
}
