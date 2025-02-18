package com.moayong.api.domain.season.dto.response;

import com.moayong.api.domain.enums.SeasonStatus;
import com.moayong.api.domain.season.domain.Season;

public record SeasonResponse (
        Long id,
        Integer season,
        SeasonStatus status
){
    public SeasonResponse(Season season) {
        this(
                season.getId(),
                season.getSeason(),
                season.getStatus()
        );
    }
}
