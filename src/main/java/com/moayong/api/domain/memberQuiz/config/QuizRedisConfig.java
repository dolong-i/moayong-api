package com.moayong.api.domain.memberQuiz.config;

import com.moayong.api.domain.quiz.domain.Quiz;
import com.moayong.api.global.config.RedisConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class QuizRedisConfig {
    private final RedisConfig redisConfig;

    public QuizRedisConfig(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }

    @Bean
    public RedisTemplate<String, Quiz> quizRedisTemplate() {
        return redisConfig.createRedisTemplate(Quiz.class);
    }
}
