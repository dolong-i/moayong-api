package com.moayong.api.domain.memberquiz.dto.redis;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Builder
@RedisHash("userDailyQuiz")
@ToString
public class UserDailyQuiz {
        @Id
        private String id;
        @Indexed
        private Long userId;
        private Long quizId;
        private String financeTitle;
        private String financeDescription;

        @Setter
        private String status;

        @TimeToLive
        private final long ttl;
}
