package com.moayong.api.domain.auth.config;

import com.moayong.api.domain.auth.domain.UserTemporary;
import com.moayong.api.global.config.RedisConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class AuthRedisConfig {
    private final RedisConfig redisConfig;

    public AuthRedisConfig(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }

    @Bean
    public RedisTemplate<String, UserTemporary> userTemporaryRedisTemplate() {
        return redisConfig.createRedisTemplate(UserTemporary.class);
    }
}
