package com.moayong.api.domain.memberquiz.repository;

import com.moayong.api.domain.quiz.domain.Quiz;

import java.util.List;

public interface MemberQuizRepositoryCustom {
    List<Long> findSolvedQuizzesByUserId(Long userId);
}
