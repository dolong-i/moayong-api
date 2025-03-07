package com.moayong.api.domain.memberquiz.config;

import com.moayong.api.domain.memberquiz.dto.redis.UserDailyQuiz;
import com.moayong.api.domain.quiz.domain.Quiz;
import com.moayong.api.global.config.RedisConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;


@Configuration
public class QuizRedisConfig {
    private final RedisConfig redisConfig;

    public QuizRedisConfig(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }

    @Bean
    public RedisTemplate<String, UserDailyQuiz> quizRedisTemplate() {
        return redisConfig.createRedisTemplate(UserDailyQuiz.class);
    }
}
