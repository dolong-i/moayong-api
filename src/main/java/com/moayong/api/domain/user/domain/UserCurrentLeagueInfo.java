package com.moayong.api.domain.user.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash(value = "userInfo")
public class UserCurrentLeagueInfo {
        @Id
        private Long userId;
        private Long seasonId;
        private Long leagueId;
        private Integer level;
        private Long leagueMemberId;

        @TimeToLive
        private Long ttl;
}