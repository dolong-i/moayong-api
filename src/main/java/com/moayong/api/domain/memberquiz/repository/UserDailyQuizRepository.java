package com.moayong.api.domain.memberquiz.repository;

import com.moayong.api.domain.memberquiz.domain.UserDailyQuiz;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserDailyQuizRepository extends CrudRepository<UserDailyQuiz, String> {
    List<UserDailyQuiz> findAllByUserId(Long userId);
}
