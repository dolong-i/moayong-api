package com.moayong.api.domain.quiz.repository;

import com.moayong.api.domain.quiz.domain.Quiz;

import java.util.List;
import java.util.Optional;

public interface QuizRepositoryCustom {
    Optional<Quiz> findRandomQuiz(Long userId);


}
