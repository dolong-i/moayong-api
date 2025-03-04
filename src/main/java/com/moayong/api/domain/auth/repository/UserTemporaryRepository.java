package com.moayong.api.domain.auth.repository;

import com.moayong.api.domain.auth.domain.UserTemporary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
public class UserTemporaryRepository {
    private final RedisTemplate<String, UserTemporary> redisTemplate;

    public UserTemporaryRepository(RedisTemplate<String, UserTemporary> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Optional<UserTemporary> findById(String id) {
        UserTemporary userTemporary = redisTemplate.opsForValue().get(id);
        return Optional.ofNullable(userTemporary);
    }

    public void save(UserTemporary userTemporary) {
        String key = userTemporary.getCompositeKey();
        Duration ttl = Duration.ofMinutes(20); // 20분 TTL 설정
        redisTemplate.opsForValue().set(key, userTemporary, ttl);
    }

    public void deleteById(String id) {
        redisTemplate.delete(id);
    }
}