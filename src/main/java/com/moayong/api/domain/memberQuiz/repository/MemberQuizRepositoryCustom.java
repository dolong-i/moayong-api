package com.moayong.api.domain.memberQuiz.repository;

import com.moayong.api.domain.memberQuiz.domain.MemberQuiz;
import com.moayong.api.domain.quiz.domain.Quiz;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberQuizRepositoryCustom {

    List<Quiz> findSolvedQuizzesByUserAndSeason(Long userId, Long seasonId);

    List<Long> findSolvedQuizzesByUserId(Long userId);
}
