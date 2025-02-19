package com.moayong.api.domain.season.dto.response;

import com.moayong.api.domain.season.enums.SeasonStatus;
import com.moayong.api.domain.season.domain.Season;

public record SeasonResponse (
        Long id,
        Integer number,
        SeasonStatus status
){
    public SeasonResponse(Season season) {
        this(
                season.getId(),
                season.getNumber(),
                season.getStatus()
        );
    }
}
