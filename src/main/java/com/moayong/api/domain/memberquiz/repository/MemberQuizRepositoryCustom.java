package com.moayong.api.domain.memberquiz.repository;

import com.moayong.api.domain.quiz.domain.Quiz;

import java.util.List;

public interface MemberQuizRepositoryCustom {

    List<Quiz> findSolvedQuizzesByUserAndSeason(Long userId, Long seasonId);

    List<Long> findSolvedQuizzesByUserId(Long userId);
}
