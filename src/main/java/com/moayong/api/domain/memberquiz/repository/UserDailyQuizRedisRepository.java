package com.moayong.api.domain.memberquiz.repository;

import com.moayong.api.domain.memberquiz.dto.redis.UserDailyQuiz;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserDailyQuizRedisRepository extends CrudRepository<UserDailyQuiz, String> {
    List<UserDailyQuiz> findAllByUserId(Long userId);
}
