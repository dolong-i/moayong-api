package com.moayong.api.domain.quiz.repository;

import com.moayong.api.domain.quiz.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long>, QuizRepositoryCustom {
    List<Quiz> findAllByIdNotIn(List<Long> ids);
}
