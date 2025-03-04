package com.moayong.api.domain.quiz.repository;

import com.moayong.api.domain.quiz.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long>, QuizRepositoryCustom {
}
