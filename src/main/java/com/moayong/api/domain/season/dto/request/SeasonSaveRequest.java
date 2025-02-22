package com.moayong.api.domain.season.dto.request;

import com.moayong.api.domain.season.enums.SeasonStatus;
import com.moayong.api.domain.season.domain.Season;
import jakarta.validation.constraints.NotNull;

public record SeasonSaveRequest (
        @NotNull(message = "시즌번호은 Null 일 수 없습니다")
        Integer number,
        @NotNull(message = "상태는 Null 일 수 없습니다")
        SeasonStatus status
){
    public Season toEntity() {
        return Season.builder()
                .number(number)
                .status(status)
                .build();
    }
}
