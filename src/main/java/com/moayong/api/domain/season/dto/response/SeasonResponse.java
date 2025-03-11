package com.moayong.api.domain.season.dto.response;

import com.moayong.api.domain.season.enums.SeasonStatus;
import com.moayong.api.domain.season.domain.Season;

import java.time.LocalDateTime;

public record SeasonResponse (
        Long id,
        SeasonStatus status,
        LocalDateTime startedAt,
        LocalDateTime endedAt
){
    public SeasonResponse(Season season) {
        this(
                season.getId(),
                season.getStatus(),
                season.getStartedAt(),
                season.getEndedAt()
        );
    }
}
