package com.moayong.api.domain.league.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LeagueErrorCode {
    LEAGUE_NOT_FOUND(HttpStatus.BAD_REQUEST, "리그를 찾을 수 없습니다");

    private final HttpStatus status;
    private final String message;
}
