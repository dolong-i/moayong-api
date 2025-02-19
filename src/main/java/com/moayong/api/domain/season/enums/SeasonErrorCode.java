package com.moayong.api.domain.season.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SeasonErrorCode {
    LEAGUE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "리그를 찾을 수 없습니다"),
    CURRENT_LEAGUE_NOT_OPEN(HttpStatus.INTERNAL_SERVER_ERROR, "현재 리그가 열려 있지 않습니다");

    private final HttpStatus status;
    private final String message;
}
