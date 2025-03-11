package com.moayong.api.domain.memberquiz.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor
@ToString
@RedisHash("userDailyQuiz")
public class UserDailyQuiz {
        @Id
        private String id;

        @Indexed
        private Long memberId;
        private Long quizId;

        @Setter
        private String status; // DailyQuizStatus

        @TimeToLive
        private long ttl;

        @Builder
        public UserDailyQuiz(Long memberId, Long quizId, String status, long ttl) {
                this.id = memberId + ":" + quizId;
                this.memberId = memberId;
                this.quizId = quizId;
                this.status = status;
                this.ttl = ttl;
        }
}
